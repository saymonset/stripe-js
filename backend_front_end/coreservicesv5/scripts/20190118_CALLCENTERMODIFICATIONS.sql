-- BD:SERVICES_DB_V2
-- USER: SERVICES
-- SCHEMA: PUBLIC


CREATE TABLE EXTERNAL_PAYMENT_TYPE (
	ID	INTEGER,
	NAME	VARCHAR(64) NOT NULL,
	ICON_URL	VARCHAR,
	CONSTRAINT EXTERNAL_PT_PK PRIMARY KEY (ID),
	CONSTRAINT EXTERNAL_PT_NAME_UQ UNIQUE (NAME)
);
INSERT INTO EXTERNAL_PAYMENT_TYPE(ID, NAME) VALUES (1, 'PAYPAL');
INSERT INTO EXTERNAL_PAYMENT_TYPE(ID, NAME) VALUES (2, 'CASH');
INSERT INTO EXTERNAL_PAYMENT_TYPE(ID, NAME) VALUES (3, 'AIRTM');
INSERT INTO EXTERNAL_PAYMENT_TYPE(ID, NAME) VALUES (4, 'TRANSFER');

CREATE TABLE CUSTOMER_SUPPORTED_EXTERNAL_PAYMENT_TYPE (
	CUSTOMER_ID	BIGINT,
	EXTERNAL_PT_ID	INTEGER,
	IS_ENABLED	BOOLEAN DEFAULT TRUE,
	CONSTRAINT CUSTOMER_SUPP_EXT_PT_PK PRIMARY KEY (CUSTOMER_ID, EXTERNAL_PT_ID),
	CONSTRAINT CUSTOMER_SUPP_EXT_PT_CUSTOMER_FK FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(ID),
	CONSTRAINT CUSTOMER_SUPP_EXT_PT_EX_PT_FK FOREIGN KEY (EXTERNAL_PT_ID) REFERENCES EXTERNAL_PAYMENT_TYPE(ID)
);
INSERT INTO CUSTOMER_SUPPORTED_EXTERNAL_PAYMENT_TYPE(CUSTOMER_ID, EXTERNAL_PT_ID) VALUES (32, 1);
INSERT INTO CUSTOMER_SUPPORTED_EXTERNAL_PAYMENT_TYPE(CUSTOMER_ID, EXTERNAL_PT_ID) VALUES (32, 2);
INSERT INTO CUSTOMER_SUPPORTED_EXTERNAL_PAYMENT_TYPE(CUSTOMER_ID, EXTERNAL_PT_ID) VALUES (32, 3);
INSERT INTO CUSTOMER_SUPPORTED_EXTERNAL_PAYMENT_TYPE(CUSTOMER_ID, EXTERNAL_PT_ID) VALUES (32, 4);


CREATE TABLE UNIQUE_CUSTOMER_APPLICATION_USER_CODE (
	ID 					BIGSERIAL,
	CUSTOMER_USER_ID	BIGINT,
	APPLICATION_ID		BIGINT,
	USER_CODE 			VARCHAR,
	CREATED_AT			timestamp without time zone DEFAULT now(),
	UNSUCCESSFUL_VALIDATION_TRIES	INTEGER DEFAULT 0,
	IS_ENABLED			BOOLEAN DEFAULT TRUE,
	CONSTRAINT UNIQUE_CUSTOMER_APPLICATION_USER_CODE_PK PRIMARY KEY (ID),
	CONSTRAINT UNIQUE_CUSTOMER_APPLICATION_USER_CODE_CUST_US_FK FOREIGN KEY (CUSTOMER_USER_ID) REFERENCES CUSTOMER_USER(ID),
	CONSTRAINT UNIQUE_CUSTOMER_APPLICATION_USER_CODE_APP_FK FOREIGN KEY (APPLICATION_ID) REFERENCES APPLICATION(ID)
)

CREATE INDEX UNIQUE_CUSTOMER_APP_US_CODE_CUSTUSERIDxAPPLICATIONIDxIS_ENABLED ON UNIQUE_CUSTOMER_APPLICATION_USER_CODE (CUSTOMER_USER_ID, APPLICATION_ID, IS_ENABLED);

CREATE TABLE AUTHENTICATION_METHOD (
	ID 			INTEGER,
	NAME 		VARCHAR,
	CONSTRAINT AUTHENTICATION_METHOD_PK PRIMARY KEY (ID),
	CONSTRAINT AUTHENTICATION_METHOD_UQ UNIQUE (NAME)
);

