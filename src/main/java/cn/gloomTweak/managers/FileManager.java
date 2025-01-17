package cn.gloomTweak.managers;

import cn.gloomTweak.Configuration;
import cn.gloomTweak.Main;
import cn.gloomTweak.utils.configuration.ConfigurationManager;

import java.io.File;

public class FileManager {
    private final File configFile;
    private final Main plugin;

    public FileManager(Main main) {
        this.plugin = main;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        load();
    }

    public void load() {
        try {
            ConfigurationManager.load(Configuration.class, configFile, "version");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
