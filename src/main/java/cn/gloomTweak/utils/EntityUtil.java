package cn.gloomTweak.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityUtil {

    public static EntityUtil instance;

    public EntityUtil() {
        instance = this;
    }

    public static String getI18nName(Entity entity) {
        return entity.customName() == null ? "<translate:" + entity.getType().translationKey() + ">" : entity.getCustomName();
    }

    public static Set<EntityType> entityToSet(List<String> entities) {
        return entities.stream()
                .map(String::toUpperCase)
                .map(s -> {
                    try {
                        return EntityType.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        XLogger.warn(s + " is not a valid entity type.");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public static Set<CreatureSpawnEvent.SpawnReason> spawnReasonToSet(List<String> spawnReason) {
        return spawnReason.stream()
                .map(String::toUpperCase)
                .map(s -> {
                    try {
                        return CreatureSpawnEvent.SpawnReason.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        XLogger.warn(s + " is not a valid spawn reason.");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

}
