import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

class ManageQuestionsPanel extends JPanel {                                             //inheritance
    private final DataStore store;                                                      //encapsulation
    private final DefaultListModel<Question> listModel = new DefaultListModel<>();      //encapsulation
    private final JList<Question> questionList = new JList<>(listModel);                //encapsulation

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
            setMargin(new java.awt.Insets(10, 10, 10, 10));
            setPreferredSize(new java.awt.Dimension(100, 35));
        }
    }

    ManageQuestionsPanel(DataStore store) {
        super(new BorderLayout());
        this.store = store;                                            //encapsulation
        setBackground(new Color(245, 245, 245));

        for (Question q : store.getQuestions()) listModel.addElement(q);

        questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questionList.setCellRenderer(new DefaultListCellRenderer(){          
            @Override                                                           //polymorphism
            public Component getListCellRendererComponent(JList<?> l, Object val, int idx, boolean sel, boolean focus) {
                JLabel lab = (JLabel) super.getListCellRendererComponent(l, ((Question)val).getText(), idx, sel, focus);
                lab.setToolTipText(((Question)val).getText());
                return lab;
            }
        });

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        listPanel.add(new JScrollPane(questionList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton addBtn = new NavyButton("Add");
        JButton editBtn = new NavyButton("Edit");
        JButton delBtn = new NavyButton("Delete");
        JButton setCodeBtn = new NavyButton("Set Access Code");

        buttonPanel.add(addBtn);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(editBtn);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(delBtn);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(setCodeBtn);

        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(new LineBorder(new Color(0x00, 0x29, 0x52), 5));
        mainPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            mainPanel.getBorder(),
            new EmptyBorder(12, 12, 12, 12)
        ));

        mainPanel.add(listPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            QuestionEditorDialog dlg = new QuestionEditorDialog(null);
            if (dlg.getResult() != null) {
                listModel.addElement(dlg.getResult());
                store.getQuestions().add(dlg.getResult());
            }
        });

        editBtn.addActionListener(e -> {
            Question sel = questionList.getSelectedValue();
            int idx = questionList.getSelectedIndex();
            if (sel == null) { JOptionPane.showMessageDialog(this,"Select a question first"); return; }
            QuestionEditorDialog dlg = new QuestionEditorDialog(sel);
            if (dlg.getResult() != null) {
                listModel.set(idx, dlg.getResult());
                store.getQuestions().set(idx, dlg.getResult());
            }
        });

        delBtn.addActionListener(e -> {
            int idx = questionList.getSelectedIndex();
            if (idx == -1) { JOptionPane.showMessageDialog(this,"Select a question first"); return; }
            if (JOptionPane.showConfirmDialog(this,"Delete selected question?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                store.getQuestions().remove(idx);
                listModel.remove(idx);
            }
        });

        setCodeBtn.addActionListener(e -> {
            // use masked input
            JPasswordField pf = new JPasswordField(store.getAccessCode());
            int ok = JOptionPane.showConfirmDialog(this, pf, "Set Access Code (masked)", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok == JOptionPane.OK_OPTION) {
                store.setAccessCode(new String(pf.getPassword()).trim());
                store.save();
                JOptionPane.showMessageDialog(this,"Access code updated");
            }
        });
    }
}