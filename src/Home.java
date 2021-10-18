import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends JFrame {

    private JPanel homePanel;
    private JLabel titleLabel;
    private JButton contentViewerButton;
    private JButton contentAnalystButton;
    private JLabel welcomeLabel;
    public static boolean clicked = false;

    public Home(String title) {
        super(title); // inheriting the title from GUI
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(homePanel);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.pack();
        this.setVisible(true);

        // Choose between contentViewer or contentAnalyst

        contentViewerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //rec.setVisible(true);
                Login log = new Login();
                log.setVisible(true);
                Home.this.dispose();

            }
        });
        contentAnalystButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Trending trending = new Trending();
                trending.setVisible(true);
                Home.this.dispose();
            }

        });
    }


    public static void main(String[] args) {

        Home frame = new Home("Home page");

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
