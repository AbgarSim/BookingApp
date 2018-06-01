package appGUI;

import dataBaseManipulations.DataBaseConnection;

import javax.swing.*;
import java.awt.event.*;

public class LoginFrame {
    private JFrame frame;
    private JButton button;
    private JTextField textField1, textField2;
    private JPasswordField textField3;
    private JLabel status;
    private JLabel welcomeText1, welcomeText2, welcomeText3, loggerTextField;
    private JTextArea logger;
    private DataBaseConnection dataBase;

    public void initLoginFrame(){
        frame = new JFrame("Login to your postgres database");
        button = new JButton("Submit");
        textField1 = new JTextField();
        textField2 = new JTextField();
        textField3 = new JPasswordField();

        welcomeText1 = new JLabel("Input data base url");
        welcomeText2 = new JLabel("Input data base username");
        welcomeText3 = new JLabel("Input data base password");
        status = new JLabel("Please input data");

        welcomeText1.setBounds(50, 50, 300, 30);
        textField1.setBounds(50, 75, 450, 20);
        welcomeText2.setBounds(50, 100, 300, 30);
        textField2.setBounds(50, 125, 450, 20);
        welcomeText3.setBounds(50, 150, 300, 30);
        textField3.setBounds(50, 175, 450, 20);
        status.setBounds(50, 200, 450, 20);
        button.setBounds(50, 300, 95, 30);
        button.addActionListener(new DataListener());
        logger = new JTextArea();
        loggerTextField = new JLabel("Logger");
        loggerTextField.setBounds(625, 50, 200, 30);
        logger.setBounds(550,75, 200,250);

        frame.add(button);
        frame.add(status);
        frame.add(welcomeText1);
        frame.add(textField1);
        frame.add(welcomeText2);
        frame.add(textField2);
        frame.add(welcomeText3);
        frame.add(textField3);
        frame.add(logger);
        frame.add(loggerTextField);

        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class DataListener implements ActionListener {
        private String url;
        private String username;
        private String password;
        public void actionPerformed(ActionEvent e) {
            url = textField1.getText();
            username = textField2.getText();
            password = String.valueOf(textField3.getPassword());
            if (url != null && username != null) {
                dataBase = new DataBaseConnection(url, username, password);
                if (dataBase.connectToDataBase().equals("Connected")) {
                    status.setText("Connected!");
                    logger.setText(dataBase.connectToDataBase());
                    frame.dispose();
                    BookingFrame bookingFrame = new BookingFrame();
                    bookingFrame.start();
                } else {
                    status.setText("Failed to connect!");
                    logger.setText(dataBase.connectToDataBase());
                }
            }
        }
    }
}
