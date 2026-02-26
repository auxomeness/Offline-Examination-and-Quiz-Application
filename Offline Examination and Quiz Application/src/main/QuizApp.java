import javax.swing.SwingUtilities;

public class QuizApp {
    private static DataStore store;

    public static DataStore getStore() {
        return store;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            store = DataStore.load(); // encapsulation
            new StartFrame(store);
        });
    }
}