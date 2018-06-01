package appGUI;

import dataBaseManipulations.DataBaseConnection;
import javax.swing.*;
import java.awt.event.*;

public final class LoginFrame {
    private JFrame loginFrame;
    private JTextField urlInput, usernameInput;
    private JPasswordField passwordInput;
    private JLabel status;
    private JTextArea logger;

    public void initLoginFrame(){
        loginFrame = new JFrame("Login to your postgres database");
        JButton button = new JButton("Submit");
        urlInput = new JTextField();
        usernameInput = new JTextField();
        passwordInput = new JPasswordField();

        JLabel urlLabel = new JLabel("Input data base url");
        JLabel usernameLabel = new JLabel("Input data base username");
        JLabel passwordLabel = new JLabel("Input data base password");
        status = new JLabel("Please input data");

        urlLabel.setBounds(50, 50, 300, 30);
        urlInput.setBounds(50, 75, 450, 20);
        usernameLabel.setBounds(50, 100, 300, 30);
        usernameInput.setBounds(50, 125, 450, 20);
        passwordLabel.setBounds(50, 150, 300, 30);
        passwordInput.setBounds(50, 175, 450, 20);
        status.setBounds(50, 200, 450, 20);
        button.setBounds(50, 300, 95, 30);
        button.addActionListener(new DataListener());
        logger = new JTextArea();
        JLabel loggerTextField = new JLabel("Logger");
        loggerTextField.setBounds(625, 50, 200, 30);
        logger.setBounds(550,75, 200,250);

        loginFrame.add(button);
        loginFrame.add(status);
        loginFrame.add(urlLabel);
        loginFrame.add(urlInput);
        loginFrame.add(usernameLabel);
        loginFrame.add(usernameInput);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordInput);
        loginFrame.add(logger);
        loginFrame.add(loggerTextField);

        loginFrame.setSize(800, 400);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setLayout(null);
        loginFrame.setVisible(true);
        loginFrame.setResizable(false);
        loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private class DataListener implements ActionListener {
        private String url;
        private String username;
        private String password;
        public void actionPerformed(ActionEvent e) {
            url = urlInput.getText();
            username = usernameInput.getText();
            password = String.valueOf(passwordInput.getPassword());
            if (url != null && username != null) {
                DataBaseConnection dataBaseConnection = new DataBaseConnection(url, username, password);
                if (dataBaseConnection.connectToDataBase().equals("Connected")) {
                    status.setText("Connected!");
                    logger.setText(dataBaseConnection.connectToDataBase());
                    loginFrame.dispose();
                    BookingFrame bookingFrame = new BookingFrame(dataBaseConnection);
                    bookingFrame.start();
                } else {
                    status.setText("Failed to connect!");
                    logger.setText(dataBaseConnection.connectToDataBase());
                }
            }
        }
    }

}
