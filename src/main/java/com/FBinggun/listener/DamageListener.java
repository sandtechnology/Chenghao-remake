package com.FBinggun.listener;

import com.FBinggun.ChengHao;
import com.FBinggun.config.AttitudeDamageMessageConfig;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Random;

public class DamageListener implements Listener {
    private final static Random random = new Random();
    private final ChengHao plugin;
    private final AttitudeDamageMessageConfig messageConfig;

    public DamageListener(ChengHao plugin,AttitudeDamageMessageConfig messageConfig) {
        this.plugin = plugin;
        this.messageConfig = messageConfig;
    }

    public static boolean isTriggered(int triggerChance) {
        return (1 + random.nextInt(101)) > (100 - triggerChance);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) event.getEntity();
            Player damager = (Player) event.getDamager();
            Map<String, Integer> attitudeMap = plugin.getConfigManager().getPlayerData(damager.getName()).getCurrentChengHao().getAttitudeUnmodifiableMap();
            for (Map.Entry<String, Integer> entry : attitudeMap.entrySet()) {
                switch (entry.getKey()) {
                    case "Fatal":
                        target.setHealth(1.0D);
                        break;
                    case "Realdamage":
                        if (entry.getValue() != 0) {
                            target.setHealth(Math.max(((Damageable) event.getEntity()).getHealth() - attitudeMap.getOrDefault("Realdamage", 0), 0.0D));
                        }
                        break;
                    case "CritChance":
                        if (isTriggered(entry.getValue())) {
                            double baseDamage = event.getDamage() + attitudeMap.getOrDefault("Attack", 0);
                            double criticalDamage = baseDamage * attitudeMap.getOrDefault("CritDamage", 1) * 0.01D;
                            double extraDamage = baseDamage * attitudeMap.getOrDefault("Attackadd", 1);
                            event.setDamage(baseDamage + criticalDamage + ((baseDamage + criticalDamage) * extraDamage));
                        } else {
                            event.setDamage(event.getDamage() + attitudeMap.getOrDefault("Attack", 0) + attitudeMap.getOrDefault("Attackadd", 1));
                        }
                        break;
                    case "Armorchance":
                        if (isTriggered(entry.getValue())) {
                            double health = target.getHealth();
                            double armorDamage = attitudeMap.getOrDefault("Armordamage", 1);
                            target.setHealth(Math.max(0.0D, health - armorDamage));
                        }
                        break;
                    case "Life":

                        if (isTriggered(entry.getValue())) {
                            int lifeDraw = attitudeMap.getOrDefault("Lifedraw", 1);
                            if (damager.getHealth() + lifeDraw < damager.getMaxHealth()) {
                                damager.setHealth(event.getFinalDamage() * lifeDraw * 0.01D);
                            } else {
                                damager.setHealth(damager.getMaxHealth());
                            }
                            damager.sendMessage(String.valueOf(messageConfig.MLifedraw()) + ((int) event.getDamage() * lifeDraw * 0.01D));
                        }
                        break;
                    case "Wither":
                        if (isTriggered(entry.getValue())) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 90, 1), true);
                        }
                        break;
                    case "Poison":
                        if (isTriggered(entry.getValue())) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 90, 1), true);
                        }
                        break;
                    case "Frozen":
                        if (isTriggered(entry.getValue())) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 90, 99), true);
                        }
                        break;
                    case "Weakening":
                        if (isTriggered(entry.getValue())) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 150, 1), true);
                        }
                        break;
                    case "Nausea":
                        if (isTriggered(entry.getValue())) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 1), true);
                        }
                        break;
                    case "Blindness":
                        if (isTriggered(entry.getValue())) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 150, 1), true);
                        }
                        break;
                    case "Dogde":
                        if (isTriggered(entry.getValue())) {
                            event.setDamage(0.0D);
                        } else {
                            int armoredDamage = attitudeMap.getOrDefault("Armor", 0);
                            event.setDamage(Math.max(event.getDamage() - (armoredDamage + (armoredDamage * attitudeMap.getOrDefault("Armoradd", 1))), 0.0D));
                        }
                        break;
                    case "ThornsChance":
                        if (event.getEntity() instanceof Player) {
                            if (isTriggered(entry.getValue())) {
                                int thornsValue = attitudeMap.getOrDefault("ThornsValue", 0);
                                target.setHealth(Math.max(0.0D, target.getHealth() - event.getDamage() * thornsValue * 0.01D));
                                damager.sendMessage(String.valueOf(messageConfig.MThorns()) + (target.getHealth() - event.getDamage() * thornsValue * 0.01D));
                            }
                        }
                        break;
                    default:
                }
            }
        }


    }
}


