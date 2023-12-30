package Net;

import view.ConnectionMenu;
import view.MenuFrame;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;
    private String snakes;
    private String foods;
    private String messageFromServer;

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

    public void write(String str){
        if (socket.isConnected()){
            try {
                bufferedWriter.write(userName + "-" + str);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }catch (IOException ignore){

            }
        }else {
            System.out.println("server disconnected");
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

        while (socket.isConnected()){
            try {
                messageFromServer=bufferedReader.readLine();
                snakes=messageFromServer.split("&")[0];
                foods=messageFromServer.split("&")[1];
                System.out.println(messageFromServer);
            }catch (IOException e){
                closeEverithing(socket,bufferedWriter,bufferedReader);
            }
        }
    }

    public String getMessageFromServer(){
        return messageFromServer;
    }
    public String getFoods(){
        return foods;
    }
    public String getSnakes(){
        return snakes;
    }

    public String getUserName() {
        return userName;
    }

    public static void main(String [] args) throws IOException {
        new ConnectionMenu().setVisible(true);/*
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter username for chat");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket,username);
        new Thread(client::listenForMessage).start();
        new Thread(client::sendMessage).start();*/
    }
}

