package uz.alex2276564.iafurnitureexplosiondrop;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import uz.alex2276564.iafurnitureexplosiondrop.listeners.IAFurnitureExplosionListener;
import uz.alex2276564.iafurnitureexplosiondrop.utils.runner.BukkitRunner;
import uz.alex2276564.iafurnitureexplosiondrop.utils.runner.Runner;
import uz.alex2276564.iafurnitureexplosiondrop.utils.UpdateChecker;

public final class IAFurnitureExplosionDrop extends JavaPlugin {

    @Getter
    private Runner runner;

    @Override
    public void onEnable() {
        try {
            setupRunner();
            registerListeners();
            checkUpdates();

            getLogger().info("IAFurnitureExplosionDrop has been enabled successfully!");
        } catch (Exception e) {
            getLogger().severe("Failed to enable IAFurnitureExplosionDrop: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void setupRunner() {
        runner = new BukkitRunner(this);
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new IAFurnitureExplosionListener(), this);
    }

    private void checkUpdates() {
        UpdateChecker updateChecker = new UpdateChecker(this, "alex2276564/IAFurnitureExplosionDrop", runner);
        updateChecker.checkForUpdates();
    }

    @Override
    public void onDisable() {
        runner.cancelTasks();
    }
}
