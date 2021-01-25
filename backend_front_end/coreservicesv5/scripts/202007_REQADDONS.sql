CREATE TABLE ADD_ON_CATEGORY (
	id	integer,
	name	varchar not null,
	constraint EVENT_ADD_ON_CATEGORY_PK PRIMARY KEY (ID),
	constraint EVENT_ADD_ON_CAT_NAME_UQ UNIQUE (name),
);

INSERT INTO ADD_ON_CATEGORY (ID, NAME) VALUES (1, 'BOOK');

CREATE TABLE ADD_ON_CATEGORY_LANG (
	add_on_category_id integer,
	lang_id bigint not null,
	name	varchar,
	constraint ADD_ON_CAT_LANG_PK PRIMARY KEY (add_on_category_id, lang_id),
	constraint ADD_ON_CAT_LG_CAT_FK FOREIGN KEY (add_on_category_id) REFERENCES CUSTOMER_ADD_ON_CATEGORY(id),
	constraint ADD_ON_CAT_LG_LANG_FK FOREIGN KEY (lang_id) REFERENCES LANG(id)
);

INSERT INTO ADD_ON_CATEGORY_LANG (ADD_ON_CATEGORY_ID, LANG_ID, NAME) VALUES (1, 1, 'LIBRO');
INSERT INTO ADD_ON_CATEGORY_LANG (ADD_ON_CATEGORY_ID, LANG_ID, NAME) VALUES (1, 2, 'BOOK');

CREATE TABLE CATEGORY_FIELD_DATA_TYPE (
	id	integer,
	name	varchar not null,
	constraint CATEGORY_FIELD_DATA_TYPE_PK PRIMARY KEY (id),
	constraint CATEGORY_FIELD_DATA_TYPE_UQ UNIQUE (name)
);

INSERT INTO CATEGORY_FIELD_DATA_TYPE(ID, NAME) VALUES (1, 'STRING');
INSERT INTO CATEGORY_FIELD_DATA_TYPE(ID, NAME) VALUES (2, 'NUMERIC');
INSERT INTO CATEGORY_FIELD_DATA_TYPE(ID, NAME) VALUES (3, 'BOOLEAN');


CREATE TABLE CATEGORY_FIELD (
	id	bigint,
	name	varchar not null,
	is_required boolean default true,
	category_field_data_type_id	integer not null,
	add_on_category_id	bigint not null,
	constraint ADD_ON_META_DATA_FIELD_PK PRIMARY KEY (ID),
	constraint ADD_ON_META_DATA_FIELD_N_UQ UNIQUE (CUSTOMER_ID, APPLICATION_ID, NAME),
	constraint ADD_ON_META_DATA_FIELD_CAT_FK FOREIGN KEY (add_on_category_id) REFERENCES ADD_ON_CATEGORY(id),
	constraint ADD_ON_META_DATA_FIELD_DATA_T FOREIGN KEY (category_field_data_type_id) REFERENCES CATEGORY_FIELD_DATA_TYPE(id)
);

INSERT INTO CATEGORY_FIELD(ad_on_category_id, name, category_field_data_type_id, is_required) VALUES (1,'YEAR',2,true);
INSERT INTO CATEGORY_FIELD(ad_on_category_id, name, category_field_data_type_id, is_required) VALUES (2,'ISBN',2,true);
INSERT INTO CATEGORY_FIELD(ad_on_category_id, name, category_field_data_type_id, is_required) VALUES (3,'AUTHOR',1,true);
INSERT INTO CATEGORY_FIELD(ad_on_category_id, name, category_field_data_type_id, is_required) VALUES (4,'PUBLISHER',1,false);

CREATE TABLE CATEGORY_FIELD_LANG (
	category_field_id	bigint,
	lang_id	bigint,
	label	varchar,
	constraint CATEGORY_FIELD_LANG_PK PRIMARY KEY (category_field_id, lang_id),
	constraint CATEGORY_FIELD_LANG_CAT_FK FOREIGN KEY (category_field_id) REFERENCES CATEGORY_FIELD(id),
	constraint CATEGORY_FIELD_LANG_LANG_FK FOREIGN KEY (lang_id) REFERENCES LANG(id)
);

