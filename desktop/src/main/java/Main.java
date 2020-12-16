import gui.frame.MainFrame;
import network.RetrofitController;


/**
 * Main class with the Main-Method
 *
 */
public class Main {
    /**
     * Main-Method.
     * @param args
     */
    public static void main(String[] args) {
        // start networking
        RetrofitController.start();
        // start gui
        MainFrame.startTheMainFrame();
    }
}
