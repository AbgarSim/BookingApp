package appGUI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

import dataBaseManipulations.DataBaseConnection;
import org.apache.commons.lang3.StringUtils;


public final class BookingFrame {
    private JFrame bookingFrame;
    private JTextArea text;
    private JPanel panelButtons;
    private DataBaseConnection baseConnection;


    BookingFrame(DataBaseConnection dataBaseConnection) {
        this.baseConnection = dataBaseConnection;
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

    //Builds the frame
    public void start() {
        bookingFrame = new JFrame("Demo");

        text = new JTextArea(10, 30);
        text.setText("Logger field");
        text.setLineWrap(true);
        text.setMaximumSize(text.getMaximumSize());
        JScrollPane scroller = new JScrollPane(text);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panelButtons = new JPanel();
        setButtons();

        bookingFrame.getContentPane().add(BorderLayout.NORTH, scroller);
        bookingFrame.getContentPane().add(BorderLayout.CENTER, panelButtons);
        bookingFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        bookingFrame.setSize(800, 400);
        bookingFrame.setLocationRelativeTo(null);
        bookingFrame.setResizable(false);
        bookingFrame.setVisible(true);
    }
    //set up buttons
    private void setButtons() {
        int amountOfPlaces = 25;
        for (int i = 0; i < amountOfPlaces; i++) {
            BookingButton button = new BookingButton(i + 1, i + 1 + " Seat");
            if (baseConnection.selectSql(button.ID) != null) {
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
        //registers a new record in the database if credentials are valid
        void createBookingEntry(BookingButton b, int ID) {
            List<String> list = new ArrayList<String>();
            list.add(String.valueOf(ID));
            list.add(JOptionPane.showInputDialog
                    (bookingFrame, "Type in name of the passenger", "Type in information", JOptionPane.PLAIN_MESSAGE));
            list.add(JOptionPane.showInputDialog
                    (bookingFrame, "Type in surname of the passenger", "Type in information", JOptionPane.PLAIN_MESSAGE));
            list.add(JOptionPane.showInputDialog
                    (bookingFrame, "Type in email of the passenger", "Type in information", JOptionPane.PLAIN_MESSAGE));
            int f = list.size();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i).isEmpty() || StringUtils.isBlank(list.get(i))) {
                    JOptionPane.showMessageDialog(bookingFrame, "Lack of information");
                    break;
                }
                if (f == i + 1) {
                    b.setBackground(Color.RED);
                    baseConnection.insertSql(Integer.parseInt(list.get(0)), list.get(1), list.get(2), list.get(3));
                }
            }
        }
    }
    //either deletes the reservation or prints the info about reservation
    private void treatBookedSeat(BookingButton b) {
        Object[] options = {"Check the info", "Delete the info"};
        int choice = JOptionPane.showOptionDialog(bookingFrame,
                "Choose what to do",
                "What to do?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        if (choice == 0) {
            String data[] = baseConnection.selectSql(b.ID);
            assert data != null;
            text.append("Seat id: " + data[0] +
                    "\nPassenger name: " + data[1] +
                    "\nPassenger surname: " + data[2] +
                    "\nPassenger email: " + data[3]);
        }
        if (choice == 1) {
            baseConnection.deleteSql(b.ID);
            text.append("\nSuccessfully removed record from the seat number " + b.ID);
            b.setBackground(Color.GREEN);
        }
    }


}
