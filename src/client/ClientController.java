package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ClientController extends JFrame {
    private ClientModel clientModel;
    private ClientView clientView;


    public ClientController() {
        this.clientModel = new ClientModel();
        this.clientView = new ClientView(clientModel);

        clientModel.addObserver(clientView);

        clientView.getConnectButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomComplet = clientView.getnomComplet();
                String username = clientView.getUsernameField();
                String serverIPAddress = "localhost";
                String job = clientView.getJobTextField();
                if (nomComplet.isBlank()){
                //JOptionPane.showMessageDialog(null, "Please enter a nomComplet");
                    clientView.setErrorMessage("Svp entrez votre nom");
                }else {
                    clientModel.connect(username, nomComplet, serverIPAddress, job);
                    clientModel.listenForMessage();
                    //clientView.showHome();
                    //System.out.println("Change to chatPannel");
                    clientView.showChat();
                    setTitle("Client - " + nomComplet);

                }


            }
        });

        clientView.getBackButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Back to chatPannel");
                clientModel.getOnlineUsers();
                clientModel.disconnect();
                clientView.showHome();
            }
        });

        clientView.getSendButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = clientView.getMessage();
                try {
                    clientModel.sendMessage(message);
                    clientView.getMessageField().setText("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    clientModel.disconnect();
                    clientView.showHome();
                }

            }
        });

        clientView.getMessageField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String message = clientView.getMessage();
                if (e.getKeyChar() == KeyEvent.VK_ENTER){
                    try {
                        clientModel.sendMessage(message);
                        clientView.getMessageField().setText("");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


//        clientView.getServerAddressField().addFocusListener(new FocusListener() {
//            @Override
//            public void focusGained(FocusEvent e) {
//                if(clientView.getServerAddressField().getText().equals("Default: Localhost")) {
//                    clientView.getServerAddressField().setText("");
//                    clientView.getServerAddressField().setForeground(Color.black);
//                }
//            }
//
//            @Override
//            public void focusLost(FocusEvent e) {
//                if (clientView.getServerAddressField().getText().isBlank()) {
//                    clientView.getServerAddressField().setForeground(Color.gray);
//                    clientView.getServerAddressField().setText("Default: Localhost");
//
//                }
//            }
//        });


        add(clientView.getClientPannel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(clientView.getMenuBar());
        setTitle("Client");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
