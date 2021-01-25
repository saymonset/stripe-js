package com.us.weavx.core.constants;

public class ServiceReturnMessages {
	
	public static final String INVALID_ACCESS_KEYS = "Incorrect key or secret.";
	public static final int INVALID_ACCESS_KEYS_CODE = -1;
	
	public static final String SUCCESS = "Success";
	public static final int SUCCESS_CODE = 0;
	
	public static final int EXPIRED_TOKEN_CODE = -2;
	public static final String EXPIRED_TOKEN = "The token has expired.";
	
	public static final int BLOCKED_TOKEN_CODE = -3;
	public static final String BLOCKED_TOKEN = "The token is blocked.";
	
	public static final int GENERAL_ERROR_CODE = -4;
	public static final String GENERAL_ERROR = "General Error";
	
	public static final int ERROR_GENERATING_TOKEN_CODE = -5;
	public static final String ERROR_GENERATING_CODE = "Error generating new Token.";
	public static final int ACCESS_DENIED_CODE = -6;
	public static final String ACCESS_DENIED = "Access denied.";
	public static final int INVALID_EXTERNAL_PROFILE_CODE = -7;
	public static final String INVALID_EXTERNAL_PROFILE = "Invalid external profile info. Check or renew your external access token.";
	public static final int UNKNOWN_IDENTITY_PROVIDER_CODE = -8;
	public static final String UNKNOWN_IDENTITY_PROVIDER = "Unknown identity provider id.";
	public static final String ACCESS_DENIED_MESSAGE = "Your developer or access keys are not valid or your developer account can't access the customer associated to the access keys provided.";
	public static final int USER_ALREADY_EXISTS_CODE = -9;
	public static final String USER_ALREADY_EXISTS = "User already exists.";
	public static final int FIRST_NAME_REQUIRED_CODE = -10;
	public static final String FIRST_NAME_REQUIRED = "firstName is required.";
	public static final int LAST_NAME_REQUIRED_CODE = -11;
	public static final String LAST_NAME_REQUIRED = "lastName is required.";
	public static final int PASSWORD_REQUIRED_CODE = -12;
	public static final String PASSWORD_REQUIRED = "password is required.";
	public static final int ERROR_GENERATING_USER_TOKEN_CODE = -13;
	public static final String ERROR_GENERATING_USER_TOKEN = "Error generating new user access token.";
	public static final int INVALID_USER_CREDENTIALS_CODE = -14;
	public static final String INVALID_USER_CREDENTIALS = "Wrong email or password.";
	public static final int WRONG_CUSTOMER_ID_ERROR_CODE = -15;
	public static final String WRONG_CUSTOMER_ID_ERROR = "Wrong customerId.";
	public static final int INVALID_USER_ACCESS_TOKEN_CODE = -16;
	public static final String INVALID_USER_ACCESS_TOKEN = "Invalid user access token.";
	public static final int EXPIRED_USER_ACCESS_TOKEN_CODE = -17;
	public static final String EXPIRED_USER_ACCESS_TOKEN = "The user token has expired.";
	public static final int INVALID_PAYMENT_TYPE_CODE = -18;
	public static final String INVALID_PAYMENT_TYPE = "Invalid Payment Type.";
	public static final int DISABLED_PAYMENT_TYPE_CODE = -19;
	public static final String DISABLED_PAYMENT_TYPE = "Payment Type disabled.";
	public static final int DENIED_TRANSACTION_CODE = -20;
	public static final String DENIED_TRANSACTION = "Denied transaction: ";
	public static final int TRANSACTION_ERROR_CODE = -21;
	public static final String TRANSACTION_ERROR = "Transaction Error: ";
	public static final int INVALID_ACCESS_TOKEN_CODE = -22;
	public static final String INVALID_ACCESS_TOKEN = "Invalid access token.";
	public static final int PAYMENT_GW_INFO_NOT_FOUND_CODE = -23;
	public static final String PAYMENT_GW_INFO_NOT_FOUND = "Payment Gw Info not found.";
	public static final int UNKNOWN_APPLICATION_CODE = -24;
	public static final String UNKNOWN_APPLICATION = "Unknown Application.";
	public static final int UNKNOWN_CUSTOMER_USER_CODE = -25;
	public static final String UNKNOWN_CUSTOMER_USER = "Unknown customer user.";
	public static final int NOTIFICATION_TEMPLATE_NOT_FOUND_CODE = -26;
	public static final String NOTIFICATION_TEMPLATE_NOT_FOUND = "Notification template not found.";
	public static final int EMAIL_SENDING_ERROR_CODE = -27;
	public static final String EMAIL_SENDING_ERROR = "Error sending email.";
	public static final int ACCESS_TOKEN_GENERATION_ERROR_CODE = -28;
	public static final String ACCESS_TOKEN_GENERATION_ERROR = "Notification template not found.";
	public static final int NOT_SUCCESS_TRANSACTIONS_IN_APP_CODE = -29;
	public static final String NOT_SUCCESS_TRANSACTIONS_IN_APP = "Success transactions for customer user in app not found.";
	public static final int IP_ADDRESS_REQUIRED_CODE = -30;
	public static final String IP_ADDRESS_REQUIRED = "ipAddress is required.";
	public static final int USER_AGENT_REQUIRED_CODE = -31;
	public static final String USER_AGENT_REQUIRED = "userAgent is required.";
	public static final int MAXIMUM_SESSIONS_EXCEEDED_CODE = -32;
	public static final String MAXIMUM_SESSIONS_EXCEEDED = "Exceeded Maximum sessions for user in app.";
	public static final int USER_ACCESS_TOKEN_REQUIRED_CODE = -33;
	public static final String USER_ACCESS_TOKEN_REQUIRED = "userAccessToken to invalidate is required.";
	public static final int TRANSACTION_ABORTED_CODE = -34;
	public static final String TRANSACTION_ABORTED = "Transaction has beed aborted by app business rules. ";
	public static final int ALREADY_USED_ACCESS_TOKEN_CODE = -35;
	public static final String ALREADY_USED_ACCESS_TOKEN = "This access token has been used.";
	public static final int ALREADY_CLOSED_ACCESS_TOKEN_CODE = -36;
	public static final String ALREADY_CLOSED_ACCESS_TOKEN = "This access token has been closed by user.";
	public static final int PAYMENT_GATEWAY_ERROR_CODE = -37;
	public static final String PAYMENT_GATEWAY_ERROR = "Payment gateway error: ";
	public static final int NO_PAYMENT_PROFILE_ERROR_CODE = -38;
	public static final String NO_PAYMENT_PROFILE_ERROR = "Payment profile not found for user.";
	public static final int INVALID_OTP_CODE = -39;
	public static final String INVALID_OTP = "Invalid OTP";
	public static final int UNKNOWN_OTP_CODE = -40;
	public static final String UNKNOWN_OTP = "OTP not found.";
	public static final int EXPIRED_OTP_CODE = -41;
	public static final String EXPIRED_OTP = "Expired OTP code.";
	public static final int OTP_ILLEGALSTATUS_CODE = -42;
	public static final String OTP_ILLEGAL_STATUS = "OTP already validated or in not expected status.";
	public static final int MAXIMUM_OTP_VALIDATION_TRIES_EXCEEDED_CODE = -43;
	public static final String MAXIMUM_OTP_VALIDATION_TRIES_EXCEEDED = "Maximum OTP validation tries exceeded.";
	public static final int FRAUD_TRANSACTION_CODE = -44;
	public static final String FRAUD_TRANSACTION = "Maximun rejected transactions has exceeded!";
	public static final int RESTRICTED_EVENT_CODE = -45;
	public static final String RESTRICTED_EVENT = "User not allowed to purchase or login in this event.";
	public static final int INVALID_EMAIL_ADDRRESS_CODE = -46;
	public static final String INVALID_EMAIL_ADDRESS = "Invalid email address.";
	public static final int USER_ACCESS_EXPIRED_CODE = -47;
	public static final String USER_ACCESS_EXPIRED = "User access for the event has expired.";
	public static final int NOT_OWNER_CODE = -48;
	public static final String NOT_OWNER = "User doesn't have access to the given object.";
	public static final int UNKNOWN_OBJECT_CODE = -49;
	public static final String UNKNOWN_OBJECT = "Unknown object:";
	public static final int UNSUPPORTED_DEVICE_CODE = -50;
	public static final String UNSUPPORTED_DEVICE = "Unsupported device.";
	public static final int ALREADY_USED_VALIDATIONCODE_CODE = -51;
	public static final String ALREADY_USED_VALIDATIONCODE = "Already used validation code.";
	public static final int SIGNATURE_REQUIRED_CODE = -52;
	public static final String SIGNATURE_REQUIRED = "Document signature is required";
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
