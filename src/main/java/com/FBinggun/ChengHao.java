package com.FBinggun;

import com.FBinggun.command.ChengHaoCommand;
import com.FBinggun.config.AttitudeDamageMessageConfig;
import com.FBinggun.config.PluginConfigStore;
import com.FBinggun.config.PlayerDataManager;
import com.FBinggun.listener.DamageListener;
import com.FBinggun.listener.PlayerListener;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ChengHao
        extends JavaPlugin {
    private static ChengHao instance;
    private PlayerDataManager playerDataManager = null;
    private PluginConfigStore pluginConfigStore = null;
    private AttitudeDamageMessageConfig attitudeDamageMessageConfig=null;

    public static ChengHao getInstance() {
        if(instance==null){
            throw new IllegalStateException("插件尚未加载！");
        }
        return instance;
    }

    public PlayerDataManager getConfigManager() {
        if(playerDataManager==null){
            throw new IllegalStateException("插件尚未加载！");
        }
        return playerDataManager;
    }

    public PluginConfigStore getConfigStore() {
        if(pluginConfigStore==null){
            throw new IllegalStateException("插件尚未加载！");
        }
        return pluginConfigStore;
    }

    public void onEnable() {
        instance = this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        if (!(new File(getDataFolder() + "/Player")).exists()) {
            (new File(getDataFolder() + "/Player")).mkdir();
        }
        File file = new File(getDataFolder(), "config.yml");
        File file1 = new File(getDataFolder(), "muban.yml");
        File file2 = new File(getDataFolder(), "sx.yml");
        if (!file.exists()) {
            saveDefaultConfig();
        }
        if (!file1.exists()) {
            saveResource("muban.yml", true);
        }
        if (!file2.exists()) {
            saveResource("sx.yml", true);
        }

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        playerDataManager = new PlayerDataManager(this);
        pluginConfigStore = new PluginConfigStore(this);
        attitudeDamageMessageConfig=new AttitudeDamageMessageConfig(this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this,attitudeDamageMessageConfig), this);
        TabExecutor tabExecutor=new ChengHaoCommand(this);
        getCommand("ch").setExecutor(tabExecutor);
        getCommand("ch").setTabCompleter(tabExecutor);
        getLogger().info("§a称号插件启动成功感谢使用此插件 版本：3.0.0");
        getLogger().info("§a原作者:疯子冰棍(F_Binggun) 重制作者:sandtechnology");
        getLogger().info("§a原作者QQ:1728482805");
        getLogger().info("§c原作者接定制插件 技术接单");
        getLogger().info("§c如有对此插件任何疑问或建议 请联系重制作者");
    }

    @Override
    public void onDisable() {
        getLogger().info("§a称号插件保存数据中...");

        pluginConfigStore.shutdown();
        playerDataManager.shutdown();
        attitudeDamageMessageConfig.shutdown();
        instance=null;
        playerDataManager =null;
        pluginConfigStore =null;
        getLogger().info("§a称号插件禁用成功感谢使用此插件 版本：3.0.0");
    }
}



