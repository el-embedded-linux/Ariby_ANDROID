package com.el.ariby.ui.main.menu.raspberry;


import android.content.ContentValues;
import android.os.AsyncTask;

public class NetworkTask extends AsyncTask<Void, Void, String> {
    String url;
    ContentValues values;
    String method;
    NetworkTaskResult listener = null;
    public NetworkTask(String url, ContentValues values, String method){
        this.url = url;
        this.values = values;
        this.method = method;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progress bar를 보여주는 등등의 행위
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = null;
        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
        if(this.method.equals("GET")){
            result = requestHttpURLConnection.getRequest(url, values);
        }
        else if(this.method.equals("POST")){
            result = requestHttpURLConnection.postRequest(url, values);
        }
        return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
    }

    @Override
    protected void onPostExecute(String result) {
        if(result==null) listener.fail(result);
        else if(result.equals("500")) listener.fail(result);
        else listener.done(result);
    }

    public void addNetworkTaskResult(NetworkTaskResult listener){
        this.listener = listener;
    }

    public interface NetworkTaskResult{
        public void done(String result);
        public void fail(String result);
    }
}
