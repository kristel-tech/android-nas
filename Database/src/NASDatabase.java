import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class NASDatabase {
    private static final String DatabaseURL = "jdbc:mysql://sql10.freemysqlhosting.net:3306/sql10406521";
    private static final String user = "sql10406521";
    private static final String password = "DDup1ZBbEw";
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultset;

    //this function returns the records of file transfers between two devices
    //the device that requests this function can have its IP address sourced by a WifiManager object,
    // while the IP address of the other device must be provided

    public void ShowFilesByDeviceID(String DeviceIPAddress, String OwnIPAddress) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DatabaseURL, user, password);
            String SQLQuery = "SELECT * FROM FileHistory WHERE (SenderIP = ? AND ReceiverIP = ?) OR ReceiverIP = ? AND SenderIP = ?;";
            PreparedStatement identity = connection.prepareStatement(SQLQuery);
            identity.setString(1, OwnIPAddress);
            identity.setString(2, DeviceIPAddress);
            identity.setString(3, OwnIPAddress);
            identity.setString(4, DeviceIPAddress);
            resultset = identity.executeQuery(SQLQuery);
            while (resultset.next()) {
                System.out.println(resultset.next());
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }catch(Exception e){
            System.out.println("we are not flying");
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
            String SQLQuery = "SELECT * FROM FileHistory WHERE SenderIP = ? OR ReceiverIP = ?;";
            PreparedStatement filehistory = connection.prepareStatement(SQLQuery);
            filehistory.setString(1, OwnIPAddress);
            filehistory.setString(2, OwnIPAddress);
            resultset = filehistory.executeQuery(SQLQuery);
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
    public void ShowFilesByDate(String OwnIPAddress, Timestamp filedate)  throws SQLException {
        try {
            connection = DriverManager.getConnection(DatabaseURL, user, password);
            String SQLQuery = "SELECT * FROM FileHistory WHERE (SenderIP = ? OR ReceiverIP = ?) AND DateSent = ?;";
            PreparedStatement datesearch = connection.prepareStatement(SQLQuery);
            datesearch.setString(1, OwnIPAddress);
            datesearch.setString(2, OwnIPAddress);
            datesearch.setTimestamp(3, filedate);
            resultset = datesearch.executeQuery(SQLQuery);
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

    public void CreateFileRecord(String SenderIP, String ReceiverIP, double FileSizeInMegabytes, String filename) throws SQLException {
       try {
           connection = DriverManager.getConnection(DatabaseURL, user, password);
           
           String SQLQuery =  "INSERT INTO filehistory(SenderIP, ReceiverIP, FileSizeInMegabytes, DateSent, FileName) VALUES (?,?,?,CURRENT_TIMESTAMP,?);";
           
           PreparedStatement updatefilehistory = connection.prepareStatement(SQLQuery);
           updatefilehistory.setString(1, SenderIP);
           updatefilehistory.setString(2, ReceiverIP);
           updatefilehistory.setDouble(3, FileSizeInMegabytes);
           updatefilehistory.setString(4, filename);
           updatefilehistory.executeUpdate();
           
       }
       catch (SQLException sqlEx) {
           sqlEx.printStackTrace();
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