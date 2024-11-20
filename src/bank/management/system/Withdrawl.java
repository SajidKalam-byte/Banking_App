package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Withdrawl extends JFrame implements ActionListener {

    String pin;
    JTextField textField;
    JButton withdrawButton, backButton;

    public Withdrawl(String pin) {
        this.pin = pin;

        // Background Setup
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550, 830, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setBounds(0, 0, 1550, 830);
        add(background);

        JLabel label1 = new JLabel("MAXIMUM WITHDRAWAL IS RS.10,000");
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("System", Font.BOLD, 16));
        label1.setBounds(460, 180, 700, 35);
        background.add(label1);

        JLabel label2 = new JLabel("PLEASE ENTER YOUR AMOUNT");
        label2.setForeground(Color.WHITE);
        label2.setFont(new Font("System", Font.BOLD, 16));
        label2.setBounds(460, 220, 400, 35);
        background.add(label2);

        textField = new JTextField();
        textField.setBackground(new Color(65, 125, 128));
        textField.setForeground(Color.WHITE);
        textField.setBounds(460, 260, 320, 25);
        textField.setFont(new Font("Raleway", Font.BOLD, 22));
        background.add(textField);

        withdrawButton = new JButton("WITHDRAW");
        withdrawButton.setBounds(700, 362, 150, 35);
        withdrawButton.setBackground(new Color(65, 125, 128));
        withdrawButton.setForeground(Color.WHITE);
        withdrawButton.addActionListener(this);
        background.add(withdrawButton);

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
        if (e.getSource() == withdrawButton) {
            try {
                String amount = textField.getText().trim();
                if (amount.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter the Amount you want to withdraw");
                    return;
                }

                double withdrawAmount = Double.parseDouble(amount);
                if (withdrawAmount <= 0 || withdrawAmount > 10000) {
                    JOptionPane.showMessageDialog(null, "Invalid amount. Please enter an amount between Rs. 1 and Rs. 10,000.");
                    return;
                }

                try (Con con = new Con()) {
                    // Fetch current balance
                    double balance = 0.0;
                    String balanceQuery = "SELECT type, amount FROM bank WHERE pin = ?";
                    PreparedStatement balanceStmt = con.connection.prepareStatement(balanceQuery);
                    balanceStmt.setString(1, pin);

                    ResultSet resultSet = balanceStmt.executeQuery();
                    while (resultSet.next()) {
                        String type = resultSet.getString("type");
                        double amt = Double.parseDouble(resultSet.getString("amount"));
                        if ("Deposit".equalsIgnoreCase(type)) balance += amt;
                        else if ("Withdrawal".equalsIgnoreCase(type)) balance -= amt;
                    }

                    if (balance < withdrawAmount) {
                        JOptionPane.showMessageDialog(null, "Insufficient Balance.");
                        return;
                    }

                    // Format date to MySQL-compatible format
                    Date now = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = sdf.format(now);

                    // Perform withdrawal
                    String insertQuery = "INSERT INTO bank (pin, date, type, amount) VALUES (?, ?, ?, ?)";
                    PreparedStatement insertStmt = con.connection.prepareStatement(insertQuery);
                    insertStmt.setString(1, pin);
                    insertStmt.setString(2, formattedDate); // Use formatted date
                    insertStmt.setString(3, "Withdrawal");
                    insertStmt.setString(4, String.valueOf(withdrawAmount));
                    insertStmt.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Rs. " + withdrawAmount + " Debited Successfully.");
                    setVisible(false);
                    new main_Class(pin);
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred. Please try again.");
            }
        } else if (e.getSource() == backButton) {
            setVisible(false);
            new main_Class(pin);
        }
    }

    public static void main(String[] args) {
        new Withdrawl("1234");
    }
}