INSERT INTO AUTHENTICATION_METHOD (ID, NAME) VALUES (1, 'OTP');
INSERT INTO AUTHENTICATION_METHOD (ID, NAME) VALUES (2, 'MAGIC LINK');
INSERT INTO AUTHENTICATION_METHOD (ID, NAME) VALUES (3, 'PURCHASE CODE');

CREATE TABLE CUSTOMER_APPLICATION_AUTHENTICATION_METHODS_SUPPORTED (
	CUSTOMER_ID 		BIGINT,
	APPLICATION_ID 		BIGINT,
	AUTHENTICATION_METHOD_ID	INTEGER,
	ORDER_AT						INTEGER,
	CONSTRAINT CUSTOMER_APPLICATION_AUTHENTICATION_METHODS_SUPPORTED_PK PRIMARY KEY (CUSTOMER_ID, APPLICATION_ID, AUTHENTICATION_METHOD_ID),
	CONSTRAINT CUST_APP_AUTH_METH_SUPP_CUSTOMER_FK FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(ID),
	CONSTRAINT CUST_APP_AUTH_METH_SUPP_APPLICATION_FK FOREIGN KEY (APPLICATION_ID) REFERENCES APPLICATION(ID),
	CONSTRAINT CUST_APP_AUTH_METH_SUPP_AUTH_METH_FK FOREIGN KEY (AUTHENTICATION_METHOD_ID) REFERENCES AUTHENTICATION_METHOD(ID),
	CONSTRAINT CUST_APP_AUTH_METH_SUPP_ORDER_UQ UNIQUE (CUSTOMER_ID, APPLICATION_ID, ORDER_AT) 	
);

INSERT INTO CUSTOMER_APPLICATION_AUTHENTICATION_METHODS_SUPPORTED (CUSTOMER_ID, APPLICATION_ID, AUTHENTICATION_METHOD_ID, ORDER_AT) VALUES (32, 35, 2, 1);
INSERT INTO CUSTOMER_APPLICATION_AUTHENTICATION_METHODS_SUPPORTED (CUSTOMER_ID, APPLICATION_ID, AUTHENTICATION_METHOD_ID, ORDER_AT) VALUES (32, 35, 1, 2);
INSERT INTO CUSTOMER_APPLICATION_AUTHENTICATION_METHODS_SUPPORTED (CUSTOMER_ID, APPLICATION_ID, AUTHENTICATION_METHOD_ID, ORDER_AT) VALUES (32, 35, 3, 3);

CREATE TABLE DISCOUNT_TYPE (
	ID	INTEGER,
	NAME	VARCHAR(32) NOT NULL,
	CONSTRAINT DISCOUNT_TYPE_PK PRIMARY KEY (ID),
	CONSTRAINT DISCOUNT_TYPE_UQ UNIQUE (NAME)
);
INSERT INTO DISCOUNT_TYPE (ID, NAME) VALUES (1, 'PERCENT_DISCOUNT');
INSERT INTO DISCOUNT_TYPE (ID, NAME) VALUES (2, 'ABSOLUTE_DISCOUNT');

CREATE TABLE COUPON_STATUS (
	ID 		INTEGER,
	NAME 	VARCHAR NOT NULL,
	CONSTRAINT COUPON_STATUS_PK PRIMARY KEY (ID),
	CONSTRAINT COUPON_STATUS_NAME_UQ UNIQUE (NAME)
);

INSERT INTO COUPON_STATUS (ID, NAME) VALUES (1, 'GENERATED');
INSERT INTO COUPON_STATUS (ID, NAME) VALUES (2, 'ACTIVE');
INSERT INTO COUPON_STATUS (ID, NAME) VALUES (3, 'APPLIED');
INSERT INTO COUPON_STATUS (ID, NAME) VALUES (4, 'EXPIRED');
INSERT INTO COUPON_STATUS (ID, NAME) VALUES (5, 'BLOCKED');

