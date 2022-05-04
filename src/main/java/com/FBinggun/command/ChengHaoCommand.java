package com.FBinggun.command;

import com.FBinggun.ChengHao;
import com.FBinggun.config.PluginConfigStore;
import com.FBinggun.Utils;
import com.FBinggun.config.PlayerDataManager;
import com.FBinggun.config.PlayerChengHaoBean;
import com.FBinggun.config.PlayerDataBean;
import com.FBinggun.inv.ChengHaoGUI;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.FBinggun.Constants.AttitudeList;

public class ChengHaoCommand implements TabExecutor {
    private final ChengHao plugin;
    private final PlayerDataManager playerDataManager;
    private final PluginConfigStore pluginConfigStore;

    public ChengHaoCommand(ChengHao plugin) {
        this.plugin = plugin;
        this.playerDataManager = plugin.getConfigManager();
        this.pluginConfigStore = plugin.getConfigStore();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("ch")) {
            if (args.length == 0) {
                if (sender.hasPermission("chenghao.open")) {
                    sender.sendMessage("§a/ch open 打开称号仓库");
                }
                if (sender.hasPermission("chenghao.mb")) {
                    sender.sendMessage("§a/ch mb xxx[id] xxx[模板] 0[时间]");
                }
                if (sender.hasPermission("chenghao.info")) {
                    sender.sendMessage("§a/ch info xxx[id] 查看此玩家称号");
                }
                if (sender.hasPermission("chenghao.remove")) {
                    sender.sendMessage("§a/ch remove xxx[id] 0[称号序号] 先输入/ch info xxx[id] 获取序号");
                }
                if (sender.hasPermission("chenghao.give")) {
                    sender.sendMessage("§a/ch give xxx[id] xxx[称号] 0[时间] Attack:10 …………");
                    sender.sendMessage("§7Attack:10为攻击力+10 后面可加其他属性 例如：");
                    sender.sendMessage("§7/ch give xxx[id] xxx[称号] 0[时间] Attack:10 Armor:2 speed:2 …………");
                    sender.sendMessage("§7Attack 攻击 Armor 防御  speed 速度 Dogde 闪避 CritChance 暴击率  CritDamage 暴击伤害");
                }
            } else if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    return true;
                }
                Player player = (Player) sender;

