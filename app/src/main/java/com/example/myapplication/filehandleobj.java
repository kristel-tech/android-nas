package com.example.myapplication;

import android.net.Uri;

import androidx.documentfile.provider.DocumentFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Stack;

public class filehandleobj {
    public static filehandleobj singlefilehandleobj = null;

    public static String[] fileindir;
    public static Uri[] fileuris;
    public static String[] filesizes;
    public static JSONObject JsonFileList;
    private int size;
    private boolean readpermission;
    private String basefilenme;
    private DocumentFile rootDocFile;
    private Stack<DocumentFile> Docfiles;

    private filehandleobj(){
        basefilenme = null;
        rootDocFile = null;
        Docfiles = null;
        size = 0;
        readpermission = false;
        fileuris = null;
        filesizes = null;
        fileindir = null;
        JsonFileList = null;
    }

    public static filehandleobj getInstence(){
        if (singlefilehandleobj == null){
            singlefilehandleobj = new filehandleobj();
        }
        return singlefilehandleobj;
    }

    public void setFileobj(DocumentFile rootDocFile){
        this.rootDocFile = rootDocFile;
        basefilenme = rootDocFile.getName();
        readpermission = rootDocFile.canRead();
        Docfiles = new Stack<>();
        for(DocumentFile f:rootDocFile.listFiles()){
            if (f.isFile() == true) {
                System.out.println( f.getName());
                Docfiles.add(f);
                size++;
            }
        }
    }

    public int getSize(){return size;}
    public boolean getRradpermission(){return readpermission;}
    public String getBasefilenme(){return basefilenme;}
    public DocumentFile getRootDocFile(){return rootDocFile;}
    public Uri getUriWithIndex(int index){return fileuris[index];}
    public String getfilesizeWindex(int index){return filesizes[index];}
    public JSONObject getJsonFileList(){return JsonFileList;}
    public String getFileindirWithIndex(int index){return getFileindir()[index];}

    public DocumentFile getDocfiles(int id) {
        Iterator<DocumentFile> itr = Docfiles.iterator();
        DocumentFile file = null;
        int index = 0;
        while (itr.hasNext() && index < id)
        {
            file = itr.next();
            index++;
        }
        return file;
    }

    public String[] getFileindir(){
        if (fileindir != null)
            return fileindir;

        fileindir = new String[size];
        fileuris = new Uri[size];
        filesizes = new String[size];
        Iterator<DocumentFile> Docfilesitr = Docfiles.iterator();

        // hasNext() returns true if the stack has more elements
        int  index = 0;
        DocumentFile Docfilesitrindex = null;
        while (Docfilesitr.hasNext())
        {
            // next() returns the next element in the iteration
            Docfilesitrindex = Docfilesitr.next();
            fileuris[index] = Docfilesitrindex.getUri();
            fileindir[index] = Docfilesitrindex.getName();
            filesizes[index++] = readableFileSize(Docfilesitrindex.length());
        }
        return fileindir;
    }

    public String getFileList(){
        String list = "";
        if (size > 0)
            for (int i = 0; i < size; i++) {
             list += getFileindirWithIndex(i) + " " + getfilesizeWindex(i) + "\n";
            }

        return list;
    }

    public String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private JSONObject getfilejson(int index) throws JSONException{
        JSONObject file = new JSONObject();
        file.put("FileName", getFileindirWithIndex(index));
        file.put("FileSize", getfilesizeWindex(index));
        file.put("FileURI", getUriWithIndex(index));
        file.put("FileID", index);
        file.put("href", "http://localhost:5000/getfile/"+index);

        return file;
    }

    public String getJsonList(){
        if (JsonFileList != null)
            return JsonFileList.toString();

        if (size <= 0)
            return "{\"ERROR\" : true}";

            JSONObject main = new JSONObject();
            try {
                main.put("RootFile", basefilenme);
                main.put("NumOfAvailableFiles", size);
                main.put("href", "http://localhost:5000/List");
                JSONArray file = new JSONArray();
                for(int i = 0; i < size; i++)
                    file.put(getfilejson(i));
                main.put("FileList", file);
            } catch (JSONException e) {
                return e.toString();
            }
        JsonFileList = main;

        return JsonFileList.toString();
    }
}
