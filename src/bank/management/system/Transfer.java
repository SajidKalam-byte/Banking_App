package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transfer extends JFrame implements ActionListener {

    JTextField recipientField, amountField;
    JButton transferButton, backButton;
    String senderPin;

    public Transfer(String senderPin) {
        this.senderPin = senderPin;

        // Frame Setup
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550, 830, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setBounds(0, 0, 1550, 830);
        add(background);

        JLabel label1 = new JLabel("TRANSFER FUNDS");
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("System", Font.BOLD, 22));
        label1.setBounds(460, 180, 400, 35);
        background.add(label1);

        JLabel recipientLabel = new JLabel("Enter Recipient's PIN:");
        recipientLabel.setForeground(Color.WHITE);
        recipientLabel.setFont(new Font("System", Font.BOLD, 16));
        recipientLabel.setBounds(460, 230, 200, 25);
        background.add(recipientLabel);

        recipientField = new JTextField();
        recipientField.setBackground(new Color(65, 125, 128));
        recipientField.setForeground(Color.WHITE);
        recipientField.setBounds(700, 230, 200, 25);
        recipientField.setFont(new Font("Raleway", Font.BOLD, 16));
        background.add(recipientField);

        JLabel amountLabel = new JLabel("Enter Amount:");
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setFont(new Font("System", Font.BOLD, 16));
        amountLabel.setBounds(460, 280, 200, 25);
        background.add(amountLabel);

        amountField = new JTextField();
        amountField.setBackground(new Color(65, 125, 128));
        amountField.setForeground(Color.WHITE);
        amountField.setBounds(700, 280, 200, 25);
        amountField.setFont(new Font("Raleway", Font.BOLD, 16));
        background.add(amountField);

        transferButton = new JButton("TRANSFER");
        transferButton.setBounds(700, 362, 150, 35);
        transferButton.setBackground(new Color(65, 125, 128));
        transferButton.setForeground(Color.WHITE);
        transferButton.addActionListener(this);
        background.add(transferButton);

        backButton = new JButton("BACK");
        backButton.setBounds(700, 406, 150, 35);
        backButton.setBackground(new Color(65, 125, 128));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(this);
        background.add(backButton);

        setLayout(null);
        setSize(1550, 1080);
        setLocation(0, 0);
        setUndecorated(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            setVisible(false);
            new main_Class(senderPin);
        } else if (e.getSource() == transferButton) {
            String recipientPin = recipientField.getText().trim();
            String amountStr = amountField.getText().trim();

            if (recipientPin.isEmpty() || amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                return;
            }

            try {
                double transferAmount = Double.parseDouble(amountStr);

                if (transferAmount <= 0) {
                    JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
                    return;
                }

                try (Con con = new Con()) {
                    // Fetch sender's current balance
                    double senderBalance = 0.0;
                    String balanceQuery = "SELECT type, amount FROM bank WHERE pin = ?";
                    PreparedStatement balanceStmt = con.connection.prepareStatement(balanceQuery);
                    balanceStmt.setString(1, senderPin);

                    ResultSet rs = balanceStmt.executeQuery();
                    while (rs.next()) {
                        String type = rs.getString("type");
                        double amt = Double.parseDouble(rs.getString("amount"));
                        if ("Deposit".equalsIgnoreCase(type)) senderBalance += amt;
                        else if ("Withdrawal".equalsIgnoreCase(type)) senderBalance -= amt;
                    }

                    // Check if sender has enough balance
                    if (senderBalance < transferAmount) {
                        JOptionPane.showMessageDialog(null, "Insufficient Balance.");
                        return;
                    }

                    // Verify recipient PIN exists
                    String recipientCheckQuery = "SELECT COUNT(*) AS count FROM bank WHERE pin = ?";
                    PreparedStatement recipientCheckStmt = con.connection.prepareStatement(recipientCheckQuery);
                    recipientCheckStmt.setString(1, recipientPin);

                    ResultSet recipientResult = recipientCheckStmt.executeQuery();
                    if (recipientResult.next() && recipientResult.getInt("count") == 0) {
                        JOptionPane.showMessageDialog(null, "Recipient PIN does not exist.");
                        return;
                    }

                    // Format date to MySQL-compatible format
                    Date now = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = sdf.format(now);

                    // Deduct amount from sender
                    String debitQuery = "INSERT INTO bank (pin, date, type, amount) VALUES (?, ?, ?, ?)";
                    PreparedStatement debitStmt = con.connection.prepareStatement(debitQuery);
                    debitStmt.setString(1, senderPin);
                    debitStmt.setString(2, formattedDate);
                    debitStmt.setString(3, "Withdrawal");
                    debitStmt.setString(4, String.valueOf(transferAmount));
                    debitStmt.executeUpdate();

                    // Credit amount to recipient
                    String creditQuery = "INSERT INTO bank (pin, date, type, amount) VALUES (?, ?, ?, ?)";
                    PreparedStatement creditStmt = con.connection.prepareStatement(creditQuery);
                    creditStmt.setString(1, recipientPin);
                    creditStmt.setString(2, formattedDate);
                    creditStmt.setString(3, "Deposit");
                    creditStmt.setString(4, String.valueOf(transferAmount));
                    creditStmt.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Rs. " + transferAmount + " Transferred Successfully.");
                    setVisible(false);
                    new main_Class(senderPin);
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        new Transfer("1234");
    }
}
