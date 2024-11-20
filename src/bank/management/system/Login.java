package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

public class Login extends JFrame implements ActionListener {
    JTextField cardField;
    JPasswordField pinField;
    JButton loginButton, clearButton, signupButton;

    public Login() {
        super("Bank Management System");

        // UI Setup (Simplified for illustration)
        setLayout(null);

        JLabel cardLabel = new JLabel("Card No:");
        cardLabel.setBounds(100, 100, 100, 30);
        add(cardLabel);

        cardField = new JTextField();
        cardField.setBounds(200, 100, 200, 30);
        add(cardField);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setBounds(100, 150, 100, 30);
        add(pinLabel);

        pinField = new JPasswordField();
        pinField.setBounds(200, 150, 200, 30);
        add(pinField);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 200, 100, 30);
        loginButton.addActionListener(this);
        add(loginButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(210, 200, 100, 30);
        clearButton.addActionListener(this);
        add(clearButton);

        signupButton = new JButton("Signup");
        signupButton.setBounds(320, 200, 100, 30);
        signupButton.addActionListener(this);
        add(signupButton);

        setSize(500, 400);
        setLocation(300, 200);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String enteredPin = new String(pinField.getPassword());
            try (Con con = new Con()) {
                String query = "SELECT pin FROM login WHERE card_number = ?";
                PreparedStatement ps = con.connection.prepareStatement(query);
                ps.setString(1, cardField.getText());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String storedHashedPin = rs.getString("pin");
                    if (BCrypt.checkpw(enteredPin, storedHashedPin)) {
                        JOptionPane.showMessageDialog(null, "Login Successful");
                        setVisible(false);
                        new main_Class(enteredPin); // Pass to the next class
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid PIN");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Card number not found");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred. Please try again.");
            }
        } else if (e.getSource() == clearButton) {
            cardField.setText("");
            pinField.setText("");
        } else if (e.getSource() == signupButton) { // Handle Signup Button
            setVisible(false); // Hide the current login frame
            new Signup(); // Open the Signup form
        }
    }


    public static void main(String[] args) {
        new Login();
    }
}
