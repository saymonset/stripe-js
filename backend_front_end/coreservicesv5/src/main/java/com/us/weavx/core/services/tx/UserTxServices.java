package com.us.weavx.core.services.tx;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.us.weavx.core.constants.TokenStatus;
import com.us.weavx.core.data.CoreServicesTxDAO;
import com.us.weavx.core.exception.AlreadyUsedAccessTokenException;
import com.us.weavx.core.exception.CoreServicesGeneralException;
import com.us.weavx.core.exception.EmailSendingGeneralException;
import com.us.weavx.core.exception.ExpiredOTPException;
import com.us.weavx.core.exception.ExpiredUserAccessTokenException;
import com.us.weavx.core.exception.InvalidOTPCodeException;
import com.us.weavx.core.exception.InvalidUserAccessTokenException;
import com.us.weavx.core.exception.InvalidUserCredentialsException;
import com.us.weavx.core.exception.MaximumCustomerUserSessionsForAppExceededException;
import com.us.weavx.core.exception.MaximumOTPValidationTriesExceededException;
import com.us.weavx.core.exception.OTPIllegalStatusException;
import com.us.weavx.core.exception.RequiredApplicationParameterNotFoundException;
import com.us.weavx.core.exception.UnknownApplicationException;
import com.us.weavx.core.exception.UnknownCustomerPropertyException;
import com.us.weavx.core.exception.UnknownCustomerUserException;
import com.us.weavx.core.exception.UnknownNotificationTemplateException;
import com.us.weavx.core.exception.UnknownOTPException;
import com.us.weavx.core.exception.UserAccessTokenGenerationException;
import com.us.weavx.core.model.AdminRoleFunctions;
import com.us.weavx.core.model.Application;
import com.us.weavx.core.model.AuthenticatedUserInfo;
import com.us.weavx.core.model.CustomerUser;
import com.us.weavx.core.model.CustomerUserAdminProfile;
import com.us.weavx.core.model.CustomerUserPaymentProfile;
import com.us.weavx.core.model.CustomerUserWIthEmail;
import com.us.weavx.core.model.EmailRecipient;
import com.us.weavx.core.model.EmailType;
import com.us.weavx.core.model.ExternalProfile;
import com.us.weavx.core.model.OTPKey;
import com.us.weavx.core.model.Transaction;
import com.us.weavx.core.model.User;
import com.us.weavx.core.model.UserAccessToken;
import com.us.weavx.core.model.UserAccessTokenInfo;
import com.us.weavx.core.model.ValidationCode;
import com.us.weavx.core.services.exception.ClosedByUserAccessTokenException;
import com.us.weavx.core.util.ApplicationParametersManager;
import com.us.weavx.core.util.CoreServicesSecurity;
import com.us.weavx.core.util.CustomerPropertyManager;
import com.us.weavx.core.util.GeneralUtilities;
import com.us.weavx.core.util.OTPGeneratorUtil;
import com.us.weavx.core.util.OTPKeyStatusSelector;
import com.us.weavx.core.util.TokenGeneratorUtil;
@Service
public class UserTxServices {
	
	@Autowired
	private CoreServicesTxDAO dao;
	
	@Autowired
	private AdminTxServices adminServices;
	
	@Autowired
	private TransactionTxServices txServices;
	
	@Autowired
	private UtilTxServices utilServices;
		
	@Autowired
	private CustomerPropertyManager custPropManager;
	
