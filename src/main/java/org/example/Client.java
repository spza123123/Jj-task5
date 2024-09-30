package org.example;

import org.example.server.Server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{
    private final Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;

    public Client(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                while (socket.isConnected()){
                    try {
                        message = bufferedReader.readLine();
                        System.out.println(message);
                    } catch (IOException e){
                        closeEverything(socket,bufferedReader,bufferedWriter);
                        e.printStackTrace();
                    }


                }
            }
        }).start();
    }

    public void sendMessage(){
        try {
            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner =new Scanner(System.in);
            while(socket.isConnected()){
                String message = scanner.nextLine();
                bufferedWriter.write(name + ": " + message );
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }

    }

    private void closeEverything(Socket socket
            ,BufferedReader bufferedReader
            ,BufferedWriter bufferedWriter){
        try {
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }

        }
    }

    private void broadcastMessage(String message) {
        for (Client client : Server.showClientList()) {
            if (!client.name.equals(name)) {
                try {
                    client.bufferedWriter.write(message);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }
    }
}
