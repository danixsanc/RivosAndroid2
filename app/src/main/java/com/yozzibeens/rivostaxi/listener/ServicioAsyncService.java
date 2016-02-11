package com.YozziBeens.rivostaxi.listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class ServicioAsyncService extends AsyncTask<String, String, HashMap<String, Object>> {
    private static final String DEBUG_TAG = "Act";

    private AsyncTaskListener asyncTaskListener;
    private HashMap<String, Object> resultados;
    private Context context;
    private String servicioURL;
    private String rawJson;


    public ServicioAsyncService(Context prContext, String prUrl, String prRawJson) {
        this.context = prContext;
        this.servicioURL = prUrl;
        this.rawJson = prRawJson;
        resultados = new HashMap<String, Object>();

    }

    @Override
    protected void onPreExecute() {
        asyncTaskListener.onTaskStart();
        super.onPreExecute();
    }

    @Override
    protected HashMap<String, Object> doInBackground(String... params) {

        disableConnectionReuseIfNecessary();
        HttpURLConnection urlConnection = null;
        try {
            // create connection
            //Thread.sleep(1000);
            URL urlToRequest = new URL(servicioURL);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            if(rawJson != null) {
                OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
                osw.write(rawJson);
                osw.flush();
                osw.close();
            }

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
                resultados.put("IsValid", false);
                resultados.put("StatusCode", statusCode);
                resultados.put("Resultado", null);
                return resultados;
            }

            // create JSON object from content
            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());
            String resultado = getResponseText(in);
            resultados.put("IsValid", true);
            resultados.put("StatusCode",0);
            resultados.put("Resultado", resultado);
            onTaskDownloadedFinished(resultados);
            return resultados;

        } catch (MalformedURLException e) {
            // URL is invalid
            resultados.put("IsValid", false);
            resultados.put("Resultado", null);
            resultados.put("StatusCode", 300);
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
            resultados.put("IsValid", false);
            resultados.put("Resultado", null);
            resultados.put("StatusCode", 300);
        } catch (IOException e) {
            resultados.put("IsValid", false);
            resultados.put("Resultado", null);
            resultados.put("StatusCode", 300);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return resultados;
    }

    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        String resultado = new Scanner(inStream).useDelimiter("\\A").next();
        Log.d(DEBUG_TAG, resultado);
        return resultado;
    }

    @Override
    protected void onPostExecute(HashMap<String, Object> result) {
        asyncTaskListener.onTaskComplete(result);
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        super.onProgressUpdate(progress[0]);
        asyncTaskListener.onTaskUpdate(progress[0]);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCancelled(HashMap<String, Object> result) {
        asyncTaskListener.onTaskCancelled(null);
        super.onCancelled(result);
    }

    public void setOnCompleteListener(AsyncTaskListener asyncTaskListener) {
        this.asyncTaskListener = asyncTaskListener;
    }

    private void onTaskDownloadedFinished(HashMap<String, Object> result){
        asyncTaskListener.onTaskDownloadedFinished(result);
    }


}
