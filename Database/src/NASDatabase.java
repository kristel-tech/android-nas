import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;


public class NASDatabase {
    private static final String DatabaseURL = "jdbc:mysql://localhost:<port number>/<DB name>";
    private static final String user = "user";
    private static final String password = "password";
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultset;

    //this function returns the records of file transfers between two devices
    //the device that requests this function can have its IP address sourced by a WifiManager object,
    // while the IP address of the other device must be provided

    public void ShowFilesByDeviceID(String DeviceIPAddress, String OwnIPAddress) throws SQLException {
        try {
            connection = DriverManager.getConnection(DatabaseURL, user, password);
            statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM FileHistory WHERE (SenderIP = " + DeviceIPAddress + " AND ReceiverIP = " + OwnIPAddress + ") OR ReceiverIP = " + DeviceIPAddress + " AND SenderIP = " + OwnIPAddress + ";";
            resultset = statement.executeQuery(SQLQuery);
            while (resultset.next()) {
                System.out.println(resultset.next());
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }
    //this function returns all the records of file transfers of a single device
    //the device that requests this function can have its IP address sourced by a WifiManager object
    public void ShowFileHistory(String OwnIPAddress)  throws SQLException {
        try {
            connection = DriverManager.getConnection(DatabaseURL, user, password);
            statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM FileHistory WHERE SenderIP = " + OwnIPAddress + " OR ReceiverIP = " + OwnIPAddress + ";";
            resultset = statement.executeQuery(SQLQuery);
            while (resultset.next()) {
                System.out.println(resultset.next());
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }
    //this function returns all the records of file transfers from a single date from a device
    //the device that requests this function can have its IP address sourced by a WifiManager object
    public void ShowFilesByDate(String OwnIPAddress, LocalDateTime filedate)  throws SQLException {
        try {
            connection = DriverManager.getConnection(DatabaseURL, user, password);
            statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM FileHistory WHERE (SenderIP = " + OwnIPAddress + " OR ReceiverIP = " + OwnIPAddress + ") AND DateSent = " + filedate + ";";
            resultset = statement.executeQuery(SQLQuery);
            while (resultset.next()) {
                System.out.println(resultset.next());
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }
    //this function records when a file transfer between two devices takes place

    public void CreateFileRecord(String SenderIP, String ReceiverIP, double FileSizeInMegabytes, LocalDateTime local, String filename) throws SQLException {
       try {
           connection = DriverManager.getConnection(DatabaseURL, user, password);
           statement = connection.createStatement();
           String SQLQuery = "INSERT INTO FileHistory(SenderIP,ReceiverIP,FileSizeInMegabytes,DateSent,FileName) VALUES(" + SenderIP + ", " + ReceiverIP + ", " + FileSizeInMegabytes + ", " + local + ", " + filename + ");";
           statement.executeUpdate(SQLQuery);
       }
       catch (SQLException sqlEx) {
           sqlEx.printStackTrace();
       }
       finally {
           closeConnection();
       }
    }
    public void closeConnection() {
        try {
            connection.close();
        }
        catch(SQLException se) {
            se.printStackTrace();
        }
        try {
            statement.close();
        }
        catch(SQLException se) {
            se.printStackTrace();
             }
        try {
            resultset.close();
        }
        catch(SQLException se) {
            se.printStackTrace();
        }
    }
}
