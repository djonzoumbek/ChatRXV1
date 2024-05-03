package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket clientSocket;
    private String username;
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private ServerModel serverModel;

    public ClientHandler(Socket clientConnection, ServerModel serverModel) throws IOException, ClassNotFoundException {
        this.clientSocket = clientConnection;
        this.serverModel = serverModel;
        clientHandlers.add(this);

        this.objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.username = (String) objectInputStream.readObject();

        serverModel.addOlineUser(username);
        serverModel.addMessage("SERVER : "+ username+"has connect to the server");

        broadCastMessage("SERVER : "+ username+"has connect to the server");
        broadCastUsers(serverModel.getOnlineUsers());
    }

    private void broadCastUsers(ArrayList<String> onlineUsers) throws IOException {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendUsers(onlineUsers);
        }
    }

    public void sendUsers(ArrayList<String> onlineUsers) throws IOException {
        objectOutputStream.writeObject(onlineUsers);
        objectOutputStream.reset();
    }

    private void broadCastMessage(String message) throws IOException {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
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
            clientHandlers.remove(this);
            serverModel.removeUser(username);
            broadCastMessage("SERVER"+username+" has disconnect from the server");
            broadCastUsers(serverModel.getOnlineUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
