package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Withdrawl extends JFrame implements ActionListener {

    String pin;
    TextField textField;
    JButton b1, b2;

    Withdrawl(String pin) {
        this.pin = pin;

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/atm2.png"));
        Image i2 = i1.getImage().getScaledInstance(1550, 830, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l3 = new JLabel(i3);
        l3.setBounds(0, 0, 1550, 830);
        add(l3);

        JLabel label1 = new JLabel("MAXIMUM WITHDRAWAL IS RS.10,000");
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("System", Font.BOLD, 16));
        label1.setBounds(460, 180, 700, 35);
        l3.add(label1);

        JLabel label2 = new JLabel("PLEASE ENTER YOUR AMOUNT");
        label2.setForeground(Color.WHITE);
        label2.setFont(new Font("System", Font.BOLD, 16));
        label2.setBounds(460, 220, 400, 35);
        l3.add(label2);

        textField = new TextField();
        textField.setBackground(new Color(65, 125, 128));
        textField.setForeground(Color.WHITE);
        textField.setBounds(460, 260, 320, 25);
        textField.setFont(new Font("Raleway", Font.BOLD, 22));
        l3.add(textField);

        b1 = new JButton("WITHDRAW");
        b1.setBounds(700, 362, 150, 35);
        b1.setBackground(new Color(65, 125, 128));
        b1.setForeground(Color.WHITE);
        b1.addActionListener(this);
        l3.add(b1);

        b2 = new JButton("BACK");
        b2.setBounds(700, 406, 150, 35);
        b2.setBackground(new Color(65, 125, 128));
        b2.setForeground(Color.WHITE);
        b2.addActionListener(this);
        l3.add(b2);

        setLayout(null);
        setSize(1550, 1080);
        setLocation(0, 0);
        setVisible(true);
    }

    @Override

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            try {
                String amountText = textField.getText().trim();

                if (amountText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter the Amount you want to withdraw");
                    return;
                }

                double withdrawalAmount;
                try {
                    withdrawalAmount = Double.parseDouble(amountText);
                    if (withdrawalAmount <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid positive amount.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid amount entered. Please enter a numeric value.");
                    return;
                }

                if (withdrawalAmount > 10000) {
                    JOptionPane.showMessageDialog(null, "You can withdraw a maximum of Rs.10,000 at a time.");
                    return;
                }

                // Database Connection
                Connn c = new Connn();
                ResultSet resultSet = c.statement.executeQuery("SELECT * FROM bank WHERE pin = '" + pin + "'");

                double balance = 0.0;
                while (resultSet.next()) {
                    String transactionType = resultSet.getString("transaction_type");
                    double transactionAmount = resultSet.getDouble("amount"); // ✅ Use getDouble() for decimal type

                    if ("Deposit".equalsIgnoreCase(transactionType)) {
                        balance += transactionAmount;
                    } else if ("Withdraw".equalsIgnoreCase(transactionType)) {
                        balance -= transactionAmount;
                    }
                }

                System.out.println("Current Balance: " + balance);

                if (balance < withdrawalAmount) {
                    JOptionPane.showMessageDialog(null, "Insufficient Balance");
                    return;
                }

                // ✅ **Corrected SQL Query (Removed transaction_date, Used DECIMAL for amount)**
                String sql = "INSERT INTO bank (pin, transaction_type, amount) VALUES ('"
                        + pin + "', 'Withdraw', " + withdrawalAmount + ")";

                System.out.println("Executing Query: " + sql);

                c.statement.executeUpdate(sql);

                JOptionPane.showMessageDialog(null, "Rs. " + withdrawalAmount + " Debited Successfully");

                setVisible(false);
                new main_Class(pin);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        } else if (e.getSource() == b2) {
            setVisible(false);
            new main_Class(pin);
        }
    }


    public static void main(String[] args) {
        new Withdrawl("");
    }
}
