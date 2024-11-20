package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class main_Class extends JFrame implements ActionListener {
    JButton depositButton, withdrawButton, fastCashButton, miniStatementButton, pinChangeButton, balanceEnquiryButton, exitButton,transferButton;
    String pin;

    public main_Class(String pin) {
        this.pin = pin;

        if (pin == null || pin.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid PIN. Please log in again.");
            System.exit(0);
        }

        // Background Image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550, 830, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setBounds(0, 0, 1550, 830);
        add(background);

        // Title
        JLabel title = new JLabel("Please Select Your Transaction");
        title.setBounds(430, 180, 700, 35);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("System", Font.BOLD, 28));
        background.add(title);

        // Buttons
        depositButton = createButton("DEPOSIT", 410, 274, background);
        withdrawButton = createButton("CASH WITHDRAWAL", 700, 274, background);
        fastCashButton = createButton("FAST CASH", 410, 318, background);
        miniStatementButton = createButton("MINI STATEMENT", 700, 318, background);
        pinChangeButton = createButton("PIN CHANGE", 410, 362, background);
        balanceEnquiryButton = createButton("BALANCE ENQUIRY", 700, 362, background);
        transferButton = createButton("TRANSFER", 410, 406, background);

        exitButton = createButton("EXIT", 700, 406, background);

        setLayout(null);
        setSize(1550, 1080);
        setLocation(0, 0);
        setUndecorated(true);
        setVisible(true);
    }

    private JButton createButton(String text, int x, int y, Container container) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(65, 125, 128));
        button.setBounds(x, y, 150, 35);
        button.addActionListener(this);
        container.add(button);
        return button;
    }

    private void openWindow(JFrame newWindow) {
        setVisible(false);
        newWindow.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == depositButton) {
            openWindow(new Deposit(pin));
        } else if (e.getSource() == withdrawButton) {
            openWindow(new Withdrawl(pin));
        } else if (e.getSource() == fastCashButton) {
            openWindow(new FastCash(pin));
        } else if (e.getSource() == miniStatementButton) {
            openWindow(new MiniStatement(pin));
        } else if (e.getSource() == pinChangeButton) {
            openWindow(new Pin(pin));
        } else if (e.getSource() == balanceEnquiryButton) {
            openWindow(new BalanceEnquiry(pin));
        } else if (e.getSource() == transferButton) {
            openWindow(new Transfer(pin));
        }else if (e.getSource() == exitButton) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        new main_Class("1234"); // Example PIN for testing
    }
}
