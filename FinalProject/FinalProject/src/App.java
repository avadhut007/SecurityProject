import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;





public class App {

    static void insertIntoDB(Connection con,String Name,String Phone){
        String query = "insert into personinfo(name,phnumber) values(?, ?)";
        int insertedRecordCount;
        try (PreparedStatement pStatement = con.prepareStatement(query)) {
            pStatement.setString(1, Name);
            pStatement.setString(2, Phone);

            insertedRecordCount = pStatement.executeUpdate();
            System.out.println(insertedRecordCount+" record inserted");
        }catch (SQLException e) {

            System.out.println("Already exists in DB - insert");
            System.exit(1);
            e.printStackTrace();
        }
        try {
            //Long uid = new com.sun.security.auth.module.UnixSystem().getUid();
            FileWriter myWriter = new FileWriter("G:\\MSCS\\Sem1\\CSE5382\\Assignment11\\logs.txt", true);
            String timeStamp = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss").format(new Date());
            //myWriter.write(timeStamp+" Userid: "+ uid +" added "+Name+ " in the DB");
            myWriter.write(timeStamp+" Userid:  added "+Name+ " in the DB");
            myWriter.write(System.lineSeparator());
            myWriter.close();
            System.out.println("Successfully wrote to the file. - insert");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }

    }
    static void delFromDB(Connection con,String NumOrPh){
        // use or condtion in delete statement
        String query = "delete from personinfo where name = ? or phnumber = ?";
        int deletedRecordCount;
        try (PreparedStatement pStatement = con.prepareStatement(query)) {
            pStatement.setString(1, NumOrPh);
            pStatement.setString(2, NumOrPh);
            deletedRecordCount = pStatement.executeUpdate();
            System.out.println(deletedRecordCount+" record deleted");
        }catch (SQLException e) {

            System.out.println("Does not exist in the DB - delete");
            System.exit(1);
            e.printStackTrace();
        }

        try {
            //Long uid = new com.sun.security.auth.module.UnixSystem().getUid();
            FileWriter myWriter = new FileWriter("G:\\MSCS\\Sem1\\CSE5382\\Assignment11\\logs.txt", true);      
            String timeStamp = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss").format(new Date());
            //myWriter.write(timeStamp+" Userid: "+ uid +" removed "+NumOrPh+ " from the DB");
            myWriter.write(timeStamp+" Userid: removed "+NumOrPh+ " from the DB");  
            myWriter.write(System.lineSeparator());
            myWriter.close();
            System.out.println("Successfully wrote to the file. - delete");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
    static void listFromDB(Connection con){
        String query = "select * from personinfo";
        try (PreparedStatement pStatement = con.prepareStatement(query)) {
            try (ResultSet result = pStatement.executeQuery()) {
                while (result.next()) {
                    System.out.println("Name: "+result.getString("name")+"   Phone: "+result.getString("phnumber"));
                }
            }
        }catch (SQLException e) {

            System.out.println("Error connecting to the DB - list");
            System.exit(1);
            e.printStackTrace();
        }

        try {
            //Long uid = new com.sun.security.auth.module.UnixSystem().getUid();
            FileWriter myWriter = new FileWriter("G:\\MSCS\\Sem1\\CSE5382\\Assignment11\\logs.txt", true);
            String timeStamp = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss").format(new Date());
            //myWriter.write(timeStamp+" Userid: "+ uid +" listed data from the DB");
            myWriter.write(timeStamp+" Userid:  listed data from the DB");
            myWriter.write(System.lineSeparator());
            myWriter.close();
            System.out.println("Successfully wrote to the file. - List");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    public static void main(String[] args) throws Exception {
        //java -cp  ".;sqlite-jdbc-3.36.0.jar" App ADD 'Sdsffd Fsds' "12345"

        try {
            File myObj = new File("G:\\MSCS\\Sem1\\CSE5382\\Assignment11\\logs.txt");
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            } else {
              System.out.println("File already exists.");
            }
          } catch (IOException e) {
            System.out.println("An error occurred when checking log file");
            e.printStackTrace();
          }

        String jdbcURL = "jdbc:sqlite:G:\\MSCS\\Sem1\\CSE5382\\Assignment11\\db\\mydb.db";
        Connection connection = null;
        try {
            
            connection = DriverManager.getConnection(jdbcURL);
            
            ArrayList<String> AllowedInputModes = new ArrayList<>();
            AllowedInputModes.add("add");
            AllowedInputModes.add("del");
            AllowedInputModes.add("list");

            if (!AllowedInputModes.contains(args[0].toLowerCase())) {
            System.out.println(" Input Mode return 1");
            System.exit(1);
            }

            Integer NoOfInputs = args.length;
            //System.out.println(" len is "+NoOfInputs );
            String PhRegex = "(\\+?)[0-9]*[0-9 \\-*(\\(\\))*\\.*]{5,20}$";
            String NameRegex = "[A-Z]?\\'?[A-Z][a-z]{2,20}\\,?(\\s+[A-Z]?\\'?[A-Z][a-z]+\\-?\\s?[A-Za-z]*\\.?)?";
            
            if (args[0].toLowerCase().equals("add") && NoOfInputs == 3){
                Boolean NameCondition = args[1].matches(NameRegex);
                if (!NameCondition) {
                    System.out.println(" NameCondition return 1");
                    System.exit(1);
                }
    
                Boolean PhCondition = args[2].matches(PhRegex);
                System.out.println("input condition are: 2. "+NameCondition+" 3. "+PhCondition);
                if (!PhCondition) {
                    System.out.println(" PhCondition return 1");
                    System.exit(1);
                }
                insertIntoDB(connection,args[1],args[2]);

            } else if (args[0].toLowerCase().equals("del") && NoOfInputs == 2){
                
                Boolean NameCondition = args[1].matches(NameRegex);
                Boolean PhCondition = args[1].matches(PhRegex);
                //System.out.println("input condition are: 2. "+NameCondition+" 3. "+PhCondition);
                if (NameCondition || PhCondition) {
                    delFromDB(connection,args[1]); 
                }
                else {
                    System.out.println(" NameCondition return 1");
                    System.exit(1);
                }
       
            } else if (args[0].toLowerCase().equals("list") && NoOfInputs == 1){
                listFromDB(connection); 
            }
            //Do the logging

            //System.out.println("inputs are: 1. "+args[0].toLowerCase()+" 2. "+args[1]+" 3. "+args[2]);

            
        } catch (SQLException e) {

            System.out.println("Error connecting to the DB");
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        System.exit(0);

        // Final (\+?)[0-9]*[0-9 \-*(\(\))*\.*]{5,20}$

        //Final [A-Z]?\'?[A-Z][a-z]{2,20}\,?(\s+[A-Z]?\'?[A-Z][a-z]+\-?\s?[A-Za-z]*\.?)?

    }

}
