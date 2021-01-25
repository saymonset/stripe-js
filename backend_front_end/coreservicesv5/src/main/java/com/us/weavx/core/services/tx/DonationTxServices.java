package com.us.weavx.core.services.tx;

import com.us.weavx.core.constants.SystemConstants;
import com.us.weavx.core.data.DonationServicesTxDAO;
import com.us.weavx.core.donationfrequency.DonationFrequencyHandler;
import com.us.weavx.core.exception.DonationGeneralException;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.CustomerUserCreditCard;
import com.us.weavx.core.model.DonationFrequencyDescriptionKey;
import com.us.weavx.core.model.DonationSchedule;
import com.us.weavx.core.model.DonationScheduleStatus;
import com.us.weavx.core.model.DonationStatusDescriptionKey;
import com.us.weavx.core.model.EmailRecipient;
import com.us.weavx.core.model.EmailType;
import com.us.weavx.core.model.FundDescriptionKey;
import com.us.weavx.core.model.PaymentGateway;
import com.us.weavx.core.model.PaymentGatewayInfo;
import com.us.weavx.core.model.PaymentResult;
import com.us.weavx.core.model.ScheduledDonation;
import com.us.weavx.core.model.ScheduledDonationFrequency;
import com.us.weavx.core.model.ScheduledDonationFrequencyDescription;
import com.us.weavx.core.model.ScheduledDonationFundDetail;
import com.us.weavx.core.model.ScheduledDonationHistory;
import com.us.weavx.core.model.ScheduledDonationInfo;
import com.us.weavx.core.model.ScheduledDonationSettingsLang;
import com.us.weavx.core.model.ScheduledDonationSettingsLangKey;
import com.us.weavx.core.model.ScheduledDonationStatus;
import com.us.weavx.core.model.ScheduledDonationStatusDescription;
import com.us.weavx.core.model.Transaction;
import com.us.weavx.core.model.TransactionCampaing;
import com.us.weavx.core.model.TransactionDetail;
import com.us.weavx.core.model.TransactionSource;
import com.us.weavx.core.model.TransactionUserData;
import com.us.weavx.core.model.User;
import com.us.weavx.core.util.CoreServicesClassLoader;
import com.us.weavx.core.util.CustomerPropertyManager;
import com.us.weavx.core.util.DonationFrequencyDescriptionManager;
import com.us.weavx.core.util.DonationFrequencyManager;
import com.us.weavx.core.util.DonationStatusDescriptionManager;
import com.us.weavx.core.util.FundDescriptionManager;
import com.us.weavx.core.util.GeneralUtilities;
import com.us.weavx.core.util.ScheduledDonationSettingsManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("donationTxServices")
public class DonationTxServices {

    @Autowired
    private DonationServicesTxDAO dao;

    @Autowired
    private UserTxServices userServices;

    @Autowired
    private UtilTxServices utilServices;

    @Autowired
    private TransactionTxServices transactionServices;
	
	@Autowired 
	private ApplicationContext context;

	@Autowired
	private DonationFrequencyManager manager;
	
	@Autowired
	private ScheduledDonationSettingsManager settingsManager;
	
	@Autowired
	private CustomerPropertyManager custPropManager;
	
	@Autowired
	private FundDescriptionManager fundManager;		
	
	@Autowired
	private DonationFrequencyDescriptionManager frequencyDescriptionManager;