	@Autowired
	private ApplicationParametersManager appParamManager;

	
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=false)
	private int getActiveApplicationSessionsActiveByCustomerUser(long applicationId, long customerUserId) throws MaximumCustomerUserSessionsForAppExceededException, CoreServicesGeneralException {
		//Se valida si se ha excedido la cantidad de sesiones permitidas por usuario
		String maxSessionsAllowed = appParamManager.getApplicationParameter(applicationId, "MAX_SESSIONS");
		if (maxSessionsAllowed == null) {
			throw new CoreServicesGeneralException("Please define MAX_SESSIONS parameter.");
		}
		//Se verifica si el customer_user tiene asociado el rol de ADMIN para saber si debe o no ser restringido su numero de sesiones
		boolean restrictedUser = true;
		try {
			CustomerUserAdminProfile userProfile = adminServices.findCustomerUserAdminProfile(customerUserId);
			for (AdminRoleFunctions aRF : userProfile.getAdminRoleFunctions()) {
				if (aRF.getAdminRole().getName().equals("ADMIN_ACCESS")) {
					restrictedUser = false;
					break;
				}
			}
		} catch (Exception e1) {
			//ignore
		}
		final int MAX_SESSIONS = Integer.parseInt(maxSessionsAllowed);
		List<UserAccessToken> currentSessions = dao.findActiveUserSessionAccessTokens(customerUserId, applicationId);
		ArrayList<UserAccessToken> invalidSessions = new ArrayList<>();
		for (UserAccessToken uAT: currentSessions) {
			try {
				uAT = validateUserAccessToken(uAT);
			} catch (Exception e) {
				invalidSessions.add(uAT);
			}
		}
		currentSessions.removeAll(invalidSessions);
		if (restrictedUser && currentSessions.size() >= MAX_SESSIONS) {
			throw new MaximumCustomerUserSessionsForAppExceededException("Max sessions exceeded.", currentSessions);
		}
		return currentSessions.size();
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public UserAccessToken createNewCustomerUserSessionToken(long customerUserId, long applicationId, String ipAddress, String userAgent, int initialStatus) throws UserAccessTokenGenerationException, MaximumCustomerUserSessionsForAppExceededException {
		try {
			getActiveApplicationSessionsActiveByCustomerUser(applicationId, customerUserId);
		} catch (CoreServicesGeneralException e) {
			throw new UserAccessTokenGenerationException(e);
		}
		//Se crea la nueva sesion
		//Se obtiene informacion de la aplicacion
		Application app = dao.findApplicationInfo(applicationId);
		UserAccessToken newUserAccessToken = null;
		for (int i = 0; i < 10; i++) {
			String userAccessToken = TokenGeneratorUtil.generateToken();
			newUserAccessToken = new UserAccessToken();
			newUserAccessToken.setApplicationId(applicationId);
			newUserAccessToken.setCustomerUserId(customerUserId);
			newUserAccessToken.setToken(userAccessToken);
			newUserAccessToken.setIpAddress(ipAddress);
			newUserAccessToken.setUserAgent(userAgent);
			newUserAccessToken.setStatus(initialStatus);
			//Se debe registar el nuevo accessToken
			newUserAccessToken = dao.registerNewUserAccessToken(newUserAccessToken, app.getShortUserTokenDuration());
			if (newUserAccessToken != null) {
				break;
			}
		}
		if (newUserAccessToken == null) {
			throw new UserAccessTokenGenerationException();
		} 
		return newUserAccessToken;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	private UserAccessToken validateUserAccessToken(UserAccessToken uAT) throws InvalidUserAccessTokenException, ExpiredUserAccessTokenException {
		if (uAT == null || uAT.getStatus() == TokenStatus.EXPIRED || uAT.getStatus() == TokenStatus.BLOCKED || uAT.getStatus() == TokenStatus.CLOSED_BY_USER) {
			throw new InvalidUserAccessTokenException();
		} else {
			if (uAT.getExpiresAt().before(Timestamp.valueOf(LocalDateTime.now()))) {
				uAT.setStatus(TokenStatus.EXPIRED);
				dao.changeUserAccessTokenStatus(uAT);
				throw new ExpiredUserAccessTokenException();
			} else {
				return uAT;
			}
		}
	}

	@Transactional(propagation=Propagation.SUPPORTS,readOnly=false)
	public UserAccessToken validateUserAccessToken(String userAccessToken) throws InvalidUserAccessTokenException, ExpiredUserAccessTokenException {
		UserAccessToken uAT = dao.getUserTokenInfo(userAccessToken);
		return validateUserAccessToken(uAT);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public UserAccessToken getUserTokenInfo(String userAccessToken) {
		return dao.getUserTokenInfo(userAccessToken);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<UserAccessTokenInfo> getUserAccessTokenInfoByEvent(String userEmail, long customerId, long applicationId) {
		return dao.getUserAccessTokenInfoByEvent(userEmail, customerId, applicationId);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public AuthenticatedUserInfo authenticateCustomerUser(long customerId, String email, String password, long applicationId, String ipAddress, String userAgent) throws InvalidUserCredentialsException, UnknownApplicationException, UserAccessTokenGenerationException, MaximumCustomerUserSessionsForAppExceededException {
		//Encriptar password
		String encryptedPassword = CoreServicesSecurity.encryptPassword(password);
		//Buscar usuario
		CustomerUser custUser = dao.findCustomerUserByAccessCredentials(email, encryptedPassword, customerId);
		if (custUser == null) {
			throw new InvalidUserCredentialsException();
		}
		//Generar y registrar UserAccessToken
		UserAccessToken uAT = null;
		try {
			uAT = this.createNewCustomerUserSessionToken(custUser.getId(), applicationId, ipAddress, userAgent, TokenStatus.VALID);
		} catch (UserAccessTokenGenerationException e) {
			throw e;
		}
		CustomerUserWIthEmail cuEmail = new CustomerUserWIthEmail(custUser, email);
		return new AuthenticatedUserInfo(uAT, cuEmail);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public boolean authenticateCustomerUserByEmail(String email, long customerId, long applicationId, String ipAddress, String userAgent, int langId) throws UnknownCustomerUserException, UnknownNotificationTemplateException, EmailSendingGeneralException, UnknownApplicationException, UserAccessTokenGenerationException, MaximumCustomerUserSessionsForAppExceededException, UnknownCustomerPropertyException {
		return authenticateCustomerUserByEmailV2(email, customerId, applicationId, ipAddress, userAgent, langId); 
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public boolean authenticateCustomerUserByEmailV2(String email, long customerId, long applicationId, String ipAddress, String userAgent, int langId) throws UnknownCustomerUserException, UnknownNotificationTemplateException, EmailSendingGeneralException, UnknownApplicationException, UserAccessTokenGenerationException, MaximumCustomerUserSessionsForAppExceededException, UnknownCustomerPropertyException {
		CustomerUser cu = dao.findCustomerUserByEmail(email, customerId);
		boolean result = false;
		if (cu == null) {
			throw new UnknownCustomerUserException();
		}
		//Generar userAccessToken
		UserAccessToken uAT = null;
		try {
			uAT = this.createNewCustomerUserSessionToken(cu.getId(), applicationId, ipAddress, userAgent, TokenStatus.PENDING_VALIDATION);
		} catch (UserAccessTokenGenerationException e) {
			throw e;
		}
		//Validar si tiene compras el usuario indicado
		List<Transaction> txs = txServices.findCustomerUserSuccessTransactionsByApplication(customerId, cu.getId(), applicationId);
		String template = null;
		String subject = null;
		int emailType = 0;
		if (txs != null && txs.size() > 0) {
			//Tiene compras exitosas
			result = true;
			template = custPropManager.findCustomerProperty(customerId, langId, "AUTH_EMAIL_TEMPLATE"); 
			subject = custPropManager.findCustomerProperty(customerId, langId, "AUTH_EMAIL_SUBJECT");
			emailType = EmailType.AUTHENTICATION_EMAIL;
		} else {
			//No tiene compras exitosas
			template = custPropManager.findCustomerProperty(customerId, langId, "PERFORM_PURCHASE_EMAIL_TEMPLATE");
			subject = custPropManager.findCustomerProperty(customerId, langId, "PERFORM_PURCHASE_EMAIL_SUBJECT");
			emailType = EmailType.INCOMPLETE_PURCHASE_EMAIL;
		}
		subject = subject.replaceAll(".nombre.", cu.getFirstName());
	
		//subject = configurationServices.findCustomerProperty(customerId, langId, "AUTH_EMAIL_SUBJECT").getPropertyValue();
		//Se le suma timestamp
		subject = new StringBuilder(subject).append(" ").append(GeneralUtilities.currentDateTime()).toString();
		
		String url = custPropManager.findCustomerProperty(customerId, langId, "AUTH_EMAIL_URL");
		String fromName = custPropManager.findCustomerProperty(customerId, langId, "AUTH_EMAIL_FROM_NAME");
		String from = custPropManager.findCustomerProperty(customerId, langId, "AUTH_EMAIL_FROM");
		HashMap<String, String> parameters = new HashMap<>();
		String token = uAT.getToken();
		if (langId != 0) {
			StringBuilder sB = new StringBuilder(token);
			sB.append("?l=").append(langId);
			token = sB.toString();
		}
		ArrayList<EmailRecipient> dests = new ArrayList<>();
		EmailRecipient tmpEMail = new EmailRecipient();
		tmpEMail.setEmail(email);
		tmpEMail.setName(cu.getFirstName());
		Map<String, String> recipientInfo = new HashMap<>();
		recipientInfo.put("Name", cu.getFirstName());
		recipientInfo.put("URL", url);
		recipientInfo.put("TOKEN", token);
		recipientInfo.put("Lastname", cu.getLastName());
		tmpEMail.setRecipientInfo(recipientInfo);
		dests.add(tmpEMail);
		utilServices.sendEmailWithManager(from, fromName, subject, template, dests, new ArrayList<>(), parameters, emailType);
		return result;
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=false)
	public AuthenticatedUserInfo authenticateUserWithAccessToken(String userAccessToken, long customerId) throws InvalidUserAccessTokenException, ExpiredUserAccessTokenException, AlreadyUsedAccessTokenException, ClosedByUserAccessTokenException, MaximumCustomerUserSessionsForAppExceededException {
		UserAccessToken uAT = this.validateUserAccessToken(userAccessToken);
		CustomerUserWIthEmail cuEmail = dao.findCustomerUserWIthEmailById(uAT.getCustomerUserId(), customerId);
		CustomerUser cu = cuEmail.getCustUser();
		if (cu == null) {
			throw new InvalidUserAccessTokenException();
		}
		switch (uAT.getStatus()) {
		case TokenStatus.PENDING_VALIDATION: case TokenStatus.VALID:	
			//TOken valido pero debe revisarse que no haya excedido las sesiones
			try {
				getActiveApplicationSessionsActiveByCustomerUser(uAT.getApplicationId(), uAT.getCustomerUserId());
			} catch (MaximumCustomerUserSessionsForAppExceededException e) {
				e.setTokenOwner(cuEmail);
				throw e;
			}
			//Al llegar aqui no ha excedido las sesiones.
			uAT.setStatus(TokenStatus.ACTIVE);
			dao.changeUserAccessTokenStatus(uAT);
			return new AuthenticatedUserInfo(uAT,cuEmail);
		case TokenStatus.ACTIVE:
			AlreadyUsedAccessTokenException e = new AlreadyUsedAccessTokenException();
			e.setTokenOwner(cuEmail);
			throw e;
		case TokenStatus.EXPIRED:
			ExpiredUserAccessTokenException e2 = new ExpiredUserAccessTokenException();
			e2.setTokenOwner(cuEmail);
			throw e2;
		case TokenStatus.CLOSED_BY_USER:
			ClosedByUserAccessTokenException e3 = new ClosedByUserAccessTokenException();
			e3.setTokenOwner(cuEmail);
			throw e3;
		default: 
			throw new InvalidUserAccessTokenException();
		}
	}
		
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=false)
	public AuthenticatedUserInfo authenticateExternalUser(long customerUserId, long customerId, long applicationId, String ipAddress, String userAgent) throws UnknownCustomerUserException, UserAccessTokenGenerationException, MaximumCustomerUserSessionsForAppExceededException {	
		CustomerUserWIthEmail cu = dao.findCustomerUserWIthEmailById(customerUserId, customerId);
		if (cu == null) {
			throw new UnknownCustomerUserException();
		}
		UserAccessToken uAT = this.createNewCustomerUserSessionToken(customerUserId, applicationId, ipAddress, userAgent, TokenStatus.ACTIVE);
		return new AuthenticatedUserInfo(uAT,cu);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=false)
	public AuthenticatedUserInfo authenticateCampaingUser(long customerUserId, long customerId, long applicationId, String ipAddress, String userAgent) throws UnknownCustomerUserException, UserAccessTokenGenerationException, MaximumCustomerUserSessionsForAppExceededException {	
		CustomerUserWIthEmail cu = dao.findCustomerUserWIthEmailById(customerUserId, customerId);
		if (cu == null) {
			throw new UnknownCustomerUserException();
		}
		UserAccessToken uAT = this.createNewCustomerUserSessionToken(customerUserId, applicationId, ipAddress, userAgent, TokenStatus.PENDING_VALIDATION);
		return new AuthenticatedUserInfo(uAT,cu);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=false)
	public User findUserByEmail(String email) {
		email = email.toLowerCase();
		return dao.findUserByEmail(email);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=false)
	public User findUserById(long userId) {
		return dao.findUserById(userId);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public User findUserByCustomerUserId(final long customerUserId) {
		return dao.findUserByCustomerUserId(customerUserId);
	}
	
	
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=false)
	public Long existsCustomerUser(long userId, long customerId) {
		return dao.findCustomerUserIdByUserId(userId, customerId);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public User registerNewUser(User user) {
		return dao.registerNewUser(user);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public AuthenticatedUserInfo registerNewCustomerUser(CustomerUser user, long applicationId, String ipAddress, String userAgent) {
		CustomerUser newUser = dao.registerNewCustomerUser(user);
		User u = dao.findUserById(newUser.getUserId());
		UserAccessToken token = null;
		try {
			token = this.createNewCustomerUserSessionToken(newUser.getId(), applicationId, ipAddress, userAgent, TokenStatus.PENDING_VALIDATION);
		} catch (UserAccessTokenGenerationException e) {
			e.printStackTrace();
		} catch (MaximumCustomerUserSessionsForAppExceededException e) {
			e.printStackTrace();
		}
		CustomerUserWIthEmail cuEmail = new CustomerUserWIthEmail(newUser, u.getEmail());
		AuthenticatedUserInfo uInfo = new AuthenticatedUserInfo(token,cuEmail);
		return uInfo;
	}	
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public AuthenticatedUserInfo updateCustomerUser(CustomerUser user, long applicationId, String ipAddress, String userAgent) throws MaximumCustomerUserSessionsForAppExceededException, UnknownCustomerUserException {
		CustomerUser updatedUser = dao.updateCustomerUser(user);
		if (updatedUser == null) {
			throw new UnknownCustomerUserException();
		}
		User u = dao.findUserByCustomerUserId(updatedUser.getId());
		UserAccessToken token = null;
		try {
			List<UserAccessToken> currentSessions = dao.findActiveUserSessionAccessTokens(user.getId(), applicationId);
			ArrayList<String> userAccessTokensStrList = new ArrayList<>();
			for (UserAccessToken tok: currentSessions) {
				userAccessTokensStrList.add(tok.getToken());
			}
			this.invalidateUserAccessToken(userAccessTokensStrList, applicationId);
			token = this.createNewCustomerUserSessionToken(updatedUser.getId(), applicationId, ipAddress, userAgent, TokenStatus.PENDING_VALIDATION);
		} catch (UserAccessTokenGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CustomerUserWIthEmail cuEmail = new CustomerUserWIthEmail(updatedUser,u.getEmail());
		AuthenticatedUserInfo uInfo = new AuthenticatedUserInfo(token,cuEmail);
		return uInfo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public AuthenticatedUserInfo updateCustomerUserPassword(CustomerUser user, long applicationId, String ipAddress, String userAgent) throws MaximumCustomerUserSessionsForAppExceededException, UnknownCustomerUserException {
		CustomerUser updatedUser = dao.updateCustomerUserPassword(user);
		if (updatedUser == null) {
			throw new UnknownCustomerUserException();
		}
		User u = dao.findUserByCustomerUserId(updatedUser.getId());
		UserAccessToken token = null;
		try {
			List<UserAccessToken> currentSessions = dao.findActiveUserSessionAccessTokens(user.getId(), applicationId);
			ArrayList<String> userAccessTokensStrList = new ArrayList<>();
			for (UserAccessToken tok: currentSessions) {
				userAccessTokensStrList.add(tok.getToken());
			}
			this.invalidateUserAccessToken(userAccessTokensStrList, applicationId);
			token = this.createNewCustomerUserSessionToken(updatedUser.getId(), applicationId, ipAddress, userAgent, TokenStatus.PENDING_VALIDATION);
		} catch (UserAccessTokenGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CustomerUserWIthEmail cuEmail = new CustomerUserWIthEmail(updatedUser,u.getEmail());
		AuthenticatedUserInfo uInfo = new AuthenticatedUserInfo(token,cuEmail);
		return uInfo;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public Long findCustomerUserIdByEmail(String email, long customerId) {
		email = email.toLowerCase();
		return dao.findCustomerUserIdByEmail(email, customerId);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public CustomerUser findCustomerUserByUserId(long userId, long customerId) {
		return dao.findCustomerUserByUserId(userId, customerId);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public CustomerUser findCustomerUserById(long customerUserId) {
		return dao.findCustomerUserById(customerUserId);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public ExternalProfile findUserExternalProfile(int identityProviderId, long userId) {
		return dao.findUserExternalProfile(identityProviderId, userId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public ExternalProfile registerNewExternalUser(ExternalProfile user) {
		return dao.registerNewExternalUser(user);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void  updateExternalUserProfileToken(final ExternalProfile user) {
		dao.updateExternalUserProfileToken(user);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public int invalidateUserAccessToken(List<String> tokens, long applicationId) {
		int invalidatedTokens = 0;
		for (String s: tokens) {
			UserAccessToken uAT = dao.getUserTokenInfo(s);
			if (uAT != null) {
				try {
					if (uAT.getApplicationId() == applicationId) {
						uAT.setStatus(TokenStatus.CLOSED_BY_USER);
						dao.changeUserAccessTokenStatus(uAT);
						invalidatedTokens++;
					} 
				} catch (DataAccessException e) {
					
				}
			} 
		}
		return invalidatedTokens;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void closeAllCustomerUserSessions(long customerUserId) {
		dao.closeAllCustomerUserSessions(customerUserId);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void updateUserAccessTokenStatus(UserAccessToken uAT) {
		dao.changeUserAccessTokenStatus(uAT);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public UserAccessToken createAccessTokenForForeignApp(String foreignAppUserAccessToken, long applicationId, long customerId) throws InvalidUserAccessTokenException, ExpiredUserAccessTokenException, UserAccessTokenGenerationException, MaximumCustomerUserSessionsForAppExceededException {
		UserAccessToken validAppToken = validateUserAccessToken(foreignAppUserAccessToken);
		if (validAppToken.getApplicationId() == applicationId) {
			//SI la aplicacion es la misma se devuelve el mismo token
			return validAppToken;
		}
		//Se verifica si existe el customer user y el user asociados al userAccessToken
		User foreignU = dao.findUserByCustomerUserId(validAppToken.getCustomerUserId());
		if (foreignU == null) {
			throw new InvalidUserAccessTokenException("foreign user not valid.");
		}
		CustomerUser foreignCU = dao.findCustomerUserById(validAppToken.getCustomerUserId());
		if (foreignCU == null) {
			throw new InvalidUserAccessTokenException("foreign customer user not valid.");
		}
		//Se valida que exista el user y el customer user en el cliente destino.
		CustomerUser currentCU = dao.findCustomerUserByEmail(foreignU.getEmail(), customerId);
		if (currentCU == null) {
			//Se registra el nuevo customer user
			
			try {
				currentCU = (CustomerUser) foreignCU.clone();
			} catch (CloneNotSupportedException e) {
				throw new InvalidUserAccessTokenException("not cloneable customer user.");
			}
			currentCU.setUserId(foreignU.getId());
			currentCU.setCustomerId(customerId);
			currentCU = dao.registerNewCustomerUser(currentCU);
			if (currentCU == null) {
				throw new UserAccessTokenGenerationException("Can't create customer user in current app.");
			}
		}
		//Se verifica que el cliente posea perfil en stripe
		CustomerUserPaymentProfile foreignCUPP = dao.findCustomerPaymentProfile(foreignCU.getId(), 2);
		if (foreignCUPP == null) {
			throw new InvalidUserAccessTokenException("user doesn't have stripe payment profile.");
		}
		//Se verifica que el cliente posea perfil para stripe en el nuevo cliente.
		CustomerUserPaymentProfile currentCUPP = dao.findCustomerPaymentProfile(currentCU.getId(), 2);
		if (currentCUPP == null) {
			//Se crea el perfil stripe en el nuevo cliente
			currentCUPP = foreignCUPP;
			currentCUPP.setCustomerId(customerId);
			currentCUPP.setCustomerUserId(currentCU.getId());
			currentCUPP = dao.addCustomerUserPaymentProfile(currentCUPP);
			if (currentCUPP == null) {
				throw new UserAccessTokenGenerationException("Can't create customer payment profile.");
			}
		}
		//Se crea un nuevo token asociado a la nueva app
		UserAccessToken newToken = createNewCustomerUserSessionToken(currentCU.getId(), applicationId, validAppToken.getIpAddress(), validAppToken.getUserAgent(), TokenStatus.PENDING_VALIDATION);
		return newToken;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public OTPKey generateAuthenticationOTP(String email, long customerId, long applicationId,  int langId, boolean isOTPEmailRequired) throws UnknownCustomerUserException {
		try {
			email = email.toLowerCase();
			CustomerUser cu = dao.findCustomerUserByEmail(email, customerId);
			if (cu == null) {
				throw new UnknownCustomerUserException();
			}
			String template = custPropManager.findCustomerProperty(customerId, langId, "OTP_EMAIL_TEMPLATE"); 
			String subject = custPropManager.findCustomerProperty(customerId, langId, "OTP_EMAIL_SUBJECT");
			int emailType = EmailType.AUTHENTICATION_EMAIL;
			subject = subject.replaceAll(".nombre.", cu.getFirstName());
			String fromName = custPropManager.findCustomerProperty(customerId, langId, "OTP_EMAIL_FROM_NAME");
			String from = custPropManager.findCustomerProperty(customerId, langId, "OTP_EMAIL_FROM");
			HashMap<String, String> parameters = new HashMap<>();
			String otp = OTPGeneratorUtil.generateOTP();
			subject = subject.replaceAll(".otpcode.", otp);
			OTPKey newOTP = new OTPKey();
			newOTP.setApplication_id(applicationId);
			newOTP.setCustomer_user_id(cu.getId());
			newOTP.setCode(otp);
			newOTP = dao.registerNewOTPKey(newOTP);
			ArrayList<EmailRecipient> dests = new ArrayList<>();
			EmailRecipient tmpEMail = new EmailRecipient();
			tmpEMail.setEmail(email);
			tmpEMail.setName(cu.getFirstName());
			Map<String, String> recipientInfo = new HashMap<>();
			recipientInfo.put("Name", cu.getFirstName());
			recipientInfo.put("OTP", otp);
			recipientInfo.put("Lastname", cu.getLastName());
			parameters.put("Name", cu.getFirstName());
			parameters.put("OTP", otp);
			parameters.put("Lastname", cu.getLastName());			
			tmpEMail.setRecipientInfo(recipientInfo);
			dests.add(tmpEMail);
			if (isOTPEmailRequired) {
				utilServices.sendEmailWithManager(from, fromName, subject, template, dests, new ArrayList<>(), parameters, emailType);
			}
			return newOTP;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public OTPKey generateAuthenticationCodeForAdmin(String email, long customerId, long applicationId,  int langId) throws UnknownCustomerUserException {
		try {
			email = email.toLowerCase();
			CustomerUser cu = dao.findCustomerUserByEmail(email, customerId);
			if (cu == null) {
				throw new UnknownCustomerUserException();
			}
			String otp = OTPGeneratorUtil.generateOTP();
			OTPKey newOTP = new OTPKey();
			newOTP.setApplication_id(applicationId);
			newOTP.setCustomer_user_id(cu.getId());
			newOTP.setCode(otp);
			newOTP = dao.registerNewOTPKey(newOTP);
			return newOTP;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public AuthenticatedUserInfo authenticateCustomerUserByOTP(long otpId, String code, String ipAddress, String userAgent) throws InvalidOTPCodeException, UnknownApplicationException, UnknownCustomerUserException, UserAccessTokenGenerationException, MaximumCustomerUserSessionsForAppExceededException, MaximumOTPValidationTriesExceededException, UnknownOTPException, ExpiredOTPException, OTPIllegalStatusException, RequiredApplicationParameterNotFoundException {
		//Obtener OTP info
		OTPKey otpKey = dao.findOTPById(otpId);
		if (otpKey == null) {
			throw new UnknownOTPException();
		}
		//Obtener parametros de aplicacion
		String tmp = appParamManager.getApplicationParameter(otpKey.getApplication_id(), "MAX_OTP_TRIES");
		if (tmp == null) {
			throw new RequiredApplicationParameterNotFoundException("parameter MAX_OTP_TRIES is required.");
		}
		int MAX_OTP_TRIES = Integer.parseInt(tmp);
		tmp = appParamManager.getApplicationParameter(otpKey.getApplication_id(), "OTP_EXPIRATION_TIME");
		if (tmp == null) {
			throw new RequiredApplicationParameterNotFoundException("parameter OTP_EXPIRATION_TIME is required.");
		}
		int EXPIRATION_TIME = Integer.parseInt(tmp);
		//validar la OTP
		OTPKey userOTP = dao.findCustomerUserOTPByApplicationAndCode(otpKey.getCustomer_user_id(), otpKey.getApplication_id(), code);
		if (userOTP == null) {
			otpKey.setValidation_try_count(otpKey.getValidation_try_count()+1);
			dao.updateOTPKey(otpKey);
			throw new InvalidOTPCodeException();
		}
		if (otpKey.getOtp_key_status_id() != OTPKeyStatusSelector.PENDING_FOR_USER_CONFIRMATION) {
			throw new OTPIllegalStatusException("Invalid status: "+otpKey.getOtp_key_status_id());
		} else {
			if ((System.currentTimeMillis() - otpKey.getCreated_at().getTime()) > EXPIRATION_TIME) {
				otpKey.setOtp_key_status_id(OTPKeyStatusSelector.EXPIRED);
				dao.updateOTPKey(otpKey);
				throw new ExpiredOTPException();
			}
			if (otpKey.getValidation_try_count() >= MAX_OTP_TRIES) {
				otpKey.setOtp_key_status_id(OTPKeyStatusSelector.VALIDATION_TRIES_EXCEEDED);
				dao.updateOTPKey(otpKey);
				throw new MaximumOTPValidationTriesExceededException();
			} else {
				//Validar el codigo
				if (!otpKey.getCode().equals(code)) {
					//Validacion fallida
					otpKey.setValidation_try_count(otpKey.getValidation_try_count()+1);
					dao.updateOTPKey(otpKey);
					throw new InvalidOTPCodeException();
				} else {
					//Validaci�n exitosa
					otpKey.setOtp_key_status_id(OTPKeyStatusSelector.VALIDATED);
					dao.updateOTPKey(otpKey);
					//Se procede a crear la sesion del usuario
					CustomerUser custUser = dao.findCustomerUserById(otpKey.getCustomer_user_id());
					if (custUser == null) {
						throw new UnknownCustomerUserException();
					}
					//Generar y registrar UserAccessToken
					UserAccessToken uAT = null;
					uAT = this.createNewCustomerUserSessionToken(custUser.getId(), otpKey.getApplication_id(), ipAddress, userAgent, TokenStatus.ACTIVE);
					User u = dao.findUserById(custUser.getUserId());
					CustomerUserWIthEmail cuEmail = new CustomerUserWIthEmail(custUser, u.getEmail());
					return new AuthenticatedUserInfo(uAT, cuEmail);
				}
			}
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	public AuthenticatedUserInfo authenticateCustomerUserByOTPFree(long otpId, String code, String ipAddress, String userAgent) throws InvalidOTPCodeException, UnknownApplicationException, UnknownCustomerUserException, UserAccessTokenGenerationException, MaximumCustomerUserSessionsForAppExceededException, MaximumOTPValidationTriesExceededException, UnknownOTPException, ExpiredOTPException, OTPIllegalStatusException, RequiredApplicationParameterNotFoundException {
		//Obtener OTP info
		OTPKey otpKey = dao.findOTPById(otpId);
		if (otpKey == null) {
			throw new UnknownOTPException();
		}
		//Obtener parametros de aplicacion
		String tmp = appParamManager.getApplicationParameter(otpKey.getApplication_id(), "MAX_OTP_TRIES");
		if (tmp == null) {
			throw new RequiredApplicationParameterNotFoundException("parameter MAX_OTP_TRIES is required.");
		}
		int MAX_OTP_TRIES = Integer.parseInt(tmp);
		tmp = appParamManager.getApplicationParameter(otpKey.getApplication_id(), "OTP_EXPIRATION_TIME");
		if (tmp == null) {
			throw new RequiredApplicationParameterNotFoundException("parameter OTP_EXPIRATION_TIME is required.");
		}
		int EXPIRATION_TIME = Integer.parseInt(tmp);
		//validar la OTP
		if (otpKey.getOtp_key_status_id() != OTPKeyStatusSelector.PENDING_FOR_USER_CONFIRMATION) {
			throw new OTPIllegalStatusException("Invalid status: "+otpKey.getOtp_key_status_id());
		} else {
			if ((System.currentTimeMillis() - otpKey.getCreated_at().getTime()) > EXPIRATION_TIME) {
				otpKey.setOtp_key_status_id(OTPKeyStatusSelector.EXPIRED);
				dao.updateOTPKey(otpKey);
				throw new ExpiredOTPException();
			}
			if (otpKey.getValidation_try_count() >= MAX_OTP_TRIES) {
				otpKey.setOtp_key_status_id(OTPKeyStatusSelector.VALIDATION_TRIES_EXCEEDED);
				dao.updateOTPKey(otpKey);
				throw new MaximumOTPValidationTriesExceededException();
			} else {
				//Validar el codigo
				if (!otpKey.getCode().equals(code)) {
					//Validacion fallida
					otpKey.setValidation_try_count(otpKey.getValidation_try_count()+1);
					dao.updateOTPKey(otpKey);
					throw new InvalidOTPCodeException();
				} else {
					//Validaci�n exitosa
					otpKey.setOtp_key_status_id(OTPKeyStatusSelector.VALIDATED);
					dao.updateOTPKey(otpKey);
					//Se procede a crear la sesion del usuario
					CustomerUser custUser = dao.findCustomerUserById(otpKey.getCustomer_user_id());
					if (custUser == null) {
						throw new UnknownCustomerUserException();
					}
					//Generar y registrar UserAccessToken
					UserAccessToken uAT = null;
					uAT = this.createNewCustomerUserSessionToken(custUser.getId(), otpKey.getApplication_id(), ipAddress, userAgent, TokenStatus.PENDING_VALIDATION);
					User u = dao.findUserById(custUser.getUserId());
					CustomerUserWIthEmail cuEmail = new CustomerUserWIthEmail(custUser, u.getEmail());
					return new AuthenticatedUserInfo(uAT, cuEmail);
				}
			}
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public CustomerUser updateCustomerUserEmail(final CustomerUser user) {
		return dao.updateCustomerUserEmail(user);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public ValidationCode registerNewValidationCode(final ValidationCode validationCode) {
		return dao.registerNewValidationCode(validationCode);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public ValidationCode findValidationCodeById(long validationCodeId) {
		return dao.findValidationCodeById(validationCodeId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void updateValidationCode(ValidationCode validationCode) {
		dao.updateValidationCode(validationCode);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public HashMap<String, Object> findUsersEventInfo(Long customerId) {
		return dao.findUsersEventInfo(customerId);
	}

}
