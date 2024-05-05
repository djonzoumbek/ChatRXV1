package server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ServerController extends JFrame {

    private ServerModel serverModel;
    private ServerView serverView;

    public ServerController() {
        this.serverModel = new ServerModel();
        this.serverView = new ServerView(serverModel);

        this.serverModel.addObserver(serverView);

//        serverView.getSaveChatButton().addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Save chat log ...TODO");
//            }
//        });
//
//        serverView.getLoadChatButton().addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Load chat log ...TODO");
//            }
//        });

        serverView.getStartServerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        serverView.getStopServerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Stop server ...TODO");
            }
        });

        add(serverView.getServerPannel());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Server ");
        setVisible(true);

    }

    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverModel.startServer();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}
