//import javafx.util.Callback;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;

public class Search extends JFrame {
    private JPanel searchPanel;
    private JLabel titleLabel;
    private JTextField searchBar;
    private JCheckBox history;
    private JCheckBox adventure;
    private JCheckBox comedy;
    private JCheckBox romance;
    private JCheckBox action;
    private JCheckBox drama;
    private JButton goButton;
    private JButton back_home;
    private JButton backButton;

    public Search()
    {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(searchPanel);
        this.pack();
        back_home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WatchHistory wh = new WatchHistory();
                wh.setVisible(true);
                Search.this.dispose();
            }
        });
    }
}
