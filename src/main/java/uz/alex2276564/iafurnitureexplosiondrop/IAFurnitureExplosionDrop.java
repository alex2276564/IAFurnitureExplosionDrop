package uz.alex2276564.iafurnitureexplosiondrop;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import uz.alex2276564.iafurnitureexplosiondrop.listeners.IAFurnitureExplosionListener;
import uz.alex2276564.iafurnitureexplosiondrop.utils.HttpUtils;
import uz.alex2276564.iafurnitureexplosiondrop.utils.UpdateChecker;
import uz.alex2276564.iafurnitureexplosiondrop.utils.runner.FoliaRunner;
import uz.alex2276564.iafurnitureexplosiondrop.utils.runner.Runner;

import java.util.logging.Level;

public final class IAFurnitureExplosionDrop extends JavaPlugin {

    @Getter
    private Runner runner;

    @Getter
    private HttpUtils httpUtils;

    @Getter
    private UpdateChecker updateChecker;

    @Override
    public void onEnable() {
        try {
            setupRunner();
            setupHttpClient();
            setupUpdateChecker();
            registerListeners();
            checkUpdates();

            getLogger().info("IAFurnitureExplosionDrop has been enabled successfully!");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to enable IAFurnitureExplosionDrop", e);
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

    private void setupHttpClient() {
        this.httpUtils = new HttpUtils();
    }

    private void setupUpdateChecker() {
        this.updateChecker = new UpdateChecker(
                getDescription().getName(),
                getDescription().getVersion(),
                "alex2276564/IAFurnitureExplosionDrop",
                runner,
                httpUtils,
                getLogger()
        );
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new IAFurnitureExplosionListener(), this);
    }

    private void checkUpdates() {
        if (updateChecker != null) {
            updateChecker.checkForUpdates();
        }
    }

    @Override
    public void onDisable() {
        if (runner != null) {
            runner.cancelAllTasks();
        }
    }
}
