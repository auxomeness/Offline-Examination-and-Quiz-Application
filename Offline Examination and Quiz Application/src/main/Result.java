import java.io.Serializable;

class Result implements Serializable {      //inheritance
    private String studentId;                       //encapsulation
    private int correct;                            //encapsulation
    private int total;                              //encapsulation
    private int percent;                            //encapsulation

    Result(String studentId, int correct, int total) {
        this.studentId = studentId;
        this.correct = correct;
        this.total = total;
        this.percent = Math.round((correct * 100f) / Math.max(1, total));
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    @Override                           //polymorphism
    public String toString() {
        return studentId + " - " + correct + " / " + total + " - " + percent + "%";
    }
}