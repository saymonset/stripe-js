CREATE TABLE RESTRICTED_EVENT_ATTENDEE (
	id 				bigserial,
	customer_id		bigint,
	application_id	bigint,
	email			varchar not null,
	is_active		boolean default true,
	constraint REST_EVENT_ATTENDEE_PK primary key (id),
	constraint REST_EVENT_ATTENDEE_UK unique (customer_id, application_id, email),
	constraint REST_EVENT_ATTENDEE_CUSTOMER_FK foreign key (customer_id) references CUSTOMER(id) on delete cascade,
	constraint REST_EVENT_ATTENDEE_APPLICATION_FK foreign key (application_id) references APPLICATION(id) on delete cascade,
	constraint REST_EVENT_ATTENDEE_UQ unique (customer_id, application_id, email, is_active)
);

CREATE INDEX RESTRICTED_EVENT_ATTENDE_I1 ON RESTRICTED_EVENT_ATTENDEE(customer_id, application_id, email, is_active);
CREATE INDEX RESTRICTED_EVENT_ATTENDE_I2 ON RESTRICTED_EVENT_ATTENDEE(customer_id, application_id);

CREATE TABLE EVENT_FUND_SETTINGS (
	id 				bigserial,
	customer_id		bigint,
	application_id	bigint,
	fund_id			bigint,
	allowedDaysToAccess	integer not null,
	start_date		timestamp without time zone default current_timestamp,
	end_date		timestamp without time zone default '2100-12-21',
	constraint EVENT_FUND_SETTINGS_PK primary key (id),
	constraint EVENT_FUND_SETTINGS_UK unique (customer_id, application_id, fund_id),
	constraint EVENT_FUND_SETTINGS_CUST_FK foreign key (customer_id) references CUSTOMER(id) on delete cascade,
	constraint EVENT_FUND_SETTINGS_APP_FK foreign key (application_id) references APPLICATION(id) on delete cascade,
	constraint EVENT_FUND_SETTINGS_FUND_FK foreign key (fund_id) references FUND(id) on delete cascade
);

INSERT INTO TOKEN_STATUS(ID, NAME, DESCRIPTION) VALUES (6, 'Valid', 'Valid Token');

INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('addRestrictedAttendees','Method for add restricted attendees for an event.', 'com.us.weavx.core.services.impl.AddRestrictedEventAttendeesMethodImpl',3,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('updateRestrictedAttendee','Method for update specific restricted attendee.', 'com.us.weavx.core.services.impl.UpdateRestrictedEventAttendeeMethodImpl',3,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('removeRestrictedAttendee','Method for remove restricted attendees for an event.', 'com.us.weavx.core.services.impl.RemoveRestrictedEventAttendeeMethodImpl',3,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('listRestrictedEventAttendees','Method for list all restricted attendees for an event.', 'com.us.weavx.core.services.impl.ListAllRestrictedEventAttendeesMethodImpl',2,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('addEventFundSettings','Method for add a Event Fund Settings.', 'com.us.weavx.core.services.impl.AddEventFundSettingsMethodImpl',3,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('updateEventFundSettings','Method for update a existing Event Fund Settings.', 'com.us.weavx.core.services.impl.UpdateEventFundSettingsMethodImpl',3,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('removeEventFundSettings','Method for remove a existing Event Fund Settings.', 'com.us.weavx.core.services.impl.RemoveEventFundSettingsMethodImpl',3,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('findEventFundSettings','Method for get the Event Fund Settings.', 'com.us.weavx.core.services.impl.FindEventFundSettingsMethodImpl',2,3);



INSERT INTO TRANSACTION_STATUS (ID, NAME) VALUES (10, 'USER_EXPIRED_ACCESS');

INSERT INTO SYSTEM_MESSAGE(ID, NAME) VALUES (39, 'USER_RESTRICTED_ACCESS');
INSERT INTO SYSTEM_MESSAGE(ID, NAME) VALUES (40, 'USER_EVENT_ACCESS_EXPIRED');

INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (54, 39, 1, 'No estas autorizado para comprar este evento, si consideras que esto es un error por favor llama al teléfono: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (54, 39, 2, 'You are not authorized to buy this event, if you believe this is a mistake please call us at: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (55, 39, 1, 'No estas autorizado para comprar este evento, si consideras que esto es un error por favor llama al teléfono: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (55, 39, 2, 'You are not authorized to buy this event, if you believe this is a mistake please call us at: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (56, 39, 1, 'No estas autorizado para comprar este evento, si consideras que esto es un error por favor llama al teléfono: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (56, 39, 2, 'You are not authorized to buy this event, if you believe this is a mistake please call us at: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (57, 39, 1, 'No estas autorizado para comprar este evento, si consideras que esto es un error por favor llama al teléfono: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (57, 39, 2, 'You are not authorized to buy this event, if you believe this is a mistake please call us at: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (58, 39, 1, 'No estas autorizado para comprar este evento, si consideras que esto es un error por favor llama al teléfono: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (58, 39, 2, 'You are not authorized to buy this event, if you believe this is a mistake please call us at: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (59, 39, 1, 'No estas autorizado para comprar este evento, si consideras que esto es un error por favor llama al teléfono: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (59, 39, 2, 'You are not authorized to buy this event, if you believe this is a mistake please call us at: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (60, 39, 1, 'No estas autorizado para comprar este evento, si consideras que esto es un error por favor llama al teléfono: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (60, 39, 2, 'You are not authorized to buy this event, if you believe this is a mistake please call us at: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (61, 39, 1, 'No estas autorizado para comprar este evento, si consideras que esto es un error por favor llama al teléfono: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (61, 39, 2, 'You are not authorized to buy this event, if you believe this is a mistake please call us at: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (62, 39, 1, 'No estas autorizado para comprar este evento, si consideras que esto es un error por favor llama al teléfono: +1(111)111-1111');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (62, 39, 2, 'You are not authorized to buy this event, if you believe this is a mistake please call us at: +1(111)111-1111');

INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (54, 40, 1, 'Tu período de acceso al evento ha finalizado');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (54, 40, 2, 'Your access period to the event has ended.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (55, 40, 1, 'Tu período de acceso al evento ha finalizado');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (55, 40, 2, 'Your access period to the event has ended.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (56, 40, 1, 'Tu período de acceso al evento ha finalizado');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (56, 40, 2, 'Your access period to the event has ended.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (57, 40, 1, 'Tu período de acceso al evento ha finalizado');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (57, 40, 2, 'Your access period to the event has ended.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (58, 40, 1, 'Tu período de acceso al evento ha finalizado');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (58, 40, 2, 'Your access period to the event has ended.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (59, 40, 1, 'Tu período de acceso al evento ha finalizado');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (59, 40, 2, 'Your access period to the event has ended.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (60, 40, 1, 'Tu período de acceso al evento ha finalizado');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (60, 40, 2, 'Your access period to the event has ended.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (61, 40, 1, 'Tu período de acceso al evento ha finalizado');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (61, 40, 2, 'Your access period to the event has ended.');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (62, 40, 1, 'Tu período de acceso al evento ha finalizado');
INSERT INTO CUSTOMER_SYSTEM_MESSAGES_LANG(customer_id, system_message_id, lang_id, message) VALUES (62, 40, 2, 'Your access period to the event has ended.');



