package client;

import javax.swing.*;
import java.awt.*;

public class ClientView {
    private JPanel clientPannel;
    private JPanel connecPannel;
    private JPanel chatPannel;
    private JTextField nomCompletField;
    private JButton connectButton;
    private JLabel errormessage;
    private JTextArea chatarea;
    private JTextArea onlineUsersarea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton backButton;
    private JMenuBar menuBar;
    private JTextField jobTextField;
    private JTextField usernameField;


    private ClientModel clientModel;


    public ClientView(ClientModel clientModel) {
        this.clientModel = clientModel;
    }


    public JPanel getClientPannel(){
        return clientPannel;
    }

    public String getnomComplet(){
        return nomCompletField.getText();
    }
    public String getUsernameField(){
        return usernameField.getText();
    }

    public String getJobTextField(){
        return jobTextField.getText();
    }


    public JButton getConnectButton() {
        return connectButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public String getMessage() {
        return messageField.getText();
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public JButton getSendButton() {
        return sendButton;
    }


    public JTextField getMessageField(){
        return messageField;
    }

    public void showChat() {
        CardLayout cardLayout = (CardLayout) clientPannel.getLayout();
        cardLayout.show(clientPannel, "chat");

    }

    public void showHome() {
        CardLayout cardLayout = (CardLayout) clientPannel.getLayout();
        cardLayout.show(clientPannel, "home");

    }

    public void updateMessage() {
        chatarea.append(clientModel.getLatestMessage()+ "\n");
        //chatarea.append("Hello tout le monde\n");
        System.out.println(messageField.getText());
    }

    public void updateUserList() {
        onlineUsersarea.setText("");
        for (String user : clientModel.getOnlineUsers()) {
            onlineUsersarea.append(user + "\n");
        }
    }

    public void setErrorMessage(String errorMessage) {
        this.errormessage.setText(errorMessage);
    }
}