INSERT INTO CATEGORY_FIELD_LANG(CATEGORY_FIELD_ID, LANG_ID) VALUES (1, 1, 'AÑO');
INSERT INTO CATEGORY_FIELD_LANG(CATEGORY_FIELD_ID, LANG_ID) VALUES (1, 2, 'YEAR');
INSERT INTO CATEGORY_FIELD_LANG(CATEGORY_FIELD_ID, LANG_ID) VALUES (2, 1, 'ISBN');
INSERT INTO CATEGORY_FIELD_LANG(CATEGORY_FIELD_ID, LANG_ID) VALUES (2, 2, 'ISBN');
INSERT INTO CATEGORY_FIELD_LANG(CATEGORY_FIELD_ID, LANG_ID) VALUES (3, 1, 'AUTOR');
INSERT INTO CATEGORY_FIELD_LANG(CATEGORY_FIELD_ID, LANG_ID) VALUES (3, 2, 'AUTHOR');
INSERT INTO CATEGORY_FIELD_LANG(CATEGORY_FIELD_ID, LANG_ID) VALUES (4, 1, 'EDITORIAL');
INSERT INTO CATEGORY_FIELD_LANG(CATEGORY_FIELD_ID, LANG_ID) VALUES (4, 2, 'PUBLISHER');


CREATE TABLE ADD_ON_MEDIA_TYPE (
	id	bigint,
	name	varchar,
	constraint ADD_ON_MEDIA_TYPE_PK PRIMARY KEY (id),
	constraint ADD_ON_MEDIA_TYPE_NAME_UQ UNIQUE (name)
);

INSERT INTO ADD_ON_MEDIA_TYPE (id, name) VALUES (1, 'PICTURE');
INSERT INTO ADD_ON_MEDIA_TYPE (id, name) VALUES (2, 'VIDEO');

CREATE TABLE EVENT_ADD_ON (
	id	bigserial,
	name	varchar not null,
	add_on_media_type_id	bigint,
	add_on_media_url	varchar,
	add_on_category_id	integer not null,
	customer_id	bigint not null,
	application_id bigint not null,
	price	number(10,2) not null,
	constraint EVENT_ADD_ON_PK PRIMARY KEY (ID),
	constraint EVENT_ADD_ON_NAME_UQ UNIQUE (name, customer_id, application_id),
	constraint EVENT_ADD_ON_CUST_FK FOREIGN KEY (customer_id) REFERENCES CUSTOMER(ID),
	constraint EVENT_ADD_ON_APP_FK FOREIGN KEY (application_id) REFERENCES APPLICATION(ID)
);

INSERT INTO EVENT_ADD_ON (name, add_on_category_id, customer_id, application_id, price, add_on_media_type_id, add_on_media_url) VALUES ('INVESTING WITH POWER',1,163,173,100.00,1,'URL_FOTO');
INSERT INTO EVENT_ADD_ON (name, add_on_category_id, customer_id, application_id, price, add_on_media_type_id, add_on_media_url) VALUES ('CREANDO RIQUEZA II',1,163,173,100.00,1,'URL_FOTO_2');

CREATE TABLE EVENT_ADD_ON_FIELDS (
	event_add_on_id	bigint,
	category_field_id bigint,
	field_value	varchar,
	constraint EVENT_ADD_ON_FIELDS_PK PRIMARY KEY (event_add_on_id, category_field_id),
	constraint EVENT_ADD_ON_FIELDS_CAT_FK FOREIGN KEY (category_field_id) REFERENCES CATEGORY_FIELD(id),
	constraint EVENT_ADD_ON_FIELDS_ADDON_FK FOREIGN KEY (event_add_on_id) REFERENCES EVENT_ADD_ON(id)
);

ALTER TABLE TRANSACTION_DETAIL
ADD EVENT_ADD_ON_ID BIGINT;

ALTER TABLE TRANSACTION_DETAIL
ADD CONSTRAINT TRANS_DETAIL_EVENT_ADD_ON_FK FOREIGN KEY (EVENT_ADD_ON_ID) REFERENCES EVENT_ADD_ON(id);

ALTER TABLE TRANSACTION_DETAIL
ALTER COLUMN fund_id integer NULL;
