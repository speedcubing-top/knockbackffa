package top.speedcubing.knockbackffa.games.tag.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;
import java.util.UUID;

public class TagGamePartyDisbandEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private List<UUID> partyList;
    public TagGamePartyDisbandEvent(List<UUID> partyList) {
        this.partyList = partyList;
    }

    public List<UUID> getPartyList() {
        return partyList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
