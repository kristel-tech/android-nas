
import java.io.*;
import java.time.LocalDateTime;

class ErrorSingleton {
    private static ErrorSingleton singleton;
    public static Exception error;
    private ErrorSingleton(Exception err,String errormessage) throws IOException {
        error = err;
        String filepath = "errorlog.txt";
        FileWriter errorwriter = new FileWriter(filepath);
        String error = "Error Message: " + errormessage;
        errorwriter.write(LocalDateTime.now().toString() + " " + error);
        errorwriter.close();
    }
    public static ErrorSingleton instance(Exception err, String errormessage) throws IOException {
        if (singleton == null) {
            singleton = new ErrorSingleton(err,errormessage);
        }
        return singleton;
    }
}