package cn.gloomTweak.utils;

import cn.gloomTweak.Main;
import org.bukkit.NamespacedKey;

public class ModuleUtil {
    public static ModuleUtil instance;
    private final Main plugin;
    private final NamespacedKey eggCapture;

    public ModuleUtil(Main main) {
        instance = this;
        plugin = main;
        this.eggCapture = new NamespacedKey(plugin, "eggCapture");;
    }

    public static NamespacedKey getEggCapture() {
        return instance.eggCapture;
    }
}
