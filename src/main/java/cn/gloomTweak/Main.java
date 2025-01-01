package cn.gloomTweak;

import cn.gloomTweak.managers.FileManager;
import cn.gloomTweak.managers.ModulesManager;
import cn.gloomTweak.utils.ModuleUtil;
import cn.gloomTweak.utils.Scheduler;
import cn.gloomTweak.utils.XLogger;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public FileManager fileManager;
    public ModulesManager modulesManager;
    private boolean folia;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        new XLogger(this);
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException e) {
            folia = false;
        }

        fileManager = new FileManager(this);
        new Scheduler(this);
        new ModuleUtil(this);
        modulesManager = new ModulesManager(this);
        PluginCommand mainCommand = getCommand("gloomtweak");
        if (mainCommand != null) {
            mainCommand.setExecutor(new Command(this));
        } else {
            XLogger.err("Failed to load command.");
        }
        
        long elapsedTime = System.currentTimeMillis() - startTime;
        XLogger.info("Plugin loaded successfully in " + elapsedTime + " ms");
    }

    @Override
    public void onDisable() {
        Scheduler.cancelAll();
    }

    public boolean isFolia() {
        return folia;
    }

    public void reload(){
        fileManager.load();
        modulesManager.reload();
    }
}
