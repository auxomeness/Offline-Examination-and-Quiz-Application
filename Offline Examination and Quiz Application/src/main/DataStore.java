import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// majority of the changes here are encapsulation

class DataStore implements Serializable {
    private static final long serialVersionUID = 1L; // encapsulation
    private List<Question> questions = new ArrayList<>();
    private boolean published = false;
    private String accessCode = "1234"; 

    private Map<String,String> instructors = new HashMap<>();

    private String adminPassword = "admin"; // encapsulation

    private Set<String> allowedStudentIds = new HashSet<>();

    private List<Result> results = new ArrayList<>();

    private static final String FILE = "entire.db"; // encapsulation

    static DataStore load() { // encapsulation
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE))) {
            return (DataStore) in.readObject();
        } catch (Exception e) {

            System.err.println("DataStore.load: failed to read " + FILE + " — creating fresh store");
            e.printStackTrace();

            DataStore ds = new DataStore();

            ds.instructors.put("instructor", "instructor");
            ds.save();
            return ds;
        }
    }

    void save() { // encapsulation
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public Map<String, String> getInstructors() {
        return instructors;
    }

    public void setInstructors(Map<String, String> instructors) {
        this.instructors = instructors;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public Set<String> getAllowedStudentIds() {
        return allowedStudentIds;
    }

    public void setAllowedStudentIds(Set<String> allowedStudentIds) {
        this.allowedStudentIds = allowedStudentIds;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void addResult(Result r) {
        results.add(r);
    }

    public void removeResult(Result r) {
        results.remove(r);
    }
}