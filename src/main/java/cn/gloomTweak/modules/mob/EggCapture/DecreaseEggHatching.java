package cn.gloomTweak.modules.mob.EggCapture;

import cn.gloomTweak.utils.ModuleUtil;
import com.destroystokyo.paper.event.entity.ThrownEggHatchEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.ThreadLocalRandom;

public class DecreaseEggHatching implements Listener {
    private final NamespacedKey key;

    public DecreaseEggHatching() {
        key = ModuleUtil.getEggCapture();
    }

    @EventHandler
    public void onEggThrow(ThrownEggHatchEvent event) {
        if (event.getEgg().getShooter() instanceof Player) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                if (event.getEgg().getPersistentDataContainer().has(key)) {
                    event.setHatching(false);
                }
            }
        }
    }
}
