package cn.gloomTweak.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MaterialUtil {

    public static MaterialUtil instance;

    public MaterialUtil(){
        instance = this;
    }

    public static String getI18nName(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return  meta.hasDisplayName() ? meta.getDisplayName() : getI18nName(item.getType());
    }

    public static String getI18nName(Material material) {
        return  "<translate:" + material.translationKey() + ">" ;
    }

    public static Set<Material> materialToSet(List<String> materials){
        return materials.stream()
                .map(String::toUpperCase)
                .map(s->{
                    try {
                        return Material.valueOf(s);
                    } catch (IllegalArgumentException e) {
                        XLogger.warn(s + " is not a valid material type.");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public static Set<PotionEffectType> potionToSet(List<String> potions){
        return potions.stream()
                .map(String::toLowerCase)
                .map(s->{
                    try {
                        return Registry.POTION_EFFECT_TYPE.get(NamespacedKey.fromString(s));
                    } catch (IllegalArgumentException e) {
                        XLogger.warn(s + " is not a valid potion type.");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }



}
