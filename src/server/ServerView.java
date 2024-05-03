package server;

import javax.swing.*;

public class ServerView {
    private JPanel serverPannel;
    private JTextArea chatArea;
    private JTextArea onlineUsersArea;
    private JButton saveChatButton;
    private JButton loadChatButton;
    private JButton startServerButton;
    private JButton stopServerButton;


    private ServerModel serverModel;

    public ServerView(ServerModel serverModel) {
        this.serverModel = serverModel;
    }


    //getter

    public JPanel getServerPannel() {
        return serverPannel;
    }
    public JButton getSaveChatButton() {
        return saveChatButton;
    }

    public JButton getLoadChatButton() {
        return loadChatButton;
    }

    public JButton getStartServerButton() {
        return startServerButton;
    }

    public JButton getStopServerButton() {
        return stopServerButton;
    }


    public void updateUsers() {
        onlineUsersArea.setText("");
        for (String user : serverModel.getOnlineUsers()) {
            onlineUsersArea.append(user);
        }
    }

    public void updateMessage() {
        chatArea.append(serverModel.getLatestMessage());
    }
}
