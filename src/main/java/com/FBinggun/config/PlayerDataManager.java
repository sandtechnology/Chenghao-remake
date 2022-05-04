package com.FBinggun.config;

import com.FBinggun.ChengHao;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    private final ChengHao plugin;
    private final Map<String, PlayerDataBean> playerDataBeanMap = new ConcurrentHashMap<>();

    public PlayerDataManager(ChengHao plugin) {
        this.plugin = plugin;
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            loadPlayerData(onlinePlayer.getName());
        }
    }

    private synchronized PlayerDataBean loadPlayerData(String playerName) {
        playerName = playerName.toLowerCase(Locale.ROOT);
        PlayerDataBean dataBean = new PlayerDataBean(plugin, playerName);
        playerDataBeanMap.put(playerName, dataBean);
        return dataBean;
    }

    public PlayerDataBean getPlayerData(String playerName) {
        playerName = playerName.toLowerCase(Locale.ROOT);
        PlayerDataBean dataBean = playerDataBeanMap.get(playerName);
        if (dataBean == null) {
            return loadPlayerData(playerName);
        } else {
            return dataBean;
        }
    }

    public void purgePlayerDataFromMemory(String playerName) {
        playerName = playerName.toLowerCase(Locale.ROOT);
        PlayerDataBean dataBean = playerDataBeanMap.remove(playerName);
        if (dataBean != null) {
            dataBean.save();
        }
    }
    public void shutdown(){
        for (PlayerDataBean value : playerDataBeanMap.values()) {
            value.save();
        }
        playerDataBeanMap.clear();
    }
}
