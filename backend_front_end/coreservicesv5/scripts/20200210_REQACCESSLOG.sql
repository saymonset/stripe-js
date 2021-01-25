CREATE TABLE AUDIT_LEVEL (
	id	integer,
	name	varchar not null,
	constraint AUDIT_LEVEL_PK primary key (id),
	constraint AUDIT_LEVEL_UQ unique (name)
)

INSERT INTO AUDIT_LEVEL(ID, NAME) values (0, 'ACTIVITY');
INSERT INTO AUDIT_LEVEL(ID, NAME) values (1, 'DEBUG');
INSERT INTO AUDIT_LEVEL(ID, NAME) values (2, 'ERROR');


CREATE TABLE AUDIT (
	id				bigserial,
	customer_id		bigint not null,
	application_id	bigint not null,
	who				varchar not null,
	created_at		timestamp without time zone default current_timestamp,
	source			varchar not null,
	what			varchar not null,
	ip_address		varchar not null,
	user_agent		varchar not null,
	audit_level_id		integer	default 0,
	constraint ACCESS_LOG_PK primary key (id),
	constraint ACCESS_LOG_CUSTOMER_FK foreign key (customer_id) references CUSTOMER(id),
	constraint ACCESS_LOG_APPLICATION_FK foreign key (application_id) references APPLICATION(id)
)



CREATE INDEX AUDIT_INDEX_WHO ON AUDIT(UPPER(who));
CREATE INDEX AUDIT_LOG_INDEX_WHEN ON AUDIT(created_at);
CREATE INDEX AUDIT_LOG_INDEX_WHERE ON AUDIT(UPPER(source));
CREATE INDEX AUDIT_LOG_INDEX_WHAT ON AUDIT(UPPER(what));
CREATE INDEX AUDIT_LOG_INDEX_IP ON AUDIT(UPPER(IP_ADDRESS));
CREATE INDEX AUDIT_LOG_INDEX_UA ON AUDIT(UPPER(USER_AGENT));

INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (18, 'AUDIT_QUERY', 'Permite consultar los registros de auditoria del sistema.');

INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (3, 18);

INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('auditReport','Method for query de audit records.', 'com.us.weavx.core.services.impl.AuditReportMethodImpl',3,7);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('addAuditRecord','Method for add new auditRecord', 'com.us.weavx.core.services.impl.AddAuditRecordMethodImpl',3,7);

INSERT INTO SYSTEM_PROPERTY(NAME, VALUE, DESCRIPTION) VALUES ('EMAIL_UPDATE_OTP_ENABLED', 'false', 'Enable or disable otp validation in update customer user email address');

ALTER TABLE CUSTOMER_USER
ADD CONSTRAINT CUSTOMER_USER_UQ UNIQUE (CUSTOMER_ID, USER_ID);

ALTER TABLE AUDIT 
	ADD data	varchar;

CREATE INDEX AUDIT_LOG_INDEX_DATA ON AUDIT(UPPER(data));


