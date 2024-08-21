package top.speedcubing.knockbackffa.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MapSwitchEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final String oldMapName;
    private final String newMapName;
    public MapSwitchEvent(String oldMapName, String newMapName) {
        this.oldMapName = oldMapName;
        this.newMapName = newMapName;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getNewMapName() {
        return newMapName;
    }

    public String getOldMapName() {
        return oldMapName;
    }
}
