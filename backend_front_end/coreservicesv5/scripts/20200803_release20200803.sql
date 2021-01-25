INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('globalTransactionReport','Method for get global report.', 'com.us.weavx.core.services.impl.GlobalTransactionReportMethodImpl',2,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('findLastCustomerUserDataByEmail','Method for get global report.', 'com.us.weavx.core.services.impl.FindLastCustomerUserTransactionMethodImpl',2,3);
INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (21, 'GLOBAL_REPORT', 'Permite a un operador consultar el reporte global de transacciones.');
INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (2, 21);

CREATE INDEX I3_TRANSACTION_REPORT_EMAIL ON TRANSACTION_REPORT(person_email);
CREATE INDEX I4_TRANSACTION_REPORT_EMAIL ON TRANSACTION_REPORT(person_firstname);
CREATE INDEX I5_TRANSACTION_REPORT_EMAIL ON TRANSACTION_REPORT(person_lastname);

