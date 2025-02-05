package top.speedcubing.knockbackffa.original.list;

import top.speedcubing.knockbackffa.original.configuration.Configuration;
import top.speedcubing.knockbackffa.original.KnockBackFFA;
import top.speedcubing.knockbackffa.original.Tunnel;
import top.speedcubing.lib.bukkit.entity.Hologram;

public class EntityList {
    public static Hologram clickToggle;
    public static Hologram toBlueTunnel;
    public static Hologram toGreenTunnel;
    public static final Hologram[] personal = new Hologram[2];

    public static void init() {
        personal[0] = new Hologram("", false, false).
                world("kbffa").
                setLocation(Configuration.holoX, Configuration.centerY + 2.4, Configuration.holoZ, 0, 0).
                setClickEvent(KnockBackFFA.clickEvent);

        personal[1] = new Hologram("", false, false).
                world("kbffa").
                setLocation(Configuration.holoX, Configuration.centerY - 1.8, Configuration.holoZ, 0, 0).
                setClickEvent(KnockBackFFA.clickEvent);

        clickToggle = new Hologram("§6§lClick to toggle!", true, true).
                setLocation(Configuration.holoX, 148.5, Configuration.holoZ, 0, 0).
                world("world").
                setClickEvent(KnockBackFFA.clickEvent).spawn();

        //blue
        toGreenTunnel = new Hologram("§6§lTo Green Pipeline", true, true).
                world("world").
                setLocation(30027.5, 98, 20034.5, 0, 0).
                setClickEvent((r) -> Tunnel.tunnel(r.getPlayer())).spawn();

        //green
        toBlueTunnel = new Hologram("§6§lTo Blue Pipeline", true, true).
                world("world").
                setLocation(29973.5, 98, 20010.5, 0, 0).
                setClickEvent((r) -> Tunnel.tunnel(r.getPlayer())).spawn();
    }
}
