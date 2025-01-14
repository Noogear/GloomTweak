package cn.gloomTweak;

import cn.gloomTweak.managers.FileManager;
import cn.gloomTweak.managers.ModulesManager;
import cn.gloomTweak.utils.Message;
import cn.gloomTweak.utils.ModuleUtil;
import cn.gloomTweak.utils.SchedulerUtil.CommonScheduler;
import cn.gloomTweak.utils.SchedulerUtil.FoliaScheduler;
import cn.gloomTweak.utils.SchedulerUtil.SchedulerInterface;
import cn.gloomTweak.utils.XLogger;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public FileManager fileManager;
    public ModulesManager modulesManager;
    private SchedulerInterface schedulerInterface;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        new XLogger(this);
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            schedulerInterface = new FoliaScheduler(this);
        } catch (ClassNotFoundException e) {
            schedulerInterface = new CommonScheduler(this);
        }

        fileManager = new FileManager(this);
        new ModuleUtil(this);
        modulesManager = new ModulesManager(this);
        new Message(this);
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
        schedulerInterface.cancelAll();
    }

    public SchedulerInterface getSchedule(){
        return schedulerInterface;
    }

    public void reload(){
        fileManager.load();
        modulesManager.reload();
    }
}
