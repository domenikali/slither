package Net;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public Client (Socket socket,String userName){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.userName=userName;
        }catch (IOException e){
            closeEverithing( socket, bufferedWriter, bufferedReader);
        }
    }

    public void sendMessage(){
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String messgaeToSend = scanner.nextLine();
                bufferedWriter.write(userName +": " + messgaeToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeEverithing(socket,bufferedWriter,bufferedReader);
        }
    }

    public void closeEverithing(Socket socket,BufferedWriter bufferedWriter,BufferedReader bufferedReader) {
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

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromServer;
                while (socket.isConnected()){
                    try {
                        msgFromServer=bufferedReader.readLine();
                        System.out.println(msgFromServer);
                    }catch (IOException e){
                        closeEverithing(socket,bufferedWriter,bufferedReader);
                    }
                }
            }
        }).start();
    }

    public static void main(String [] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter username for chat");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket,username);
        client.listenForMessage();
        client.sendMessage();
    }
}

