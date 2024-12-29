package cn.gloomTweak.managers;

import cn.gloomTweak.Configuration;
import cn.gloomTweak.Main;
import cn.gloomTweak.modules.mob.EggCapture;
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

        if(Configuration.mob.enabled){
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

    }

    private void registerMobModule() {
        if(Configuration.Mob.eggCapture.enabled){
            pluginManager.registerEvents(new EggCapture(plugin), plugin);
        }
    }

}
