package com.FBinggun.config;

import com.FBinggun.Constants;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerChengHaoBean extends ChengHaoBean {
    private final ConfigurationSection section;
    private boolean isUsing;
    private long expireTime;

    PlayerChengHaoBean(String key, ConfigurationSection section, Map<String, Integer> attitudeMap) {
        super(key, attitudeMap);
        this.section = section;
        isUsing = section.getBoolean("use");
        //History Typo
        if (section.isSet("tiem")) {
            expireTime = section.getInt("tiem");
            section.set("tiem", null);
        } else {
            expireTime = section.getLong("expireTime");
        }
    }

    static PlayerChengHaoBean create(String key, ConfigurationSection section) {
        Map<String, Integer> attitudeMap = new HashMap<>();
        Set<String> sectionKeys = section.getKeys(false);
        for (String attitude : Constants.AttitudeList) {
            if (sectionKeys.contains(attitude)) {
                attitudeMap.put(attitude, section.getInt(attitude));
            }
        }
        return new PlayerChengHaoBean(key, section, attitudeMap);
    }

    static PlayerChengHaoBean create(MuBanBean bean, ConfigurationSection section) {
        for (Map.Entry<String, Integer> entry : bean.getAttitudeUnmodifiableMap().entrySet()) {
            section.set(entry.getKey(), entry.getValue());
        }
        return new PlayerChengHaoBean(bean.getKey(), section, new HashMap<>(bean.getAttitudeUnmodifiableMap()));
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
        section.set("expireTime", expireTime);
    }

    public boolean isExpired() {
        return expireTime != 0 && expireTime >= Instant.now().getEpochSecond();
    }

    public boolean isUsing() {
        return isUsing;
    }

    public void setUsing(boolean using) {
        isUsing = using;
        section.set("use", isUsing);
    }
}
