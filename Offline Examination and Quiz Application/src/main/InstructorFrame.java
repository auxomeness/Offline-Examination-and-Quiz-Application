import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

class InstructorFrame extends JFrame {          // inheritance
    private DataStore store;
    private JFrame parent;
    private String instructorUser;

    private static class NavyButton extends JButton {
        private static final java.awt.Color NAVY = new java.awt.Color(0x00, 0x29, 0x52);

        public NavyButton(String text) {
            super(text);
            setForeground(java.awt.Color.WHITE);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(8,15,8,15));
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            g.setColor(NAVY);
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    InstructorFrame(DataStore store, JFrame parent, String instructorUser) {
        super("Instructor Dashboard | " + instructorUser);
        this.store = store;                     // encapsulation
        this.parent = parent;                   // encapsulation
        this.instructorUser = instructorUser;   // encapsulation

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900,560);
        setLocationRelativeTo(null);

        // Set main content pane background
        getContentPane().setBackground(new Color(245,245,245));
        setLayout(new BorderLayout());

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(245,245,245));
        JLabel titleLabel = new JLabel("Instructor Dashboard | " + instructorUser, SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titleLabel.setForeground(new Color(0x00, 0x29, 0x52));
        titleLabel.setBorder(new EmptyBorder(15,0,15,0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        // Main panel with padding and navy border
        JPanel mainPanel = new JPanel(new BorderLayout(8,8));
        mainPanel.setBorder(new LineBorder(new Color(0x00, 0x29, 0x52), 5));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(10,20,10,20));
        mainPanel.setLayout(new BorderLayout(8,8));

        JTabbedPane tabs = new JTabbedPane();
        ManageQuestionsPanel manageQuestion = new ManageQuestionsPanel(store);
        ManageAccessPanel manageAccessPanel = new ManageAccessPanel(store);
        tabs.addTab("Questions", manageQuestion);
        tabs.addTab("Access & Results", manageAccessPanel);
        mainPanel.add(tabs, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JButton saveBtn = new NavyButton("Save");
        JButton publishBtn = new NavyButton(store.isPublished() ? "Unpublish" : "Publish");
        JButton backBtn = new NavyButton("Back");
        bottom.add(saveBtn);
        bottom.add(Box.createHorizontalStrut(15));
        bottom.add(publishBtn);
        bottom.add(Box.createHorizontalStrut(15));
        bottom.add(backBtn);
        mainPanel.add(bottom, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        saveBtn.addActionListener(e -> { store.save(); JOptionPane.showMessageDialog(this,"Saved"); });
        publishBtn.addActionListener(e -> {
            store.setPublished(!store.isPublished());
            publishBtn.setText(store.isPublished() ? "Unpublish" : "Publish");
            store.save();
            JOptionPane.showMessageDialog(this, store.isPublished() ? "Quiz published" : "Quiz unpublished");
        });
        backBtn.addActionListener(e -> { parent.setVisible(true); dispose(); });

        setVisible(true);
    }

    public DataStore getStore() {
        return store;
    }

    public JFrame getParent() {
        return parent;
    }

    public String getInstructorUser() {
        return instructorUser;
    }
}