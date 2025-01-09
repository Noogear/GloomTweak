package cn.gloomTweak.modules.mob.EggCapture;

import cn.gloomTweak.Configuration;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class EggToFeather implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEggDrop(EntityDropItemEvent event) {
        if (!(event.getEntity() instanceof Chicken)) return;
        Item drop = event.getItemDrop();
        if (drop.getItemStack().getType() == Material.EGG) {
            if (ThreadLocalRandom.current().nextInt(100) < Configuration.Mob.EggCapture.balance.eggToFeather) {
                drop.setItemStack(new ItemStack(Material.FEATHER, 1));
            }
        }
    }

}
