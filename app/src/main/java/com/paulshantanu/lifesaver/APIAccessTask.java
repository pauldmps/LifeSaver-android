package com.paulshantanu.lifesaver;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Shantanu Paul on 16-12-2015.
 */

class APIResponseObject{
    int responseCode;
    String response;

    APIResponseObject(int responseCode,String response)
    {
        this.responseCode = responseCode;
        this.response = response;
    }
}


public class APIAccessTask extends AsyncTask<String,Void,APIResponseObject> {
    URL requestUrl;
    Context context;
    HttpURLConnection urlConnection;
    List<Pair<String,String>> postData, headerData;
    String method;
    int responseCode = HttpURLConnection.HTTP_NOT_FOUND;

    interface OnCompleteListener{
        void onComplete(APIResponseObject result);
    }

    public OnCompleteListener delegate = null;

    APIAccessTask(Context context, String requestUrl, String method, OnCompleteListener delegate){
        this.context = context;
        this.delegate = delegate;
        this.method = method;
        try {
            this.requestUrl = new URL(requestUrl);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


    APIAccessTask(Context context, String requestUrl, String method, List<Pair<String,String>> postData, OnCompleteListener delegate){
        this(context, requestUrl, method, delegate);
        this.postData = postData;
    }

    APIAccessTask(Context context, String requestUrl, String method, List<Pair<String,String>> postData,
                  List<Pair<String,String>> headerData, OnCompleteListener delegate ){
        this(context, requestUrl,method,postData,delegate);
        this.headerData = headerData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected APIResponseObject doInBackground(String... params) {

        setupConnection();

        if(method.equals("POST"))
        {
            return httpPost();
        }

        if(method.equals("GET"))
        {
            return httpGet();
        }

        if(method.equals("PUT"))
        {
            return httpPut();
        }

        if(method.equals("DELETE"))
        {
            return httpDelete();
        }
        if(method.equals("PATCH"))
        {
            return httpPatch();
        }

        return null;
    }

    @Override
    protected void onPostExecute(APIResponseObject result) {
        Toast.makeText(context,result.response,Toast.LENGTH_LONG).show();
        delegate.onComplete(result);
        super.onPostExecute(result);
    }

    void setupConnection(){
        try {
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

    }


    private APIResponseObject httpPost(){

        try{
            urlConnection.setRequestMethod("POST");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return new APIResponseObject(responseCode,stringifyResponse());
    }



    APIResponseObject httpGet(){

        try{
            urlConnection.setRequestMethod("GET");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return new APIResponseObject(responseCode,stringifyResponse());
    }

    APIResponseObject httpPut(){

        try{
            urlConnection.setRequestMethod("PUT");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return new APIResponseObject(responseCode,stringifyResponse());
    }

    APIResponseObject httpDelete(){
        try{
            urlConnection.setRequestMethod("DELETE");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return new APIResponseObject(responseCode,stringifyResponse());

    }

    APIResponseObject httpPatch(){
        try{
            urlConnection.setRequestMethod("PATCH");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return new APIResponseObject(responseCode,stringifyResponse());

    }

    String stringifyResponse() {

        StringBuilder sb = new StringBuilder();
        try {
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(getQuery(postData));
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();
            responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }


    private String getQuery(List<Pair<String,String>> params) throws UnsupportedEncodingException{
        Uri.Builder builder = null;
        for (Pair pair : params)
        {
             builder = new Uri.Builder()
                    .appendQueryParameter(pair.first.toString(), pair.second.toString());
                    }
        return builder.build().getEncodedQuery();
    }


}
