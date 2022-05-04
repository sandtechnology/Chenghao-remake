package com.FBinggun.config;

import com.FBinggun.Constants;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MuBanBean extends ChengHaoBean {
    MuBanBean(String key, Map<String, Integer> attitudeMap) {
        super(key, attitudeMap);
    }

    public static MuBanBean create(String key, ConfigurationSection section) {
        Map<String, Integer> attitudeMap = new HashMap<>();
        Set<String> sectionKeys = section.getKeys(false);
        for (String attitude : Constants.AttitudeList) {
            if (sectionKeys.contains(attitude)) {
                attitudeMap.put(attitude, section.getInt(attitude));
            }
        }
        return new MuBanBean(section.getString("name", key), attitudeMap);
    }
}
