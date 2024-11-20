package bank.management.system;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.util.Random;

public class Signup extends JFrame implements ActionListener {

    JRadioButton r1, r2;
    JButton next;
    JTextField textName, textFname, textPhone;
    JDateChooser dateChooser;
    String formNo;

    public Signup() {
        super("Application Form");

        // Generate a random form number
        Random ran = new Random();
        long first4 = (ran.nextLong() % 9000L) + 1000L;
        formNo = String.valueOf(Math.abs(first4));

        // Frame setup
        setLayout(null);
        setSize(850, 550); // Increased height for new phone field
        setLocation(360, 40);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBounds(0, 0, 850, 100);
        headerPanel.setBackground(new Color(72, 61, 139));
        add(headerPanel);

        JLabel headerTitle = new JLabel("Application Form No. " + formNo);
        headerTitle.setBounds(200, 30, 500, 40);
        headerTitle.setFont(new Font("Raleway", Font.BOLD, 28));
        headerTitle.setForeground(Color.WHITE);
        headerPanel.add(headerTitle);

        // Name Label and TextField
        JLabel labelName = createLabel("Name:", 100, 150, 150, 30);
        add(labelName);

        textName = createTextField(300, 150, 400, 30);
        add(textName);

        // Father's Name Label and TextField
        JLabel labelfName = createLabel("Father's Name:", 100, 200, 150, 30);
        add(labelfName);

        textFname = createTextField(300, 200, 400, 30);
        add(textFname);

        // Gender Label and Radio Buttons
        JLabel labelGender = createLabel("Gender:", 100, 250, 150, 30);
        add(labelGender);

        r1 = createRadioButton("Male", 300, 250, 100, 30);
        r2 = createRadioButton("Female", 450, 250, 100, 30);
        add(r1);
        add(r2);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(r1);
        genderGroup.add(r2);

        // Date of Birth Label and DateChooser
        JLabel labelDob = createLabel("Date of Birth:", 100, 300, 150, 30);
        add(labelDob);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(300, 300, 400, 30);
        dateChooser.setFont(new Font("Raleway", Font.PLAIN, 14));
        dateChooser.setForeground(Color.BLACK);
        add(dateChooser);

        // Phone Number Label and TextField
        JLabel labelPhone = createLabel("Phone Number:", 100, 350, 150, 30);
        add(labelPhone);

        textPhone = createTextField(300, 350, 400, 30);
        add(textPhone);

        // Next Button
        next = createButton("Next", 300, 450, 150, 40);
        add(next);

        // Add action listener to the Next button
        next.addActionListener(this);

        // Display the frame
        setUndecorated(true);
        setVisible(true);
    }

    private JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Raleway", Font.BOLD, 18));
        label.setBounds(x, y, width, height);
        return label;
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Raleway", Font.PLAIN, 16));
        textField.setBounds(x, y, width, height);
        return textField;
    }

    private JRadioButton createRadioButton(String text, int x, int y, int width, int height) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(new Font("Raleway", Font.PLAIN, 16));
        radioButton.setBackground(new Color(240, 248, 255));
        radioButton.setBounds(x, y, width, height);
        return radioButton;
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Raleway", Font.BOLD, 16));
        button.setBackground(new Color(72, 61, 139));
        button.setForeground(Color.WHITE);
        button.setBounds(x, y, width, height);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = textName.getText();
        String fname = textFname.getText();
        String dob = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText();
        String gender = r1.isSelected() ? "Male" : r2.isSelected() ? "Female" : null;
        String phone = textPhone.getText();

        if (name.isEmpty() || fname.isEmpty() || dob.isEmpty() || gender == null || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits.");
            return;
        }

        try {
            // Convert date to MySQL format
            java.util.Date date = dateChooser.getDate();
            if (date == null) {
                JOptionPane.showMessageDialog(this, "Invalid date. Please select a valid date of birth.");
                return;
            }
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String formattedDob = sdf.format(date);

            // Insert into database
            Con con = new Con();
            String query = "INSERT INTO signup (form_no, name, father_name, dob, gender, phone) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.connection.prepareStatement(query);
            ps.setString(1, formNo);
            ps.setString(2, name);
            ps.setString(3, fname);
            ps.setString(4, formattedDob); // Use formatted date
            ps.setString(5, gender);
            ps.setString(6, phone);
            ps.executeUpdate();

            new Signup3(formNo);
            setVisible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred. Please try again.");
        }
    }

    public static void main(String[] args) {
        new Signup();
    }
}
