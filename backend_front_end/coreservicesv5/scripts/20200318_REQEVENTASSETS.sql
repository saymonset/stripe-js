CREATE TABLE ASSET (
	id	integer,
	name	varchar not null,
	description varchar,
	created_at timestamp without time zone default current_timestamp,
	constraint EVENT_ASSET_PK primary key (id),
	constraint EVENT_ASSET_UQ unique (name)
);

INSERT INTO ASSET(ID, NAME, DESCRIPTION) VALUES (1, 'urlPlayer', 'Video Player URL');
INSERT INTO ASSET(ID, NAME, DESCRIPTION) VALUES (2, 'bannerRightImage', 'Right Banner image URL');
INSERT INTO ASSET(ID, NAME, DESCRIPTION) VALUES (3, 'bannerRightAction', 'Right Banner action URL');
INSERT INTO ASSET(ID, NAME, DESCRIPTION) VALUES (4, 'bannerLeftImage', 'Left Banner image URL');
INSERT INTO ASSET(ID, NAME, DESCRIPTION) VALUES (5, 'bannerLeftAction', 'Left Banner action URL');
INSERT INTO ASSET(ID, NAME, DESCRIPTION) VALUES (6, 'liveBackgroundImage', 'Live background image URL');


CREATE TABLE EVENT_ASSET_LANG (
	id	bigserial,
	asset_id	integer not null,
	customer_id	bigint not null,
	application_id	bigint not null,
	lang_id	integer not null,
	asset_value	varchar not null,
	constraint EVENT_ASSET_LANG_PK primary key (id),
	constraint EVENT_ASSET_LANG_UQ unique (asset_id, customer_id, application_id, lang_id),
	constraint EVENT_ASSET_ASSET_FK foreign key (asset_id) references ASSET(id) on delete cascade,
	constraint EVENT_ASSET_CUST_FK foreign key (customer_id) references CUSTOMER(id) on delete cascade,
	constraint EVENT_ASSET_APP_FK foreign key (application_id) references APPLICATION(id) on delete cascade,
	constraint EVENT_ASSET_LANG_FK foreign key (lang_id) references LANG(id)
);

INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('listAllAssets','Method for list all supported assets for an event', 'com.us.weavx.core.services.impl.ListAllAssetsMethodImpl',2,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('findEventAssets','Method for get the Assets of a given event', 'com.us.weavx.core.services.impl.FindEventAssetsMethodImpl',2,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('findEventAssetsByLang','Method for get the Assets of a given event and language', 'com.us.weavx.core.services.impl.FindEventAssetsByLangMethodImpl',2,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('updateEventAssets','Method for update a existing Event Assets.', 'com.us.weavx.core.services.impl.UpdateEventAssetsMethodImpl',3,3);
INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('addEventAssets','Method for set event assets.', 'com.us.weavx.core.services.impl.AddEventAssetsMethodImpl',3,3);

INSERT INTO ADMIN_FUNCTION(ID, NAME, DESCRIPTION) VALUES (19, 'EVENT_ASSETS', 'Permite modificar los assets de un evento.');

INSERT INTO ADMIN_ROLE_FUNCTION(ADMIN_ROLE_ID, ADMIN_FUNCTION_ID) VALUES (3, 19);

INSERT INTO APPLICATION_PARAMETER(application_id, name, value) values (,'PAYWALL_URL','http://54.87.118.120:8080/seminario-de-inversiones-18abr20');