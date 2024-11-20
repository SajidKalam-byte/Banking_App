package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BalanceEnquiry extends JFrame implements ActionListener {

    String pin;
    JLabel balanceLabel;
    JButton backButton;

    public BalanceEnquiry(String pin) {
        this.pin = pin;

        // Background Image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550, 830, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setBounds(0, 0, 1550, 830);
        add(background);

        // Balance Label
        JLabel balanceTitle = new JLabel("Your Current Balance is Rs:");
        balanceTitle.setForeground(Color.WHITE);
        balanceTitle.setFont(new Font("System", Font.BOLD, 16));
        balanceTitle.setBounds(430, 180, 700, 35);
        background.add(balanceTitle);

        balanceLabel = new JLabel();
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(new Font("System", Font.BOLD, 16));
        balanceLabel.setBounds(430, 220, 400, 35);
        background.add(balanceLabel);

        // Back Button
        backButton = new JButton("Back");
        backButton.setBounds(700, 406, 150, 35);
        backButton.setBackground(new Color(65, 125, 128));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(this);
        background.add(backButton);

        // Calculate and Display Balance
        calculateBalance();

        setLayout(null);
        setSize(1550, 1080);
        setLocation(0, 0);
        setUndecorated(true);
        setVisible(true);
    }

    private void calculateBalance() {
        double balance = 0.0;

        try (Con con = new Con()) {
            String query = "SELECT type, amount FROM bank WHERE pin = ?";
            PreparedStatement ps = con.connection.prepareStatement(query);
            ps.setString(1, pin);

            ResultSet resultSet = ps.executeQuery();

            // Calculate balance
            while (resultSet.next()) {
                String type = resultSet.getString("type");
                double amount = Double.parseDouble(resultSet.getString("amount"));

                if ("Deposit".equalsIgnoreCase(type)) {
                    balance += amount;
                } else if ("Withdrawal".equalsIgnoreCase(type)) {
                    balance -= amount;
                } else if ("Transfer".equalsIgnoreCase(type)) {
                    // Deduct transfer amount only if this pin initiated the transfer
                    balance -= amount;
                }
            }

            // Display balance
            balanceLabel.setText(String.format("Rs %.2f", balance));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching balance. Please try again.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            setVisible(false);
            new main_Class(pin);
        }
    }

    public static void main(String[] args) {
        new BalanceEnquiry("1234");
    }
}
