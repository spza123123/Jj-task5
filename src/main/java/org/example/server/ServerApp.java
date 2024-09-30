package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerApp {
    public static void main(String[] args) {
        try {
            Server server = new Server(new ServerSocket(1400));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
