package top.speedcubing.knockbackffa.games.tag.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class TagGamePartyJoinEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private UUID uuid;
    public TagGamePartyJoinEvent(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
