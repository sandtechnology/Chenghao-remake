package com.FBinggun.config;

import com.FBinggun.ChengHao;
import com.FBinggun.Utils;

import java.util.Collections;
import java.util.Map;

abstract public class ChengHaoBean {
    private final String key;
    private final String coloredName;
    private final Map<String, Integer> attitudeMap;

    ChengHaoBean(String key, Map<String, Integer> attitudeMap) {
        this.key = key;
        this.coloredName = Utils.translateColorCode(key);
        this.attitudeMap = attitudeMap;
    }

    public String getKey() {
        return key;
    }

    public String getColoredName() {
        if (key.equalsIgnoreCase("default")) {
            return Utils.translateColorCode(ChengHao.getInstance().getConfigStore().getDefaultChengHao());
        }
        return coloredName;
    }

    public Map<String, Integer> getAttitudeUnmodifiableMap() {
        return Collections.unmodifiableMap(attitudeMap);
    }
}
