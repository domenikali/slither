package Net;

import model.Direction;
import model.Snake;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

    private final ServerSocket serverSocket;
    private final List<ClientHandler> clinetHandlers;
    private GameServer gameServer;

    public Server(ServerSocket serverSocket) {
        clinetHandlers = new ArrayList<>();
        this.serverSocket = serverSocket;
        this.gameServer=new GameServer(this);
    }
    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                System.out.println("New player connected: "+clientHandler.getClientUserNamme());
                clinetHandlers.add(clientHandler);
                gameServer.addPlayer(clientHandler,new Snake(110, Direction.RIGHT));
                Thread thread = new Thread(clientHandler);
                thread.start();

            }
        }
        catch(IOException ignore){
            closeServerSocket();
        }
    }
    public int  getInitialPosition(){
        double pos = Math.random();
        return 200;
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
            try { //stable 60fps
                long start = System.currentTimeMillis();
                Thread t= new Thread(()-> gameServer.update());
                t.start();
                t.join();
                long finish = System.currentTimeMillis()-start;

                if(finish<16)
                    Thread.sleep(17-(finish));
            }catch (InterruptedException ignore){
            }
        }
    }

    public static void removePlayer(){

    }

    public List<ClientHandler> getClinetHandlers(){
        return clinetHandlers;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serversocket = new ServerSocket(1234);
        Server server = new Server(serversocket);
        new Thread(server::respond).start();
        new Thread(()->server.startServer()).start();
    }
}


