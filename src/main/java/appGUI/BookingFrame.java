package appGUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.*;
import java.util.List;
import javax.swing.*;

import dataBaseManipulations.DataBaseConnection;
import org.apache.commons.lang3.StringUtils;


public class BookingFrame {
    private JFrame frame;
    private JTextArea text;
    private JPanel panelButtons;
    private DataBaseConnection dataBaseConnection;

    BookingFrame(DataBaseConnection dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
    }

    class BookingButton extends JButton {
        final int ID;

        public BookingButton(int id) {
            ID = id;
        }

        BookingButton(int id, String text) {
            super(text);
            ID = id;
        }
    }

    public void start() {
        frame = new JFrame("Demo");

        text = new JTextArea(10, 30);
        text.setText("Logger field");
        text.setLineWrap(true);
        text.setMaximumSize(text.getMaximumSize());
        JScrollPane scroller = new JScrollPane(text);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panelButtons = new JPanel();
        setButtons();

        frame.getContentPane().add(BorderLayout.NORTH, scroller);
        frame.getContentPane().add(BorderLayout.CENTER, panelButtons);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void setButtons() {
        int amountOfPlaces = 25;
        for (int i = 0; i < amountOfPlaces; i++) {
            BookingButton button = new BookingButton(i + 1, i + 1 + " Seat");
            if (dataBaseConnection.selectSql(button.ID) != null) {
                button.setBackground(Color.RED);
            } else {
                button.setBackground(Color.GREEN);
            }
            button.addActionListener(new BookingAction());
            panelButtons.add(button);
        }
    }

    class BookingAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            BookingButton b = (BookingButton) e.getSource();
            if (b.getBackground() == Color.GREEN) {
                createBookingEntry(b, b.ID);
            } else {
                treatBookedSeat(b);
            }
        }

        void createBookingEntry(BookingButton b, int ID) {
            List<String> list = new ArrayList<String>();
            list.add(String.valueOf(ID));
            list.add(JOptionPane.showInputDialog
                    (frame, "Type in name of the passenger", "Type in information", JOptionPane.PLAIN_MESSAGE));
            list.add(JOptionPane.showInputDialog
                    (frame, "Type in surname of the passenger", "Type in information", JOptionPane.PLAIN_MESSAGE));
            list.add(JOptionPane.showInputDialog
                    (frame, "Type in email of the passenger", "Type in information", JOptionPane.PLAIN_MESSAGE));
            int f = list.size();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i).isEmpty() || StringUtils.isBlank(list.get(i))) {
                    JOptionPane.showMessageDialog(frame, "Lack of information");
                    break;
                }
                if (f == i + 1) {
                    b.setBackground(Color.RED);
                    dataBaseConnection.insertSql(Integer.parseInt(list.get(0)), list.get(1), list.get(2), list.get(3));
                }
            }
        }
    }

    private void treatBookedSeat(BookingButton b) {
        Object[] options = {"Check the info", "Delete the info"};
        int choice = JOptionPane.showOptionDialog(frame,
                "Choose what to do",
                "What to do?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        if (choice == 0) {
            String data[] = dataBaseConnection.selectSql(b.ID);
            assert data != null;
            text.append("Seat id: " + data[0] +
                    "\nPassenger name: " + data[1] +
                    "\nPassenger surname: " + data[2] +
                    "\nPassenger email: " + data[3]);
        }
        if (choice == 1) {
            dataBaseConnection.deleteSql(b.ID);
            text.append("\nSuccessfully removed record from the seat number " + b.ID);
            b.setBackground(Color.GREEN);
        }
    }


}
