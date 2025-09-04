package uz.alex2276564.iafurnitureexplosiondrop;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import uz.alex2276564.iafurnitureexplosiondrop.listeners.IAFurnitureExplosionListener;
import uz.alex2276564.iafurnitureexplosiondrop.utils.runner.FoliaRunner;
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
        runner = new FoliaRunner(this);
        getLogger().info("Initialized " + runner.getPlatformName() + " scheduler support");

        if (runner.isFolia()) {
            getLogger().info("Folia detected - using RegionScheduler and EntityScheduler for optimal performance");
        }
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
        if (runner != null) {
            runner.cancelAllTasks();
        }
    }
}
