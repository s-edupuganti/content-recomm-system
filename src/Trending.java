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

public class Trending extends JFrame {
    private JPanel trendingPanel;
    private JLabel titleLabel;
    private JButton filterButton;
    private JButton backButton;
    private JScrollBar scrollBar1;
    private JTable myTable;
    private JScrollPane tableScrollPane;
    private JComboBox comboBox1;
    public DefaultTableModel model;
    public String[] columnNames;


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

                dbSetup my = new dbSetup();
                //Building the connection
                Connection conn = null;
                String s = (String) comboBox1.getSelectedItem();
                if (s.equals("Hollywood Pairs")) {
                    model.setColumnCount(1);
                    model.setRowCount(0);
                    JTableHeader header = myTable.getTableHeader();
                    TableColumnModel colMod = header.getColumnModel();
                    TableColumn tabCol = colMod.getColumn(0);
                    tabCol.setHeaderValue("Hollywood Pairs");
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

                        // FIRST SQL QUERY
                        String sqlStatement1 = (
                                "SELECT title_id FROM titles WHERE average_rating IN" +
                                        " (SELECT MAX(average_rating) FROM titles) LIMIT 1;"
                        );

                        System.out.println(sqlStatement1);
                        //send statement to DBMS
                        ResultSet result1 = stmt.executeQuery(sqlStatement1);

                        //OUTPUT
                        System.out.println("Database");
                        System.out.println("______________________________________");

                        while (result1.next()) {
                            title = result1.getString("title_id");
                        }
                        System.out.println(title);

                        // SECOND SQL QUERY
                        //create an SQL statement
                        String sqlStatement = (
                                "SELECT principals.title_id, string_agg(primary_name, ', ') AS hollywood_pairs FROM names INNER JOIN principals ON names.nmconst = " +
                                        "principals.nmconst AND title_id='" + title +"' AND category LIKE '%act%' GROUP BY 1 LIMIT 2;"
                        );

                        System.out.println(sqlStatement);
                        //send statement to DBMS
                        ResultSet result = stmt.executeQuery(sqlStatement);

                        //OUTPUT
                        System.out.println("Database");
                        System.out.println("______________________________________");


                        while (result.next()) {
                            System.out.println("HERE 1");

                           String hollywoodPairs =  result.getString("hollywood_pairs");


                            Object[] information = {hollywoodPairs};
                            System.out.println(information[0]);
                            model.addRow(information);
                        }
                        System.out.println("HERE 2");

                    } catch (Exception x) {
                        System.out.println("Error accessing Database.");
                    }

                } else if (s.equals("Tomato Number")) {

                    tomatoTitle1 = JOptionPane.showInputDialog("Please input first content title:");

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

                        System.out.println(sqlStatement);
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

                        System.out.println(sqlStatement2);
                        //send statement to DBMS
                        ResultSet result2 = stmt.executeQuery(sqlStatement2);

                        //OUTPUT
                        System.out.println("Database");
                        System.out.println("______________________________________");


                        while (result2.next()) {

                            String customer = result2.getString("customer_id");

                            customerSeen2.add(customer);
                        }

                        Map<String, ArrayList<String>> tomatoMap1 = new HashMap<>();
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


                        System.out.println("REACHED END");


                    } catch (Exception f){
                        System.out.println("Error accessing Database.");
                    }

                }


            }
        });
    }

    private void createUIComponents() {
        comboBox1 = new JComboBox();
        comboBox1.addItem("Hollywood Pairs");
        comboBox1.addItem("Tomato Number");

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

            System.out.println(sqlStatement);
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
