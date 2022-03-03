package speedcubing.All;

import cubing.bukkit.PlayerUtils;
import cubingserver.StringList.GlobalString;
import cubingserver.connection.ServerUtils;
import cubingserver.connection.SocketUtils;
import cubingserver.connection.UDPSocketUtils;
import cubingserver.libs.LogListener;
import cubingserver.libs.PlayerData;
import cubingserver.speedcubingServer;
import cubingserver.things.Cps;
import cubingserver.things.froze;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import speedcubing.KnockBackFFA;
import speedcubing.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.util.UUID;

public class SocketServer {
    public void Load() {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[1024], 1024);
        new Thread(() -> {
            while (true) {
                try {
                    UDPSocketUtils.socket.receive(datagramPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String[] rs = new String(datagramPacket.getData(), 0, datagramPacket.getLength()).split("\\|");
                if (rs[0].equals("o") && rs[1].equals("1")) {
                    ServerUtils.AllPlayers = Integer.parseInt(rs[2]);
                    for (User u : User.users.values()) {
                        PlayerUtils.sendTabHeaderFooter(((CraftPlayer) u.player).getHandle().playerConnection, GlobalString.LobbyTabList[0][u.lang], GlobalString.LobbyTabList[1][u.lang].replace("%int%", rs[2]));
                    }
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                String receive = "";
                try {
                    receive = new BufferedReader(new InputStreamReader(SocketUtils.socket.accept().getInputStream())).readLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ServerUtils.SocketEvent(receive);
            }
        }).start();
    }
}
