package com.example.gameserver;

public class Network {
    public static final int PORT = 54555;

    public static class JoinRequest {
        public String playerName;
    }

    public static class JoinResponse {
        public boolean accepted;
        public int playerId;
        public JoinResponse() {}
        public JoinResponse(boolean accepted, int playerId) {
            this.accepted = accepted;
            this.playerId = playerId;
        }
    }

    public static class PlayerAction {
        public int playerId;
        public String action;
        public float x, y;
    }

    public static class GameState {
        public float p1x, p1y, p2x, p2y;
        public String status;
    }

    public static void register(com.esotericsoftware.kryo.Kryo kryo) {
        kryo.register(JoinRequest.class);
        kryo.register(JoinResponse.class);
        kryo.register(PlayerAction.class);
        kryo.register(GameState.class);
        kryo.register(String.class);
    }
}
