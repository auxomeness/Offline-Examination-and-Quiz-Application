import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

class QuestionEditorDialog extends JDialog {                    //inheritance
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
    private Question result = null;                                    //encapsulation
    private JTextField qField;                                           //encapsulation
    private JTextField opt0;                                             //encapsulation
    private JTextField opt1;                                             //encapsulation
    private JTextField opt2;                                             //encapsulation
    private JTextField opt3;                                             //encapsulation
    private JComboBox<String> ansBox;      //encapsulation
    private JTextArea fb;                               //encapsulation    

    QuestionEditorDialog(Question existing) {
        super((Frame)null, true);
        setTitle(existing == null ? "Add Question" : "Edit Question");
        setSize(620,420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8,8));

        JPanel main = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        qField = new JTextField();
        opt0 = new JTextField();
        opt1 = new JTextField();
        opt2 = new JTextField();
        opt3 = new JTextField();
        ansBox = new JComboBox<>(new String[]{"0","1","2","3"});
        fb = new JTextArea(4,40);
        fb.setLineWrap(true);
        fb.setWrapStyleWord(true);

        if (existing != null) {
            qField.setText(existing.getText());
            if (existing.getOptions().size() > 0) opt0.setText(existing.getOptions().get(0));
            if (existing.getOptions().size() > 1) opt1.setText(existing.getOptions().get(1));
            if (existing.getOptions().size() > 2) opt2.setText(existing.getOptions().get(2));
            if (existing.getOptions().size() > 3) opt3.setText(existing.getOptions().get(3));
            ansBox.setSelectedIndex(existing.getAnswerIndex());
            fb.setText(existing.getFeedback());
        }

        gc.gridx=0; gc.gridy=0; main.add(new JLabel("Question:"), gc);
        gc.gridx=1; gc.gridy=0; main.add(qField, gc);

        gc.gridx=0; gc.gridy=1; main.add(new JLabel("Option 1:"), gc);
        gc.gridx=1; gc.gridy=1; main.add(opt0, gc);

        gc.gridx=0; gc.gridy=2; main.add(new JLabel("Option 2:"), gc);
        gc.gridx=1; gc.gridy=2; main.add(opt1, gc);

        gc.gridx=0; gc.gridy=3; main.add(new JLabel("Option 3:"), gc);
        gc.gridx=1; gc.gridy=3; main.add(opt2, gc);

        gc.gridx=0; gc.gridy=4; main.add(new JLabel("Option 4:"), gc);
        gc.gridx=1; gc.gridy=4; main.add(opt3, gc);

        gc.gridx=0; gc.gridy=5; main.add(new JLabel("Answer index (0..3):"), gc);
        gc.gridx=1; gc.gridy=5; main.add(ansBox, gc);

        gc.gridx=0; gc.gridy=6; gc.anchor = GridBagConstraints.NORTH;
        main.add(new JLabel("Feedback:"), gc);
        gc.gridx=1; gc.gridy=6; main.add(new JScrollPane(fb), gc);

        add(main, BorderLayout.CENTER);

        JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.X_AXIS));
        b.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton ok = new NavyButton("OK");
        JButton cancel = new NavyButton("Cancel");

        // Set preferred size to ensure full text visibility
        ok.setPreferredSize(new java.awt.Dimension(100, 35));
        cancel.setPreferredSize(new java.awt.Dimension(200, 35));

        // Center buttons with spacing
        b.add(Box.createHorizontalGlue());
        b.add(cancel);
        b.add(Box.createHorizontalStrut(15));
        b.add(ok);
        b.add(Box.createHorizontalGlue());

        add(b, BorderLayout.SOUTH);

        ok.addActionListener(e -> {
            String qText = qField.getText().trim();
            List<String> opts = Arrays.asList(opt0.getText().trim(), opt1.getText().trim(), opt2.getText().trim(), opt3.getText().trim());
            int ai = ansBox.getSelectedIndex();
            String fback = fb.getText().trim();
            if (qText.isEmpty() || opts.stream().anyMatch(String::isEmpty)) {
                JOptionPane.showMessageDialog(this,"Please fill question and all options");
                return;
            }
            result = new Question(qText, opts, ai, fback);
            dispose();
        });

        cancel.addActionListener(e -> { result = null; dispose(); });

        setVisible(true);
    }

    public Question getResult() {
        return result;
    }

    public JTextField getQField() {
        return qField;
    }

    public JTextField getOpt0() {
        return opt0;
    }

    public JTextField getOpt1() {
        return opt1;
    }

    public JTextField getOpt2() {
        return opt2;
    }

    public JTextField getOpt3() {
        return opt3;
    }

    public JComboBox<String> getAnsBox() {
        return ansBox;
    }

    public JTextArea getFb() {
        return fb;
    }
}