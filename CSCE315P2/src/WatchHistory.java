import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class WatchHistory extends JFrame {
    private JPanel watchHistoryPanel;
    private JLabel titleLabel;
    private JButton backButton;
    private JButton searchButton;
    private JButton back_home;
    private JTable trendingTable;
    private JScrollBar scrollBar1;
    private JLabel headerLabel;
    private JButton filterButton;
//    private JButton loadButton;


    public WatchHistory()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(watchHistoryPanel);
        this.pack();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewerTrending vt = new ViewerTrending();
                vt.setVisible(true);
                WatchHistory.this.dispose();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Search search = new Search();
                search.setVisible(true);
                WatchHistory.this.dispose();
            }
        });
        back_home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home home = new Home("Home page");
                home.setVisible(true);
                WatchHistory.this.dispose();
            }
        });

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(WatchHistory.this,
                        "Filter Button Pressed!");


            }
        });

    }

    private void createUIComponents() {

                    List<Object[]> list = new ArrayList<Object[]>();
//                    Object[] information = {"", "", "", "", ""};


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
                        Statement stmt = conn.createStatement();
                        //create an SQL statement
                        String sqlStatement = (
                                "SELECT original_title, start_year, genres, average_rating, runtime_minutes " +
                                    "FROM titles " +
                                    "INNER JOIN customer_ratings " +
                                    "ON titles.title_id = customer_ratings.title_id " +
                                    "AND customer_ratings.customer_id = '" + Login.userInfoInt + "';"
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


//                            Object[] output = {titleName, year, genre, avgRev, runtime};
//
//                            for (int i = 0; i < 5; i++) {
//                                information[i] = output[i];
//                            }





//                            ArrayList<Integer> = new ArrayList<Integer>();


//                            String titleID = result.getString("title_id");
//
//                            String sqlStatement2 = "SELECT original_title, start_year, genres, average_rating, runtime_minutes FROM titles WHERE title_id = '" + titleID + "';";
//
//                            System.out.println(sqlStatement2);
//
//                            ResultSet result2 = stmt.executeQuery(sqlStatement2);
//
//                            while (result2.next()) {
//
//
//                                String titleName = result2.getString("original_title");
//                                String year = result2.getString("start_year");
//                                String genre = result2.getString("genres");
//                                String avgRev = result2.getString("average_rating");
//                                String runtime = result2.getString("runtime_minutes");
//
//                                Object[] output = {titleName, year, genre, avgRev, runtime};
//
//
//                                for (int i = 0; i < 5; i++) {
//                                    information[i] = output[i];
//                                }
//
//                            }
//
////                            result2.absolute(1);
//
////                            result2.first();
////
////                            result.first();
//
                            list.add(information);
//
//                            System.out.println("GOT TO THE END!");


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
        // TODO: place custom component creation code here
    }
}
