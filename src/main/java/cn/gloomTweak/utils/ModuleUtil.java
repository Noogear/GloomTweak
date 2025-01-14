package cn.gloomTweak.utils;

import cn.gloomTweak.Main;
import org.bukkit.NamespacedKey;

public class ModuleUtil {
    public static ModuleUtil instance;
    private final NamespacedKey eggCapture;

    public ModuleUtil(Main main) {
        instance = this;
        this.eggCapture = new NamespacedKey(main, "eggCapture");;
    }

    public static NamespacedKey getEggCapture() {
        return instance.eggCapture;
    }
}
