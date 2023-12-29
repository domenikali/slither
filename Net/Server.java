package Net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

    private final ServerSocket serverSocket;
    private final List<ClientHandler> clinetHandlers;

    public Server(ServerSocket serverSocket) {
        clinetHandlers = new ArrayList<>();
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("new client connected");
                ClientHandler clientHandler = new ClientHandler(socket);
                clinetHandlers.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();

            }
        }
        catch(IOException ignore){
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String str){
        for(ClientHandler clientHandler: clinetHandlers){
            clientHandler.write(str);
        }
    }

    public void respond(){
        Scanner scanner = new Scanner(System.in);
        String mes;
        System.out.println("inizio risposte");
        while(!serverSocket.isClosed()){
            if(scanner.hasNextLine())
                sendMessage(scanner.nextLine());
        }
    }



    public static void main(String[] args) throws IOException {
        ServerSocket serversocket = new ServerSocket(1234);
        Server server = new Server(serversocket);
        new Thread(server::respond).start();
        server.startServer();
    }
}


