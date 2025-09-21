package uz.alex2276564.iafurnitureexplosiondrop.listeners;

import dev.lone.itemsadder.api.CustomFurniture;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import uz.alex2276564.iafurnitureexplosiondrop.events.FurnitureExplosionEvent;

import java.util.ArrayList;
import java.util.List;

public class IAFurnitureExplosionListener implements Listener {

    @EventHandler
    // keep ignoreCancelled=false to bypass protection plugins (WorldGuard) intentionally
    public void on(EntityExplodeEvent event) {
        final Entity entity = event.getEntity();

        // radius <= 0 means "unsupported explosion source"
        final double r = explosionRadiusOf(entity);

        /*
          Not return 0.0 if the entity is a known, safe-to-handle explosion source.
          Prevents issues with certain entities (e.g., EnderDragon) that can crash the server.
         */
        if (r <= 0.0) return;

        // One nearby query; no yield-based pseudo-radius
        final List<Entity> nearby = entity.getNearbyEntities(r, r, r);
        final List<Entity> affectedFurniture = new ArrayList<>();

        for (Entity target : nearby) {
            if (target.getType() == EntityType.ARMOR_STAND) {
                if (CustomFurniture.byAlreadySpawned(target) != null) {
                    affectedFurniture.add(target);
                }
            }
        }

        // No need to fire event if nothing to do
        if (affectedFurniture.isEmpty()) return;

        FurnitureExplosionEvent furnitureEvent = new FurnitureExplosionEvent(event, affectedFurniture);
        Bukkit.getPluginManager().callEvent(furnitureEvent);

        if (furnitureEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void on(FurnitureExplosionEvent event) {
        for (Entity furniture : event.getAffectedFurniture()) {
            CustomFurniture.remove(furniture, true);
        }
    }

    // Unified "support + radius" mapper:
    // returns >0 for supported explosion sources; <=0 => not supported
    private double explosionRadiusOf(Entity e) {
        final EntityType t = e.getType();
        return switch (t) {
            case PRIMED_TNT -> 5.5;
            case CREEPER -> {
                // charged creeper → slightly larger radius
                if (e instanceof Creeper c && c.isPowered()) yield 6.0;
                yield 4.0;
            }
            case ENDER_CRYSTAL -> 6.5;
            case WITHER -> 7.0;
            case MINECART_TNT -> 5.0;
            case FIREBALL, SMALL_FIREBALL, WITHER_SKULL -> 3.5;
            // DRAGON_FIREBALL intentionally excluded to avoid Ender Dragon interactions
            default -> 0.0;
        };
    }
}