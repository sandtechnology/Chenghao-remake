package com.FBinggun.listener;

import com.FBinggun.ChengHao;
import com.FBinggun.config.PluginConfigStore;
import com.FBinggun.config.PlayerDataManager;
import com.FBinggun.config.PlayerChengHaoBean;
import com.FBinggun.config.PlayerDataBean;
import com.FBinggun.inv.ChengHaoGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class PlayerListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final PluginConfigStore pluginConfigStore;

    public PlayerListener(ChengHao plugin) {
        this.playerDataManager = plugin.getConfigManager();
        this.pluginConfigStore = plugin.getConfigStore();
    }

    @EventHandler
    public void onClickItemInGUI(InventoryMoveItemEvent event) {
        if (event.getDestination().getHolder() instanceof ChengHaoGUI || event.getSource().getHolder() instanceof ChengHaoGUI) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClickItemInGUI(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof ChengHaoGUI) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClickItemInGUI(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Inventory currentInv = event.getInventory();
        if (currentInv != null) {
            InventoryHolder holder = currentInv.getHolder();
            if (holder instanceof ChengHaoGUI) {
                event.setCancelled(true);
                int slotIndexClicked = event.getRawSlot();
                int index = 0;
                PlayerDataBean dataBean = playerDataManager.getPlayerData(player.getName());
                for (PlayerChengHaoBean chenghao : dataBean.getChengHaoBeanMap().values()) {
                    if (slotIndexClicked == index) {
                        dataBean.setCurrentChengHao(chenghao.getKey());
                        if (pluginConfigStore.isApplyChengHaoToTab()) {
                            pluginConfigStore.applyTabChengHao(player.getName());
                        }
                        break;
                    }
                    index++;
                }
                ((ChengHaoGUI) holder).refreshInv();
            }
        }
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PlayerDataBean dataBean = playerDataManager.getPlayerData(p.getName());
        e.setFormat(dataBean.getCurrentChengHao().getColoredName() + e.getFormat());
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        PlayerDataBean playerData = playerDataManager.getPlayerData(playerName);
        for (PlayerChengHaoBean chengHao : playerData.getChengHaoBeanMap().values()) {
            if (chengHao.isExpired()) {
                playerData.removeChengHao(chengHao.getKey());
                event.getPlayer().sendMessage("&a你的称号:" + chengHao.getColoredName() + "已过期");
            }
        }
        if (pluginConfigStore.isApplyChengHaoToTab()) {
            pluginConfigStore.applyTabChengHao(playerName);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        String playerName = e.getPlayer().getName();
        playerDataManager.purgePlayerDataFromMemory(playerName);
    }
}



