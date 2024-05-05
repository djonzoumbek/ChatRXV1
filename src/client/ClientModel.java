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
    private String nomComplet;
    private String job;

    private ClientView clientView;

    public void connect(String username, String nomComplet, String serverAddress, String job) {
        try {
            clientsocket = new Socket(serverAddress, 4321);
            this.nomComplet = nomComplet;
            this.job = job;
            objectOutputStream = new ObjectOutputStream(clientsocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientsocket.getInputStream());
            objectOutputStream.writeObject(username);
            objectOutputStream.writeObject(nomComplet);
            objectOutputStream.writeObject(job);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void sendMessage(String message) throws IOException {
        if (objectOutputStream != null) {
            objectOutputStream.writeObject(nomComplet + " de "+ job + ": " + message);
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
                clientView.updateUserList();
            }
            if (objectOutputStream != null){
                objectOutputStream.close();
                clientView.updateUserList();
            }
            if (objectInputStream != null){
                objectInputStream.close();
                clientView.updateUserList();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
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
