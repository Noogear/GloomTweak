package cn.gloomTweak.utils;

import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static PlayerUtil instance;

    public PlayerUtil() {
        instance = this;
    }

    public static int getTotalExperience(int level) {
        if (level <= 15) {
            return (int) Math.round(Math.pow(level, 2) + 6 * level);
        } else if (level <= 30) {
            return (int) Math.round((2.5 * Math.pow(level, 2) - 40.5 * level + 360));
        }
        return (int) Math.round(((4.5 * Math.pow(level, 2) - 162.5 * level + 2220)));
    }

    public static int getTotalExperience(Player player) {
        return Math.round(player.getExp() * player.getExpToLevel()) + getTotalExperience(player.getLevel());
    }



}
