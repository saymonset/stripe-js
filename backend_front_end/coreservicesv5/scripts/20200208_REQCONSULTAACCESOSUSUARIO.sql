INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (17, 'USERS_ACCESS_QUERY', 'Permite consultar los accesos históricos de usuarios.');
INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (3, 17);

INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('findUserAccessTokenInfoByEvent','Method for obtain customer user historic access tokens.', 'com.us.weavx.core.services.impl.GetUserAccessTokenInfoByEmailAndEventMethodImpl',3,7);
