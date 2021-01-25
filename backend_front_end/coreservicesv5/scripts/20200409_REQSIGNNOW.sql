ALTER TABLE EVENT_FUND_SETTINGS
ADD signature_required boolean default false, ADD signature_document_id varchar;	

CREATE TABLE CUSTOMER_USER_EVENT_CONTRACT_SIGN (
	id		bigserial,
	customer_user_id	bigint not null,
	application_id		bigint not null,
	signature_url		varchar not null,
	signature_status	boolean,
	signed_at			timestamp without time zone,
	signature_data		varchar,
	transaction_id		bigint not null,
	constraint CUST_USER_EV_CONT_SG_PK primary key (id),
	constraint CUST_USER_EV_CONT_SG_CU_FK foreign key (customer_user_id) references customer_user(id) on delete cascade,
	constraint CUST_USER_EV_CONT_SG_AP_FK foreign key (application_id) references application(id) on delete cascade,
	constraint CUST_USER_EV_CONT_SG_TX_FK foreign key (transaction_id) references transaction(id) on delete cascade
);

CREATE TABLE SIGNATURE_GATEWAY (
	id		integer,
	name	varchar not null,
	sandbox_url	varchar not null,
	production_url	varchar not null,
	constraint SIGNATURE_GATEWAY_PK primary key (id),
	constraint SIGNATURE_GATEWAY_NAME_UQ unique (name)
);

INSERT INTO SIGNATURE_GATEWAY (id, name, sandbox_url, production_url) VALUES (1, 'SIGNNOW', 'https://api-eval.signnow.com/','https://api.signnow.com/');

CREATE TABLE CUSTOMER_SIGNATURE_GATEWAY (
	customer_id		bigint,
	signature_gateway_id	int,
	signatureApiParam1		varchar,
	signatureApiParam2		varchar,
	signatureApiParam3		varchar,
	signatureApiParam4		varchar,
	constraint CUSTOMER_SIGNATURE_GATEWAY_PK primary key (customer_id, signature_gateway_id),
	constraint CUST_SIGN_GW_CUST_FK foreign key (customer_id) references customer(id) on delete cascade,
	constraint CUST_SIGN_GW_SGW_FK foreign key (signature_gateway_id) references signature_gateway(id) on delete cascade
);

INSERT INTO CUSTOMER_SIGNATURE_GATEWAY (customer_id, signature_gateway_id, signatureApiParam1, signatureApiParam2, signatureApiParam3, signatureApiParam4) VALUES (131, 1, 'a4cf89ca1a5ec7257d35beb9ff18bb70', '19ddd2a63747f612963247b1af406b80', 'services@seminariocreandoriqueza.com', 'Inversiones@2019++');

INSERT INTO APPLICATION_PARAMETER(application_id, name, value) values (126, 'signature_template_id', '039bd3f7eb32a6a015516e0d2e7633c3a13b77b1');

INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('registerNewDocumentSignature','method for register new document signature', 'com.us.weavx.core.services.impl.RegisterDocumentSignatureMethodImpl',3,3);

INSERT INTO PROPERTY (ID, NAME) VALUES (212, '.lang-hi-signature');
INSERT INTO PROPERTY (ID, NAME) VALUES (213, '.lang-sign-message');
INSERT INTO PROPERTY (ID, NAME) VALUES (214, '.lang-sign-accept');



INSERT INTO CUSTOMER_PROPERTY_LANG(PROPERTY_ID, CUSTOMER_ID, LANG_ID, PROPERTY_VALUE) VALUES (212, 116, 1, 'Hola *firmante*!');
INSERT INTO CUSTOMER_PROPERTY_LANG(PROPERTY_ID, CUSTOMER_ID, LANG_ID, PROPERTY_VALUE) VALUES (212, 116, 2, 'Hi *firmante*!');
INSERT INTO CUSTOMER_PROPERTY_LANG(PROPERTY_ID, CUSTOMER_ID, LANG_ID, PROPERTY_VALUE) VALUES (213, 116, 1, 'Antes de entrar requerimos que cada participante firme los acuerdos de participación y de derechos reservados.<br> Si necesita ayuda puedes llamarnos al <b>786-789-3033</b>.');
INSERT INTO CUSTOMER_PROPERTY_LANG(PROPERTY_ID, CUSTOMER_ID, LANG_ID, PROPERTY_VALUE) VALUES (213, 116, 2, 'Before entering we require each participant to sign the participation and copyright agreements. <br> If you need help you can call us at <b> 786-789-3033 </b>.');
INSERT INTO CUSTOMER_PROPERTY_LANG(PROPERTY_ID, CUSTOMER_ID, LANG_ID, PROPERTY_VALUE) VALUES (214, 116, 1, 'Firmar y aceptar');
INSERT INTO CUSTOMER_PROPERTY_LANG(PROPERTY_ID, CUSTOMER_ID, LANG_ID, PROPERTY_VALUE) VALUES (214, 116, 2, 'Sign and accept');

INSERT INTO SYSTEM_PROPERTY(id, name, value, description) values (16, 'SIGN_SERVICE_URL', 'https://sse.harvestful.org/signnow/generateSignatureURL', 'URL for internal service for get signature URL');
