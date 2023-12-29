package Net;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{
    public static List<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String clientUserNamme;

    private String newPos;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUserNamme = bufferedReader.readLine();
            clientHandlers.add(this);
            brodcastMessage("SERVER: " + clientUserNamme + "has entered the chat");
        }catch (IOException e){
            closeEverything(socket,bufferedWriter,bufferedReader);
        }
    }

    @Override
    public void run() {
        receiveMessage();

    }



    public void receiveMessage(){
        String messageFromClinet;

        while (socket.isConnected()){
            try{
                messageFromClinet = bufferedReader.readLine();
                newPos=messageFromClinet;
                System.out.println(messageFromClinet);
            }catch (IOException e){
                closeEverything(socket,bufferedWriter,bufferedReader);
                break;
            }
        }
    }

    public void brodcastMessage(String messageToSend){
        for (ClientHandler clinetHandler : clientHandlers){
            try{
                if(!clinetHandler.clientUserNamme.equals(clientUserNamme)){
                    clinetHandler.bufferedWriter.write(messageToSend);
                    clinetHandler.bufferedWriter.newLine();
                    clinetHandler.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket,bufferedWriter,bufferedReader);
            }
        }
    }

    public void write (String mes){
        try {
            bufferedWriter.write(mes);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException ignore){

        }
    }

    public void writeToAll(String mes){
        for(ClientHandler clientHandler: clientHandlers){
            clientHandler.write(mes);
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        brodcastMessage("SERVER: " + clientUserNamme + " has left the chat");
    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        removeClientHandler();
        try {
            if (socket != null)
                socket.close();
            if (bufferedReader != null)
                bufferedReader.close();
            if (bufferedWriter != null)
                bufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getClientUserNamme() {
        return clientUserNamme;
    }

    public String getNewPos() {
        return newPos;
    }
}
