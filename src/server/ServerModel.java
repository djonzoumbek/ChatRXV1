package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerModel {
    private ArrayList<String> message = new ArrayList<>();
    private ArrayList<String> onlineUsers = new ArrayList<>();
    private ServerSocket serverSocket;
    private ServerView serverView;


    public void startServer() throws IOException, ClassNotFoundException {
        serverSocket = new ServerSocket(4321);
        addMessage("Server started");

        while(true) {
            Socket clientConnection = serverSocket.accept();
            Conversation conversation = new Conversation(clientConnection, this);
            Thread thread = new Thread(conversation);
            thread.start();
        }
    }

    public void stopServer() throws IOException {
        serverSocket.close();
        addMessage("Server stop");

    }

    public void addOlineUser(String username, String job) {
        onlineUsers.add(username + "|" + job +"\n");
        serverView.updateUsers();
    }
    public void removeUser(String username) {
        onlineUsers.remove(username+"\n");
        serverView.updateUsers();
    }

    public ArrayList<String> getOnlineUsers() {
        return onlineUsers;
    }

    public void addMessage(String s) {
        message.add(s+"\n");
        serverView.updateMessage();
    }


    public String getLatestMessage() {
        return message.get(message.size()-1);
    }

    public void addObserver(ServerView serverView) {
        this.serverView = serverView;
    }


}
