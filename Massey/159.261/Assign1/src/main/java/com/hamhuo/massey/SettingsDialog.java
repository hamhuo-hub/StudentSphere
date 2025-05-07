package com.hamhuo.massey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsDialog extends JDialog {
    private JTextField lengthField;
    private JTextField colorField;
    private JTextField speedField;
    private boolean confirmed = false;
    private int initialLength;
    private String grassColor;
    private double snakeSpeed;

    public SettingsDialog(JFrame parent) {
        super(parent, "Game Settings", true);
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(1000, 200);
        setLocationRelativeTo(parent);

        // 初始长度
        add(new JLabel("Snake Length (3-20):"));
        lengthField = new JTextField("3");
        add(lengthField);

        // 蛇的颜色
        add(new JLabel("Grass Color (e.g., #E1EEBC):"));
        colorField = new JTextField("#328E6E");
        add(colorField);

        // 移动速度
        add(new JLabel("Snake Speed (0-10) bigger is faster:"));
        speedField = new JTextField("10");
        add(speedField);

        // 按钮
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInputs()) {
                    confirmed = true;
                    setVisible(false);
                }
            }
        });
        add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                setVisible(false);
            }
        });
        add(cancelButton);
    }

    private boolean validateInputs() {
        try {
            initialLength = Integer.parseInt(lengthField.getText());
            if (initialLength < 3 || initialLength > 20) {
                JOptionPane.showMessageDialog(this, "Length must be between 3 and 20.");
                return false;
            }

            grassColor = colorField.getText();

            snakeSpeed = Double.parseDouble(speedField.getText());
            return true;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            return false;
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public int getInitialLength() {
        return initialLength;
    }

    public String getGrassColor() {
        return grassColor;
    }

    public double getSnakeSpeed() {
        return snakeSpeed;
    }
}