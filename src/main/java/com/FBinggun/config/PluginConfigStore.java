package com.FBinggun.config;

import com.FBinggun.ChengHao;
import com.FBinggun.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


public class PluginConfigStore extends FileWatcher{

    private final ChengHao plugin;
    private FileConfiguration configuration;
    private final MuBanDataBean muBanDataBean;

    public PluginConfigStore(ChengHao plugin) {
        super(plugin,new File(plugin.getDataFolder(),"config.yml"));
        this.plugin = plugin;
        this.configuration = plugin.getConfig();
        this.muBanDataBean = new MuBanDataBean(plugin);
    }


    public String getDefaultChengHao() {
        return Utils.translateColorCode(configuration.getString("default"));
    }


    public String Mgive() {
        return Utils.translateColorCode(configuration.getString("Message.give"));
    }

    public String MRenewal() {
        return Utils.translateColorCode(configuration.getString("Message.xf"));
    }

    public String Mremove() {
        return Utils.translateColorCode(configuration.getString("Message.remove"));
    }

    public void info(Player target, CommandSender commandSender) {
        PlayerDataBean dataBean = plugin.getConfigManager().getPlayerData(target.getName());
        commandSender.sendMessage("§a" + target.getName() + "玩家拥有的称号:");
        int index = 0;
        for (PlayerChengHaoBean value : dataBean.getChengHaoBeanMap().values()) {
            commandSender.sendMessage(index + "." + value.getColoredName());
            index++;
        }
    }


    public void removeChengHao(Player target, int removeIndex) {
        PlayerDataBean dataBean = plugin.getConfigManager().getPlayerData(target.getName());
        int index = 0;
        for (PlayerChengHaoBean chengHaoBean : dataBean.getChengHaoBeanMap().values()) {
            if (removeIndex == index) {
                dataBean.removeChengHao(chengHaoBean.getKey());
                target.sendMessage(Mremove());
                return;
            }
            index++;
        }
    }

    public void applyMuBan(Player target, String key, int timeDay) {
        MuBanBean muBanBean = muBanDataBean.getMuBanBean(key);
        if (muBanBean == null) {
            return;
        }
        PlayerDataBean playerDataBean = plugin.getConfigManager().getPlayerData(target.getName());

        PlayerChengHaoBean chengHaoBean = playerDataBean.getChengHaoBeanMap().get(muBanBean.getKey());

        if (chengHaoBean != null) {
            if (timeDay == 0) {
                chengHaoBean.setExpireTime(0);
            } else {
                chengHaoBean.setExpireTime(TimeUnit.DAYS.toSeconds(timeDay) + chengHaoBean.getExpireTime());
            }
            target.sendMessage(MRenewal());
        } else {
            if (timeDay == 0) {
                playerDataBean.setCurrentChengHao(muBanBean, timeDay);
            } else {
                playerDataBean.setCurrentChengHao(muBanBean, Utils.getExpiredTimeSec(timeDay));
            }
            target.sendMessage(Mgive());
        }
        playerDataBean.save();
    }


    public String permanentMessage() {
        return Utils.translateColorCode(configuration.getString("Message.permanent"));
    }

    public boolean isApplyChengHaoToTab() {
        return configuration.getString("TAB").equalsIgnoreCase("true");
    }

    public String expiredTimeMessage() {
        return Utils.translateColorCode(configuration.getString("Message.expirationtime"));
    }

    public String Muse() {
        return Utils.translateColorCode(configuration.getString("Message.use"));
    }

    public String MUnUse() {
        return Utils.translateColorCode(configuration.getString("Message.unuse"));
    }

    public void applyTabChengHao(String playerName) {
        PlayerDataBean dataBean = plugin.getConfigManager().getPlayerData(playerName);
        String currentChengHao = dataBean.getCurrentChengHao().getColoredName();
        if (currentChengHao.length() > 16) {
            currentChengHao = currentChengHao.substring(0, 16);
        }
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team t = board.getTeam(playerName);
        if (t == null) {
            t = board.registerNewTeam(playerName);
        } else {
            t = board.getTeam(playerName);
        }
        t.setPrefix(currentChengHao);
        t.addEntry(playerName);
        for (Player o : Bukkit.getOnlinePlayers())
            o.setScoreboard(board);
    }
    @Override
    public void shutdown(){
        super.shutdown();
        muBanDataBean.shutdown();
        plugin.saveConfig();
    }

    @Override
    protected void onModified() {
        plugin.getLogger().log(Level.INFO,"检测到配置文件config.yml重载，自动重载中...");
        plugin.reloadConfig();
        configuration=plugin.getConfig();
    }
}



