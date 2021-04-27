package com.example.myapplication;

import android.widget.Switch;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DatabaseInfo {

    final String strMessage = "https://javanas.000webhostapp.com//";
    private String SQL;
    private DBCallType callType;
    private String SenderIP;
    private String ReceiverIP;
    private String FileSizeInMegabytes;
    private String DateSent;
    private String FileName;
    private String result;
    private String username;
    public boolean ReceivedResult;
    public boolean ReceivedError;

    public DatabaseInfo(){
        this.SQL = null;
        this.callType = DBCallType.EMPTY;
        this.SenderIP = null;
        this.ReceiverIP = null;
        this.FileSizeInMegabytes = null;
        this.DateSent = null;
        this.FileName = null;
        this.result = null;
        this.username = null;
        ReceivedResult = false;
        ReceivedError = false;
    }


    public void SQLCall(DBCallType CallType){
        this.callType = CallType;
        switch(CallType){
            case FILESBYDATE:
                SQL = encodeValue("SELECT * FROM FileHistory WHERE (SenderIP = ? OR ReceiverIP = ?) AND DateSent = ? ORDER BY DateSent DESC;");
                break;
            case SHOWFILEHISTORY:
                SQL = encodeValue("SELECT * FROM FileHistory WHERE SenderIP = ? OR ReceiverIP = ? ORDER BY DateSent DESC;");
                break;
            case BYDEVICEID:
                SQL = encodeValue("SELECT * FROM FileHistory WHERE (SenderIP = ? AND ReceiverIP = ?) OR ReceiverIP = ? AND SenderIP = ? ORDER BY DateSent DESC;");
                break;
            case SMALLERTHANSIZE:
                SQL = encodeValue("SELECT * FROM FileHistory WHERE (SenderIP = ? OR ReceiverIP = ?) AND FileSizeInMegabytes < ? ORDER BY DateSent DESC;");
                break;
            case LARGERTHANSIZE:
                SQL = encodeValue("SELECT * FROM FileHistory WHERE (SenderIP = ? OR ReceiverIP = ?) AND FileSizeInMegabytes > ? ORDER BY DateSent DESC;");
                break;
            case CREATE:
                SQL = encodeValue("INSERT INTO FileHistory(SenderIP, ReceiverIP, FileSizeInMegabytes, DateSent, FileName) VALUES (?,?,?,CURRENT_TIMESTAMP,?);");
                break;
            case CREATELOGIN:
                SQL = encodeValue("INSERT INTO LoginTable (UserName, DateSet) VALUES (?,CURRENT_TIMESTAMP);");
                break;
            case DELETELOGIN:
                SQL = encodeValue("DELETE FROM LoginTable WHERE UserName = ?");
                break;
            case GETLOGIN:
                SQL = encodeValue("SELECT * FROM LoginTable WHERE UserName = ? AND Password <= CURRENT_TIMESTAMP - 200;");
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


    public void setLoginPostData(String username){
        this.username = username;
    }


    public void setFileHistoryPostData(String SenderIP , String ReceiverIP, String FileSizeInMegabytes, String DateSent, String FileName){
        this.SenderIP = SenderIP;
        this.ReceiverIP = ReceiverIP;
        this.FileSizeInMegabytes = FileSizeInMegabytes;
        this.DateSent = DateSent;
        this.FileName = FileName;
    }

    private String CompileString(String... values){
        String concat = "";
        int index = 0;
        for(String s:values){
            concat += "&value" + String.valueOf(index++) + "=" + encodeValue(s);
        }
        return "&len=" + String.valueOf(index) + concat;
    }

    private String mustreturn(boolean returnsomething){
        if (returnsomething)
            return "&mustreturn=true";
        else
            return "&mustreturn=false";
    }

    public String getRequestFileHistoryData(){
        String values = "";
        Boolean needreturn = true;
        switch(callType){
            case FILESBYDATE:
                values = CompileString(SenderIP,ReceiverIP, DateSent);
                needreturn = true;
//                        SELECT * FROM FileHistory WHERE (SenderIP = ? OR ReceiverIP = ?) AND DateSent = ? ORDER BY DateSent DESC;
                break;
            case SHOWFILEHISTORY:
                values = CompileString(SenderIP,ReceiverIP, DateSent);
                needreturn = true;
//                        SELECT * FROM FileHistory WHERE SenderIP = ? OR ReceiverIP = ? ORDER BY DateSent DESC;
                break;
            case BYDEVICEID:
                values = CompileString(SenderIP,ReceiverIP, SenderIP,ReceiverIP);
                needreturn = true;
//                        SELECT * FROM FileHistory WHERE (SenderIP = ? AND ReceiverIP = ?) OR ReceiverIP = ? AND SenderIP = ? ORDER BY DateSent DESC;
                break;
            case SMALLERTHANSIZE:
                values = CompileString(SenderIP,ReceiverIP, FileSizeInMegabytes);
                needreturn = true;
//                      SELECT * FROM FileHistory WHERE (SenderIP = ? OR ReceiverIP = ?) AND FileSizeInMegabytes < ? ORDER BY DateSent DESC;
                break;
            case LARGERTHANSIZE:
                values = CompileString(SenderIP,ReceiverIP, FileSizeInMegabytes);
                needreturn = true;
//                      SELECT * FROM FileHistory WHERE (SenderIP = ? OR ReceiverIP = ?) AND FileSizeInMegabytes > ? ORDER BY DateSent DESC;
                break;
            case CREATE:
                values = CompileString(SenderIP,ReceiverIP, FileSizeInMegabytes,FileName);
                needreturn = false;
//                        INSERT INTO FileHistory(SenderIP, ReceiverIP, FileSizeInMegabytes, DateSent, FileName) VALUES (?,?,?,CURRENT_TIMESTAMP,?) ORDER BY DateSent DESC;
                break;
            case CREATELOGIN:
                values = CompileString(username);
                needreturn = false;
                // INSERT INTO LoginTable (UserName, DateSet) VALUES (?,CURRENT_TIMESTAMP);
                break;
            case DELETELOGIN:
                values = CompileString(username);
                needreturn = false;
                // INSERT INTO LoginTable (UserName, DateSet) VALUES (?,CURRENT_TIMESTAMP);
                break;
            case GETLOGIN:
                values = CompileString(username);
                needreturn = true;
                // SELECT * FROM LoginTable WHERE UserName = ? AND DateSet <= CURRENT_TIMESTAMP - 200;
                break;
        }
//        SELECT CURRENT_TIMESTAMP
//        UNION
//        SELECT CURRENT_TIMESTAMP - 1000
        return strMessage+"?SQL="+SQL+mustreturn(needreturn)+values;
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