	@Autowired
	private DonationStatusDescriptionManager statusManager;

	
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<ScheduledDonationFrequency> listAllScheduledDonationFrequencies() {
		return dao.listAllScheduledDonationFrequencies();
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<ScheduledDonationFrequencyDescription> listAllScheduledDonationFrequencyDescriptions() {
		return dao.listAllScheduledDonationFrequencyDescriptions();
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<ScheduledDonationInfo> listAllScheduledDonationsInfo(long customerUserId) {
		List<ScheduledDonationInfo> result = new ArrayList<>();
		List<ScheduledDonation> donations = dao.findScheduledDonationsByCustomerUserId(customerUserId);
		for (ScheduledDonation sd : donations) {
			List<ScheduledDonationFundDetail> funds = dao.findScheduledDonationFunds(sd.getId());
			ScheduledDonationInfo donationInfo = new ScheduledDonationInfo();
			donationInfo.setScheduledDonation(sd);
			donationInfo.setFunds(funds);
			result.add(donationInfo);
		}
		Collections.sort(result);
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public ScheduledDonation addScheduledDonation(long customerId, long applicationId, int langId, ScheduledDonation scheduledDonation, List<ScheduledDonationFundDetail> funds) throws DonationGeneralException {
		try {
			double donationTotal = 0;
			for (ScheduledDonationFundDetail fd : funds) {
				donationTotal += fd.getAmount();
			}
			scheduledDonation.setAmount(donationTotal);
			//Se determina la siguiente fecha de donaci�n
			ScheduledDonationFrequency scheduledDonationFrequency = manager.getScheduledDonationFrequency(scheduledDonation.getDonation_frequency_id());
			Class c = CoreServicesClassLoader.getInstance().loadClass(scheduledDonationFrequency.getFrequency_handler_class(), CoreServicesClassLoader.DONATION_FREQUENCY_HANDLER);
			DonationFrequencyHandler handler = (DonationFrequencyHandler) context.getBean(c);
			scheduledDonation.setNext_date(new Timestamp(handler.findNextDonationDateMillis(scheduledDonation)));
			scheduledDonation.setDonation_status_id(ScheduledDonationStatus.ACTIVE);
			scheduledDonation.setLang_id(langId);
			ScheduledDonation newDonation = dao.registerScheduledDonation(scheduledDonation);
			dao.addScheduledDonationFunds(funds, newDonation.getId());
			//Falta programar la nueva donacion
			DonationSchedule newSchedule = new DonationSchedule();
			newSchedule.setScheduled_date(newDonation.getNext_date());
			newSchedule.setScheduled_donation_id(newDonation.getId());
			newSchedule.setScheduled_status(DonationScheduleStatus.PENDING);
			dao.registerDonationSchedule(newSchedule);
			//Se envia correo informando de la nueva donacion programada
			ScheduledDonationSettingsLang donationSettings = settingsManager.getScheduledDonationSettingsLang(new ScheduledDonationSettingsLangKey(customerId, applicationId, langId));
			String dateFormat = custPropManager.findCustomerProperty(customerId, langId, "DATE_FORMAT");
			String amountFormat = custPropManager.findCustomerProperty(customerId, langId, "AMOUNT_FORMAT");
			DecimalFormat dF = (DecimalFormat) DecimalFormat.getInstance();
			dF.applyPattern(amountFormat);
			SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
			formatter.applyPattern(dateFormat);
			ArrayList<EmailRecipient> dests = new ArrayList<>();
			EmailRecipient recipient = new EmailRecipient();
			CustomerUser cu = userServices.findCustomerUserById(scheduledDonation.getCustomer_user_id());
			User u = userServices.findUserById(cu.getUserId());
			recipient.setEmail(u.getEmail());
			recipient.setName(cu.getFirstName());
			recipient.setRecipientInfo(new HashMap<>());
			dests.add(recipient);
			double total = 0;
			String itemsHtml = "";
			for (ScheduledDonationFundDetail detail : funds) {
				HashMap<String, String> contentToFill = new HashMap<>();
				contentToFill.put("Purpose", fundManager.getFundDescription(customerId, new FundDescriptionKey(detail.getFund_id(), langId)).getFundLabel());
				contentToFill.put("Amount", dF.format(detail.getAmount()));
				String scheduledDonationCreatedEmailRow = GeneralUtilities.geTemplateFileContentFilled(donationSettings.getCreated_email_row_template(), contentToFill);
				itemsHtml+=scheduledDonationCreatedEmailRow;
				total+=detail.getAmount();
			}
			HashMap<String, String> params = new HashMap<>();
			params.put("Name", cu.getFirstName());
			params.put("Items", itemsHtml);
			params.put("Amount", dF.format(total));
			params.put("NextDonation", formatter.format(scheduledDonation.getNext_date()));
			params.put("Frequency", frequencyDescriptionManager.getScheduledDonationFrequency(new DonationFrequencyDescriptionKey(scheduledDonation.getDonation_frequency_id(),scheduledDonation.getLang_id())).getLabel().replace("<br>", " "));
			String subject = new StringBuilder(donationSettings.getCreated_email_subject()).append(" ").append(GeneralUtilities.currentDateTime()).toString();
			utilServices.sendEmailWithManager(donationSettings.getCreated_email_from(), donationSettings.getCreated_email_from_name(), subject, donationSettings.getCreated_email_template(), dests, new ArrayList<>(), params, EmailType.SCHEDULED_PURCHASE_EMAIL);	
			return newDonation;
		} catch (Exception e) {
			throw new DonationGeneralException(e);
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<ScheduledDonationStatus> listAllScheduledDonationStatus() {
		return dao.listAllScheduledDonationStatus();
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<ScheduledDonationStatusDescription> listAllScheduledDonationStatusDescriptions() {
		return dao.listAllScheduledDonationStatusDescriptions();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void updateScheduleDonationStatus(long applicationId, ScheduledDonation donation) throws DonationGeneralException {
		try {
			dao.updateScheduleDonationStatus(donation);
			donation = dao.findScheduledDonationById(donation.getId());
			CustomerUser cu = userServices.findCustomerUserById(donation.getCustomer_user_id());
			User u = userServices.findUserById(cu.getUserId());
			ArrayList<EmailRecipient> dests = new ArrayList<>();
			EmailRecipient recipient = new EmailRecipient();
			recipient.setEmail(u.getEmail());
			recipient.setName(cu.getFirstName());
			recipient.setRecipientInfo(new HashMap<>());
			dests.add(recipient);
			String dateFormat = custPropManager.findCustomerProperty(cu.getCustomerId(), donation.getLang_id(), "DATE_FORMAT");
			String amountFormat = custPropManager.findCustomerProperty(cu.getCustomerId(), donation.getLang_id(), "AMOUNT_FORMAT");
			DecimalFormat dF = (DecimalFormat) DecimalFormat.getInstance();
			dF.applyPattern(amountFormat);
			SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
			formatter.applyPattern(dateFormat);
			ScheduledDonationSettingsLang donationSettings = settingsManager.getScheduledDonationSettingsLang(new ScheduledDonationSettingsLangKey(cu.getCustomerId(), applicationId, donation.getLang_id()));
			HashMap<String, String> emailParameters = new HashMap<>();
			emailParameters.put("Name", cu.getFirstName());
			emailParameters.put("NextDate", formatter.format(donation.getNext_date()));
			emailParameters.put("Status", statusManager.getScheduledDonationStatusDescription(new DonationStatusDescriptionKey(donation.getDonation_status_id(),donation.getLang_id())).getLabel());
			emailParameters.put("Login", custPropManager.findCustomerProperty(cu.getCustomerId(), donation.getLang_id(), "AUTH_URL"));
			String subject = new StringBuilder(donationSettings.getStatus_change_email_subject()).append(" ").append(GeneralUtilities.currentDateTime()).toString();
			utilServices.sendEmailWithManager(donationSettings.getStatus_change_email_from(), donationSettings.getStatus_change_email_from_name(), subject, donationSettings.getStatus_change_email_template(), dests, new ArrayList<>(), emailParameters, EmailType.NOTIFICATION_EMAIL);		
		} catch (Exception e) {
			throw new DonationGeneralException(e);
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<ScheduledDonationSettingsLang> listAllScheduledDonationSettingsLang() {
		return dao.listAllScheduledDonationSettingsLang();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void proccessScheduledDonations(long applicationId) {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		Timestamp from = new Timestamp(now.getTimeInMillis());
		now.add(Calendar.DAY_OF_MONTH, 1);
		Timestamp to = new Timestamp(now.getTimeInMillis());
		List<DonationSchedule> donations = dao.findDonationsScheduledBetween(from, to);
		final Logger logger
        = Logger.getLogger(DonationTxServices.class);
		for (DonationSchedule d : donations) {
			try {
				ScheduledDonation donation = dao.findScheduledDonationById(d.getScheduled_donation_id());
				if (donation.getDonation_status_id() == ScheduledDonationStatus.ACTIVE) {
					CustomerUser cu = userServices.findCustomerUserById(donation.getCustomer_user_id());
					User u = userServices.findUserById(cu.getUserId());
					//Creates TransactionUserData object
					TransactionUserData txUserData = new TransactionUserData();
					txUserData.setName(cu.getFirstName());
					txUserData.setLastname(cu.getLastName());
					txUserData.setAddress(cu.getAddress());
					txUserData.setCity(cu.getCityId());
					txUserData.setCityText(cu.getCityText());
					txUserData.setCountry(cu.getCountryId());
					txUserData.setCountryText("");
					txUserData.setEmail(u.getEmail());
					txUserData.setPostcode(cu.getPostalCode());
					txUserData.setState(cu.getStatedId());
					txUserData.setStateText(cu.getStateText());
					txUserData.setCustomerUserId(cu.getId());
					//Creates Payment Gateway Info
					HashMap<String, Object> paymentGwParameters = new HashMap<>();
					int paymentGwId = PaymentGateway.STRIPE; //Only Supports Stripe at this time
					List<CustomerUserCreditCard> userCards = transactionServices.listCustomerUserCardsByPaymentGateway(cu.getId(), cu.getCustomerId(), paymentGwId);
					CustomerUserCreditCard defaultCard = findDefaultCard(userCards);
					if (defaultCard == null) {
						throw new Exception("No default credit card for user.");
					}
					paymentGwParameters.put("paymentInstrumentId", defaultCard.getCardId());
					switch (donation.getLang_id()) {
					case 1:
						paymentGwParameters.put("orderDescription", "Donaci�n Programada");
						break;
					case 2:
						paymentGwParameters.put("orderDescription", "Scheduled Donation");
						break;
					default:
						paymentGwParameters.put("orderDescription", "Scheduled Donation");
					}
					PaymentGatewayInfo paymentGwInfo = new PaymentGatewayInfo();
					paymentGwInfo.setPaymentGatewayId(paymentGwId);
					paymentGwInfo.setPaymentGwParameters(paymentGwParameters);
					//Creates Transaction and Details object
					List<ScheduledDonationFundDetail> donationFunds = dao.findScheduledDonationFunds(donation.getId());
					ArrayList<TransactionDetail> txDetails = new ArrayList<>();
					for (ScheduledDonationFundDetail donationFund : donationFunds) {
						TransactionDetail txDetailTmp = new TransactionDetail();
						txDetailTmp.setFundId(donationFund.getFund_id());
						txDetailTmp.setAmount(donationFund.getAmount());
						txDetails.add(txDetailTmp);
					}
					Transaction transaction = new Transaction();
					transaction.setCustomerId(cu.getCustomerId());
					transaction.setApplicationId(applicationId); 
					transaction.setAmount(donation.getAmount());
					transaction.setLangId(donation.getLang_id());
					transaction.setSourceId(TransactionSource.SCHEDULED);
					transaction.setCampaingId(TransactionCampaing.NO_CAMPAING);
					PaymentResult txResult = transactionServices.proccessCreditCardTransaction(transaction, txDetails, paymentGwInfo, txUserData, null, donation.getLang_id(), null, "SCHEDULED", "N/A");
					long currentMillis = System.currentTimeMillis();
					boolean successDonation = false;
					switch (txResult.getResult()) {
					case PaymentResult.APPROVED:
						//Mark scheduled donation try as success and schedule a new donation for next date
						successDonation = true;
						donation.setLast_successful(new Timestamp(currentMillis));
						ScheduledDonationFrequency scheduledDonationFrequency = manager.getScheduledDonationFrequency(donation.getDonation_frequency_id());
						Class c = CoreServicesClassLoader.getInstance().loadClass(scheduledDonationFrequency.getFrequency_handler_class(), CoreServicesClassLoader.DONATION_FREQUENCY_HANDLER);
						DonationFrequencyHandler handler = (DonationFrequencyHandler) context.getBean(c);
						long nextDonationMillis = handler.findNextDonationDateMillis(donation);
						donation.setNext_date(new Timestamp(nextDonationMillis));
						//Agendar donacion para proxima fecha
						d.setScheduled_date(new Timestamp(nextDonationMillis));
						d.setExecuted_at(new Timestamp(currentMillis));
						dao.updateDonationSchedule(d);
						logger.info("Donation->"+d+"|OK");
						break;
					case PaymentResult.DENIED:
						logger.info("Donation->"+d+"|PAYMENT DENIED");
						donation.setLast_failed(new Timestamp(currentMillis));
						//Send failed card email to user
						//Se envia correo informando de la nueva donacion programada
						ScheduledDonationSettingsLang donationSettings = settingsManager.getScheduledDonationSettingsLang(new ScheduledDonationSettingsLangKey(cu.getCustomerId(), applicationId, donation.getLang_id()));
						String dateFormat = custPropManager.findCustomerProperty(cu.getCustomerId(), donation.getLang_id(), "DATE_FORMAT");
						String amountFormat = custPropManager.findCustomerProperty(cu.getCustomerId(), donation.getLang_id(), "AMOUNT_FORMAT");
						DecimalFormat dF = (DecimalFormat) DecimalFormat.getInstance();
						dF.applyPattern(amountFormat);
						SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
						formatter.applyPattern(dateFormat);
						ArrayList<EmailRecipient> dests = new ArrayList<>();
						EmailRecipient recipient = new EmailRecipient();
						recipient.setEmail(u.getEmail());
						recipient.setName(cu.getFirstName());
						recipient.setRecipientInfo(new HashMap<>());
						dests.add(recipient);
						HashMap<String, String> emailParameters = new HashMap<>();
						emailParameters.put("Name", cu.getFirstName());
						emailParameters.put("Time", "4");
						emailParameters.put("Date", formatter.format(new Date(currentMillis)));
						String subject = new StringBuilder(donationSettings.getFailed_email_subject()).append(" ").append(GeneralUtilities.currentDateTime()).toString();
						utilServices.sendEmailWithManager(donationSettings.getStatus_change_email_from(), donationSettings.getFailed_email_from_name(), subject, donationSettings.getFailed_email_template(), dests, new ArrayList<>(), emailParameters, EmailType.NOTIFICATION_EMAIL);
						logger.info("Donation->"+d+"|PAYMENT DENIED EMAIL SENT");
						break;
					default:
						//Failed Donation
						donation.setLast_failed(new Timestamp(currentMillis));
						logger.info("Donation->"+d+"|FAILED DONATION");
					}
					//Update donation
					dao.updateDonation(donation);
					logger.info("Donation->"+d+"|DONATION UPDATED");
					//Register donation in history
					ScheduledDonationHistory hist = new ScheduledDonationHistory();
					hist.setExecuted_at(new Timestamp(currentMillis));
					hist.setScheduled_donation_id(donation.getId());
					hist.setSuccess_donation(successDonation);
					hist.setTransaction_id((long)txResult.getAuthorizationInfo().get(SystemConstants.TRANSACTION));
					dao.registerDonationScheduleHistory(hist);
					logger.info("Donation->"+d+"|DONATION HISTORY CREATED");
				} 
			} catch (Exception e) {
				logger.error("Donation->"+d+" Error->"+e);
			}
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void notifyNextDonations(long applicationId) {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		Timestamp to = new Timestamp(now.getTimeInMillis());
		now.add(Calendar.DAY_OF_MONTH, -2);
		Timestamp from = new Timestamp(now.getTimeInMillis());
		List<DonationSchedule> donations = dao.findDonationsScheduledBetween(from, to);
		final Logger logger
        = Logger.getLogger(DonationTxServices.class);
		for (DonationSchedule d : donations) {
			try {
				ScheduledDonation donation = dao.findScheduledDonationById(d.getScheduled_donation_id());
				logger.info("Processing 48h alert for: "+donation);
				List<ScheduledDonationFundDetail> funds = dao.findScheduledDonationFunds(d.getScheduled_donation_id());
				String fundDescriptions = null;
				CustomerUser cu = userServices.findCustomerUserById(donation.getCustomer_user_id());
				User u = userServices.findUserById(cu.getUserId());
				for (ScheduledDonationFundDetail detailTmp : funds) {
					String fundLabel = fundManager.getFundDescription(cu.getCustomerId(), new FundDescriptionKey(detailTmp.getFund_id(), donation.getLang_id())).getFundLabel();
					fundDescriptions = (fundDescriptions==null)?fundLabel:fundDescriptions+", "+fundLabel;
				}
				//Se envia correo informando de la nueva donacion programada
				ScheduledDonationSettingsLang donationSettings = settingsManager.getScheduledDonationSettingsLang(new ScheduledDonationSettingsLangKey(cu.getCustomerId(), applicationId, donation.getLang_id()));
				String amountFormat = custPropManager.findCustomerProperty(cu.getCustomerId(), donation.getLang_id(), "AMOUNT_FORMAT");
				DecimalFormat dF = (DecimalFormat) DecimalFormat.getInstance();
				dF.applyPattern(amountFormat);
				ArrayList<EmailRecipient> dests = new ArrayList<>();
				EmailRecipient recipient = new EmailRecipient();
				recipient.setEmail(u.getEmail());
				recipient.setName(cu.getFirstName());
				recipient.setRecipientInfo(new HashMap<>());
				dests.add(recipient);
				HashMap<String, String> emailParameters = new HashMap<>();
				emailParameters.put("Name", cu.getFirstName());
				emailParameters.put("Time", "48");
				emailParameters.put("Purposes", fundDescriptions);
				emailParameters.put("Amount", dF.format(donation.getAmount()));
				String subject = new StringBuilder(donationSettings.getConfirmation_before_email_subject()).append(" ").append(GeneralUtilities.currentDateTime()).toString();
				utilServices.sendEmailWithManager(donationSettings.getConfirmation_before_email_from(), donationSettings.getConfirmation_before_email_from_name(), subject, donationSettings.getConfirmation_before_email_template(), dests, new ArrayList<>(), emailParameters, EmailType.NOTIFICATION_EMAIL);
				logger.info("send48HEmail: Donation->"+d+"|48H PREVIOUS ALERT SENT");
			} catch (Exception e) {
				logger.error("send48HEmail: Donation->"+d+" Error->"+e);
			}
		}
	}
	
		
	private CustomerUserCreditCard findDefaultCard(List<CustomerUserCreditCard> userCards) {
		for (CustomerUserCreditCard card : userCards) {
			if (card.isDefault()) {
				return card;
			}
		}
		return null;
	}
}
