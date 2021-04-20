import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class home {
	
    public static void main(String args[]) throws Exception {

    	NASDatabase hold = new NASDatabase();

        //public void CreateFileRecord(String SenderIP, String ReceiverIP, double FileSizeInMegabytes, LocalDateTime local, String filename) throws SQLException 
        try{
        hold.CreateFileRecord("tttttt", "ttttt", 3.3, "bihhhh");
        }
        catch(Exception e ){
            System.out.println("shit happans");
            System.out.println(e);
        }
        System.out.println("hello");


    }

}
