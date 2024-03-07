package top.speedcubing.knockbackffa.games.tag.events;

import java.util.List;
import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TagGameEndEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private List<UUID> partyList;
    public TagGameEndEvent(List<UUID> partyList) {
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
