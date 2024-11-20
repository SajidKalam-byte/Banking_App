package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MiniStatement extends JFrame implements ActionListener {

    String pin;
    JButton backButton;

    public MiniStatement(String pin) {
        this.pin = pin;

        // Background Image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550, 830, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setBounds(0, 0, 1550, 830);
        add(background);

        JLabel miniStatementTitle = new JLabel("Mini Statement");
        miniStatementTitle.setForeground(Color.WHITE);
        miniStatementTitle.setFont(new Font("System", Font.BOLD, 18));
        miniStatementTitle.setBounds(430, 180, 700, 35);
        background.add(miniStatementTitle);

        JTextArea statementArea = new JTextArea();
        statementArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statementArea.setBounds(200, 220, 900, 400);
        background.add(statementArea);

        // Fetch transactions
        fetchTransactions(statementArea);

        // Back Button
        backButton = new JButton("Back");
        backButton.setBounds(700, 640, 150, 35);
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

    private void fetchTransactions(JTextArea statementArea) {
        try (Con con = new Con()) {
            String query = "SELECT date, type, amount FROM bank WHERE pin = ?";
            PreparedStatement ps = con.connection.prepareStatement(query);
            ps.setString(1, pin);

            ResultSet rs = ps.executeQuery();
            StringBuilder statement = new StringBuilder("Date\t\tType\t\tAmount\n");
            statement.append("----------------------------------------------------\n");

            while (rs.next()) {
                statement.append(rs.getString("date"))
                        .append("\t")
                        .append(rs.getString("type"))
                        .append("\t")
                        .append(rs.getString("amount"))
                        .append("\n");
            }

            statementArea.setText(statement.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching transactions.");
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
        new MiniStatement("1234");
    }
}
