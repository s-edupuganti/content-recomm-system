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
import java.lang.Math;
import java.util.List;

public class ViewerBeware extends JFrame {
    private JPanel viewerBewarePanel;
    public JTable vbTable;
    private JButton back_home;
    private JButton searchButton;
    private JButton backButton;
    public String[] columnNames;
    public DefaultTableModel model;

    public ViewerBeware() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(viewerBewarePanel);
        this.pack();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Recommended rec = new Recommended();
                rec.setVisible(true);
                ViewerBeware.this.dispose();
            }
        });

        back_home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home home = new Home("Home page");
                home.setVisible(true);
                ViewerBeware.this.dispose();
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        ArrayList<String> list = new ArrayList<String>();
        List<Object[]> finalOutput = new ArrayList<>();

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
                    "SELECT DISTINCT customer_id " +
                            "FROM customer_ratings;"
            );

            System.out.println(sqlStatement);
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);

            //OUTPUT
            System.out.println("Database");
            System.out.println("______________________________________");

            while (result.next()) {

                String input = result.getString("customer_id");
                if (!input.equals(Login.userInfo)) {
                    System.out.println(input);
                    list.add(input);
                }


                System.out.println("CHECK!");

            }

            System.out.println("CHECK 2!");

            String countWatchHistory = (
                    "SELECT COUNT(title_id) AS number FROM customer_ratings WHERE customer_id = '" + Login.userInfoInt + "' AND rating < 3;"
                    );

            System.out.println(countWatchHistory);

            System.out.println("HELLO 1!");

            ResultSet watchHist = stmt.executeQuery(countWatchHistory);

            System.out.println("HELLO 2!");

            int count = 0;

            System.out.println("HELLO 3!");

            while (watchHist.next()) {

                String watchHistNum = watchHist.getString("number");
                System.out.println(watchHistNum);
                count = Integer.valueOf(watchHistNum);

                System.out.println("HELLO 4!");


            }

            System.out.println("HELLO 5!");







            String avoidUser = "";
            int greatDislikeSim = 0;

            int countDbl = 0;

            for (int i = 0; i < list.size(); i++) {


                if (i == 0) {
                    countDbl = (int) Math.floor(count / 1.5);
                } else if (i == 20) {
                    countDbl = (int) Math.floor(count / 2);
                } else if (i == 50) {
                    countDbl = (int) Math.floor(count / 2.5);
                } else if (i == 100) {
                    countDbl =  (int) Math.floor(count / 3);
                }

                System.out.println("THRESHOLD IS: " + countDbl);


                String sqlStatement2 = (
                            "SELECT COUNT(title_id) AS num_similar " +
                                    "FROM (SELECT title_id " +
                                    "FROM customer_ratings " +
                                    "WHERE customer_id = '" + Login.userInfoInt + "' AND rating < 3 " +
                                    "INTERSECT " +
                                    "SELECT title_id " +
                                    "FROM customer_ratings " +
                                    "WHERE customer_id = '" + list.get(i) + "' AND rating < 3) I;"
                    );

                    System.out.println(sqlStatement2);

                    ResultSet result2 = stmt.executeQuery((sqlStatement2));

                    int dislikesCommonInt = 0;

                    while (result2.next()) {

                        System.out.println("CHECK 3!");

                        String dislikesCommon = result2.getString("num_similar");
                        dislikesCommonInt = Integer.parseInt(dislikesCommon);


                    }

                if (dislikesCommonInt >= countDbl) {
                    greatDislikeSim = dislikesCommonInt;
                    avoidUser = list.get(i);
                    break;
                }




            }

            System.out.println("CHECK 6!");

            System.out.println("Use with similar dislikes is " + avoidUser);
            System.out.println("This user has " + greatDislikeSim + " similarities!");


            String sqlStatement3 = (
                    "SELECT original_title, start_year, genres, average_rating, runtime_minutes " +
                            "FROM titles " +
                            "INNER JOIN customer_ratings " +
                            "ON titles.title_id = customer_ratings.title_id " +
                            "WHERE customer_ratings.customer_id = '" + avoidUser + "' " +
                            "AND rating < 3 " +
                            "AND titles.title_id " +
                            "NOT IN " +
                            "(SELECT title_id " +
                                "FROM customer_ratings " +
                                "WHERE customer_id = '" + Login.userInfoInt + "' " +
                                "AND rating < 3);"
            );

            ResultSet result3 = stmt.executeQuery(sqlStatement3);

            System.out.println(sqlStatement3);

            System.out.println("CHECK 7!");


            while (result3.next()) {


                String titleName = result3.getString("original_title");
                String year = result3.getString("start_year");
                String genre = result3.getString("genres");
                String avgRev = result3.getString("average_rating");
                String runtime = result3.getString("runtime_minutes");

                System.out.println("GETTING HERE 2!");

                System.out.println("GETTING HERE 3!");

                Object[] information = {titleName, year, genre, avgRev, runtime};

                System.out.println("GETTING HERE 4!");

                finalOutput.add(information);


            }

        } catch (Exception f){
            System.out.println("Error accessing Database.");
        }

        Object[][] data = finalOutput.toArray(new Object[list.size()][5]);


//        String[] columnNames = {"Title", "Year", "Genre", "Avg Review", "Runtime"};
        columnNames = new String[]{"Title", "Year", "Genre", "Avg Review", "Runtime"};
        model = new DefaultTableModel(data, columnNames);

        vbTable = new JTable(model);
        vbTable.setFillsViewportHeight(true);
        vbTable.setEnabled(false);
    }
}
