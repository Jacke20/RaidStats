package raid;

/**
 * This Class executes the MainFrame in the AWT event dispatch thread since swing objects are not thread safe.
 * Created by Jacke on 2014-04-29.
 */
public class RaidAssister {
    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame();
                frame.main(args);
            }
        });
    }
}
