package com.us.weavx.core.application.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.html2pdf.HtmlConverter;
import com.us.weavx.core.application.ApplicationController;
import com.us.weavx.core.constants.TokenStatus;
import com.us.weavx.core.exception.ApplicationControllerGeneralException;
import com.us.weavx.core.exception.EmailSendingGeneralException;
import com.us.weavx.core.exception.UnknownNotificationTemplateException;
import com.us.weavx.core.model.ApplicationControllerParameterSelector;
import com.us.weavx.core.model.ApplicationControllerResult;
import com.us.weavx.core.model.Assistant;
import com.us.weavx.core.model.Attachment;
import com.us.weavx.core.model.BlackListItem;
import com.us.weavx.core.model.EmailRecipient;
import com.us.weavx.core.model.EmailType;
import com.us.weavx.core.model.EventCommissionSettings;
import com.us.weavx.core.model.PaymentGatewayInfo;
import com.us.weavx.core.model.PaymentResult;
import com.us.weavx.core.model.Ticket;
import com.us.weavx.core.model.TicketHistory;
import com.us.weavx.core.model.TicketStatus;
import com.us.weavx.core.model.Transaction;
import com.us.weavx.core.model.TransactionDetailInfo;
import com.us.weavx.core.model.TransactionUserData;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.paymentgw.PaymentParameterSelector;
import com.us.weavx.core.paymentgw.impl.StripePaymentGwHandlerImpl;
import com.us.weavx.core.services.tx.AssistantServicesTx;
import com.us.weavx.core.services.tx.BlackListTxServices;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
import com.us.weavx.core.services.tx.TicketServicesTx;
import com.us.weavx.core.services.tx.TransactionTxServices;
import com.us.weavx.core.services.tx.UserTxServices;
import com.us.weavx.core.services.tx.UtilTxServices;
import com.us.weavx.core.util.BlackListDataTypeSelector;
import com.us.weavx.core.util.CommissionPayerSelector;
import com.us.weavx.core.util.CustomerPropertyManager;
import com.us.weavx.core.util.GeneralUtilities;
import com.us.weavx.core.util.SystemSettingsManager;
import com.us.weavx.core.util.TokenGeneratorUtil;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
@Component
public class PayPerViewEventApplicationControllerImpl implements ApplicationController {
	@Autowired
	private TransactionTxServices transactionServices;
	@Autowired
	private BlackListTxServices blackListServices;
	@Autowired
	private UtilTxServices utilServices;
	@Autowired
	private UserTxServices userServices;
	@Autowired
	private ConfigurationTxServices configurationServices;
	@Autowired
	private SystemSettingsManager systemSettingsmanager;
	@Autowired
	private CustomerPropertyManager cpManager;
	@Autowired
	private AssistantServicesTx assistantServiceTx;
	@Autowired
	private TicketServicesTx ticketServicesTx;
	
	public PayPerViewEventApplicationControllerImpl() {
		//default constructor
	}

