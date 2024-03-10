package top.speedcubing.knockbackffa.games.tag.events;

import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TagGamePartyKickEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private UUID uuid;
    public TagGamePartyKickEvent(UUID uuid) {
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
