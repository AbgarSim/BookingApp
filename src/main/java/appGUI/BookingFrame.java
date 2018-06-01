package appGUI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.apache.commons.lang3.StringUtils;


public class BookingFrame {

    //Элементы GUI
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem menuItem;
    private JTextArea text;
    private JPanel panel, panelButtons;
    private JLabel label, label2;
    private JButton button;

    public void start() {
        frame = new JFrame("Demo");
        menuBar = new JMenuBar();

        menu = new JMenu("Menu");
        menuItem = new JMenuItem("Choose carriege");
        menuItem.addActionListener(new ChangeCarriage());
        menu.add(menuItem);
        menuItem = new JMenuItem("Check information about all passangers");
        menuItem.addActionListener(new PrintOutAllInfo());
        menu.add(menuItem);
        menuBar.add(menu);

        text = new JTextArea(10, 30);
        text.setText("Logger field");
        text.setLineWrap(true);
        text.setMaximumSize(text.getMaximumSize());
        JScrollPane scroller = new JScrollPane(text);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panelButtons = new JPanel();
        setButtons();

        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.NORTH, scroller);
        frame.getContentPane().add(BorderLayout.CENTER, panelButtons);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void setButtons() {
        int amountOfPlaces = 25;

        for (int i = 0; i < amountOfPlaces; i++) {
            //JButton button = new JButton("Кнопка");
            button = new JButton(i + 1 + " Seat");
            //Тут должно быть считывание из БД и какая либо проверка на занято ли место или нет
            //пока поставил лидл ID

            Object ID = new Object();
            //если ID = null - то значит место свободное и ставить надо GREEN, если не null, то занято и RED
            ID = null;
            if (ID == null) {
                button.setBackground(Color.GREEN);
            } else {
                button.setBackground(Color.RED);
            }

            button.addActionListener(new BookingAction());
            panelButtons.add(button);
        }
    }

    class BookingAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JButton b = (JButton) e.getSource();
            if (b.getBackground() == Color.GREEN) {
                createBookingEntry(b);
//                b.setAction(new GAction(b));
            } else {
                treatBookedSeat(b);
            }
        }

        void createBookingEntry(JButton b) {
            ArrayList<String> list = new ArrayList();

            list.add(JOptionPane.showInputDialog
                    (frame, "Type in name of the passenger", "Type in information", JOptionPane.PLAIN_MESSAGE));
            list.add(JOptionPane.showInputDialog
                    (frame, "Type in surname of the passenger", "Type in information", JOptionPane.PLAIN_MESSAGE));
            list.add(JOptionPane.showInputDialog
                    (frame, "Type in ALO of the passenger", "Type in information", JOptionPane.PLAIN_MESSAGE));

//            Запись list в БД

            int f = list.size();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i).isEmpty() || StringUtils.isBlank(list.get(i))) {
                    JOptionPane.showMessageDialog(frame, "Lack of information");
                    break;
                }
                if (f == i + 1)
                    b.setBackground(Color.RED);
            }
        }
    }

    private void treatBookedSeat(JButton b) {
        Object[] options = {"Check the info", "Delete the info"};
        int choise = JOptionPane.showOptionDialog(frame,
                "Choose what to do",
                "What to do?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        if (choise == 0) {
            //Тут сделать вывод на экран информации о пассажире из БД
            text.append("ALO");
        }
        if (choise == 1) {
            //Тут сделать удаление информации о пассажире из БД
            b.setBackground(Color.GREEN);
        }
    }

    class ChangeCarriage implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //TO DO 
        }
    }

    class PrintOutAllInfo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //Тут получаем ВСЮ инфу из БД и выводим на экран
            //ты мне инфу эту получи, а я её выведу.
            text.append("");
        }
    }

    public static void main(String[] args) {
        new BookingFrame();
    }
}
