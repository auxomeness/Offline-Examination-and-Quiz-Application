import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

class QuizFrame extends JFrame {          //inheritance
    private DataStore store;                     //encapsulation
    private String studentId;                   //encapsulation
    private JFrame loginWindow;                //encapsulation
    private JFrame startWindow;               //encapsulation

    // quiz state
    private List<Question> questions;       //encapsulation
    private int current = 0;
    private int[] userAnswers; // -1 unanswered, else selected index               //encapsulation   
    private String[] userFeedbacks;                                               //encapsulation            
    private JPanel optionsPanel;                                                 //encapsulation
    private JLabel statusLeft, statusRight;                                     //encapsulation
    private JLabel questionLabel;                                              //encapsulation
    private JButton checkBtn, nextBtn, backBtn;                               //encapsulation
    private JTextArea feedbackArea;                                          //encapsulation
    private ButtonGroup group;                                              //encapsulation    

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
            setPreferredSize(new java.awt.Dimension(120, 40));
        }
    }

    public DataStore getStore() {
        return store;
    }
    public String getStudentId() {
        return studentId;
    }
    public JFrame getLoginWindow() {
        return loginWindow;
    }
    public JFrame getStartWindow() {
        return startWindow;
    }
    public List<Question> getQuestions() {
        return questions;
    }
    public int[] getUserAnswers() {
        return userAnswers;
    }
    public String[] getUserFeedbacks() {
        return userFeedbacks;
    }
    public JPanel getOptionsPanel() {
        return optionsPanel;
    }
    public JLabel getStatusLeft() {
        return statusLeft;
    }
    public JLabel getStatusRight() {
        return statusRight;
    }
    public JLabel getQuestionLabel() {
        return questionLabel;
    }
    public JButton getCheckBtn() {
        return checkBtn;
    }
    public JButton getNextBtn() {
        return nextBtn;
    }
    public JButton getBackBtn() {
        return backBtn;
    }
    public JTextArea getFeedbackArea() {
        return feedbackArea;
    }

    QuizFrame(DataStore store, String studentId, JFrame loginWindow, JFrame startWindow) {
        super("Quiz - Student: " + studentId);
        this.store = store;
        this.studentId = studentId;
        this.loginWindow = loginWindow;
        this.startWindow = startWindow;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900,560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // copy questions order (do not modify original)
        this.questions = new ArrayList<>(store.getQuestions());
        this.userAnswers = new int[questions.size()];
        Arrays.fill(userAnswers, -1);
        this.userFeedbacks = new String[questions.size()];

        // top status
        JPanel top = new JPanel(new BorderLayout());
        statusLeft = new JLabel("Answered: 0 / " + questions.size());
        statusRight = new JLabel("Correct: 0");
        top.add(statusLeft, BorderLayout.WEST);
        top.add(statusRight, BorderLayout.EAST);
        top.setBorder(new EmptyBorder(8,12,8,12));
        add(top, BorderLayout.NORTH);

        // center question + options
        JPanel center = new JPanel(new BorderLayout(8,8));
        questionLabel = new JLabel("", SwingConstants.LEFT);
        questionLabel.setFont(questionLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
        questionLabel.setBorder(new EmptyBorder(8,12,8,12));
        center.add(questionLabel, BorderLayout.NORTH);

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        JScrollPane sc = new JScrollPane(optionsPanel);
        sc.setBorder(new EmptyBorder(0,12,12,12));
        center.add(sc, BorderLayout.CENTER);

        feedbackArea = new JTextArea(3,40);
        feedbackArea.setEditable(false);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setBorder(new EmptyBorder(6,12,6,12));
        center.add(feedbackArea, BorderLayout.SOUTH);

        add(center, BorderLayout.CENTER);

        // controls
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 10));
        backBtn = new NavyButton("Back");
        checkBtn = new NavyButton("Check");
        nextBtn = new NavyButton("Next");
        controls.add(backBtn); controls.add(checkBtn); controls.add(nextBtn);
        add(controls, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> {
            if (current > 0) { current--; refreshQuestion(); }
        });

        nextBtn.addActionListener(e -> {
            if (current < questions.size() - 1) { current++; refreshQuestion(); }
        });

        checkBtn.addActionListener(e -> {
            int selected = getSelectedOptionIndex();
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Please select an option.");
                return;
            }
            // record answer and feedback, disable options
            userAnswers[current] = selected;
            if (selected == questions.get(current).getAnswerIndex()) {
                userFeedbacks[current] = "Correct! " + questions.get(current).getFeedback();
            } else {
                userFeedbacks[current] = "Wrong. " + questions.get(current).getFeedback();
            }
            feedbackArea.setText(userFeedbacks[current]);
            setOptionsEnabled(false);
            checkBtn.setEnabled(false);
            updateStatus();
            // if last question + submit
            if (current == questions.size() - 1) {
                showResults();
            }
        });

        // initial render
        refreshQuestion();

        // window closed: return to start
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                startWindow.setVisible(true);
            }
        });

        setVisible(true);
    }

    private void refreshQuestion() {
        Question q = questions.get(current);
        questionLabel.setText((current+1) + ". " + q.getText());
        optionsPanel.removeAll();
        group = new ButtonGroup();
        for (int i=0;i<q.getOptions().size();i++) {
            JRadioButton rb = new JRadioButton(q.getOptions().get(i));
            rb.setActionCommand(String.valueOf(i));
            rb.setAlignmentX(Component.LEFT_ALIGNMENT);
            group.add(rb);
            optionsPanel.add(rb);
            optionsPanel.add(Box.createVerticalStrut(6));
            // restore selection if answered
            if (userAnswers[current] == i) rb.setSelected(true);
            // disable if already answered
            if (userAnswers[current] != -1) rb.setEnabled(false);
        }
        // update buttons text/enable state
        backBtn.setEnabled(current > 0);
        nextBtn.setEnabled(current < questions.size()-1);
        checkBtn.setEnabled(userAnswers[current] == -1);
        checkBtn.setText(current == questions.size()-1 ? "Submit" : "Check");
        // restore feedback text
        feedbackArea.setText(userFeedbacks[current] != null ? userFeedbacks[current] : "");
        optionsPanel.revalidate();
        optionsPanel.repaint();
        updateStatus();
    }

    private int getSelectedOptionIndex() {
        if (group == null) return -1;
        ButtonModel sel = group.getSelection();
        if (sel == null) return -1;
        try { return Integer.parseInt(sel.getActionCommand()); }
        catch (Exception ex) { return -1; }
    }

    private void setOptionsEnabled(boolean enabled) {
        for (Component c : optionsPanel.getComponents()) {
            if (c instanceof JRadioButton) ((JRadioButton)c).setEnabled(enabled);
        }
    }

    private void updateStatus() {
        int answered = 0, correct = 0;
        for (int i=0;i<userAnswers.length;i++) {
            if (userAnswers[i] != -1) {
                answered++;
                if (userAnswers[i] == questions.get(i).getAnswerIndex()) correct++;
            }
        }
        statusLeft.setText("Answered: " + answered + " / " + questions.size());
        statusRight.setText("Correct: " + correct);
    }

    private void showResults() {
        int total = questions.size();
        int correct = 0;
        int answered = 0;
        for (int i=0;i<userAnswers.length;i++) {
            if (userAnswers[i] != -1) {
                answered++;
                if (userAnswers[i] == questions.get(i).getAnswerIndex()) correct++;
            }
        }
        int percent = Math.round((correct * 100f) / total);
        // record or update result
        Optional<Result> existing = store.getResults().stream().filter(r -> r.getStudentId().equals(studentId)).findFirst();
        if (existing.isPresent()) {
            // update
            Result r = existing.get();
            r.setCorrect(correct);
            r.setTotal(total);
            r.setPercent(Math.round((correct * 100f) / Math.max(1, total)));
        } else {
            store.addResult(new Result(studentId, correct, total));
        }
        store.save();

        String pass = percent >= 60 ? "You passed " : "You failed ";
        JPanel res = new JPanel(new BorderLayout(8,8));
        res.setBorder(new EmptyBorder(12,12,12,12));
        JLabel h = new JLabel("Final Score", SwingConstants.CENTER);
        h.setFont(h.getFont().deriveFont(18f).deriveFont(Font.BOLD));
        res.add(h, BorderLayout.NORTH);
        JTextArea stats = new JTextArea();
        stats.setEditable(false);
        stats.setText("Correct: " + correct + " / " + total + "\nAnswered: " + answered + " / " + total + "\nPercentage: " + percent + "%\n\n" + pass);
        res.add(stats, BorderLayout.CENTER);
        JOptionPane.showMessageDialog(this, res, "Results", JOptionPane.INFORMATION_MESSAGE);
        // after close, leave the summary on screen
        getContentPane().removeAll();
        add(res, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}