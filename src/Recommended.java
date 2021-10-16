import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.View;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Recommended extends JFrame {

    private JPanel recommendedPanel;
    private JLabel titleLabel;
    private JButton nextButton;
    private JButton searchButton;
    private JButton backButton;
    private JButton back_home;
    private JTable table1;
    private JButton viewerBeware;
    public String[] columnNames;
    public DefaultTableModel model;

    public Recommended() {
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
        viewerBeware.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewerBeware vb = new ViewerBeware();
                vb.setVisible(true);
                Recommended.this.dispose();

            }
        });
    }

    private void createUIComponents() {
        List<Object[]> list = new ArrayList<Object[]>();
        List<Object[]> list2 = new ArrayList<Object[]>();

        dbSetup my = new dbSetup();
        //Building the connection
        Connection conn = null;
        Connection conn1 = null;


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
                    "SELECT genres FROM titles INNER JOIN " +
                            "customer_ratings ON titles.title_id = " +
                            "customer_ratings.title_id AND customer_ratings.customer_id = " +
                            "'1488844' ORDER BY customer_ratings.rating DESC LIMIT 30;"
            );

            System.out.println(sqlStatement);
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);

            //OUTPUT
            System.out.println("Database");
            System.out.println("______________________________________");

            while (result.next()) {

                //System.out.println("GETTING HERE 1!");

                //String titleName = result.getString("original_title");
                //String year = result.getString("start_year");
                String genre = result.getString("genres");
                //String avgRev = result.getString("average_rating");
                //String runtime = result.getString("runtime_minutes");

                //System.out.println("GETTING HERE 2!");

                //String dateWatched = result.getString("date_posted");

                //System.out.println("GETTING HERE 3!");

                //Object[] information = {titleName, year, genre, avgRev, runtime, dateWatched};
                Object[] information = new Object[1];

                if (genre.contains(",")) {
                    String[] parts = genre.split(",");
                    for (int i = 0; i < parts.length; i++ ) {
                        information[0] = parts[i];
                        list.add(information);
                        //System.out.println(information[0]);
                    }
                }
                else { // no comma
                    information[0] = genre;
                    list.add(information);
                    //System.out.println(information[0]);
                }



                //System.out.println("GETTING HERE 4!");

                //list.add(information);

            }
            stmt.close();

        } catch (Exception f){
            System.out.println("Error accessing Database.");

        }
        // take list of genres and iterate through
        System.out.println(list.get(0)[0]);

        // iterating through all of the genres

        for (int m = 0; m < list.size(); m++) {


            //System.out.println("Opened database successfully");
            //String cus_lname1 = "";
            //  String username = "";
            try{
                //create a statement object
                Statement stmt1 = conn.createStatement();
                //create an SQL statement
                String sqlStatement1 = (
                        "SELECT DISTINCT original_title, start_year, genres, average_rating," +
                                " runtime_minutes FROM titles INNER JOIN customer_ratings ON " +
                                "titles.title_id = customer_ratings.title_id AND titles.genres " +
                                "LIKE '%"+ list.get(m)[0] +"%'LIMIT 5;"
                );

                //System.out.println(sqlStatement1);
                //send statement to DBMS
                ResultSet result1 = stmt1.executeQuery(sqlStatement1);

                //OUTPUT
                //System.out.println("Database");
                //System.out.println("______________________________________");

                while (result1.next()) {

                    //System.out.println("GETTING HERE 1!");

                    String titleName = result1.getString("original_title");
                    String year = result1.getString("start_year");
                    String genre = result1.getString("genres");
                    String avgRev = result1.getString("average_rating");
                    String runtime = result1.getString("runtime_minutes");

                    //System.out.println("GETTING HERE 2!");

                    //String dateWatched = result1.getString("date_posted");

                    //System.out.println("GETTING HERE 3!");

                    Object[] information = {titleName, year, genre, avgRev, runtime};

                    //System.out.println("GETTING HERE 4!");

                    list2.add(information);

                }

            } catch (Exception f){
                System.out.println("Error accessing Database.");
            }

        }


        Object[][] data = list2.toArray(new Object[list.size()][5]);

        System.out.println("Hello1");
        columnNames = new String[]{"Title", "Year", "Genre", "Avg Review", "Runtime"};
        model = new DefaultTableModel(data, columnNames);

        table1 = new JTable(model);
        table1.setFillsViewportHeight(true);
        table1.setEnabled(false);


        // TODO: place custom component creation code here
    }
}