CREATE TABLE COUPON_PROMOTION (
	ID 		BIGSERIAL,
	NAME 	VARCHAR,
	CREATED_BY_CUSTOMER_ID 			BIGINT NOT NULL,
	CREATED_AT			timestamp without time zone DEFAULT current_timestamp,
	DISCOUNT_TYPE_ID	INTEGER NOT NULL,
	DISCOUNT_AMOUNT		NUMERIC(18,2),
	MAX_COUPONS			BIGINT,
	CURRENT_COUPONS		BIGINT DEFAULT 0,
	VALID_FROM			timestamp without time zone NOT NULL,
	VALID_TO			timestamp without time zone NOT NULL,
	APPLICATION_LIMIT	BIGINT DEFAULT 1,
	USER_APPLICATION_LIMIT	BIGINT DEFAULT 1,	
	IS_GENERIC				BOOLEAN default false,
	IS_ENABLED			BOOLEAN default true,
	CONSTRAINT COUPON_PROMOTION_PK PRIMARY KEY (ID),
	CONSTRAINT COUPON_PROMOTION_NAME_UQ UNIQUE (CREATED_BY_CUSTOMER_ID, NAME),
	CONSTRAINT COUPON_PROMOTION_CUSTOMER_FK FOREIGN KEY (CREATED_BY_CUSTOMER_ID) REFERENCES CUSTOMER(ID) ON DELETE CASCADE
);

CREATE TABLE COUPON (
	ID 		BIGSERIAL,
	CODE 	VARCHAR,
	COUPON_STATUS_ID	INTEGER DEFAULT 1,
	APPLIER 			VARCHAR,
	APPLIED_TIMES		BIGINT DEFAULT 0,
	COUPON_PROMOTION_ID	BIGINT NOT NULL,
	CONSTRAINT COUPON_PK PRIMARY KEY (ID),
	CONSTRAINT COUPON_CODE_UQ UNIQUE (CODE),
	CONSTRAINT COUPON_COUPON_STATUS_FK FOREIGN KEY (COUPON_STATUS_ID) REFERENCES COUPON_STATUS(ID),
	CONSTRAINT COUPON_COUPON_PROMOTION_ID FOREIGN KEY (COUPON_PROMOTION_ID) REFERENCES COUPON_PROMOTION(ID)
);

CREATE TABLE COUPON_EVENT (
	ID 		INTEGER,
	NAME 	VARCHAR NOT NULL,
	CONSTRAINT COUPON_EVENT_PK PRIMARY KEY (ID),
	CONSTRAINT COUPON_EVENT_NAME_UQ UNIQUE (NAME)
);

INSERT INTO COUPON_EVENT (ID, NAME) VALUES (1, 'GENERATION');
INSERT INTO COUPON_EVENT (ID, NAME) VALUES (2, 'ACTIVATION');
INSERT INTO COUPON_EVENT (ID, NAME) VALUES (3, 'VALIDATION');
INSERT INTO COUPON_EVENT (ID, NAME) VALUES (4, 'EXPIRATION');
INSERT INTO COUPON_EVENT (ID, NAME) VALUES (5, 'SECURITY_BLOCK');
INSERT INTO COUPON_EVENT (ID, NAME) VALUES (6, 'APPLICATION');

CREATE TABLE COUPON_LOG (
	ID 		BIGSERIAL,
	COUPON_STATUS_ID INTEGER NOT NULL,
	COUPON_EVENT_ID INTEGER NOT NULL,
	EVENT_DATE	timestamp without time zone DEFAULT current_timestamp,
	COUPON_ID 	BIGINT NOT NULL,
	USER_EMAIL	VARCHAR,
	USER_AGENT	VARCHAR NOT NULL,
	IP_ADDRESS	VARCHAR NOT NULL,
	CONSTRAINT COUPON_LOG_PK PRIMARY KEY (ID),
	CONSTRAINT COUPON_LOG_STATUS_FK FOREIGN KEY (COUPON_STATUS_ID) REFERENCES COUPON_STATUS(ID),
	CONSTRAINT COUPON_LOG_EVENT_FK FOREIGN KEY (COUPON_EVENT_ID) REFERENCES COUPON_EVENT(ID)
);

