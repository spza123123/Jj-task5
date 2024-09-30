package org.example;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please insert your name: ");
            String name = scanner.nextLine();
            Socket socket = new Socket("localhost",1400);
            Client client = new Client(socket,name);
            client.listenForMessage();
            client.sendMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
