ALTER TABLE EVENT_ASSET_LANG 
ADD ASSET_PARAMS VARCHAR;

INSERT INTO METHOD (NAME, DESCRIPTION, IMPLEMENTOR_CLASS, METHOD_TYPE_ID, MODULE_ID) VALUES ('findSimLiveEventData','Method for get the Sim-Live-Event current parameters.', 'com.us.weavx.core.services.impl.FindSimLiveEventDataMethodImpl',2,3);





