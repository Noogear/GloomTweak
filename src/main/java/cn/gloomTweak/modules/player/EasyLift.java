package cn.gloomTweak.modules.player;

import cn.gloomTweak.Configuration;
import cn.gloomTweak.utils.MaterialUtil;
import cn.gloomTweak.utils.XLogger;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashSet;
import java.util.Set;

public class EasyLift implements Listener {
    private Sound liftSound;
    private boolean soundEnabled = true;
    private Set<Material> liftBlock;
    private Set<String> worldBlackList;

    public EasyLift() {

        try {
            String sound = Configuration.Player.easyLift.sound;
            if (sound.isEmpty()) {
                soundEnabled = false;
                return;
            }
            liftSound = Sound.valueOf(sound.toUpperCase());
        } catch (Exception e) {
            XLogger.err("EasyLift failed to load sound: %s" + e);
        }

        liftBlock = MaterialUtil.materialToSet(Configuration.Player.easyLift.block);
        worldBlackList = new HashSet<>(Configuration.Player.EasyLift.blackList.world);
        XLogger.info("EasyLift has been loaded.");

    }

    @EventHandler(ignoreCancelled = true)
    public void onUp(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        if (player.isFlying()) return;
        lift(player, true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDown(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        lift(player, false);
    }

    private void lift(Player player, Boolean up) {
        Location loc = player.getLocation();
        Block block = loc.getBlock();
        if (!liftBlock.contains(block.getType())) return;
        World world = block.getWorld();
        if (worldBlackList.contains(world.getName())) return;

        int height = world.getMaxHeight();
        int depth = world.getMinHeight();
        Location blockLoc = block.getLocation();
        Location found = null;
        int x = blockLoc.getBlockX();
        int z = blockLoc.getBlockZ();

        if (up) {
            for (int y = blockLoc.getBlockY() + 2; y <= height; y++) {
                Block b = world.getBlockAt(x, y, z);
                if (liftBlock.contains(b.getType())) {
                    if (!world.getBlockAt(x, y + 1, z).isEmpty()) continue;
                    found = b.getLocation();
                    break;
                }
            }
        } else {
            for (int y = blockLoc.getBlockY() - 3; y >= depth; y--) {
                Block b = world.getBlockAt(x, y, z);
                if (liftBlock.contains(b.getType())) {
                    if (!world.getBlockAt(x, y + 1, z).isEmpty()) continue;
                    found = b.getLocation();
                    break;
                }
            }
        }

        if (found == null) return;
        found.setYaw(loc.getYaw());
        found.setPitch(loc.getPitch());
        player.teleport(found.add(0.5, 1.0, 0.5), TeleportFlag.EntityState.RETAIN_VEHICLE, TeleportFlag.EntityState.RETAIN_PASSENGERS);
        if (soundEnabled) {
            player.playSound(found, liftSound, 10, 1);
        }
    }
}
