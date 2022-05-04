package com.FBinggun.config;

import com.FBinggun.ChengHao;
import com.FBinggun.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class MuBanDataBean extends FileWatcher{

    private final File dataFile;
    private FileConfiguration configuration;
    private final Map<String, MuBanBean> muBanBeanMap = new HashMap<>();
    private final ChengHao plugin;

    public MuBanDataBean(ChengHao plugin) {
        super(plugin,plugin.getDataFolder().toPath().resolve("muban.yml").toFile());
        this.plugin=plugin;
        dataFile = plugin.getDataFolder().toPath().resolve("muban.yml").toFile();
        configuration = Utils.load(dataFile);
        ConfigurationSection root;
        if (!configuration.isSet("chenghao")) {
            root = configuration.createSection("chenghao");
        } else {
            root = configuration.getConfigurationSection("chenghao");
        }
        for (String key : root.getKeys(false)) {
            muBanBeanMap.put(key, MuBanBean.create(key, root.getConfigurationSection(key)));
        }
    }

    public MuBanBean getMuBanBean(String key) {
        return muBanBeanMap.get(key);
    }

    @Override
    protected void onModified() {
        plugin.getLogger().log(Level.INFO,"检测到配置文件muban.yml重载，自动重载中...");
        configuration = Utils.load(dataFile);
        ConfigurationSection root;
        if (!configuration.isSet("chenghao")) {
            root = configuration.createSection("chenghao");
        } else {
            root = configuration.getConfigurationSection("chenghao");
        }
        muBanBeanMap.clear();
        for (String key : root.getKeys(false)) {
            muBanBeanMap.put(key, MuBanBean.create(key, root.getConfigurationSection(key)));
        }
    }
}
