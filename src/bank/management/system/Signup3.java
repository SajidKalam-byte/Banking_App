package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.PreparedStatement;
import java.util.Random;

public class Signup3 extends JFrame implements ActionListener {

    JRadioButton r1, r2, r3, r4;
    JCheckBox c1, c2, c3, c4, c5, c6, declarationBox;
    JButton submitButton, cancelButton;
    String formNo;

    public Signup3(String formNo) {
        this.formNo = formNo;

        // Frame setup
        setTitle("Account Signup - Step 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(null);

        // Header Panel
        JPanel header = new JPanel();
        header.setBounds(0, 0, 850, 100);
        header.setBackground(new Color(72, 61, 139));
        header.setLayout(null);
        add(header);

        JLabel headerTitle = new JLabel("Step 3: Account Details");
        headerTitle.setFont(new Font("Raleway", Font.BOLD, 28));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setBounds(250, 30, 400, 40);
        header.add(headerTitle);

        // Account Type Section
        JLabel l3 = new JLabel("Select Account Type:");
        l3.setFont(new Font("Raleway", Font.BOLD, 18));
        l3.setBounds(100, 120, 200, 30);
        add(l3);

        r1 = createRadioButton("Saving Account", 100, 160);
        r2 = createRadioButton("Fixed Deposit Account", 350, 160);
        r3 = createRadioButton("Current Account", 100, 200);
        r4 = createRadioButton("Recurring Deposit Account", 350, 200);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(r1);
        buttonGroup.add(r2);
        buttonGroup.add(r3);
        buttonGroup.add(r4);

        // Card Details
        JLabel l4 = new JLabel("Card Number:");
        l4.setFont(new Font("Raleway", Font.BOLD, 18));
        l4.setBounds(100, 260, 200, 30);
        add(l4);

        JLabel cardNumber = new JLabel("XXXX-XXXX-XXXX-XXXX");
        cardNumber.setFont(new Font("Raleway", Font.BOLD, 18));
        cardNumber.setForeground(Color.GRAY);
        cardNumber.setBounds(330, 260, 300, 30);
        add(cardNumber);

        JLabel l5 = new JLabel("(Auto-generated)");
        l5.setFont(new Font("Raleway", Font.PLAIN, 12));
        l5.setBounds(330, 290, 200, 20);
        add(l5);

        JLabel l6 = new JLabel("PIN:");
        l6.setFont(new Font("Raleway", Font.BOLD, 18));
        l6.setBounds(100, 320, 200, 30);
        add(l6);

        JLabel pinNumber = new JLabel("XXXX");
        pinNumber.setFont(new Font("Raleway", Font.BOLD, 18));
        pinNumber.setForeground(Color.GRAY);
        pinNumber.setBounds(330, 320, 200, 30);
        add(pinNumber);

        JLabel l7 = new JLabel("(Auto-generated)");
        l7.setFont(new Font("Raleway", Font.PLAIN, 12));
        l7.setBounds(330, 350, 200, 20);
        add(l7);

        // Services Section
        JLabel l8 = new JLabel("Services Required:");
        l8.setFont(new Font("Raleway", Font.BOLD, 18));
        l8.setBounds(100, 390, 200, 30);
        add(l8);

        c1 = createCheckBox("ATM CARD", 100, 430);
        c2 = createCheckBox("Internet Banking", 350, 430);
        c3 = createCheckBox("Mobile Banking", 100, 470);
        c4 = createCheckBox("EMAIL Alerts", 350, 470);
        c5 = createCheckBox("Cheque Book", 100, 510);
        c6 = createCheckBox("E-Statement", 350, 510);

        // Declaration
        declarationBox = createCheckBox("I declare that all details are correct.", 100, 580);
        declarationBox.setFont(new Font("Raleway", Font.PLAIN, 12));

        // Submit and Cancel Buttons
        submitButton = createButton("Submit", 250, 650);
        cancelButton = createButton("Cancel", 420, 650);

        submitButton.addActionListener(this);
        cancelButton.addActionListener(e -> {
            setVisible(false);
            new Login();
        });

        add(submitButton);
        add(cancelButton);

        setVisible(true);
    }

    private JRadioButton createRadioButton(String text, int x, int y) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(new Font("Raleway", Font.PLAIN, 16));
        radioButton.setBackground(new Color(240, 248, 255));
        radioButton.setBounds(x, y, 250, 30);
        add(radioButton);
        return radioButton;
    }

    private JCheckBox createCheckBox(String text, int x, int y) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(new Font("Raleway", Font.PLAIN, 16));
        checkBox.setBackground(new Color(240, 248, 255));
        checkBox.setBounds(x, y, 200, 30);
        add(checkBox);
        return checkBox;
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(new Font("Raleway", Font.BOLD, 14));
        button.setBackground(new Color(72, 61, 139));
        button.setForeground(Color.WHITE);
        button.setBounds(x, y, 150, 30);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String accountType = null;
        if (r1.isSelected()) accountType = "Saving Account";
        else if (r2.isSelected()) accountType = "Fixed Deposit Account";
        else if (r3.isSelected()) accountType = "Current Account";
        else if (r4.isSelected()) accountType = "Recurring Deposit Account";

        if (!declarationBox.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please agree to the declaration before proceeding.");
            return;
        }

        Random ran = new Random();
        String cardNumber = String.format("%04d%04d%04d%04d", ran.nextInt(10000), ran.nextInt(10000), ran.nextInt(10000), ran.nextInt(10000));
        String pinNumber = String.format("%04d", ran.nextInt(10000));
        String facilities = "";

        if (c1.isSelected()) facilities += "ATM CARD ";
        if (c2.isSelected()) facilities += "Internet Banking ";
        if (c3.isSelected()) facilities += "Mobile Banking ";
        if (c4.isSelected()) facilities += "EMAIL Alerts ";
        if (c5.isSelected()) facilities += "Cheque Book ";
        if (c6.isSelected()) facilities += "E-Statement ";

        try (Con con = new Con()) {
            String hashedPin = BCrypt.hashpw(pinNumber, BCrypt.gensalt());

            // Save to signupthree table
            String signupQuery = "INSERT INTO signupthree (form_no, account_type, card_number, pin, facility) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement signupPs = con.connection.prepareStatement(signupQuery);
            signupPs.setString(1, formNo);
            signupPs.setString(2, accountType);
            signupPs.setString(3, cardNumber);
            signupPs.setString(4, hashedPin);
            signupPs.setString(5, facilities.trim());
            signupPs.executeUpdate();

            // Save to login table
            String loginQuery = "INSERT INTO login (form_no, card_number, pin) VALUES (?, ?, ?)";
            PreparedStatement loginPs = con.connection.prepareStatement(loginQuery);
            loginPs.setString(1, formNo);
            loginPs.setString(2, cardNumber);
            loginPs.setString(3, hashedPin);
            loginPs.executeUpdate();

            JOptionPane.showMessageDialog(this, "Card Number: " + cardNumber + "\nPIN: " + pinNumber);
            setVisible(false);
            new Deposit(pinNumber);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Signup3("1234");
    }
}
