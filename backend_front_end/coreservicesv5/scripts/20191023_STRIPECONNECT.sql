
CREATE TABLE COMMISSION_TYPE (
	id	integer,
	name	varchar not null,
	description	varchar not null,
	constraint COMMISSION_TYPE_PK primary key (id),
	constraint COMMISSION_TYPE_UQ unique(name)
);


INSERT INTO COMMISSION_TYPE(id, name, description) values (1, 'ABSOLUTE', 'Exact ammount commission.');
INSERT INTO COMMISSION_TYPE(id, name, description) values (2, 'PERCENT', 'Percentage commission.');

CREATE TABLE COMMISSION_PAYER (
	id	integer,
	name	varchar not null,
	constraint COMMISSION_PAYER_PK primary key (id),
	constraint COMMISSION_PAYER_UQ unique (name)
);

INSERT INTO COMMISSION_PAYER(id, name) values (1, 'USER');
INSERT INTO COMMISSION_PAYER(id, name) values (2, 'ORGANIZER');


CREATE TABLE EVENT_COMMISSION_SETTINGS (
	id		bigserial,
	customer_id	bigint not null,
	application_id bigint not null,
	minimum_commission	decimal(10,2) default 5.0,
	maximum_commission	decimal(10,2) not null,
	commission_type_id	integer not null,
	commission_value	decimal(10,2),
	free_commission_value	decimal(10,2) default 0,
	commission_payer_id	integer not null,
	created_at	timestamp without time zone default current_timestamp,
	updated_at  timestamp without time zone,
	constraint EVENT_COMMISSION_SETTINGS_PK PRIMARY KEY (id),
	constraint EVENT_COMMISSION_SETTINGS_CUSTOMER_FK FOREIGN KEY (customer_id) REFERENCES CUSTOMER(ID),
	constraint EVENT_COMMISSION_SETTINGS_APPLICATION_FK FOREIGN KEY (application_id) REFERENCES APPLICATION(ID),
	constraint EVENT_COMMISSION_SETTINGS_UQ UNIQUE (customer_id, application_id),
	constraint EVENT_COMMISSION_SETTINGS_COM_TYPE_FK FOREIGN KEY (commission_type_id) REFERENCES COMMISSION_TYPE(ID),
	constraint EVENT_COMMISSION_SETTINGS_PAY_TYPE_FK FOREIGN KEY (commission_payer_id) REFERENCES COMMISSION_PAYER(ID)
);

ALTER TABLE CUSTOMER_PAYMENT_GATEWAY 
	ADD CONNECTED_PAYMENT_GATEWAY_ACCOUNT	VARCHAR;
	
ALTER TABLE TRANSACTION
	ADD COMMISSION	DECIMAL(10,2) DEFAULT 0.0;
	
ALTER TABLE TRANSACTION_REPORT
	ADD CONTRIBUTION_DISCOUNT DECIMAL(10,2) DEFAULT 0,0;

ALTER TABLE TRANSACTION_REPORT
	ADD CONTRIBUTION_COMMISSION DECIMAL(10,2) DEFAULT 0,0;
	
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('findEventCommissionSettings','Method for get the Event Commission Settings.', 'com.us.weavx.core.services.impl.FindEventCommissionSettingsMethodImpl',2,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('updateEventCommissionSettings','Method for update a existing Event Commission Settings.', 'com.us.weavx.core.services.impl.UpdateEventCommissionSettingsMethodImpl',3,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('addEventCommissionSettings','Method for set event commission Settings.', 'com.us.weavx.core.services.impl.AddEventCommissionSettingsMethodImpl',3,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('connectPaymentGatewayAccount','Method for connect Payment Gateway Accounts.', 'com.us.weavx.core.services.impl.ConnectNewPaymentGatewayAccountMethodImpl',3,2);

INSERT INTO PROPERTY (ID, NAME) VALUES (207, 'COMISION');
INSERT INTO PROPERTY (ID, NAME) VALUES (208, 'FEE_ROW_TEMPLATE');

INSERT INTO CUSTOMER_PROPERTY_LANG(PROPERTY_ID, CUSTOMER_ID, LANG_ID, PROPERTY_VALUE) VALUES (207, 62, 1, 'Comisión');
INSERT INTO CUSTOMER_PROPERTY_LANG(PROPERTY_ID, CUSTOMER_ID, LANG_ID, PROPERTY_VALUE) VALUES (207, 62, 2, 'Fee');

INSERT INTO CUSTOMER_PROPERTY_LANG(PROPERTY_ID, CUSTOMER_ID, LANG_ID, PROPERTY_VALUE) VALUES (208, 62, 1, '/var/lib/templates/fee_row.html');
INSERT INTO CUSTOMER_PROPERTY_LANG(PROPERTY_ID, CUSTOMER_ID, LANG_ID, PROPERTY_VALUE) VALUES (208, 62, 2, '/var/lib/templates/fee_row.html');
