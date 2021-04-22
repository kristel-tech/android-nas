package com.example.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
//useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT"





public class NASDatabase  extends AsyncTask<Void, Void, Void> {

    DatabaseInfo DBInfo;

    public NASDatabase(DatabaseInfo DBInfo){
        this.DBInfo = DBInfo;
    }


    String result;
    @Override
    protected Void doInBackground(Void... voids) {
        URL url;
        try {
            url = new URL(DBInfo.getRequestFileHistoryData());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String stringBuffer;
            String string = "";
            while ((stringBuffer = bufferedReader.readLine()) != null){
                string = String.format("%s%s", string, stringBuffer);
            }
            bufferedReader.close();
            DBInfo.SetResult(string);
        } catch (IOException e){
            e.printStackTrace();
            DBInfo.SetErrorResult(e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        System.out.println(result);
        super.onPostExecute(aVoid);
    }


}






