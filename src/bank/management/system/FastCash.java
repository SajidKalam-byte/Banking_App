package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FastCash extends JFrame implements ActionListener {

    JButton b1, b2, b3, b4, b5, b6, b7;
    String pin;

    public FastCash(String pin) {
        this.pin = pin;

        // Background setup
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550, 830, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setBounds(0, 0, 1550, 830);
        add(background);

        JLabel label = new JLabel("SELECT WITHDRAWAL AMOUNT");
        label.setBounds(445, 180, 700, 35);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("System", Font.BOLD, 23));
        background.add(label);

        // Buttons for fast cash options
        b1 = createButton("Rs. 100", 410, 274, background);
        b2 = createButton("Rs. 500", 700, 274, background);
        b3 = createButton("Rs. 1000", 410, 318, background);
        b4 = createButton("Rs. 2000", 700, 318, background);
        b5 = createButton("Rs. 5000", 410, 362, background);
        b6 = createButton("Rs. 10000", 700, 362, background);

        b7 = new JButton("BACK");
        b7.setForeground(Color.WHITE);
        b7.setBackground(new Color(65, 125, 128));
        b7.setBounds(700, 406, 150, 35);
        b7.addActionListener(this);
        background.add(b7);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b7) {
            setVisible(false);
            new main_Class(pin);
        } else {
            String amountStr = ((JButton) e.getSource()).getText().substring(4).trim();
            double withdrawAmount = Double.parseDouble(amountStr);

            try (Con con = new Con()) {
                // Calculate current balance
                double balance = 0.0;
                String balanceQuery = "SELECT type, amount FROM bank WHERE pin = ?";
                PreparedStatement balanceStmt = con.connection.prepareStatement(balanceQuery);
                balanceStmt.setString(1, pin);

                ResultSet rs = balanceStmt.executeQuery();
                while (rs.next()) {
                    String type = rs.getString("type");
                    double amt = Double.parseDouble(rs.getString("amount"));
                    if ("Deposit".equalsIgnoreCase(type)) {
                        balance += amt;
                    } else if ("Withdrawal".equalsIgnoreCase(type)) {
                        balance -= amt;
                    }
                }

                // Check if sufficient balance is available
                if (balance < withdrawAmount) {
                    JOptionPane.showMessageDialog(null, "Insufficient Balance.");
                    return;
                }

                // Record the transaction
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(now);

                String insertQuery = "INSERT INTO bank (pin, date, type, amount) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = con.connection.prepareStatement(insertQuery);
                insertStmt.setString(1, pin);
                insertStmt.setString(2, formattedDate); // Use formatted date
                insertStmt.setString(3, "Withdrawal");
                insertStmt.setString(4, String.valueOf(withdrawAmount));
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Rs. " + withdrawAmount + " Debited Successfully.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred. Please try again.");
            }

            setVisible(false);
            new main_Class(pin);
        }
    }

    public static void main(String[] args) {
        new FastCash("1234");
    }
}
