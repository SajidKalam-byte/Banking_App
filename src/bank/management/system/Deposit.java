package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Deposit extends JFrame implements ActionListener {
    String pin;
    JTextField textField; // Changed to JTextField for consistency

    JButton depositButton, backButton;

    public Deposit(String pin) {
        this.pin = pin;

        // Background image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550, 830, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setBounds(0, 0, 1550, 830);
        add(background);

        // Instruction label
        JLabel instructionLabel = new JLabel("ENTER AMOUNT YOU WANT TO DEPOSIT");
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setFont(new Font("System", Font.BOLD, 16));
        instructionLabel.setBounds(460, 180, 400, 35);
        background.add(instructionLabel);

        // Input text field
        textField = new JTextField();
        textField.setBackground(new Color(65, 125, 128));
        textField.setForeground(Color.WHITE);
        textField.setBounds(460, 230, 320, 25);
        textField.setFont(new Font("Raleway", Font.BOLD, 22));
        background.add(textField);

        // Deposit button
        depositButton = new JButton("DEPOSIT");
        depositButton.setBounds(700, 362, 150, 35);
        depositButton.setBackground(new Color(65, 125, 128));
        depositButton.setForeground(Color.WHITE);
        depositButton.addActionListener(this);
        background.add(depositButton);

        // Back button
        backButton = new JButton("BACK");
        backButton.setBounds(700, 406, 150, 35);
        backButton.setBackground(new Color(65, 125, 128));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(this);
        background.add(backButton);

        // Frame settings
        setLayout(null);
        setSize(1550, 1080);
        setLocation(0, 0);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String amount = textField.getText().trim();

            // Get the current date and time in MySQL-compatible format
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(now);

            if (e.getSource() == depositButton) {
                if (amount.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter the amount you want to deposit.");
                } else if (!amount.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid numeric amount.");
                } else {
                    Con c = new Con();
                    String query = "INSERT INTO bank (pin, date, type, amount) VALUES (?, ?, 'Deposit', ?)";
                    PreparedStatement ps = c.connection.prepareStatement(query);
                    ps.setString(1, pin);
                    ps.setString(2, formattedDate);
                    ps.setString(3, amount);
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Rs. " + amount + " Deposited Successfully.");
                    setVisible(false);
                    new main_Class(pin); // Navigate to the main menu
                }
            } else if (e.getSource() == backButton) {
                setVisible(false);
                new main_Class(pin); // Navigate back to the main menu
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred. Please try again.");
        }
    }

    public static void main(String[] args) {
        new Deposit("");
    }
}
