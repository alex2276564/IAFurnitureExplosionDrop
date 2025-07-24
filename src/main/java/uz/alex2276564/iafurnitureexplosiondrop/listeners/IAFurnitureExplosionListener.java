package uz.alex2276564.iafurnitureexplosiondrop.listeners;

import dev.lone.itemsadder.api.CustomFurniture;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import uz.alex2276564.iafurnitureexplosiondrop.events.FurnitureExplosionEvent;

import java.util.ArrayList;
import java.util.List;

public class IAFurnitureExplosionListener implements Listener {

    @EventHandler
    public void on(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        /*
          Returns true if the entity is a known, safe-to-handle explosion source.
          Prevents issues with certain entities (e.g., EnderDragon) that can crash the server.
         */
        if (isSupportedExplosiveEntity(entity)) {
            final float explosionRadius = event.getYield() * 3f;
            List<Entity> nearbyEntities = new ArrayList<>(entity.getNearbyEntities(explosionRadius, explosionRadius, explosionRadius));
            List<Entity> affectedFurniture = new ArrayList<>();

            for (Entity targetEntity : nearbyEntities) {
                if (targetEntity instanceof ArmorStand) {
                    if (CustomFurniture.byAlreadySpawned(targetEntity) != null) {
                        affectedFurniture.add(targetEntity);
                    }
                }
            }

            FurnitureExplosionEvent furnitureEvent = new FurnitureExplosionEvent(event, affectedFurniture);
            Bukkit.getPluginManager().callEvent(furnitureEvent);

            if (furnitureEvent.isCancelled()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void on(FurnitureExplosionEvent event) {
        for (Entity furniture : event.getAffectedFurniture()) {
            CustomFurniture.remove(furniture, true);
        }
    }

    private boolean isSupportedExplosiveEntity(Entity entity) {
        return entity instanceof TNTPrimed ||
                entity instanceof Creeper ||
                entity instanceof Wither ||
                entity instanceof EnderCrystal ||
                entity instanceof Fireball ||
                entity instanceof ExplosiveMinecart;
    }
}
