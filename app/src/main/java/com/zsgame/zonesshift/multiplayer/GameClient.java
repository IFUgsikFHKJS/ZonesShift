package com.zsgame.zonesshift.multiplayer;

import com.esotericsoftware.kryonet.*;

import java.io.IOException;

public class GameClient {
    private Client client;
    private int playerId;

    public GameClient() throws IOException {
        client = new Client();
        Network.register(client.getKryo());
        client.start();
        client.connect(5000, "192.168.1.58", 54555, 54777);

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof Network.JoinResponse) {
                    Network.JoinResponse resp = (Network.JoinResponse) object;
                    if (resp.accepted) {
                        playerId = resp.playerId;
                        System.out.println("Joined as player " + playerId);
                    } else {
                        System.out.println("Server full.");
                    }
                }

                if (object instanceof Network.PlayerAction) {
                    Network.PlayerAction action = (Network.PlayerAction) object;
                    handleOpponentAction(action);
                }
            }
        });

        Network.JoinRequest join = new Network.JoinRequest();
        join.playerName = "Player";
        client.sendTCP(join);
    }

    public void sendAction(String action, float x, float y) {
        Network.PlayerAction a = new Network.PlayerAction();
        a.playerId = playerId;
        a.action = action;
        a.x = x;
        a.y = y;
        client.sendTCP(a);
    }

    private void handleOpponentAction(Network.PlayerAction action) {
        System.out.println("Opponent did: " + action.action + " at (" + action.x + "," + action.y + ")");
        // синхронизировать действия
    }

    public static void main(String[] args) throws IOException {
        GameClient gameClient = new GameClient();
        // Пример: отправка действий
        // gameClient.sendAction("MOVE_RIGHT", 120f, 300f);
    }

    public static void createClient() throws IOException {
        GameClient gameClient = new GameClient();
    }
}

