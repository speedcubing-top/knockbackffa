package top.speedcubing.knockbackffa.api.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FFAPlayerDeathEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Location deathLocation;

    public FFAPlayerDeathEvent(Player player, Location deathlocation) {
        this.player = player;
        this.deathLocation = deathlocation;
    }
    public Player getPlayer() {
        return player;
    }
    public Location getDeathLocation() {
        return deathLocation;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
