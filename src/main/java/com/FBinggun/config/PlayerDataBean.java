package com.FBinggun.config;

import com.FBinggun.ChengHao;
import com.FBinggun.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

public class PlayerDataBean {
    private static final String ROOT_PATH = "chenghao";
    private static final String DEFAULT_CHENGHAO_KEY = "default";
    private final Map<String, PlayerChengHaoBean> chengHaoBeanMap = new LinkedHashMap<>();
    private final File dataFile;
    private final ChengHao plugin;
    private final FileConfiguration configuration;
    private PlayerChengHaoBean defaultChengHao;
    private PlayerChengHaoBean currentChengHao;

    public PlayerDataBean(ChengHao plugin, String playerName) {
        this.plugin=plugin;
        dataFile = plugin.getDataFolder().toPath().resolve("Player").resolve(playerName + ".yml").toFile();
        configuration = Utils.load(dataFile);
        //Init data
        if (!configuration.isSet(ROOT_PATH)) {
            configuration.set("chenghao.default.expireTime", 0);
            configuration.set("chenghao.default.use", Boolean.TRUE);
        }
        //Load data
        ConfigurationSection configurationSection = configuration.getConfigurationSection(ROOT_PATH);
        for (String key : configurationSection.getKeys(false)) {
            PlayerChengHaoBean chengHaoBean = PlayerChengHaoBean.create(key, configurationSection.getConfigurationSection(key));
            if (key.equals(DEFAULT_CHENGHAO_KEY)) {
                defaultChengHao = chengHaoBean;
            }
            if (chengHaoBean.isUsing()) {
                if (currentChengHao != null) {
                    chengHaoBean.setUsing(false);
                }
                currentChengHao = chengHaoBean;
            }
            chengHaoBeanMap.put(key, chengHaoBean);
        }
        //Fix default key
        if (defaultChengHao == null) {
            configuration.set("chenghao.default.expireTime", 0);
            configuration.set("chenghao.default.use", Boolean.FALSE);
            chengHaoBeanMap.put(DEFAULT_CHENGHAO_KEY, defaultChengHao = PlayerChengHaoBean.create(DEFAULT_CHENGHAO_KEY, configurationSection.getConfigurationSection("default")));
        }
        //Fix current ChengHao
        if (currentChengHao == null) {
            defaultChengHao.setUsing(true);
            currentChengHao = defaultChengHao;
        }
        //Clean up
        cleanUpHistoryColumn(configuration);
        //SaveChanges
        save();
    }

    private void cleanUpHistoryColumn(FileConfiguration configuration) {
        if (configuration.isSet("id")) {
            configuration.set("id", null);
        }
    }

    public ChengHaoBean getCurrentChengHao() {
        return currentChengHao;
    }

    public void setCurrentChengHao(PlayerChengHaoBean bean) {
        if (chengHaoBeanMap.containsValue(bean)) {
            currentChengHao.setUsing(false);
            bean.setUsing(true);
            currentChengHao = bean;
        }
        save();
    }

    public void setCurrentChengHao(String key) {
        PlayerChengHaoBean bean = chengHaoBeanMap.get(key);
        if (bean != null) {
            currentChengHao.setUsing(false);
            bean.setUsing(true);
            currentChengHao = bean;
        }
        save();
    }

    public void setCurrentChengHao(String key, long expiredTime) {
        PlayerChengHaoBean playerChengHaoBean = chengHaoBeanMap.get(key);
        currentChengHao.setUsing(false);
        if (playerChengHaoBean != null) {
            playerChengHaoBean.setUsing(true);
            playerChengHaoBean.setExpireTime(expiredTime);
            currentChengHao = playerChengHaoBean;
        } else {
            configuration.set("chenghao." + key + ".expireTime", expiredTime);
            configuration.set("chenghao." + key + ".use", Boolean.TRUE);
            chengHaoBeanMap.put(key, currentChengHao=PlayerChengHaoBean.create(key, configuration.getConfigurationSection(ROOT_PATH + "." + key)));
        }
        save();
    }

    public void setCurrentChengHao(String key, long expiredTime, Map<String, Integer> attitudeMap) {
        PlayerChengHaoBean playerChengHaoBean = chengHaoBeanMap.get(key);
        currentChengHao.setUsing(false);
        if (playerChengHaoBean != null) {
            playerChengHaoBean.setUsing(true);
            playerChengHaoBean.setExpireTime(expiredTime);
            currentChengHao=playerChengHaoBean;
        } else {
            configuration.set("chenghao." + key + ".expireTime", expiredTime);
            configuration.set("chenghao." + key + ".use", Boolean.TRUE);
            for (Map.Entry<String, Integer> entry : attitudeMap.entrySet()) {
                configuration.set("chenghao." + key + "." + entry.getKey(), entry.getValue());
            }
            chengHaoBeanMap.put(key, currentChengHao=PlayerChengHaoBean.create(key, configuration.getConfigurationSection(ROOT_PATH + "." + key)));
        }
        save();
    }

    public void setCurrentChengHao(MuBanBean muBan, long expiredTime) {
        String key = muBan.getKey();
        PlayerChengHaoBean playerChengHaoBean = chengHaoBeanMap.get(key);
        currentChengHao.setUsing(false);
        if (playerChengHaoBean != null) {
            playerChengHaoBean.setUsing(true);
            playerChengHaoBean.setExpireTime(expiredTime);
            currentChengHao = playerChengHaoBean;
        } else {
            configuration.set("chenghao." + key + ".expireTime", expiredTime);
            configuration.set("chenghao." + key + ".use", Boolean.TRUE);
            chengHaoBeanMap.put(key, currentChengHao=PlayerChengHaoBean.create(muBan, configuration.getConfigurationSection(ROOT_PATH + "." + key)));
        }
        save();
    }

    public void removeChengHao(String key) {
        PlayerChengHaoBean bean = chengHaoBeanMap.remove(key);
        if (bean != null) {
            if (bean.equals(currentChengHao)) {
                bean.setUsing(false);
                currentChengHao = defaultChengHao;
            }
            configuration.set(ROOT_PATH + "." + key, null);
        }
        save();
    }

    public Map<String, PlayerChengHaoBean> getChengHaoBeanMap() {
        return chengHaoBeanMap;
    }

    public void save() {
        try {
            configuration.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE,"玩家数据保存失败！",e);
        }
    }
}
