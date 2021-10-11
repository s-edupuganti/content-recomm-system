import javax.swing.*;
import javax.swing.text.View;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
//import org.apache.commons.lang3.math.NumberUtils;


public class Login extends JFrame {
    private JPanel recommendedPanel;
    private JTable recTable;
    private JButton nextButton;
    private JButton backButton;
    private JButton back_home;
    private JPanel Login_Panel;
    private JButton searchButton;
    private JLabel titleLabel;
    private JPasswordField passwordField1;
    private JPanel Main_panel;
    private JButton goButton;
    private JTextField textField1;

    public static String userInfo = "";
    public static int userInfoInt;
    public static Connection conn;

    public Login() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(Login_Panel);
        this.pack();

        back_home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home home = new Home("Home page");
                home.setVisible(true);
                Login.this.dispose();
            }
        });
        //Recommended rec = new Recommended();
        //rec.setVisible(true);
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {


                try{
                    Integer.parseInt(userInfo);
                    userInfoInt = Integer.parseInt(userInfo);


                    dbSetup my = new dbSetup();
                    //Building the connection
                    conn = null;


                    try {
                        //Class.forName("org.postgresql.Driver");
                        conn = DriverManager.getConnection(
                                "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315_914_5_db",
                                my.user, my.pswd);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getClass().getName()+": "+e.getMessage());
                        System.exit(0);
                    }//end try catch
                    System.out.println("Opened database successfully");
                    String cus_lname = "";
                    //  String username = "";
                    try{
                        //create a statement object
                        Statement stmt = conn.createStatement();
                        //create an SQL statement
                        String sqlStatement = "SELECT customer_id FROM customer_ratings WHERE customer_id = '" + userInfoInt + "' LIMIT 1;";
                        System.out.println(sqlStatement);
                        //send statement to DBMS
                        ResultSet result = stmt.executeQuery(sqlStatement);

                        //OUTPUT
                        System.out.println("Database");
                        System.out.println("______________________________________");

                        System.out.println(result.isBeforeFirst());
                        if (!result.isBeforeFirst()) {
                            //System.out.println("ERROR: Incorrect User Login Info");
                            JOptionPane.showMessageDialog(Login.this,
                                    "ERROR: Incorrect User Login Info");
                        } else {

//                            System.out.println(result.getString("customer_id"));
//
//                            Recommended rec = new Recommended();
//                            rec.setVisible(true);
//                            Login.this.dispose();

                        }




                        while (result.next()) {

                            System.out.println(result.getString("customer_id"));

                            Recommended rec = new Recommended();
                            rec.setVisible(true);
                            Login.this.dispose();


                        }
                    } catch (Exception e){
                        System.out.println("Error accessing Database.");
                    }


//                    Recommended rec = new Recommended();
//                    rec.setVisible(true);
//                    Login.this.dispose();



                }
                catch (NumberFormatException f) {
                    JOptionPane.showMessageDialog(Login.this,
                            "ERROR: Incorrect User Login Info");

                }



            }
        });

        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                userInfo = ((JTextField)e.getSource()).getText() + String.valueOf(e.getKeyChar());

//                try{
//                    Integer.parseInt(userInfo);
//                    userInfoInt = Integer.parseInt(userInfo);
//                }
//                catch (NumberFormatException f) {
//                    JOptionPane.showMessageDialog(Login.this,
//                            "ERROR: Incorrect User Login Info");
//
//                }







            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        textField1 = new JTextField();
    }
}
