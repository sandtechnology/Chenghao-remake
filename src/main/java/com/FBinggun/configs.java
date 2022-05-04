package com.FBinggun;

import org.bukkit.entity.Player;

/**
 * For backward compatibility of placeholder expansion
 */
@Deprecated
public class configs {
    public configs() {
    }

    @Deprecated
    public String getname(Player player) {
        return ChengHao.getInstance().getConfigManager().getPlayerData(player.getName()).getCurrentChengHao().getColoredName();
    }
}
