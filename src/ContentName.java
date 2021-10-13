import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContentName extends JFrame {
    private JPanel contentNamePanel;
    private JLabel titleLabel;
    private JButton recommendedButton;
    private JLabel year;
    private JButton trendingButton;

    public ContentName() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(contentNamePanel);
        this.pack();



        recommendedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Recommended rec = new Recommended();
                rec.setVisible(true);
                ContentName.this.dispose();
            }
        });
        trendingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Trending trending = new Trending();
                trending.setVisible(true);
                ContentName.this.dispose();
            }
        });
    }
}
