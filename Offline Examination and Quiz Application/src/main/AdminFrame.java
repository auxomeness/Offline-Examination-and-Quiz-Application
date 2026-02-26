import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

class AdminFrame extends JFrame { // this is an inheritance
    private DataStore store;              // this is an encapsulation
    private JFrame parent;                // this is an encapsulation   

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
            setMargin(new java.awt.Insets(10, 10, 10, 10)); // padding
            setPreferredSize(new java.awt.Dimension(150, 35));
        }
    }

    AdminFrame(DataStore store, JFrame parent) {
        super("Admin - Create Instructor Accounts");
        this.store = store;
        this.parent = parent;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(245, 245, 245));
        getContentPane().setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Instructor Management", SwingConstants.CENTER);
        titleLabel.setForeground(new Color(0x00, 0x29, 0x52));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setBorder(new EmptyBorder(15, 0, 15, 0));
        getContentPane().add(titleLabel, BorderLayout.NORTH);

        // List Panel
        DefaultListModel<String> lm = new DefaultListModel<>();
        store.getInstructors().keySet().forEach(lm::addElement);
        JList<String> list = new JList<>(lm);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Instructors"));

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Controls Panel
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
        controls.setBackground(new Color(245, 245, 245));
        controls.setBorder(new EmptyBorder(15, 0, 15, 0));

        JButton add = new NavyButton("Add Instructor");
        JButton delete = new NavyButton("Delete Selected");
        JButton back = new NavyButton("Back");

        add.setAlignmentX(CENTER_ALIGNMENT);
        delete.setAlignmentX(CENTER_ALIGNMENT);
        back.setAlignmentX(CENTER_ALIGNMENT);

        controls.add(add);
        controls.add(Box.createHorizontalStrut(15));
        controls.add(delete);
        controls.add(Box.createHorizontalStrut(15));
        controls.add(back);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        mainPanel.add(listPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(controls);

        Color navy = new Color(0, 41, 82);
        JPanel borderPanel = new JPanel(new BorderLayout());
        borderPanel.setBorder(BorderFactory.createLineBorder(navy, 5));
        borderPanel.setBackground(new Color(245, 245, 245));
        borderPanel.add(mainPanel, BorderLayout.CENTER);

        getContentPane().add(borderPanel, BorderLayout.CENTER);

        // Add Action
        add.addActionListener(e -> {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;

            JTextField userField = new JTextField(15);
            JPasswordField passField = new JPasswordField(15);

            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Username:"), gbc);
            gbc.gridx = 1;
            panel.add(userField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Password:"), gbc);
            gbc.gridx = 1;
            panel.add(passField, gbc);

            int okay = JOptionPane.showConfirmDialog(this, panel, "New Instructor", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (okay == JOptionPane.OK_OPTION) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword());
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill both fields");
                    return;
                }
                store.getInstructors().put(username, password);
                store.save();
                lm.addElement(username);
            }
        });

        // Delete Action
        delete.addActionListener(e -> {
            String sel = list.getSelectedValue();
            if (sel == null) {
                JOptionPane.showMessageDialog(this, "Select an instructor");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Delete instructor " + sel + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                store.getInstructors().remove(sel);
                store.save();
                lm.removeElement(sel);
            }
        });

        // Back Action
        back.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public DataStore getStore() {
        return store;
    }

    public JFrame getParent() {
        return parent;
    }
}