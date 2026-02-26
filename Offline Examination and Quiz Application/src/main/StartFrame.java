import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

class StartFrame extends JFrame {
    DataStore store;

    static abstract class AbstractUser {
        abstract boolean login(String credential);
    }

    StartFrame(DataStore store) {
        super("Offline Examination and Quiz Application");
        this.store = store;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 290);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(java.awt.Color.WHITE);

        // ----- TITLE -----
        JLabel title = new JLabel("Quiz and Examination", SwingConstants.CENTER);
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        title.setForeground(new java.awt.Color(0, 41, 82)); // navy blue (#002952)

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(java.awt.Color.WHITE);
        titlePanel.setBorder(new EmptyBorder(30, 0, 20, 0)); // top 30px, bottom 20px
        titlePanel.add(title, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        // ----- CENTER PANEL WITH BUTTONS -----
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(java.awt.Color.WHITE);
        center.setBorder(new EmptyBorder(0, 200, 50, 200));

        // create buttons
        JButton studentBtn = new JButton("Student Login");
        JButton instrBtn = new JButton("Instructor Login");
        JButton adminBtn = new JButton("Admin Login");

        // button size
        java.awt.Dimension btnSize = new java.awt.Dimension(180, 100);
        for (JButton b : new JButton[]{studentBtn, instrBtn, adminBtn}) {
            b.setMaximumSize(btnSize);
            b.setAlignmentX(CENTER_ALIGNMENT);
        }

        // add vertical glue to center buttons vertically under title
        center.add(Box.createVerticalGlue());
        center.add(studentBtn);
        center.add(Box.createVerticalStrut(15));    // reduced spacing
        center.add(instrBtn);
        center.add(Box.createVerticalStrut(15));
        center.add(adminBtn);
        center.add(Box.createVerticalGlue());

        add(center, BorderLayout.CENTER);

        // button colors and font
        java.awt.Color navy = new java.awt.Color(0, 41, 82);
        java.awt.Font btnFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14);

        JButton[] buttons = {studentBtn, instrBtn, adminBtn};
        for (JButton b : buttons) {
            b.setFont(btnFont);
            b.setBackground(navy);
            b.setForeground(java.awt.Color.WHITE);
            b.setOpaque(true);
            b.setContentAreaFilled(true);
            b.setBorderPainted(false);
            // Thicker top padding for a more prominent top part of the button
            b.setMargin(new java.awt.Insets(10, 10, 10, 10));
        }

        // ----- BUTTON ACTIONS -----
        studentBtn.addActionListener(e -> {
            new StudentLoginFrame(store, this);
            setVisible(false);
        });

        instrBtn.addActionListener(e -> {
            JPanel p = new JPanel(new GridLayout(2,2,6,6));
            JTextField userField = new JTextField();
            JPasswordField passField = new JPasswordField();
            p.add(new JLabel("Username:")); p.add(userField);
            p.add(new JLabel("Password:")); p.add(passField);
            int ok = JOptionPane.showConfirmDialog(this, p, "Instructor Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok == JOptionPane.OK_OPTION) {
                String user = userField.getText().trim();
                String pw = new String(passField.getPassword());
                if (user.isEmpty() || pw.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Enter username and password", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String stored = store.getInstructors().get(user);
                boolean accepted = (stored != null && stored.equals(pw)) || (user.equals("instructor") && pw.equals("instructor"));
                if (accepted) {
                    if (!store.getInstructors().containsKey(user)) {
                        store.getInstructors().put(user, pw);
                        store.save();
                    }
                    new InstructorFrame(store, this, user);
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        adminBtn.addActionListener(e -> {
            JPasswordField pf = new JPasswordField();
            int ok = JOptionPane.showConfirmDialog(this, pf, "Admin Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok == JOptionPane.OK_OPTION) {
                String pw = new String(pf.getPassword());
                AbstractUser admin = new AbstractUser() {
                    @Override
                    boolean login(String credential) {
                        return credential.equals(store.getAdminPassword()) || credential.equals("admin");
                    }
                };
                if (admin.login(pw)) {
                    if(!"admin".equals(store.getAdminPassword())) {
                        store.setAdminPassword("admin");
                        store.save();
                    }
                    new AdminFrame(store,this);
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid admin password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }
}