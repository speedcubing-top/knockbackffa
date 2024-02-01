package top.speedcubing.knockbackffa.games.tag.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;
import java.util.UUID;

public class TagGameStartEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private List<UUID> partyList;
    private int gameLeft;
    public TagGameStartEvent(List<UUID> partyList, int gameLeft) {
        this.partyList = partyList;
        this.gameLeft = gameLeft;
    }

    public List<UUID> getPartyList() {
        return partyList;
    }

    public int getGameLeft() {
        return gameLeft;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
