package com.example.myapplication;

import android.widget.Switch;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DatabaseInfo {

    final String strMessage = "https://phpjavapass.000webhostapp.com/";
    private String SQL;
    private String SenderIP;
    private String ReceiverIP;
    private String FileSizeInMegabytes;
    private String DateSent;
    private String result;
    public boolean ReceivedResult;
    public boolean ReceivedError;

    public DatabaseInfo(){
        this.SQL = null;

        this.SenderIP = null;
        this.ReceiverIP = null;
        this.FileSizeInMegabytes = null;
        this.DateSent = null;
        this.result = null;
        ReceivedResult = false;
        ReceivedError = false;
    }

    public void SQLCall(String CallType){
        switch(CallType){
            case "FilesByDate":
                SQL = encodeValue("SELECT * FROM FileHistory WHERE (SenderIP = ? OR ReceiverIP = ?) AND DateSent = ? ORDER BY DateSent DESC;");
                break;
            case "ShowFileHistory":
                SQL = encodeValue("SELECT * FROM FileHistory WHERE SenderIP = ? OR ReceiverIP = ? ORDER BY DateSent DESC;");
                break;
            case "ByDeviceID":
                SQL = encodeValue("SELECT * FROM FileHistory WHERE (SenderIP = ? AND ReceiverIP = ?) OR ReceiverIP = ? AND SenderIP = ? ORDER BY DateSent DESC;");
                break;
            case "create":
                SQL = encodeValue("INSERT INTO FileHistory(SenderIP, ReceiverIP, FileSizeInMegabytes, DateSent, FileName) VALUES (?,?,?,CURRENT_TIMESTAMP,?) ORDER BY DateSent DESC;");
                break;
        }
    }

    public void SetResult(String result){
        this.result = result;
        ReceivedResult = true;
    }

    public void SetErrorResult(String result){
        this.result = result;
        ReceivedResult = true;
        ReceivedError = true;
    }

    public String getResult(){
        return result;
    }

    public void setFileHistoryPostData(String SenderIP , String ReceiverIP, String FileSizeInMegabytes, String DateSent){
        this.SenderIP = SenderIP;
        this.ReceiverIP = ReceiverIP;
        this.FileSizeInMegabytes = FileSizeInMegabytes;
        this.DateSent = DateSent;
    }

    public String getRequestFileHistoryData(){
        return strMessage+"?SenderIP="+SenderIP+"&ReceiverIP="+ReceiverIP+"&FileSizeInMegabytes="+FileSizeInMegabytes+"&DateSent="+DateSent;
    }

    public static String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

}
