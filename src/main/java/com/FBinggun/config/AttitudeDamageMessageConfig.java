package com.FBinggun.config;

import com.FBinggun.ChengHao;
import com.FBinggun.Utils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.logging.Level;


public class AttitudeDamageMessageConfig extends FileWatcher{
    private FileConfiguration configuration;
    private final ChengHao plugin;
    public AttitudeDamageMessageConfig(ChengHao plugin) {
        super(plugin,new File(plugin.getDataFolder(), "sx.yml"));
        this.plugin=plugin;
        File file = new File(plugin.getDataFolder(), "sx.yml");
        configuration = Utils.load(file);
    }

    public static String translateColor(String a) {
        return a.replaceAll("&", "§");
    }

    public String MFatal() {
        return translateColor(configuration.getString("Message.Fatal"));
    }


    public String MCrit() {
        return translateColor(configuration.getString("Message.Crit"));
    }


    public String MArmordamage() {
        return translateColor(configuration.getString("Message.Armordamage"));
    }


    public String MWither() {
        return translateColor(configuration.getString("Message.Wither"));
    }


    public String MPoison() {
        return translateColor(configuration.getString("Message.Poison"));
    }


    public String MFrozen() {
        return translateColor(configuration.getString("Message.Frozen"));
    }


    public String MWeakening() {
        return translateColor(configuration.getString("Message.Weakening"));
    }


    public String MNausea() {
        return translateColor(configuration.getString("Message.Nausea"));
    }


    public String MBlindness() {
        return translateColor(configuration.getString("Message.Blindness"));
    }


    public String MThorns() {
        return translateColor(configuration.getString("Message.Thorns"));
    }


    public String MDogde() {
        return translateColor(configuration.getString("Message.Dogde"));
    }


    public String MLifedraw() {
        return translateColor(configuration.getString("Message.Lifedraw"));
    }

    @Override
    protected void onModified() {
        plugin.getLogger().log(Level.INFO,"检测到sx.yml文件修改，自动重载中...");
        configuration = Utils.load(file);
    }
}



