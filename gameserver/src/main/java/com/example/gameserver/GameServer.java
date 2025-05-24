package com.example.gameserver;

import com.esotericsoftware.kryonet.*;
import java.io.IOException;
import java.util.*;

public class GameServer {
    private Server server;
    private List<Connection> players = new ArrayList<>();

    public GameServer() throws IOException {
        server = new Server();
        Network.register(server.getKryo());
        server.start();
        server.bind(54555,54777);

        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof Network.JoinRequest) {
                    if (players.size() >= 2) {
                        connection.sendTCP(new Network.JoinResponse(false, -1));
                        return;
                    }

                    players.add(connection);
                    Network.JoinResponse response = new Network.JoinResponse(true, players.size());
                    connection.sendTCP(response);
                }

                if (object instanceof Network.PlayerAction) {
                    for (Connection c : players) {
                        if (c != connection) {
                            c.sendTCP(object);
                        }
                    }
                }
            }

            public void disconnected(Connection connection) {
                players.remove(connection);
            }
        });

        System.out.println("Server started on port " + Network.PORT);
    }

    public static void main(String[] args) throws IOException {
        new GameServer();
    }
}