import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.text.*;

public class WatchHistory extends JFrame {
    private JPanel watchHistoryPanel;
    private JLabel titleLabel;
    private JButton backButton;
    private JButton back_home;
    private JScrollBar scrollBar1;
    private JScrollPane tableScrollPane;
    public JTable table1;
    public DefaultTableModel model;
    public String[] columnNames;
    private JComboBox comboBox1;

    public static boolean isValidDate(String inDate) { // Helper function to check if date is valid
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

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

        back_home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home home = new Home("Home page");
                home.setVisible(true);
                WatchHistory.this.dispose();
            }
        });

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String s = (String) comboBox1.getSelectedItem();
                String startDate = "";
                String endDate = "";

                if(s.equals("Date"))
                {
                    startDate = JOptionPane.showInputDialog("Please input the start date you would like to search from (eg. YYYY-MM-DD)");
                    if(isValidDate(startDate))
                    {
                        endDate = JOptionPane.showInputDialog("Please input the end date you would like to search from (eg. YYYY-MM-DD)");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Please enter valid start date",
                                "Hey!", JOptionPane.ERROR_MESSAGE);
                    }
                    if(isValidDate(endDate) && (startDate.compareTo(endDate) < 0))
                    {
                        JOptionPane.showMessageDialog(null, "Dates inputted correctly!");

                        model.setRowCount(0);


                        List<Object[]> list = new ArrayList<Object[]>();

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
                            String sqlStatement = ( // Query to select watch history between specific dates
                                    "SELECT original_title, start_year, genres, average_rating, runtime_minutes, customer_ratings.date_posted " +
                                            "FROM titles " +
                                            "INNER JOIN customer_ratings " +
                                            "ON titles.title_id = customer_ratings.title_id " +
                                            "AND customer_ratings.customer_id = '" + Login.userInfoInt + "' " +
                                            "WHERE customer_ratings.date_posted " +
                                            "BETWEEN '" + startDate + "' AND '" + endDate + "';"
                            );

                            //send statement to DBMS
                            ResultSet result = stmt.executeQuery(sqlStatement);

                            //OUTPUT
                            System.out.println("Database");
                            System.out.println("______________________________________");

                            while (result.next()) { // included date watched so user can see when they saw content

                                String titleName = result.getString("original_title");
                                String year = result.getString("start_year");
                                String genre = result.getString("genres");
                                String avgRev = result.getString("average_rating");
                                String runtime = result.getString("runtime_minutes");
                                String dateWatched = result.getString("date_posted");

                                Object[] information = {titleName, year, genre, avgRev, runtime, dateWatched};

                                model.addRow(information);


                            }

                        } catch (Exception f){
                            System.out.println("Error accessing Database.");
                        }


                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Please enter a valid end date",
                                "Hey!", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
    }

    private void createUIComponents() {

        comboBox1 = new JComboBox();
        comboBox1.addItem("Date");
//      comboBox1.addItem("Genre");
//      comboBox1.addItem("Rating (asc)");
//      comboBox1.addItem("Rating (desc)");

                    List<Object[]> list = new ArrayList<Object[]>();

                    dbSetup my = new dbSetup();
                    //Building the connection
                    Connection conn = null;

                    //Initalize when window loads


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
                                "SELECT original_title, start_year, genres, average_rating, runtime_minutes, customer_ratings.date_posted " +
                                    "FROM titles " +
                                    "INNER JOIN customer_ratings " +
                                    "ON titles.title_id = customer_ratings.title_id " +
                                    "AND customer_ratings.customer_id = '" + Login.userInfoInt + "';"
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

                            String dateWatched = result.getString("date_posted");

                            Object[] information = {titleName, year, genre, avgRev, runtime, dateWatched};

                            list.add(information);

                        }

                    } catch (Exception f){
                        System.out.println("Error accessing Database.");
                    }

        Object[][] data = list.toArray(new Object[list.size()][5]);


        columnNames = new String[]{"Title", "Year", "Genre", "Avg Review", "Runtime", "Date Watched"};
        model = new DefaultTableModel(data, columnNames); // Construct table

        table1 = new JTable(model);
        table1.setFillsViewportHeight(true);
        table1.setEnabled(false);

    }
}
