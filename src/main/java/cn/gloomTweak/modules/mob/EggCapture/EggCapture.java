package cn.gloomTweak.modules.mob.EggCapture;

import cn.gloomTweak.Configuration;
import cn.gloomTweak.utils.EntityUtil;
import cn.gloomTweak.utils.ExpressionUtil;
import cn.gloomTweak.utils.ModuleUtil;
import cn.gloomTweak.utils.XLogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import redempt.crunch.CompiledExpression;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class EggCapture implements Listener {

    private final NamespacedKey key;
    private final Set<EntityType> entityBlackList;
    private final Set<String> worldBlackList;
    private final Set<CreatureSpawnEvent.SpawnReason> spawnReasonBlackList;
    private final CompiledExpression eggCaptureExpression;
    private double maxChance;
    private boolean actionbarEnabled;
    private String actionbar;
    private boolean particleEnabled;
    private Particle particleType;
    private int particleCount;


    public EggCapture() {

        key = ModuleUtil.getEggCapture();
        worldBlackList = new HashSet<>(Configuration.Mob.EggCapture.blackList.world);
        entityBlackList = EntityUtil.entityToSet(Configuration.Mob.EggCapture.blackList.entity);
        spawnReasonBlackList = EntityUtil.spawnReasonToSet(Configuration.Mob.EggCapture.blackList.spawnReason);

        try {
            maxChance = Configuration.Mob.EggCapture.chance.max;
            actionbar = Configuration.Mob.eggCapture.actionbar.replace("{mob}", "<mob>");
            actionbarEnabled = !actionbar.isEmpty();
            particleType = Particle.valueOf(Configuration.Mob.EggCapture.particle.type);
            particleCount = Configuration.Mob.EggCapture.particle.count;
            particleEnabled = !particleType.name().isEmpty() && particleCount > 0;

        } catch (Exception e) {
            XLogger.err("Failed to load EggCapture: %s", e);
        }
        eggCaptureExpression = ExpressionUtil.build(Configuration.Mob.EggCapture.chance.formula, "max_health", "health");

        XLogger.info("EggCapture has been loaded.");

    }


    private Material getSpawnEggFromEntity(Entity entity) {
        return Material.valueOf(entity.getType().getKey().getKey().toUpperCase(Locale.ROOT) + "_SPAWN_EGG");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Egg egg)) return;
        if (!(event.getHitEntity() instanceof LivingEntity entity)) return;
        egg.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        if (entityBlackList.contains(entity.getType())) return;
        if (entity instanceof Player) return;
        if (!(egg.getShooter() instanceof Player player)) return;
        if (worldBlackList.contains(player.getWorld().getName())) return;
        if (entity.getPersistentDataContainer().has(key)) return;

        AttributeInstance maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth == null) return;
        double chance = eggCaptureExpression.evaluate(maxHealth.getValue(), entity.getHealth());
        chance = Math.min(chance, maxChance);
        if (chance < ThreadLocalRandom.current().nextInt(100)) return;

        if (particleEnabled) {
            entity.getWorld().spawnParticle(particleType, entity.getLocation(), particleCount);
        }

        if (actionbarEnabled) {
            Component finalActionbar = MiniMessage.miniMessage().deserialize(actionbar, Placeholder.parsed("mob", EntityUtil.getI18nName(entity)));
            player.sendActionBar(finalActionbar);
        }

        ItemStack spawnEgg = new ItemStack(getSpawnEggFromEntity(entity));
        entity.remove();
        if (!player.getInventory().addItem(spawnEgg).isEmpty()) {
            player.getWorld().dropItem(player.getLocation(), spawnEgg);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent event) {
        if (spawnReasonBlackList.contains(event.getSpawnReason())) {
            event.getEntity().getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, true);
        }
    }
}
