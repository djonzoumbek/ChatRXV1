package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class ClientModel {
    private ArrayList<String> messages = new ArrayList<>();
    private  ArrayList<String> onlineUsers = new ArrayList<>();
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket clientsocket;
    private String username;

    private ClientView clientView;

    public void connect(String username, String serverAddress) throws IOException {
        clientsocket = new Socket(serverAddress, 4321);
        this.username = username;
        objectOutputStream = new ObjectOutputStream(clientsocket.getOutputStream());
        objectInputStream = new ObjectInputStream(clientsocket.getInputStream());
        objectOutputStream.writeObject(username);
    }

    public void sendMessage(String message) throws IOException {
        if (objectOutputStream != null) {
            objectOutputStream.writeObject(username + ": " + message);
            objectOutputStream.flush(); // Assure l'envoi immédiat du message
        } else {
            throw new IOException("ObjectOutputStream is not initialized.");
        }
    }

    public void readMessage() throws IOException, ClassNotFoundException {
        if (objectInputStream != null) {
            Object obj = objectInputStream.readObject();
            if (obj instanceof String) {
                String message = (String) obj;
                messages.add(message + "\n");
                clientView.updateMessage();
            } else if (obj instanceof ArrayList) {
                @SuppressWarnings("unchecked")
                ArrayList<String> onlineUsers = (ArrayList<String>) obj;
                this.onlineUsers = onlineUsers;
                clientView.updateUserList();
            } else {
                throw new IOException("Received object is neither String nor ArrayList.");
            }
        } else {
            throw new IOException("ObjectInputStream is not initialized.");
        }
    }


    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        readMessage();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void disconnect() {
        try {
            if (clientsocket.isConnected()){
                clientsocket.close();
            }
            if (objectOutputStream != null){
                objectOutputStream.close();
            }
            if (objectInputStream != null){
                objectInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLatestMessage(){
        return messages.get(messages.size() -1);
    }

    public ArrayList<String> getOnlineUsers (){
        return onlineUsers;
    }
    public void addObserver(ClientView clientView){
        this.clientView = clientView;
    }
}
