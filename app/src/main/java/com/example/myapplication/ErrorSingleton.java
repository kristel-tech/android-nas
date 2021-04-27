package com.example.myapplication;
import java.io.*;

class ErrorSingleton {
    private static ErrorSingleton singleton;
    public static Exception error;
    private ErrorSingleton(Exception err,String errormessage) {
        error = err;
        String filepath = "errorlog.txt";
        FileWriter errorwriter = new FileWriter(filepath);
        String error = "Error Message: " + errormessage;
        errorwriter.Write(DateTime.Now.ToString() + " " + error);
        errorwriter.Close();
    }
    public static ErrorSingleton instance(Exception err, String errormessage){
        if (singleton == null) {
            singleton = new ErrorSingleton(err,errormessage);
        }
        return singleton;
    }
}