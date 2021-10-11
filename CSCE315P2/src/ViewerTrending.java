import javax.swing.*;
import javax.swing.text.View;
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
    private JButton searchButton;
    private JTable trendingTable;
    private JButton nextButton;
    private JButton backButton;
    private JButton back_home;
    private JScrollBar scrollBar1;

    //private Object[] columnNames = new Object[0];
    private Object[][] output = new Object[5][5];
//    public ArrayList<String> columnNames = new ArrayList<String>();
    //private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
//    public ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

    public ViewerTrending() {
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(vTrendingPanel);
        this.pack();

        // use this.validate() and this.repaint() to refresh the table or database after a command

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Recommended rec = new Recommended();
                rec.setVisible(true);
                ViewerTrending.this.dispose();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Search search = new Search();
                search.setVisible(true);
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

            String sqlStatement = (
                    "SELECT original_title, start_year, genres, average_rating, runtime_minutes " +
                            "FROM titles " +
                            "INNER JOIN customer_ratings " +
                            "ON titles.title_id = customer_ratings.title_id " +
                            "GROUP BY titles.title_id " +
                            "ORDER BY count(*) DESC LIMIT 10;"
            );

            System.out.println(sqlStatement);
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);

            //OUTPUT
            System.out.println("Database");
            System.out.println("______________________________________");

//                        List<Object[]> list = new ArrayList<Object[]>();

            while (result.next()) {

                String titleName = result.getString("original_title");
                String year = result.getString("start_year");
                String genre = result.getString("genres");
                String avgRev = result.getString("average_rating");
                String runtime = result.getString("runtime_minutes");

                Object[] information = {titleName, year, genre, avgRev, runtime};

                list.add(information);
//                ArrayList<Integer> = new ArrayList<Integer>();



            }

        } catch (Exception f){
            System.out.println("Error accessing Database.");
        }


//                    Recommended rec = new Recommended();
//                    rec.setVisible(true);
//                    Login.this.dispose();



//        Object[] information = {"A Bridge Too Far", "1977", "Drama,History,War", "7.4", "175"};
//        List<Object[]> list = new ArrayList<Object[]>();
//        list.add(information);

        Object[][] data = list.toArray(new Object[list.size()][5]);



        String[] columnNames = {"Title", "Year", "Genre", "Avg Review", "Runtime"};
//        Object[][] data = {{"A Bridge Too Far", "1977", "Drama,History,War", "7.4", "175"}, {"A Christmas Carol", "2019", "Drama,Fantasy,Music", "5.8", "75"}};
        trendingTable = new JTable(data, columnNames);
        trendingTable.setFillsViewportHeight(true);
    }
}




