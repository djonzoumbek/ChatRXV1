package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Conversation implements Runnable{
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket clientSocket;
    private String username;
    private String job;
    private static ArrayList<Conversation> conversations = new ArrayList<>();
    private ServerModel serverModel;

    public Conversation(Socket clientConnection, ServerModel serverModel) throws IOException, ClassNotFoundException {
        this.clientSocket = clientConnection;
        this.serverModel = serverModel;
        conversations.add(this);

        this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.username = (String) objectInputStream.readObject();
        this.job = (String) objectInputStream.readObject();

        serverModel.addOlineUser(username, job);
        serverModel.addMessage("SERVER : "+ username +" de " + job +"est connecté au serveur ");

        broadCastMessage("SERVER : "+ username +"de"+ job +" est connecté au serveur ");
        broadCastUsers(serverModel.getOnlineUsers());
    }

    private void broadCastUsers(ArrayList<String> onlineUsers) throws IOException {
        for (Conversation conversation : conversations) {
            conversation.sendUsers(onlineUsers);
        }
    }

    public void sendUsers(ArrayList<String> onlineUsers) throws IOException {
        objectOutputStream.writeObject(onlineUsers);
        objectOutputStream.reset();
    }

    private void broadCastMessage(String message) throws IOException {
        for (Conversation conversation : conversations) {
            conversation.sendMessage(message);
        }

    }

    public void sendMessage(String message) throws IOException {
        objectOutputStream.writeObject(message);
    }

    public void readMessage() throws IOException, ClassNotFoundException {
        while (true) {
            String message = (String) objectInputStream.readObject();
            broadCastMessage(message);
            serverModel.addMessage(message);
        }
    }

    @Override
    public void run() {
        try {
            readMessage();
        } catch (IOException e) {
            closeConnection();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            closeConnection();
            e.printStackTrace();
        }

    }

    private void closeConnection() {
        try {
            if (clientSocket.isConnected()) {
                clientSocket.close();
            }
            if (objectInputStream != null){
                objectInputStream.close();
            }
            if (objectOutputStream != null){
                objectOutputStream.close();
            }
            conversations.remove(this);
            serverModel.removeUser(username);
            broadCastMessage("SERVER"+username+ " has disconnect from the server");
            broadCastUsers(serverModel.getOnlineUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
