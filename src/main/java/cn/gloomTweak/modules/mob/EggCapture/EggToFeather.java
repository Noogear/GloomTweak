package cn.gloomTweak.modules.mob.EggCapture;

import cn.gloomTweak.Configuration;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;

import java.util.concurrent.ThreadLocalRandom;

public class EggToFeather implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEggDrop(EntityDropItemEvent event) {
        if (!(event.getEntity() instanceof Chicken)) return;
        if (ThreadLocalRandom.current().nextInt(100) < Configuration.Mob.EggCapture.balance.eggToFeather) {
            Item drop = event.getItemDrop();
            drop.setItemStack(drop.getItemStack().withType(Material.FEATHER));
        }
    }

}
