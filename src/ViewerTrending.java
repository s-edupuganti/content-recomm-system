import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ViewerTrending extends JFrame {
    private JPanel vTrendingPanel;
    private JLabel titleLabel;
    private JButton nextButton;
    private JButton backButton;
    private JButton back_home;
    private JScrollBar scrollBar1;
    private JTable myTable;

    private Object[][] output = new Object[5][5];

    public ViewerTrending() {
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(vTrendingPanel);
        this.pack();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Recommended rec = new Recommended();
                rec.setVisible(true);
                ViewerTrending.this.dispose();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WatchHistory wh = new WatchHistory();
                wh.setVisible(true);
                ViewerTrending.this.dispose();
            }
        });
        back_home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home home = new Home("Home page");
                home.setVisible(true);
                ViewerTrending.this.dispose();
            }
        });


    }

    private void createUIComponents() {
        List<Object[]> list = new ArrayList<Object[]>();
//        Object[] information = {"", "", "", "", ""};


        dbSetup my = new dbSetup();
        //Building the connection
        Connection conn = null;


        try {
            //Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315_914_5_db",
                    my.user, my.pswd);
        } catch (Exception f) {
            f.printStackTrace();
            System.err.println(f.getClass().getName()+": "+f.getMessage());
            System.exit(0);
        }//end try catch
        System.out.println("Opened database successfully");
        String cus_lname = "";
        //  String username = "";
        try{
            //create a statement object
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //create an SQL statement

            String sqlStatement = ( // query for top 10 trending of everything within database
                    "SELECT original_title, start_year, genres, average_rating, runtime_minutes " +
                            "FROM titles " +
                            "INNER JOIN customer_ratings " +
                            "ON titles.title_id = customer_ratings.title_id " +
                            "GROUP BY titles.title_id " +
                            "ORDER BY count(*) DESC LIMIT 10;"
            );
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);

            //OUTPUT
            System.out.println("Database");
            System.out.println("______________________________________");

            while (result.next()) {

                String titleName = result.getString("original_title");
                String year = result.getString("start_year");
                String genre = result.getString("genres");
                String avgRev = result.getString("average_rating");
                String runtime = result.getString("runtime_minutes");

                Object[] information = {titleName, year, genre, avgRev, runtime};

                list.add(information);



            }

        } catch (Exception f){
            System.out.println("Error accessing Database.");
        }

        Object[][] data = list.toArray(new Object[list.size()][5]);



        String[] columnNames = {"Title", "Year", "Genre", "Avg Review", "Runtime"};
        myTable = new JTable(data, columnNames);
        myTable.setFillsViewportHeight(true);
        myTable.setEnabled(false);
    }
}




