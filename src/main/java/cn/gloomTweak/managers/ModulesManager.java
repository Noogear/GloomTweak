package cn.gloomTweak.managers;

import cn.gloomTweak.Configuration;
import cn.gloomTweak.Main;
import cn.gloomTweak.modules.mob.EggCapture.DecreaseEggHatching;
import cn.gloomTweak.modules.mob.EggCapture.EggCapture;
import cn.gloomTweak.modules.mob.EggCapture.EggToFeather;
import cn.gloomTweak.modules.mob.EggCapture.NameEggPersistent;
import cn.gloomTweak.modules.player.DeathPenalty;
import cn.gloomTweak.modules.player.EasyLift;
import cn.gloomTweak.modules.player.HealthScaled;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

public class ModulesManager {
    private final Main plugin;
    private final PluginManager pluginManager;

    public ModulesManager(Main main) {
        plugin = main;
        pluginManager = plugin.getServer().getPluginManager();
        load();
    }

    public void reload() {
        HandlerList.unregisterAll(plugin);
        load();
    }

    private void load() {
        if (!Configuration.enabled) return;

        if (Configuration.player.enabled) {
            registerPlayerModule();
        }

        if (Configuration.mob.enabled) {
            registerMobModule();
        }

    }

    private void registerPlayerModule() {
        if (Configuration.player.healthScaled) {
            pluginManager.registerEvents(new HealthScaled(), plugin);
        }

        if (Configuration.Player.easyLift.enabled) {
            pluginManager.registerEvents(new EasyLift(), plugin);
        }

        if (Configuration.Player.deathPenalty.enabled) {
            pluginManager.registerEvents(new DeathPenalty(), plugin);
        }

    }

    private void registerMobModule() {
        if (Configuration.Mob.eggCapture.enabled) {
            pluginManager.registerEvents(new EggCapture(), plugin);

            if (Configuration.Mob.EggCapture.balance.decreaseEggHatching) {
                pluginManager.registerEvents(new DecreaseEggHatching(), plugin);
            }

            if (Configuration.Mob.EggCapture.balance.eggToFeather >= 0) {
                pluginManager.registerEvents(new EggToFeather(), plugin);
            }

            if (Configuration.Mob.eggCapture.nameSpawnerEggFix) {
                pluginManager.registerEvents(new NameEggPersistent(), plugin);
            }
        }

    }

}