	@Override
	public ApplicationControllerResult onInitBuy(Map<String, Object> parameters) throws ApplicationControllerGeneralException {
		/**
		 * Para una aplicacion pay per view lo que necesitamos es validar que no haya una compra previa del cliente para el mismo evento
		 */
		Transaction transaction = (Transaction) parameters.get(ApplicationControllerParameterSelector.TRANSACTION);
		TransactionUserData transactionUserData = (TransactionUserData) parameters.get(ApplicationControllerParameterSelector.TRANSACTION_USER_DATA);
		Long customerUserId = transactionUserData.getCustomerUserId();		
		if (customerUserId != null && customerUserId.longValue() != 0) {
			//Se realiza la validacion pues se trata de un usuario existente
			try {
				String blackListEnabledStr = systemSettingsmanager.getSystemProperty("BLACKLIST_ENABLED");
				blackListEnabledStr = (blackListEnabledStr==null)?"false":blackListEnabledStr;
				boolean blackListEnabled = Boolean.parseBoolean(blackListEnabledStr);
				if (blackListEnabled) {
					//Se verifica si el IP y/o el correo están en lista negra
					String ipAddressToVerify = (String) parameters.get(ApplicationControllerParameterSelector.IP_ADDRESS);
					String emailToVerify = (String) transactionUserData.getEmail();
					
					if (ipAddressToVerify != null) {
						BlackListItem item = blackListServices.findBlackListItemByDataTypeIdAndData(BlackListDataTypeSelector.IP, ipAddressToVerify);
						if (item != null) {
							//Se debe bloquear el correo electrónico al haber detectado el IP en lista negra
							BlackListItem emailToBlock = new BlackListItem();
							emailToBlock.setDataTypeId(BlackListDataTypeSelector.EMAIL);
							emailToBlock.setData(emailToVerify);
							emailToBlock.setBlockedBy(item.getId());
							try {
								BlackListItem checkEmail = blackListServices.findBlackListItemByDataTypeIdAndData(BlackListDataTypeSelector.EMAIL, emailToVerify);
								if (checkEmail == null) {
									emailToBlock.setBlockedBy(item.getId());
									blackListServices.addBlacklistItem(emailToBlock);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						ApplicationControllerResult result = new ApplicationControllerResult();
						result.setResult(ApplicationControllerResult.ABORT_TRANSACTION);
						result.setResultMessage("IP address found in blacklist");
						return result;
						}
					}
					if (emailToVerify != null) {
						BlackListItem emailItem = blackListServices.findBlackListItemByDataTypeIdAndData(BlackListDataTypeSelector.EMAIL, emailToVerify);
						if (emailItem != null) {
							//Email encontrado en lista negra, se debe bloquear la dirección IP
							if (ipAddressToVerify != null && !ipAddressToVerify.equals("10.0.0.1")) {
								BlackListItem ipToBlock = new BlackListItem();
								ipToBlock.setDataTypeId(BlackListDataTypeSelector.IP);
								ipToBlock.setData(ipAddressToVerify);
								ipToBlock.setBlockedBy(emailItem.getId());
								try {
									BlackListItem checkIP = blackListServices.findBlackListItemByDataTypeIdAndData(BlackListDataTypeSelector.IP, ipAddressToVerify);
									if (checkIP == null) {
										ipToBlock.setBlockedBy(emailItem.getId());
										blackListServices.addBlacklistItem(ipToBlock);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								ApplicationControllerResult result = new ApplicationControllerResult();
								result.setResult(ApplicationControllerResult.ABORT_TRANSACTION);
								result.setResultMessage("Email address found in blacklist");
								return result;
							}
						}
					}
				}
				
				List<Transaction> txs = transactionServices.findCustomerUserSuccessTransactionsByApplication(transaction.getCustomerId(), transactionUserData.getCustomerUserId(), transaction.getApplicationId());
				if (txs != null && !txs.isEmpty()) {
					ApplicationControllerResult result = new ApplicationControllerResult();
					result.setResult(ApplicationControllerResult.ABORT_TRANSACTION);
					result.setResultMessage("Dupplicate transaction found.");
					return result;
				} else {
					ApplicationControllerResult result = new ApplicationControllerResult();
					result.setResult(ApplicationControllerResult.CONTINUE_TRANSACTION);
					result.setResultMessage("ok");
					return result;
				}
			} catch (Exception e) {
				ApplicationControllerResult result = new ApplicationControllerResult();
				result.setResult(ApplicationControllerResult.ABORT_TRANSACTION);
				result.setResultMessage("Exception in dupplicate transaction validation: "+e);
				return result;
			}
			
		} else {
			ApplicationControllerResult result = new ApplicationControllerResult();
			result.setResult(ApplicationControllerResult.CONTINUE_TRANSACTION);
			result.setResultMessage("OK");
			return result;
		}
	}

	@Override
	public ApplicationControllerResult onFinishBuy(Map<String, Object> parameters) throws ApplicationControllerGeneralException {
		//proposito de este metodo es enviar un correo al usuario para que pueda autenticarse.
		TransactionUserData transactionUserData = (TransactionUserData) parameters.get(ApplicationControllerParameterSelector.TRANSACTION_USER_DATA);
		Transaction transaction = (Transaction) parameters.get(ApplicationControllerParameterSelector.TRANSACTION);
		Integer langId = (Integer) parameters.get(ApplicationControllerParameterSelector.LANG);
		PaymentResult paymentResult = (PaymentResult) parameters.get(ApplicationControllerParameterSelector.PAYMENT_RESULT);
		String userAccessToken = (String) parameters.get(ApplicationControllerParameterSelector.USER_ACCESS_TOKEN);
		List<Assistant> assistants = (List<Assistant>) parameters.get(ApplicationControllerParameterSelector.TRANSACTION_ASSISTANTS);
		try {
			if (userAccessToken == null) {
				userServices.authenticateCustomerUserByEmail(transactionUserData.getEmail(), transaction.getCustomerId(), transaction.getApplicationId(), "localhost", "SYSTEM", langId);
				ApplicationControllerResult result = new ApplicationControllerResult();
				result.setResult(ApplicationControllerResult.OK);
				result.setResultMessage("OK");
				return result;
			} else {
				
				String subject = cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "WELCOME_EMAIL_SUBJECT");
				String template = cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "WELCOME_EMAIL_TEMPLATE");
				String url = cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "WELCOME_EMAIL_URL");
				String fromName = cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "WELCOME_EMAIL_FROM_NAME");
				String from = cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "WELCOME_EMAIL_FROM");
				String urlAuth = cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "WELCOME_EMAIL_AUTH_URL");
				String dateFormat = cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "DATE_FORMAT");
				String amountFormat = cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "AMOUNT_FORMAT");
				LocalDateTime localDateTime = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
		        String purchaseDate =  localDateTime.format(formatter);
				HashMap<String, String> params = new HashMap<String, String>();
				ArrayList<EmailRecipient> dests = new ArrayList<>();
				EmailRecipient recipient = new EmailRecipient();
				recipient.setEmail(transactionUserData.getEmail());
				recipient.setName(transactionUserData.getName());
				dests.add(recipient);
				if (langId != null) {
					StringBuilder sB = new StringBuilder(userAccessToken);
					sB.append("?l=").append(langId);
					userAccessToken = sB.toString();
				}
				params.put("URL_AUTH", urlAuth);
				params.put("URL", url);
				params.put("TOKEN", userAccessToken);
				params.put("Name", transactionUserData.getName());
				params.put("Lastname", transactionUserData.getLastname());
				String cardLast4 = "";
				String cardBrand = "";
				String orderDescription = "";
				if (transaction.getAmount() > 0) {
					try {
						cardLast4 = paymentResult.getAuthorizationInfo().get(StripePaymentGwHandlerImpl.CARD_LAST_4).toString();
						cardBrand = paymentResult.getAuthorizationInfo().get(StripePaymentGwHandlerImpl.CARD_BRAND).toString();
					} catch (Exception e) {
						//No es necesario abortar la transaccion por un error al obtener la data de la TDC.
					}
					try {
						orderDescription = (String) (((PaymentGatewayInfo) parameters.get(ApplicationControllerParameterSelector.PAYMENT_GW_INFO)).getPaymentGwParameters().get(PaymentParameterSelector.ORDER_DESCRIPTION));
						if (orderDescription == null || orderDescription.trim().length()==0) {
							orderDescription = (String) (((PaymentGatewayInfo) parameters.get(ApplicationControllerParameterSelector.PAYMENT_GW_INFO)).getPaymentGwParameters().get(PaymentParameterSelector.ORDER_DESCRIPTION.toLowerCase()));
						}
					} catch (Exception e) {
						//No es necesario abortar la transaccion por un error al obtener la data del GW INFO
					}
				}
				params.put("CardLast4", cardLast4);
				params.put("CardBrand", cardBrand);
				params.put("OrderDescription", orderDescription);
				params.put("purchaseDate", purchaseDate);
				params.put("orderId", new Long(transaction.getTransactionId()).toString());
				DecimalFormat dF = (DecimalFormat) DecimalFormat.getInstance();
				dF.applyPattern(amountFormat);
				params.put("Amount", dF.format(transaction.getAmount()));
				if (transaction.getDiscount()>0) {
					HashMap<String, String> discountMap = new HashMap<>();
					discountMap.put("Discount", dF.format(transaction.getDiscount()));
					String discountTemplate = GeneralUtilities.geTemplateFileContentFilled(cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "DISCOUNT-ROW-TEMPLATE"), discountMap);
					params.put("DiscountTemplate", discountTemplate);
				} else {
					params.put("DiscountTemplate", "");
				}
				EventCommissionSettings eventCommissionSettings = transactionServices.findEventCommissionSettings(transaction.getCustomerId(), transaction.getApplicationId());
				if (transaction.getAmount() - transaction.getCommission() > 0 && transaction.getCommission()>0 && eventCommissionSettings != null && eventCommissionSettings.getCommissionPayerId() == CommissionPayerSelector.USER) {
					HashMap<String, String> commissionMap = new HashMap<>();
					commissionMap.put("Fee", dF.format(transaction.getCommission()));
					String commissionTemplate = GeneralUtilities.geTemplateFileContentFilled(cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "FEE-ROW-TEMPLATE"), commissionMap);
					params.put("FeeTemplate", commissionTemplate);
				} else {
					params.put("FeeTemplate", "");
				}
				BigDecimal payableAmount = new BigDecimal(transaction.getAmount()).subtract(new BigDecimal(transaction.getDiscount()));
				if (eventCommissionSettings != null && eventCommissionSettings.getCommissionPayerId() == CommissionPayerSelector.USER) {
					if (transaction.getAmount() - transaction.getCommission() == 0) {
						//complimentary
						payableAmount = new BigDecimal(0);
						params.put("Amount", dF.format(0));
					} else {
						payableAmount = payableAmount.add(new BigDecimal(transaction.getCommission()));
					}
				}
				params.put("PayableAmount", dF.format(payableAmount.doubleValue()));
				Map<Assistant, String> emailsTickets = new HashMap<>();
				List<Attachment> attachments = new ArrayList<>();
				if(!assistants.isEmpty()) {
					TicketStatus ticketStatusInitial = ticketServicesTx.findTicketStatusByName("INITIAL");
					StringBuilder builder = new StringBuilder();
					String ticketTemplate = cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "TICKET_TEMPLATE");
					assistants.forEach(assistant -> {
						boolean registered = false;
						assistant.setTransactionId(transaction.getId());
						assistantServiceTx.addAssistant(assistant);
						Ticket ticket = new Ticket(null, assistant.getId(), ticketStatusInitial.getId());
						for (int i = 0; i < 10; i++) {
							ticket.setTicketSerial(TokenGeneratorUtil.generateToken());
							try {
								ticketServicesTx.addTicket(ticket);
							} catch (Exception e) {
								ticket.setTicketSerial(null);
							}
							if(ticket.getTicketSerial() != null) {
								break;
							}
						}
						if(ticket.getTicketSerial() != null) {
							TicketHistory ticketHistory = new TicketHistory(ticket.getId(), new Date(), ticketStatusInitial.getId(), ticketStatusInitial.getStatusName());
							ticketServicesTx.addTicketHistory(ticketHistory);
						}
						byte[] fileContent = QRCode.from(ticket.getTicketSerial()).to(ImageType.PNG).withSize(250, 250).stream().toByteArray();
						
						String encodedString = "";
						try {
							encodedString = Base64.encodeBase64String(fileContent);
							HashMap<String, String> ticketMap = new HashMap<>();
							ticketMap.put("EventName", "Nombre");
							ticketMap.put("TicketName", assistant.getFirstName()+" "+assistant.getLastName());
							ticketMap.put("TicketType", "General");
							ticketMap.put("TicketQR", encodedString);
							String mailString = GeneralUtilities.geTemplateFileContentFilled(ticketTemplate, ticketMap);
							emailsTickets.put(assistant, mailString);
							System.out.println(mailString);
							builder.append(mailString);
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							HtmlConverter.convertToPdf(mailString, out);
							attachments.add(new Attachment(out.toByteArray(), assistant.getFirstName()+"_"+assistant.getLastName()+".pdf", "application/pdf"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
					params.put("TicketsTemplate", builder.toString());
				}
				utilServices.sendEmailWithManager(from, fromName, subject, template, dests, attachments, params, EmailType.PURCHASE_EMAIL);
				if(!emailsTickets.isEmpty()) {
					String ticketTemplate = cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "ASSISTANT_TICKET_TEMPLATE");
					String ticketSubject = cpManager.findCustomerProperty(transaction.getCustomerId(), langId, "ASSISTANT_EMAIL_SUBJECT");
					emailsTickets.forEach((assistant, emailTicket) -> {
						List<Attachment> ticketttachments = new ArrayList<>();
						Map<String, String> ticketParameters = new HashMap<>();
						ArrayList<EmailRecipient> ticketDests = new ArrayList<>();
						EmailRecipient ticketRecipient = new EmailRecipient();
						ticketRecipient.setEmail(assistant.getEmail());
						ticketRecipient.setName(assistant.getFirstName()+" "+assistant.getLastName());
						ticketDests.add(ticketRecipient);
						ticketParameters.put("Name", assistant.getFirstName()+" "+assistant.getLastName());
						ticketParameters.put("TicketTemplate", emailTicket);
						
						try {
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							HtmlConverter.convertToPdf(emailTicket, out);
							ticketttachments.add(new Attachment(out.toByteArray(), assistant.getFirstName()+"_"+assistant.getLastName()+".pdf", "application/pdf"));
							utilServices.sendEmailWithManager(from, fromName, ticketSubject, ticketTemplate, ticketDests, ticketttachments, ticketParameters, EmailType.PURCHASE_EMAIL);
						} catch (UnknownNotificationTemplateException e) {
							e.printStackTrace();
						} catch (EmailSendingGeneralException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				}
				ApplicationControllerResult result = new ApplicationControllerResult();
				result.setResult(ApplicationControllerResult.OK);
				result.setResultMessage("OK");
				return result;				
			}
		} catch (Exception e) {
			ApplicationControllerResult result = new ApplicationControllerResult();
			result.setResult(ApplicationControllerResult.ERROR);
			result.setResultMessage("Email sending error: "+e);
			return result;
		}
	}

	@Override
	public void sendPurchaseReceipt(Map<String, Object> parameters) throws ApplicationControllerGeneralException {
		try {
			Long applicationId = (Long) parameters.get(ApplicationControllerParameterSelector.APPLICATION_ID);
			TransactionUserData userData = (TransactionUserData) parameters.get(ApplicationControllerParameterSelector.TRANSACTION_USER_DATA);
			Long customerId = (Long) parameters.get(ApplicationControllerParameterSelector.CUSTOMER_ID);
			Long transactionId = (Long) parameters.get(ApplicationControllerParameterSelector.TRANSACTION_ID);
			String adminEmail = (String) parameters.get(ApplicationControllerParameterSelector.ADMIN_EMAIL);
			Integer langId = null;
			List<TransactionDetailInfo> currentTransactionDetails = transactionServices.findCustomerUserTransactionInfo(transactionId);
			if (currentTransactionDetails.size() == 0 || !currentTransactionDetails.get(0).getStatus().equals("SUCCESS_TX")) {
				throw new ApplicationControllerGeneralException("The transaction is not approved.");
			} else {
				Transaction transaction = transactionServices.findTransactionById(transactionId);
				langId = transaction.getLangId();
				String subject = cpManager.findCustomerProperty(customerId, langId, "WELCOME_EMAIL_SUBJECT");
				String template = cpManager.findCustomerProperty(customerId, langId, "WELCOME_EMAIL_TEMPLATE");
				String url = cpManager.findCustomerProperty(customerId, langId, "WELCOME_EMAIL_URL");
				String fromName = cpManager.findCustomerProperty(customerId, langId, "WELCOME_EMAIL_FROM_NAME");
				String from = cpManager.findCustomerProperty(customerId, langId, "WELCOME_EMAIL_FROM");
				String urlAuth = cpManager.findCustomerProperty(customerId, langId, "WELCOME_EMAIL_AUTH_URL");
				String dateFormat = cpManager.findCustomerProperty(customerId, langId, "DATE_FORMAT");
				String amountFormat = cpManager.findCustomerProperty(customerId, langId, "AMOUNT_FORMAT");
				UserAccessToken uAT = userServices.createNewCustomerUserSessionToken(userData.getCustomerUserId(), applicationId, "N/A", "SYSTEM", TokenStatus.PENDING_VALIDATION);
				String userAccessToken = uAT.getToken();
				LocalDateTime localDateTime = transaction.getTransactionDate().toLocalDateTime();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
		        String purchaseDate =  localDateTime.format(formatter);
				HashMap<String, String> params = new HashMap<>();
				ArrayList<EmailRecipient> dests = new ArrayList<>();
				EmailRecipient recipient = new EmailRecipient();
				if (adminEmail != null) {
					recipient.setEmail(adminEmail);
				} else {
					recipient.setEmail(userData.getEmail());
					recipient.setName(userData.getName());
				}
				dests.add(recipient);
				if (langId != null) {
					StringBuilder sB = new StringBuilder(userAccessToken);
					sB.append("?l=").append(langId);
					userAccessToken = sB.toString();
				}
				params.put("URL_AUTH", urlAuth);
				params.put("URL", url);
				params.put("TOKEN", userAccessToken);
				params.put("Name", userData.getName());
				params.put("Lastname", userData.getLastname());
				String cardLast4 = "";
				String cardBrand = "";
				String orderDescription = "";
				TransactionDetailInfo transactionDetail = currentTransactionDetails.get(0); 
				if (transaction.getAmount() > 0) {
					try {
						cardLast4 = "****-"+transactionDetail.getCardMasked().substring(transactionDetail.getCardMasked().lastIndexOf("X")+1);
						cardBrand = transactionDetail.getCardBrand();
					} catch (Exception e) {
						//No es necesario abortar la transaccion por un error al obtener la data de la TDC.
					}
					orderDescription = transactionDetail.getFund();
				}
				params.put("CardLast4", cardLast4);
				params.put("CardBrand", cardBrand);
				params.put("OrderDescription", orderDescription);
				params.put("purchaseDate", purchaseDate);
				params.put("orderId", transactionDetail.getTxInternalId());
				DecimalFormat dF = (DecimalFormat) DecimalFormat.getInstance();
				dF.applyPattern(amountFormat);
				params.put("Amount", dF.format(transaction.getAmount()));
				if (transaction.getDiscount()>0) {
					HashMap<String, String> discountMap = new HashMap<>();
					discountMap.put("Discount", dF.format(transaction.getDiscount()));
					String discountTemplate = GeneralUtilities.geTemplateFileContentFilled(cpManager.findCustomerProperty(customerId, langId, "DISCOUNT-ROW-TEMPLATE"), discountMap);
					params.put("DiscountTemplate", discountTemplate);
				} else {
					params.put("DiscountTemplate", "");
				}
				EventCommissionSettings eventCommissionSettings = transactionServices.findEventCommissionSettings(customerId, applicationId);
				if (transaction.getAmount() - transaction.getCommission() > 0 && transaction.getCommission()>0 && eventCommissionSettings != null && eventCommissionSettings.getCommissionPayerId() == CommissionPayerSelector.USER) {
					HashMap<String, String> commissionMap = new HashMap<>();
					commissionMap.put("Fee", dF.format(transaction.getCommission()));
					String commissionTemplate = GeneralUtilities.geTemplateFileContentFilled(cpManager.findCustomerProperty(customerId, langId, "FEE-ROW-TEMPLATE"), commissionMap);
					params.put("FeeTemplate", commissionTemplate);
				} else {
					params.put("FeeTemplate", "");
				}
				BigDecimal payableAmount = new BigDecimal(transaction.getAmount()).subtract(new BigDecimal(transaction.getDiscount()));
				if (eventCommissionSettings != null && eventCommissionSettings.getCommissionPayerId() == CommissionPayerSelector.USER) {
					if (transaction.getAmount() == 0) {
						//complimentary
						payableAmount = new BigDecimal(0);
						params.put("Amount", dF.format(0));
					} else {
						payableAmount = payableAmount.add(new BigDecimal(transaction.getCommission()));
					}
				}
				params.put("PayableAmount", dF.format(payableAmount.doubleValue()));
				utilServices.sendEmailWithManager(from, fromName, subject, template, dests, new ArrayList<>(), params, EmailType.PURCHASE_EMAIL);
			}
		} catch (Exception e) {
			throw new ApplicationControllerGeneralException(e);
		}
	}
}
