import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Trending extends JFrame {
    private JPanel trendingPanel;
    private JLabel titleLabel;
    private JButton filterButton;
    private JButton searchButton;
    //private JTable trendingTable;
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
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Search search = new Search();
                search.setVisible(true);
                Trending.this.dispose();
            }
        });

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                columnNames = new String[]{"Most Chemistry Between:"};
                String title = "";

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

//                           if (result.absolute(1)) {
//                               System.out.println("ROW 1");
//                           }
//
//                            if (result.absolute(2)) {
//                                System.out.println("ROW 2");
//                            }



                            Object[] information = {hollywoodPairs};
                            System.out.println(information[0]);
                            model.addRow(information);
                        }
                        System.out.println("HERE 2");

                    } catch (Exception x) {
                        System.out.println("Error accessing Database.");
                    }

                }


            }
        });
    }

    private void createUIComponents() {
        comboBox1 = new JComboBox();
        comboBox1.addItem("Hollywood Pairs");

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

        Object[][] data = list.toArray(new Object[list.size()][5]);



        String[] columnNames = {"Title", "Year", "Genre", "Avg Review", "Runtime"};





//        Object[][] data = new Object[2][1];
//        data[0][0] = "Names";
        model = new DefaultTableModel(data, columnNames);

        myTable = new JTable(model);
        myTable.setFillsViewportHeight(true);
        myTable.setEnabled(false);
    }
}
