package com.zsgame.zonesshift.multiplayer;


import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class ServerFoundation {

    public static ServerFoundation instance;

    private Server server;

    public static void main(String[] args) {
        ServerFoundation.instance = new ServerFoundation();
    }

    public ServerFoundation() {
        this.server = new Server();



        this.bindServer(6334, 6334);
    }

    public void bindServer(final int tcpPort, final int udpPort) {
        this.server.start();

        try {
            this.server.bind(tcpPort, udpPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server getServer() {
        return server;
    }
}