CREATE TABLE COUPON_PROMOTION_CUSTOMER_APP_RESTRICTION (
	CUSTOMER_ID 		BIGINT,
	APPLICATION_ID 		BIGINT,
	COUPON_PROMOTION_ID BIGINT,
	CREATED_AT 			timestamp without time zone default current_timestamp,
	CONSTRAINT COUPON_PROMOTION_CUSTOMER_APP_RESTRICTION_PK PRIMARY KEY (CUSTOMER_ID, APPLICATION_ID, COUPON_PROMOTION_ID),
	CONSTRAINT COUPON_PROMOTION_CUST_APP_REST_CUST_FK FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(ID),
	CONSTRAINT COUPON_PROMOTION_CUST_APP_REST_APP_FK FOREIGN KEY (APPLICATION_ID) REFERENCES APPLICATION(ID),
	CONSTRAINT COUPON_PROMOTION_CUST_APP_REST_COUP_PROM_FK FOREIGN KEY (COUPON_PROMOTION_ID) REFERENCES COUPON_PROMOTION(ID)
);

ALTER TABLE TRANSACTION 
	ADD DISCOUNT NUMERIC(18,2) DEFAULT 0;


CREATE TABLE TRANSACTION_COUPON_APPLICATION (
	TRANSACTION_ID 		BIGINT,
	COUPON_ID 			BIGINT,
	APPLIER 			VARCHAR,
	APPLIED_AT 			timestamp without time zone default current_timestamp,
	CONSTRAINT TRANSACTION_COUPON_APPLICATION_PK PRIMARY KEY (TRANSACTION_ID, COUPON_ID),
	CONSTRAINT TRANSACTION_COUPON_APPLICATION_COUP_FK FOREIGN KEY (COUPON_ID) REFERENCES COUPON(ID),
	CONSTRAINT TRANSACTION_COUPON_APPLICATION_TRANS_FK FOREIGN KEY (TRANSACTION_ID) REFERENCES TRANSACTION(ID)
);

create index I1_TRAN_COUP_APP_APPLIER ON TRANSACTION_COUPON_APPLICATION(COUPON_ID, APPLIER);

INSERT INTO SYSTEM_MESSAGE(ID, NAME) VALUES (31, 'invalid_coupon');
INSERT INTO SYSTEM_MESSAGE(ID, NAME) VALUES (32, 'already_applied_coupon');
INSERT INTO SYSTEM_MESSAGE(ID, NAME) VALUES (33, 'expired_coupon');
INSERT INTO SYSTEM_MESSAGE(ID, NAME) VALUES (34, 'coupon_not_valid_on');
INSERT INTO SYSTEM_MESSAGE(ID, NAME) VALUES (35, 'coupon_general_error');
INSERT INTO SYSTEM_MESSAGE(ID, NAME) VALUES (36, 'email_invalid');

INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG (CUSTOMER_ID, SYSTEM_MESSAGE_ID, LANG_ID, MESSAGE) VALUES (32, 31, 1, 'Cupón no válido.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG (CUSTOMER_ID, SYSTEM_MESSAGE_ID, LANG_ID, MESSAGE) VALUES (32, 31, 2, 'Not valid coupon.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG (CUSTOMER_ID, SYSTEM_MESSAGE_ID, LANG_ID, MESSAGE) VALUES (32, 32, 1, 'El cupón ya fue utilizado.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG (CUSTOMER_ID, SYSTEM_MESSAGE_ID, LANG_ID, MESSAGE) VALUES (32, 32, 2, 'Coupon already used.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG (CUSTOMER_ID, SYSTEM_MESSAGE_ID, LANG_ID, MESSAGE) VALUES (32, 33, 1, 'El cupón se encuentra vencido.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG (CUSTOMER_ID, SYSTEM_MESSAGE_ID, LANG_ID, MESSAGE) VALUES (32, 33, 2, 'The coupon has expired.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG (CUSTOMER_ID, SYSTEM_MESSAGE_ID, LANG_ID, MESSAGE) VALUES (32, 34, 1, 'El cupón no es válido para este Seminario.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG (CUSTOMER_ID, SYSTEM_MESSAGE_ID, LANG_ID, MESSAGE) VALUES (32, 34, 2, 'Coupon not valid on this event');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG (CUSTOMER_ID, SYSTEM_MESSAGE_ID, LANG_ID, MESSAGE) VALUES (32, 35, 1, 'Ha ocurrido un error. Intente más tarde.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG (CUSTOMER_ID, SYSTEM_MESSAGE_ID, LANG_ID, MESSAGE) VALUES (32, 35, 2, 'An error occurred, please try again later.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG (CUSTOMER_ID, SYSTEM_MESSAGE_ID, LANG_ID, MESSAGE) VALUES (32, 36, 1, 'Los correos no coinciden, verifíquelos nuevamente.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG (CUSTOMER_ID, SYSTEM_MESSAGE_ID, LANG_ID, MESSAGE) VALUES (32, 36, 2, 'Los correos no coinciden, verifíquelos nuevamente.');


INSERT INTO PROPERTY(ID, NAME) VALUES (189, 'lang-coupon-input');
INSERT INTO PROPERTY(ID, NAME) VALUES (190, 'lang-coupon-validate');
INSERT INTO PROPERTY(ID, NAME) VALUES (191, 'lang-coupon-label');
INSERT INTO PROPERTY(ID, NAME) VALUES (192, 'lang-payment-type');
INSERT INTO PROPERTY(ID, NAME) VALUES (193, 'detail-a1');
INSERT INTO PROPERTY(ID, NAME) VALUES (194, 'detail-b1');
INSERT INTO PROPERTY(ID, NAME) VALUES (195, 'detail-a3');
INSERT INTO PROPERTY(ID, NAME) VALUES (196, 'discount-row-template');
INSERT INTO CUSTOMER_PROPERTY_LANG(CUSTOMER_ID, PROPERTY_ID, LANG_ID, PROPERTY_VALUE) VALUES (32, 189, 1, 'Ingrese el cupón:');
INSERT INTO CUSTOMER_PROPERTY_LANG(CUSTOMER_ID, PROPERTY_ID, LANG_ID, PROPERTY_VALUE) VALUES (32, 189, 2, 'Your coupon code:');
INSERT INTO CUSTOMER_PROPERTY_LANG(CUSTOMER_ID, PROPERTY_ID, LANG_ID, PROPERTY_VALUE) VALUES (32, 190, 1, 'Validar');
INSERT INTO CUSTOMER_PROPERTY_LANG(CUSTOMER_ID, PROPERTY_ID, LANG_ID, PROPERTY_VALUE) VALUES (32, 190, 2, 'Validate');
INSERT INTO CUSTOMER_PROPERTY_LANG(CUSTOMER_ID, PROPERTY_ID, LANG_ID, PROPERTY_VALUE) VALUES (32, 191, 1, '¿Posees cupón?');
INSERT INTO CUSTOMER_PROPERTY_LANG(CUSTOMER_ID, PROPERTY_ID, LANG_ID, PROPERTY_VALUE) VALUES (32, 192, 1, 'Información de la Tarjeta');
INSERT INTO CUSTOMER_PROPERTY_LANG(CUSTOMER_ID, PROPERTY_ID, LANG_ID, PROPERTY_VALUE) VALUES (32, 193, 1, 'Descripción');
INSERT INTO CUSTOMER_PROPERTY_LANG(CUSTOMER_ID, PROPERTY_ID, LANG_ID, PROPERTY_VALUE) VALUES (32, 194, 1, 'Total');
INSERT INTO CUSTOMER_PROPERTY_LANG(CUSTOMER_ID, PROPERTY_ID, LANG_ID, PROPERTY_VALUE) VALUES (32, 195, 1, 'Descuento');
INSERT INTO CUSTOMER_PROPERTY_LANG(CUSTOMER_ID, PROPERTY_ID, LANG_ID, PROPERTY_VALUE) VALUES (32, 196, 1, '/var/lib/templates/discount_row.html');

-- MODULO Y METODOS PARA EL CORE
INSERT INTO MODULE (NAME, DESCRIPTION) VALUES ('COUPON METHOD','Methods for managing coupons');
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('listAllExternalPaymentTypes','Method for list all supported external payment types', 'com.us.weavx.core.services.impl.ListAllExternalPaymentTypesMethodImpl',3,2);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('listCustomerExternalPaymentTypes', 'Method for list all supported external payment types for a given customer', 'com.us.weavx.core.services.impl.ListCustomerSupportedExternalPaymentTypesMethodImpl', 2,2);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('listAllDiscountTypes','Method for list all supported discount types', 'com.us.weavx.core.services.impl.ListAllDiscountTypesMethodImpl', 3,2);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('generateCoupon', 'Method for generate Coupons', 'com.us.weavx.core.services.impl.GenerateCouponMethodImpl', 3, 10);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('validateCoupon', 'Method for validate coupons', 'com.us.weavx.core.services.impl.ValidateCouponMethodImpl', 3, 10);

                                                      





