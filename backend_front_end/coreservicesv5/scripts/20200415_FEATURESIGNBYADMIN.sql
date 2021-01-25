INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('registerNewAdminDocumentSignature','method for register new document signature by admin operator', 'com.us.weavx.core.services.impl.SetSignedDocumentAdminMethodImpl',3,3);
INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (20, 'REGISTER_DOCUMENT_SIGNATURE', 'Permite a un operador margar un contrato como firmado.');
INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (3, 20);

