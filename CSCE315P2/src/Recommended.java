import javax.swing.*;
import javax.swing.text.View;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Recommended extends JFrame {

    private JPanel recommendedPanel;
    private JLabel titleLabel;
    private JTable recTable;
    private JButton nextButton;
    private JButton searchButton;
    private JButton backButton;
    private JButton back_home;
    private JScrollBar scrollBar1;

    public Recommended()
    {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(recommendedPanel);
        this.pack();
        //this.setVisible(false);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewerTrending vt = new ViewerTrending();
                vt.setVisible(true);
                Recommended.this.dispose();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home home = new Home("Home page");
                home.setVisible(true);
                Recommended.this.dispose();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Search search = new Search();
                search.setVisible(true);
                Recommended.this.dispose();
            }
        });
        back_home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home home = new Home("Home page");
                home.setVisible(true);
                Recommended.this.dispose();
            }
        });
    }

}
