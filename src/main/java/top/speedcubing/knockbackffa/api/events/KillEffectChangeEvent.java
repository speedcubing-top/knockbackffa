package top.speedcubing.knockbackffa.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KillEffectChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String effectId;
    public KillEffectChangeEvent(Player player, String effectId) {
        this.player = player;
        this.effectId = effectId;
    }

    public Player getPlayer() {
        return player;
    }

    public String getEffectId() {
        return effectId;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
