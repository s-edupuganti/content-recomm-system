import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

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
        //this.setContentPane(new View().homePanel);
        this.pack();
        this.setVisible(true);
        //contentViewerButton = new JButton("Content Viewer");
        //contentViewerButton.addActionListener(this);
        contentViewerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Home frame = new Home("Home Frame");
                //Recommended rec = new Recommended();
                //rec.setVisible(true);
                Login log = new Login();
                log.setVisible(true);
                //clicked = true;
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
/*
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if(command.equals("Content Viewer"))
        {
            Recommended rec = new Recommended();
            rec.setVisible(true);
            this.setVisible(false);
        }
    }

 */

    public static void main(String[] args) {
        //JFrame frame = new JFrame();
        //JPanel panel = new JPanel(homePanel);

        // frame.setContentPane();
        //frame.setVisible(true);
        /*if(clicked == true)
        {
            frame.setVisible(false);
        }

         */
        Home frame = new Home("Home page");



    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
