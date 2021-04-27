package com.example.myapplication;

import android.content.ContentResolver;
import android.os.Environment;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class httphandle extends Thread {

    static final String HTML_START = "<html>" + "<title>HTTP Server in java</title>" + "<body>";

    static final String HTML_END = "</body>" + "</html>";

    ServerSocket Server;
    Socket connectedClient;


    BufferedReader inFromClient = null;
    DataOutputStream outToClient = null;
    static String response = "";

    public void run(){

        ServerSocket Server = null;
        try {
            Server = new ServerSocket(5000, 10, InetAddress.getByName("127.0.0.1"));
            Log.d("MyApp","====================================================");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {

            try {
//                ServerSocket Server = new ServerSocket(5000, 10, InetAddress.getByName("127.0.0.1"));
                while (true) {
                    connectedClient = Server.accept();
                    runsocket();

                }
            }catch(Exception e) {
                System.out.println("shit happens");
                System.out.println(e);
            }
        }
    }

    public void runsocket() {

        try {

            System.out.println("The Client " + connectedClient.getInetAddress() + ":" + connectedClient.getPort()
                    + " is connected");

            inFromClient = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
            outToClient = new DataOutputStream(connectedClient.getOutputStream());
            System.out.println();
            String requestString = inFromClient.readLine();


            String headerLine = requestString;

//            StringTokenizer tokenizer = new StringTokenizer(headerLine);
//            String httpMethod = tokenizer.nextToken();
//            String httpQueryString = tokenizer.nextToken();
            if (headerLine == null)
                return;
            String[] requeststring =  headerLine.replaceAll (" HTTP/1.1", "").split("/", 3);
//            String[] requeststring1 =  headerLine.substring(1).split("/", 3);
//            for(String i : requeststring)
//                Log.d("MyApp","========================>"+i+"<============================");

//            Log.d("MyApp","========================>"+requeststring.length + "=======" +requeststring[0] + "<=====>" + requeststring[1] + "<===> "+ headerLine+"<============================");
            StringBuffer responseBuffer = new StringBuffer();
            responseBuffer.append("<b> This is the HTTP Server Home Page.... </b><BR>");
            responseBuffer.append("The HTTP Client request is ....<BR>");

            System.out.println("The HTTP request string is ....");
            while (inFromClient.ready()) {
                // Read the HTTP complete HTTP Query
                responseBuffer.append(requestString + "<BR>");
                System.out.println(requestString);
                requestString = inFromClient.readLine();
            }

            if (requeststring.length >= 2 && requeststring[0].trim().equals("GET")) {

                boolean CheckIfLoggedIn = false;
                try {
                    if (requeststring[1].trim().equals("List"))
                        CheckIfLoggedIn = checklogin(requeststring[2].trim());
                    else if (requeststring[1].trim().equals("getfile"))
                        CheckIfLoggedIn = checklogin(requeststring[3].trim());
                }catch (Exception e){
                    CheckIfLoggedIn = false;
                }

                if (!CheckIfLoggedIn){
                    sendResponse(403, httphandle.HTML_START + "<b>Forbidden</b>" + httphandle.HTML_END, false);
                }
                else if (requeststring[1].trim().equals("List")){


                    String filesPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
//                    Log.d("Files", "Path: " + path);
                    File directory = new File(filesPath);
                    File[] files = directory.listFiles();
//
                    String returnString = new String();
                    int file_size = 0;
                    for (int i = 0; i < files.length; i++)
                    {
                        returnString = filehandleobj.getInstence().getJsonList();
//
                    }
                        sendResponse(200, returnString, false);
                }
                else if (requeststring[1].trim().equals("getfile")){

                    ContentResolver resolver = MainActivity.getAppContext().getContentResolver();
                    DocumentFile docfile = filehandleobj.getInstence().getDocfiles(Integer.valueOf(requeststring[2]));
                    InputStream InputStream  = resolver.openInputStream(docfile.getUri());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(InputStream));

                    StringBuilder stringBuilder = new StringBuilder();
                    String currentline;
                    while ((currentline = reader.readLine()) != null) {
                        stringBuilder.append(currentline + "\n");
                    }

                    Log.d("MyApp","===================>"+stringBuilder+" <===================");
//                    outToClient.writeBytes(stringBuilder.toString());
//                    outToClient.close();
                    InputStream.close();

                    sendResponse(200, stringBuilder.toString(), false);
                }else {
                        sendResponse(404, httphandle.HTML_START + "<b>The Requested resource not found ...."
                                + "Usage: http://127.0.0.1:5000 or http://127.0.0.1:5000/</b>" + httphandle.HTML_END, false);
                }
            }else
                sendResponse(404, httphandle.HTML_START + "<b>The Requested resource not found ...."
                        + "Usage: http://127.0.0.1:5000 or http://127.0.0.1:5000/</b>" + httphandle.HTML_END, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void sendResponse(int statusCode, String responseString, boolean isFile) throws Exception {

        String statusLine = null;
        String serverdetails = "Server: Java HTTPServer";
        String contentLengthLine = null;
        String fileName = null;
        String contentTypeLine = "Content-Type: text/html" + "\r\n";
        FileInputStream fin = null;

        if (statusCode == 200)
            statusLine = "HTTP/1.1 200 OK" + "\r\n";
        else
            statusLine = "HTTP/1.1 404 Not Found" + "\r\n";

        if (isFile) {
            fileName = responseString;
            fin = new FileInputStream(fileName);
            contentLengthLine = "Content-Length: " + Integer.toString(fin.available()) + "\r\n";
            if (!fileName.endsWith(".htm") && !fileName.endsWith(".html"))
                contentTypeLine = "Content-Type: \r\n";
        } else {
            responseString = responseString;
            contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";
        }

        outToClient.writeBytes(statusLine);
        outToClient.writeBytes(serverdetails);
        outToClient.writeBytes(contentTypeLine);
        outToClient.writeBytes(contentLengthLine);
        outToClient.writeBytes("Connection: close\r\n");
        outToClient.writeBytes("\r\n");

        if (isFile)
            sendFile(fin, outToClient);
        else
            outToClient.writeBytes(responseString);

        outToClient.close();
    }

    public void sendFile(FileInputStream fin, DataOutputStream out) throws Exception {
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fin.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        fin.close();
    }

    public static boolean checklogin(String UID) throws ExecutionException, InterruptedException, JSONException {


        DatabaseInfo DBDataInst = new DatabaseInfo();
        DBDataInst.SQLCall(DBCallType.GETLOGIN);
        DBDataInst.setLoginPostData(UID);
        System.out.println(DBDataInst.getRequestFileHistoryData());
        NASDatabase DBInst = new NASDatabase(DBDataInst);

        DBInst.execute().get();
        String in = DBDataInst.getResult().substring(1, DBDataInst.getResult().length() - 1);
        JSONObject reader = new JSONObject(in);

//        JSONObject c = reader.getJSONObject(0);
        String UserName = reader.getString("UserName");

        if (UserName.equals(UID))
            return true;

        return false;
    }

}