                if (args[0].equalsIgnoreCase("open") &&
                        player.hasPermission("chenghao.open")) {
                    player.closeInventory();
                    player.sendMessage("§a正在为你打开称号仓库,请等待");
                    ChengHaoGUI chengHaoGUI=new ChengHaoGUI(plugin, player);
                    plugin.getServer().getScheduler().runTask(plugin, () ->
                    {player.openInventory(chengHaoGUI.getInventory());
                        chengHaoGUI.refreshInv();}
                    );
                }

            } else if (args.length == 4 &&
                    args[0].equalsIgnoreCase("mb") &&
                    sender.hasPermission("chenghao.mb")) {

                Player p = plugin.getServer().getPlayer(args[1]);
                if (p == null) {
                    if (sender instanceof Player) {
                        sender.sendMessage("§c此玩家未在线");
                    }
                    return true;
                }
                if (Utils.isInteger(args[3])) {
                    plugin.getConfigStore().applyMuBan(p, args[2], Integer.parseInt(args[3]));
                } else {
                    sender.sendMessage("§c请输入正确的指令");
                }
            } else if (args.length >= 4 &&
                    sender.hasPermission("chenghao.give") &&
                    args[0].equalsIgnoreCase("give")) {
                if (!Utils.isInteger(args[3])) {
                    sender.sendMessage("§c请正确的使用指令");
                    return true;
                }
                Player p = plugin.getServer().getPlayer(args[1]);
                if (p == null) {
                    sender.sendMessage("§c此玩家未在线");
                    return true;
                }
                PlayerDataBean dataBean = playerDataManager.getPlayerData(p.getName());
                PlayerChengHaoBean chengHaoBean = dataBean.getChengHaoBeanMap().get(args[2]);
                if (chengHaoBean != null) {
                    if (args[3].equals("0")) {
                        chengHaoBean.setExpireTime(0);
                    } else {
                        chengHaoBean.setExpireTime(TimeUnit.DAYS.toSeconds(Integer.parseInt(args[3])) + chengHaoBean.getExpireTime());
                    }
                    p.sendMessage(this.pluginConfigStore.MRenewal());
                    sender.sendMessage(this.pluginConfigStore.MRenewal());
                    return true;
                } else {
                    Map<String, Integer> attitudeMap = Collections.emptyMap();
                    if (args.length > 4) {
                        String[] attitudes = new String[args.length - 4];
                        attitudeMap = new HashMap<>();
                        System.arraycopy(args, 3, attitudes, 0, attitudes.length);
                        for (String attitude : attitudes) {
                            String[] strings = attitude.split(":", 2);
                            String attitudeName = strings[0];
                            String attitudeValue = strings[1];
                            if (Utils.isInteger(attitudeValue)) {
                                for (String builtInAttitudeName : AttitudeList) {
                                    if (builtInAttitudeName.equalsIgnoreCase(attitudeName)) {
                                        attitudeMap.put(builtInAttitudeName, Integer.parseInt(attitudeValue));
                                    }
                                }
                            }
                        }
                    }
                    if (args[3].equalsIgnoreCase("0")) {
                        dataBean.setCurrentChengHao(args[2], 0, attitudeMap);
                    } else {
                        dataBean.setCurrentChengHao(args[2], Utils.getExpiredTimeSec(Integer.parseInt(args[3])), attitudeMap);
                    }
                }
                sender.sendMessage(this.pluginConfigStore.Mgive());
                p.sendMessage(this.pluginConfigStore.Mgive());
                return true;
            } else if (args.length == 2 &&
                    args[0].equalsIgnoreCase("info") &&
                    sender.hasPermission("chenghao.info")) {

                Player p = plugin.getServer().getPlayer(args[1]);
                if (p == null) {
                    sender.sendMessage("§c此玩家未在线");
                    return true;
                }
                this.pluginConfigStore.info(p, sender);
            } else if (args.length == 3 &&
                    args[0].equalsIgnoreCase("remove")) {
                if (sender.hasPermission("chenghao.remove") &&
                        Utils.isInteger(args[2])) {

                    Player p = plugin.getServer().getPlayer(args[1]);
                    if (p == null) {
                        sender.sendMessage("§c此玩家未在线");
                        return true;
                    }
                    pluginConfigStore.removeChengHao(p, Integer.parseInt(args[2]));
                }
            }
        }


        return true;
    }

    private static final Map<Map.Entry<String,String>,String[]> tabCompleteMap=new HashMap<>();
    static {
        tabCompleteMap.put(new AbstractMap.SimpleEntry<>("open","chenghao.open"),new String[]{"open",""});
        tabCompleteMap.put(new AbstractMap.SimpleEntry<>("mb","chenghao.mb"),new String[]{"mb",null});
        tabCompleteMap.put(new AbstractMap.SimpleEntry<>("info","chenghao.info"),new String[]{"info",null});
        tabCompleteMap.put(new AbstractMap.SimpleEntry<>("remove","chenghao.remove"),new String[]{"remove",null});
        tabCompleteMap.put(new AbstractMap.SimpleEntry<>("give","chenghao.give"),new String[]{"give",null});
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> arrayList=new ArrayList<>();
        if(args.length==1){
            for (Map.Entry<Map.Entry<String, String>, String[]> entry : tabCompleteMap.entrySet()) {
                Map.Entry<String,String> stringEntry=entry.getKey();
                if(sender.hasPermission(stringEntry.getValue())){
                    arrayList.add(stringEntry.getKey());
                }
            }
        }else if(args.length==2){
            for (Map.Entry<Map.Entry<String, String>, String[]> entry : tabCompleteMap.entrySet()) {
                Map.Entry<String,String> stringEntry=entry.getKey();
                if(sender.hasPermission(stringEntry.getValue())&&args[0].equalsIgnoreCase(entry.getKey().getKey())){
                    if(entry.getValue()[1]==null){
                        return null;
                    }else {
                        arrayList.add(entry.getValue()[1]);
                    }
                }
            }
        }
        return arrayList;
    }
}
