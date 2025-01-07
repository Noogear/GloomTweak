package cn.gloomTweak.modules.player;

import cn.gloomTweak.Configuration;
import cn.gloomTweak.utils.MaterialUtil;
import cn.gloomTweak.utils.XLogger;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import io.papermc.paper.entity.TeleportFlag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
    private final Set<Material> liftBlock;
    private final Set<String> worldBlackList;
    private Sound upSound;
    private boolean upSoundEnabled;
    private Component upActionbar;
    private boolean upActionbarEnabled;
    private Sound downSound;
    private boolean downSoundEnabled;
    private Component downActionbar;
    private boolean downActionbarEnabled;

    public EasyLift() {


        try {
            upSoundEnabled = !Configuration.Player.easyLift.upSound.isEmpty();
            upSound = Sound.valueOf(Configuration.Player.easyLift.upSound.toUpperCase());
            downSoundEnabled = !Configuration.Player.easyLift.downSound.isEmpty();
            downSound = Sound.valueOf(Configuration.Player.easyLift.downSound.toUpperCase());
        } catch (Exception e) {
            XLogger.err("EasyLift failed to load sound: %s" + e);
        }

        try {
            upActionbarEnabled = !Configuration.Player.easyLift.upActionbar.isEmpty();
            upActionbar = MiniMessage.miniMessage().deserialize(Configuration.Player.easyLift.upActionbar);
            downActionbarEnabled = !Configuration.Player.easyLift.downActionbar.isEmpty();
            downActionbar = MiniMessage.miniMessage().deserialize(Configuration.Player.easyLift.downActionbar);
        } catch (Exception e) {
            XLogger.err("EasyLift failed to load actionbar: %s" + e);
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

        if (up) {
            if (upSoundEnabled) {
                player.playSound(found, upSound, 10, 1);
            }
            if (upActionbarEnabled) {
                player.sendActionBar(upActionbar);
            }
        } else {
            if (downSoundEnabled) {
                player.playSound(found, downSound, 10, 1);
            }
            if (downActionbarEnabled) {
                player.sendActionBar(downActionbar);
            }
        }

    }
}
