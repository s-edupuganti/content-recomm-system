import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JButton backButton;
    private JButton back_home;
    private JTable table1;
    private JButton viewerBeware;
    public String[] columnNames;
    public DefaultTableModel model;
    private Connection conn;

    public Recommended() {
        super();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(recommendedPanel);
        this.pack();

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
        List<Object[]> list = new ArrayList<Object[]>(); // Lists used to append Object from query
        List<Object[]> list2 = new ArrayList<Object[]>();

        dbSetup my = new dbSetup();
        //Building the connection
        Connection conn = null;
        Connection conn1 = null;


        try {
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
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            String sqlStatement = ( // Find top 30 from users watch history
                    "SELECT genres FROM titles INNER JOIN " +
                            "customer_ratings ON titles.title_id = " +
                            "customer_ratings.title_id AND customer_ratings.customer_id = " +
                            "'" + Login.userInfoInt + "' ORDER BY customer_ratings.rating DESC LIMIT 30;"
            );

            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);

            //OUTPUT
            System.out.println("Database");
            System.out.println("______________________________________");

            while (result.next()) {

                String genre = result.getString("genres");

                Object[] information = new Object[1];

                if (genre.contains(",")) { // Split genres if there are multiple
                    String[] parts = genre.split(",");
                    for (int i = 0; i < parts.length; i++ ) {
                        information[0] = parts[i];
                        list.add(information);

                    }
                }
                else { // no comma
                    information[0] = genre;
                    list.add(information);
                }


            }
            stmt.close();

        } catch (Exception f){
            System.out.println("Error accessing Database.");

        }
        // take list of genres and iterate through

        // iterating through all of the genres

        for (int m = 0; m < list.size(); m++) {

            try{
                //create a statement object
                Statement stmt1 = conn.createStatement();
                //create an SQL statement
                String sqlStatement1 = ( // Reccomendation query
                        "SELECT DISTINCT original_title, start_year, genres, average_rating," +
                                " runtime_minutes FROM titles INNER JOIN customer_ratings ON " +
                                "titles.title_id = customer_ratings.title_id AND titles.genres " +
                                "LIKE '%"+ list.get(m)[0] +"%'LIMIT 5;"
                );

                //send statement to DBMS
                ResultSet result1 = stmt1.executeQuery(sqlStatement1);

                //OUTPUT


                while (result1.next()) { // Query output


                    String titleName = result1.getString("original_title");
                    String year = result1.getString("start_year");
                    String genre = result1.getString("genres");
                    String avgRev = result1.getString("average_rating");
                    String runtime = result1.getString("runtime_minutes");



                    Object[] information = {titleName, year, genre, avgRev, runtime};


                    list2.add(information);

                }

            } catch (Exception f){
                System.out.println("Error accessing Database.");
            }

        }


        Object[][] data = list2.toArray(new Object[list.size()][5]);

        columnNames = new String[]{"Title", "Year", "Genre", "Avg Review", "Runtime"};
        model = new DefaultTableModel(data, columnNames); // model initalization

        table1 = new JTable(model);
        table1.setFillsViewportHeight(true);
        table1.setEnabled(false);


        // TODO: place custom component creation code here
    }
}
