import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Question implements Serializable {                //inheritance
    private String text;                                        //encapsulation
    private List<String> options;                               //encapsulation
    private int answerIndex; // array index of correct answer   //encapsulation
    private String feedback;                                    //encapsulation

    Question(String text, List<String> options, int answerIndex, String feedback) {
        this.text = text;                           //encapsulation
        this.options = new ArrayList<>(options);  //encapsulation
        this.answerIndex = answerIndex;            //encapsulation
        this.feedback = feedback;                  //encapsulation
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override                                       //polymorphism
    public String toString() {
        return text;
    }
}