package cn.gloomTweak.modules.player;

import cn.gloomTweak.utils.XLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HealthScaled implements Listener {
    public HealthScaled() {
        XLogger.info("HealthScaled has been loaded." );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().setHealthScaled(true);
    }
}
