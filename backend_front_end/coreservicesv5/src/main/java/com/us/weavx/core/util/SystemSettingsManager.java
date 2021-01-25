package com.us.weavx.core.util;

import com.us.weavx.core.model.SystemProperty;
import com.us.weavx.core.services.tx.ConfigurationTxServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class SystemSettingsManager {

    private HashMap<String, String> settings;


    public SystemSettingsManager(@Autowired ConfigurationTxServices services) {
        settings = new HashMap<>();
        List<SystemProperty> properties = services.getSystemProperties();
        for (SystemProperty prop : properties) {
            settings.put(prop.getName(), prop.getValue());
        }
    }

    public String getSystemProperty(String name) {
        return settings.get(name);
    }
}
