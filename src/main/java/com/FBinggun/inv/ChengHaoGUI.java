package com.FBinggun.inv;

import com.FBinggun.ChengHao;
import com.FBinggun.config.PluginConfigStore;
import com.FBinggun.Utils;
import com.FBinggun.config.PlayerChengHaoBean;
import com.FBinggun.config.PlayerDataBean;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChengHaoGUI implements InventoryHolder {
    private final Inventory invVew;
    private final String playerName;
    private final ChengHao plugin;
    private final PluginConfigStore pluginConfigStore;

    public ChengHaoGUI(ChengHao plugin, Player player) {
        this.plugin = plugin;
        invVew = Bukkit.createInventory(this, 54, "§2 称号仓库!");
        playerName = player.getName();
        pluginConfigStore = plugin.getConfigStore();
    }

    public void refreshInv() {
        invVew.clear();
        PlayerDataBean playerData = plugin.getConfigManager().getPlayerData(playerName);
        int index = 0;
        for (PlayerChengHaoBean playerChengHaoBean : playerData.getChengHaoBeanMap().values()) {
            invVew.setItem(index, getInvItem(playerChengHaoBean));
            index++;
        }
    }

    public ItemStack getInvItem(PlayerChengHaoBean playerChengHaoBean) {
        List<String> lores = new ArrayList<>();
        long timeSec = playerChengHaoBean.getExpireTime();
        if (timeSec == 0L) {
            lores.add(pluginConfigStore.permanentMessage());
        } else {
            lores.add(pluginConfigStore.expiredTimeMessage() + Utils.getDayRemain(timeSec) + "天");
        }
        ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("SXname");
        if (configurationSection == null) {
            configurationSection = plugin.getConfig().createSection("SXname");
            plugin.saveConfig();
        }
        for (Map.Entry<String, Integer> entry : playerChengHaoBean.getAttitudeUnmodifiableMap().entrySet()) {
            lores.add(configurationSection.getString(entry.getKey(), "未知SXname：" + entry.getKey()) + "：" + entry.getValue());
        }
        if (playerChengHaoBean.isUsing()) {
            lores.add(pluginConfigStore.Muse());
        } else {
            lores.add(pluginConfigStore.MUnUse());
        }
        ItemStack ChengHaoItemStack = new ItemStack(Material.BOOK, 1);
        ItemMeta im = ChengHaoItemStack.getItemMeta();
        im.setDisplayName(playerChengHaoBean.getColoredName());
        im.setLore(lores);
        ChengHaoItemStack.setItemMeta(im);
        return ChengHaoItemStack;
    }

    @Override
    public Inventory getInventory() {
        return invVew;
    }
}
