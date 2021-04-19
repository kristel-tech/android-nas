package com.example.myapplication;

import android.os.Environment;

import java.io.*;
import java.net.*;
import java.util.*;

public class httphandle extends Thread {

    static final String HTML_START = "<html>" + "<title>HTTP Server in java</title>" + "<body>";

    static final String HTML_END = "</body>" + "</html>";

    ServerSocket Server;
    Socket connectedClient;

    BufferedReader inFromClient = null;
    DataOutputStream outToClient = null;


    public void run() {

        try {

            System.out.println("The Client " + connectedClient.getInetAddress() + ":" + connectedClient.getPort()
                    + " is connected");

            inFromClient = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
            outToClient = new DataOutputStream(connectedClient.getOutputStream());

            String requestString = inFromClient.readLine();
            String headerLine = requestString;

            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            String httpMethod = tokenizer.nextToken();
            String httpQueryString = tokenizer.nextToken();

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

            if (httpMethod.equals("GET")) {
                if (httpQueryString.equals("/")) {
                    // The default home page
                    sendResponse(200, responseBuffer.toString(), false);
                }
                else if (httpQueryString.equals("/List")){

                    String filesPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
//                    Log.d("Files", "Path: " + path);
                    File directory = new File(filesPath);
                    File[] files = directory.listFiles();
//
                    String returnString = new String();
                    int file_size = 0;
                    for (int i = 0; i < files.length; i++)
                    {
                        returnString += "<p>FileName:" + files[i].getName() + "<p><br>" ;
//
                    }
                        sendResponse(200, returnString, false);
                }
                else {
                    // This is interpreted as a file name
                    String fileName = httpQueryString.replaceFirst("/", "");
                    fileName = URLDecoder.decode(fileName);
                    if (new File(fileName).isFile()) {
                        sendResponse(200, fileName, true);
                    } else {
                        sendResponse(404, "<b>The Requested resource not found ...."
                                + "Usage: http://127.0.0.1:5000 or http://127.0.0.1:5000/</b>", false);
                    }
                }
            } else
                sendResponse(404, "<b>The Requested resource not found ...."
                        + "Usage: http://127.0.0.1:5000 or http://127.0.0.1:5000/</b>", false);
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
            responseString = httphandle.HTML_START + responseString + httphandle.HTML_END;
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

    public httphandle(Socket client) {
        connectedClient = client;
    }


}