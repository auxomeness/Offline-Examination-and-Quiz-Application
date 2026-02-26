import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

class ManageAccessPanel extends JPanel {                                                //inheritance
    private final DataStore store;                                                      //encapsulation
    private final DefaultListModel<String> allowedModel = new DefaultListModel<>();     //encapsulation
    private final DefaultListModel<String> resultsModel = new DefaultListModel<>();     //encapsulation
    private final JList<String> allowedList = new JList<>(allowedModel);                //encapsulation
    private final JList<String> resultsList = new JList<>(resultsModel);                //encapsulation

    private static class NavyButton extends JButton {
        public NavyButton(String text) {
            super(text);
            java.awt.Color navy = new java.awt.Color(0, 41, 82);
            java.awt.Font btnFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12);
            setFont(btnFont);
            setBackground(navy);
            setForeground(java.awt.Color.WHITE);
            setOpaque(true);
            setContentAreaFilled(true);
            setBorderPainted(false);
            setMargin(new java.awt.Insets(12, 20, 12, 20)); // top, left, bottom, right padding
            setPreferredSize(new java.awt.Dimension(200, 35)); // width, height
        }
    }

    ManageAccessPanel(DataStore store) {
        super(new BorderLayout(8,8));
        this.store = store;                                            //encapsulation
        setBorder(new EmptyBorder(12,12,12,12));

        JPanel left = new JPanel(new BorderLayout(6,6));
        left.add(new JLabel("Allowed Student IDs"), BorderLayout.NORTH);
        for (String s : store.getAllowedStudentIds()) allowedModel.addElement(s);
        left.add(new JScrollPane(allowedList), BorderLayout.CENTER);
        JPanel lactions = new JPanel();
        JButton aAdd = new NavyButton("Add ID");
        JButton aDel = new NavyButton("Remove Selected");
        lactions.add(aAdd); lactions.add(aDel);
        left.add(lactions, BorderLayout.SOUTH);

        JPanel right = new JPanel(new BorderLayout(6,6));
        right.add(new JLabel("Results"), BorderLayout.NORTH);
        refreshResultsModel();
        right.add(new JScrollPane(resultsList), BorderLayout.CENTER);
        JPanel ractions = new JPanel();
        JButton rRefresh = new NavyButton("Refresh");
        JButton rDel = new NavyButton("Remove Selected");
        ractions.add(rRefresh); ractions.add(rDel);
        right.add(ractions, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.45);
        add(split, BorderLayout.CENTER);

        aAdd.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "Enter Student ID to allow:");
            if (id != null && !id.trim().isEmpty()) {
                store.getAllowedStudentIds().add(id.trim());
                allowedModel.addElement(id.trim());
                store.save();
            }
        });
        aDel.addActionListener(e -> {
            String sel = allowedList.getSelectedValue();
            if (sel == null) { JOptionPane.showMessageDialog(this,"Select an ID"); return; }
            store.getAllowedStudentIds().remove(sel);
            allowedModel.removeElement(sel);
            store.save();
        });

        rRefresh.addActionListener(e -> refreshResultsModel());
        rDel.addActionListener(e -> {
            int idx = resultsList.getSelectedIndex();
            if (idx == -1) { JOptionPane.showMessageDialog(this,"Select a result"); return; }
            if (JOptionPane.showConfirmDialog(this,"Remove selected result?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                store.getResults().remove(idx);
                store.save();
                refreshResultsModel();
            }
        });
    }

    private void refreshResultsModel() {
        resultsModel.clear();
        for (Result r : store.getResults()) resultsModel.addElement(r.toString());
    }
}