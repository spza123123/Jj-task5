package org.example.server;


import org.example.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final ServerSocket serverSocket;
    private static List<Client> clients;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        String name;
        clients = new ArrayList<>();
        while (!serverSocket.isClosed()){
            try {
                Socket socket = serverSocket.accept();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                name = bufferedReader.readLine();
                Client client = new Client(socket,name);
                clients.add(client);
                System.out.println(Server.showClientList());
                System.out.println("Added new Client: " + name);
                Thread thread = new Thread(client);
                thread.start();
                System.out.println("new Client started");


            } catch (IOException e) {
                closeSocket();
            }
        }
    }

    private void closeSocket(){
        try{
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e){
            throw  new RuntimeException(e);
        }
    }

    public static List<Client> showClientList(){
        return clients;
    }
}
