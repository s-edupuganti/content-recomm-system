import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.*;

public class Trending extends JFrame {
    private JPanel trendingPanel;
    private JLabel titleLabel;
    private JButton backButton;
    private JScrollBar scrollBar1;
    private JTable myTable;
    private JScrollPane tableScrollPane;
    private JComboBox comboBox1;
    public DefaultTableModel model;
    public String[] columnNames;

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


    public Trending() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(trendingPanel);
        this.pack();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home home = new Home("Home page");
                home.setVisible(true);
                Trending.this.dispose();

            }
        });

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = "";
                String tomatoTitle1 = "";
                String tomatoTitle2 = "";
                String startDate = "";
                String endDate = "";

                dbSetup my = new dbSetup();
                //Building the connection
                Connection conn = null;
                String s = (String) comboBox1.getSelectedItem();
                if (s.equals("Hollywood Pairs")) {
                    model.setColumnCount(2); // One column for each actor hence (pairs)
                    model.setRowCount(0);
                    JTableHeader header = myTable.getTableHeader();
                    TableColumnModel colMod = header.getColumnModel();
                    TableColumn tabCol = colMod.getColumn(0);
                    TableColumn tabCol1 = colMod.getColumn(1);
                    tabCol.setHeaderValue("Actor 1");
                    tabCol1.setHeaderValue("Actor 2");
                    header.repaint();


                    try {
                        //Class.forName("org.postgresql.Driver");
                        conn = DriverManager.getConnection(
                                "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315_914_5_db",
                                my.user, my.pswd);
                    } catch (Exception f) {
                        f.printStackTrace();
                        System.err.println(f.getClass().getName() + ": " + f.getMessage());
                        System.exit(0);
                    }//end try catch
                    System.out.println("Opened database successfully");
                    String cus_lname = "";
                    //  String username = "";

                    try {
                        //create a statement object
                        Statement stmt = conn.createStatement();
//
                        //create an SQL statement
                        String sqlStatement = ( // Query to find chemistry between pairs
                                "SELECT primaryname1, primaryname2, avg(average_rating) as chemistry " +
                                        "FROM (SELECT group1.nmconst as name1, group2.nmconst as name2, " +
                                        "group1.title_id, titles.average_rating, group1.primary_name as primaryname1," +
                                        " group2.primary_name as primaryname2 FROM (SELECT principals.nmconst as" +
                                        " nmconst, title_id, primary_name FROM principals inner join names on " +
                                        "principals.nmconst = names.nmconst WHERE category LIKE '%act%') as group1, " +
                                        "(SELECT principals.nmconst as nmconst, title_id , primary_name FROM principals" +
                                        " inner join names on principals.nmconst = names.nmconst WHERE category LIKE " +
                                        "'%act%') as group2 inner join titles on" +
                                        " group2.title_id=titles.title_id where group1.title_id = group2.title_id " +
                                        "and group1.nmconst <> group2.nmconst) as pairchemistry group by primaryname1," +
                                        " primaryname2 order by chemistry desc limit 10;"
                        );

                        //send statement to DBMS
                        ResultSet result = stmt.executeQuery(sqlStatement);

                        //OUTPUT
                        System.out.println("Database");
                        System.out.println("______________________________________");


                        while (result.next()) {

                            String name1 = result.getString("primaryname1");
                            String name2 = result.getString("primaryname2");
                            String chem = result.getString("chemistry");
                            Object[] information = {name1, name2};
                            model.addRow(information);
                        }

                        //}
                    } catch (Exception x) {
                        System.out.println("Error accessing Database.");
                    }

                } else if (s.equals("Tomato Number")) {

                    tomatoTitle1 = JOptionPane.showInputDialog("Please input first content title:"); // Ask inputs from user

                    tomatoTitle2 = JOptionPane.showInputDialog("Please input second content title:");

                    ArrayList<String> customerSeen1 = new ArrayList<String>();
                    ArrayList<String> customerSeen2 = new ArrayList<String>();


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
                                "SELECT customer_id FROM customer_ratings WHERE title_id = '" + tomatoTitle1 + "' AND rating > 3;"
                        );

                        //send statement to DBMS
                        ResultSet result = stmt.executeQuery(sqlStatement);

                        //OUTPUT
                        System.out.println("Database");
                        System.out.println("______________________________________");


                        while (result.next()) {

                            String customer = result.getString("customer_id");

                            customerSeen1.add(customer);


                        }

                        String sqlStatement2 = (
                                "SELECT customer_id FROM customer_ratings WHERE title_id = '" + tomatoTitle2 + "' AND rating > 3;"
                        );

                        //send statement to DBMS
                        ResultSet result2 = stmt.executeQuery(sqlStatement2);

                        //OUTPUT
                        System.out.println("Database");
                        System.out.println("______________________________________");


                        while (result2.next()) {

                            String customer = result2.getString("customer_id");

                            customerSeen2.add(customer);
                        }

                        Map<String, ArrayList<String>> tomatoMap1 = new HashMap<>(); // Hash map for each user, and all content they have seen
                        Map<String, ArrayList<String>> tomatoMap2 = new HashMap<>();

                        for (int i = 0; i < customerSeen1.size(); i++) {
                            tomatoMap1.put(customerSeen1.get(i), new ArrayList<String>());

                            String sqlStatement3 = (
                                    "SELECT title_id FROM customer_ratings WHERE customer_id = '" + customerSeen1.get(i) + "' AND rating > 3;"

                                    );

                            ResultSet result3 = stmt.executeQuery(sqlStatement3);

                            String currentCustomer = customerSeen1.get(i);

                            while (result3.next()) {
                                String contentSeen = result3.getString("title_id");

                                tomatoMap1.get(currentCustomer).add(contentSeen);

                            }


                        }

                        // END OF FOR LOOP

                        for (int i = 0; i < customerSeen2.size(); i++) {
                            tomatoMap2.put(customerSeen2.get(i), new ArrayList<String>());

                            String sqlStatement3 = (
                                    "SELECT title_id FROM customer_ratings WHERE customer_id = '" + customerSeen2.get(i) + "' AND rating > 3;"

                            );

                            ResultSet result3 = stmt.executeQuery(sqlStatement3);

                            String currentCustomer = customerSeen2.get(i);


                            while (result3.next()) {
                                String contentSeen = result3.getString("title_id");

                                tomatoMap2.get(currentCustomer).add(contentSeen);

                            }


                        }

                        outerloop:
                        for (Map.Entry<String, ArrayList<String>> entry1 : tomatoMap1.entrySet()) {
                            for (int i = 0; i < entry1.getValue().size(); i++) {
                                for (Map.Entry<String, ArrayList<String>> entry2 : tomatoMap2.entrySet()) {
                                    for (int j = 0; j < entry2.getValue().size(); j++) {

                                        if (entry1.getValue().get(i) == entry2.getValue().get(j)) {
                                            System.out.println(tomatoTitle1 + "->" + entry1.getKey() + "->" + entry1.getValue().get(i) + "->" + entry2.getKey() + "->" + tomatoTitle2);
                                            break outerloop;
                                        }
                                    }
                                }
                            }
                        }

                    } catch (Exception f){
                        System.out.println("Error accessing Database.");
                    }

                } else if (s.equals("Date")) {

                    startDate = JOptionPane.showInputDialog("Please input the start date you would like to search from (eg. YYYY-MM-DD)");
                    if(isValidDate(startDate))
                    {
                        endDate = JOptionPane.showInputDialog("Please input the end date you would like to search from (eg. YYYY-MM-DD)");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Please enter valid start date", // error handling
                                "Hey!", JOptionPane.ERROR_MESSAGE);
                    }
                    if(isValidDate(endDate) && (startDate.compareTo(endDate) < 0))
                    {
                        JOptionPane.showMessageDialog(null, "Dates inputted correctly!");

                        model.setColumnCount(5); // set column count back to normal if changed prior to selecting dates
                        model.setRowCount(0);

                        JTableHeader header = myTable.getTableHeader();
                        TableColumnModel colMod = header.getColumnModel();

                        TableColumn tabCol = colMod.getColumn(0);
                        TableColumn tabCol2 = colMod.getColumn(1);
                        TableColumn tabCol3 = colMod.getColumn(2);
                        TableColumn tabCol4 = colMod.getColumn(3);
                        TableColumn tabCol5 = colMod.getColumn(4);

                        tabCol.setHeaderValue("Title");
                        tabCol2.setHeaderValue("Year");
                        tabCol3.setHeaderValue("Genre");
                        tabCol4.setHeaderValue("Avg Review");
                        tabCol5.setHeaderValue("Runtime");

                        header.repaint();


                        List<Object[]> list = new ArrayList<Object[]>();


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
                            String sqlStatement = ( // query to find trending between inputted dates
                                    "SELECT original_title, start_year, genres, average_rating, runtime_minutes " +
                                            "FROM titles " +
                                            "INNER JOIN customer_ratings " +
                                            "ON titles.title_id = customer_ratings.title_id " +
                                            "WHERE customer_ratings.date_posted " +
                                            "BETWEEN '" + startDate + "' AND '" + endDate + "'" +
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
        comboBox1.addItem("Hollywood Pairs");
        comboBox1.addItem("Tomato Number");
        comboBox1.addItem("Date");

        List<Object[]> list = new ArrayList<Object[]>();

        dbSetup my = new dbSetup();
        //Building the connection
        Connection conn = null;


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

        model = new DefaultTableModel(data, columnNames);

        myTable = new JTable(model);
        myTable.setFillsViewportHeight(true);
        myTable.setEnabled(false);
    }
}
