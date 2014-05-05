package raid;

/**
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
