import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

class StudentLoginFrame extends JFrame {
    private DataStore store;
    private JFrame parent;

    // Custom JButton to ensure navy background works on macOS
    private static class NavyButton extends JButton {
        public NavyButton(String text) {
            super(text);
            java.awt.Color navy = new java.awt.Color(0, 41, 82);
            java.awt.Font btnFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14);
            setFont(btnFont);
            setBackground(navy);
            setForeground(java.awt.Color.WHITE);
            setOpaque(true);
            setContentAreaFilled(true);
            setBorderPainted(false);
            setMargin(new java.awt.Insets(12, 20, 12, 20));
            setPreferredSize(new java.awt.Dimension(140, 45));
        }
    }

    StudentLoginFrame(DataStore store, JFrame parent) {
        super("Student Login");
        this.store = store;
        this.parent = parent;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // ----- Input Fields Panel -----
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(16, 24, 8, 24));
        p.setBackground(Color.WHITE);

        JLabel idLabel = new JLabel("Student ID:");
        JTextField idField = new JTextField();
        JLabel codeLabel = new JLabel("Access Code:");
        JPasswordField codeField = new JPasswordField();

        idLabel.setAlignmentX(CENTER_ALIGNMENT);
        idField.setAlignmentX(CENTER_ALIGNMENT);
        codeLabel.setAlignmentX(CENTER_ALIGNMENT);
        codeField.setAlignmentX(CENTER_ALIGNMENT);

        p.add(idLabel);
        p.add(Box.createVerticalStrut(6));
        p.add(idField);
        p.add(Box.createVerticalStrut(12));
        p.add(codeLabel);
        p.add(Box.createVerticalStrut(6));
        p.add(codeField);

        add(p, BorderLayout.CENTER);

        // ----- Buttons Panel -----
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.setBorder(new EmptyBorder(0, 24, 16, 24));
        buttons.setBackground(Color.WHITE);

        JButton back = new NavyButton("Back");
        JButton enter = new NavyButton("Enter Quiz");

        Insets buttonInsets = new Insets(20, 20, 20, 20); // smaller for better layout
        back.setMargin(buttonInsets);
        back.setAlignmentX(CENTER_ALIGNMENT);
        back.setMaximumSize(new Dimension(Integer.MAX_VALUE, back.getPreferredSize().height));

        enter.setMargin(buttonInsets);
        enter.setAlignmentX(CENTER_ALIGNMENT);
        enter.setMaximumSize(new Dimension(Integer.MAX_VALUE, enter.getPreferredSize().height));

        buttons.add(back);
        buttons.add(Box.createVerticalStrut(12));
        buttons.add(enter);

        add(buttons, BorderLayout.SOUTH);

        // ----- Button Actions -----
        back.addActionListener(e -> { 
            parent.setVisible(true); 
            dispose(); 
        });

        enter.addActionListener(e -> {
            String sid = idField.getText().trim();
            String code = new String(codeField.getPassword()).trim();

            if (sid.isEmpty()) {
                JOptionPane.showMessageDialog(this,"Please enter Student ID");
                return;
            }

            if (!store.isPublished()) {
                JOptionPane.showMessageDialog(this,"Quiz is not published yet.","Locked", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (!store.getAllowedStudentIds().isEmpty() && !store.getAllowedStudentIds().contains(sid)) {
                JOptionPane.showMessageDialog(this,"Your Student ID is not allowed to take this quiz.","Access Denied", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!code.equals(store.getAccessCode())) {
                JOptionPane.showMessageDialog(this,"Invalid access code","Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            QuizFrame quizFrame = new QuizFrame(store, sid, this, parent);
            setVisible(false);
        });

        setVisible(true);
    }
}