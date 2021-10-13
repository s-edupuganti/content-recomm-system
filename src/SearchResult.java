import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchResult extends JFrame {
    private JPanel searchResultPanel;
    private JLabel searchResLabel;
    private JButton backButton;
    private JTable searchResultTable;

    public SearchResult(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(searchResultPanel);
        this.pack();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Search search = new Search();
                search.setVisible(true);
                SearchResult.this.dispose();
            }
        });
    }
}
