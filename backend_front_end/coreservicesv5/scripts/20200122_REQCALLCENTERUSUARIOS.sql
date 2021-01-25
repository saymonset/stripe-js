ALTER TABLE CUSTOMER_USER
	ADD CREATED_AT timestamp without time zone DEFAULT current_timestamp;
	
update customer_user cu set created_at = (select min(t.tx_date) from transaction t inner join transaction_user_data tud on t.tx_user_data_id = tud.id where tud.customer_user_id = cu.id) where cu.id is not null;

INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('customerUserReport','Method for find customer users.', 'com.us.weavx.core.services.impl.CustomerUserReportMethodImpl',2,7);

INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (10, 'USERS_QUERY', 'Permite consultar usuarios.');
INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (11, 'MODIFY_EMAIL', 'Permite modificar el correo de un usuario.');
INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (12, 'MODIFY_USER_DATA', 'Permite modificar los datos de un usuario.');
INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (13, 'TX_QUERY', 'Permite consultar las transacciones de un usuario.');
INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (14, 'SEND_MAGIC_LINK', 'Permite enviar un magic link al usuario.');
INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (15, 'SEND_PURCHASE_RECEIPT', 'Permite enviar el recibo de compra al usuario.');
INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (16, 'INVALIDATE_TRANSACTION', 'Permite invalidar una transacción.');

INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (2, 10);
INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (2, 11);
INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (2, 12);
INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (2, 13);
INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (2, 14);
INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (2, 15);
INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (3, 16);


CREATE TABLE DEVICE_TYPE (
	id	integer,
	name	varchar not null,
	description varchar not null,
	constraint DEVICE_TYPE_PK PRIMARY KEY (id),
	constraint DEVICE_TYPE_NAME_UQ UNIQUE (name)
);

INSERT INTO DEVICE_TYPE (ID, NAME, DESCRIPTION) values (1, 'EMAIL', 'Correo electronico');
INSERT INTO DEVICE_TYPE (ID, NAME, DESCRIPTION) values (2, 'PHONE', 'Telefono movil');


CREATE TABLE VALIDATION_CODE (
	id	bigserial,
	code	varchar not null,
	device	varchar not null,
	device_type_id	integer not null,
	try_count	integer default 0,
	is_used		boolean default false,
	is_expired	boolean default false,
	expires_at	timestamp without time zone not null,
	created_at	timestamp without time zone default current_timestamp,
	constraint VALIDATION_CODE_PK PRIMARY KEY (id),
	constraint VALIDATION_CODE_DEVICE_TYPE_FK FOREIGN KEY (device_type_id) REFERENCES DEVICE_TYPE(ID)
);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('validateDevice','Method for validate a device.', 'com.us.weavx.core.services.impl.ValidateDeviceMethodImpl',3,5);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('validateDeviceCode','Method for validate a device otp code.', 'com.us.weavx.core.services.impl.ValidateDeviceCodeMethodImpl',3,5);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('updateCustomerUserEmail','Method for validate a device otp code.', 'com.us.weavx.core.services.impl.UpdateCustomerUserEmailMethodImpl',3,4);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('findAdminCustomerUserTransactions','Method for obtain customer user transactions.', 'com.us.weavx.core.services.impl.FindCustomerUserAdminTransactionsMethodImpl',3,7);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('resendAdminPurchaseReceipt','Method for resend the purchase receipt from admin.', 'com.us.weavx.core.services.impl.ResendPurchaseReceiptAdminMethodImpl',3,7);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('invalidateTransaction','Method for invalidate transaction.', 'com.us.weavx.core.services.impl.InvalidateTransactionMethodImpl',3,4);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('listAllBlacklistItems','Method for list all blacklist items.', 'com.us.weavx.core.services.impl.ListAllBlackListItemsMethodImpl',2,7);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('cleanBlacklistItem','Method for list all blacklist items.', 'com.us.weavx.core.services.impl.CleanBlacklistDataMethodImpl',3,7);


