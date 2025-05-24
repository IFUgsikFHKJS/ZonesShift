package com.zsgame.zonesshift.multiplayer;

import com.esotericsoftware.kryo.Kryo;

public class Network {
    public static final int PORT = 54555;

    public static class JoinRequest {
        public String playerName;
    }

    public static class JoinResponse {
        public boolean accepted;
        public int playerId; // 1 или 2

        public JoinResponse(boolean accepted, int playerId) {
            this.accepted = accepted;
            this.playerId = playerId;
        }

        public JoinResponse() {

        }
    }

    public static class PlayerAction {
        public int playerId;
        public String action; // например: "MOVE_LEFT", "JUMP", "ATTACK", "FIRE", и т.п.
        public float x, y;    // позиция игрока, если нужно
    }

    public static class GameState {
        public float p1x, p1y, p2x, p2y;
        public String status; // например: "IN_PROGRESS", "PLAYER1_WINS", и т.д.
    }

    public static void register(Kryo kryo) {
        kryo.register(JoinRequest.class);
        kryo.register(JoinResponse.class);
        kryo.register(PlayerAction.class);
        kryo.register(GameState.class);
        kryo.register(String.class);
    }
}

