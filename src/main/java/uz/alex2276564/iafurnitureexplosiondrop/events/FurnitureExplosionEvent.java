package uz.alex2276564.iafurnitureexplosiondrop.events;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FurnitureExplosionEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancel = false;
    @Getter
    private final EntityExplodeEvent entityExplodeEvent;
    @Getter
    private final List<Entity> affectedFurniture;

    public FurnitureExplosionEvent(EntityExplodeEvent entityExplodeEvent, List<Entity> affectedFurniture) {
        super(entityExplodeEvent.getEntity());
        this.entityExplodeEvent = entityExplodeEvent;
        this.affectedFurniture = affectedFurniture;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}