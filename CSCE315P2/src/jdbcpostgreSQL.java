import java.sql.*;
//import javax.swing.JOptionPane;

/*
CSCE 315
9-25-2019 Original
2/7/2020 Update for AWS
 */
public class jdbcpostgreSQL {
  public static void main(String args[]) {
    //dbSetup hides my username and password
    dbSetup my = new dbSetup();
    //Building the connection
     Connection conn = null;
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
       String sqlStatement = "SELECT customer_id FROM customer_ratings WHERE customer_id = '401047456' LIMIT 1;";
       //send statement to DBMS
       ResultSet result = stmt.executeQuery(sqlStatement);

       //OUTPUT
       System.out.println("Database");
       System.out.println("______________________________________");


      if (!result.next()) {
        System.out.println("ERROR: Incorrect User Login Info");

      }


       while (result.next()) {

        // if (!result.getString("customer_id")) {

        // }

        // username = result.getString("customer_id");


          System.out.println(result.getString("customer_id"));
    

       }
   } catch (Exception e){
     System.out.println("Error accessing Database.");
   }
    //closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    }//end try catch
  }//end main
}//end Class
