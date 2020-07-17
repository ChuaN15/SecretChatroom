package chuan.messengertry.Requests;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import chuan.messengertry.Adapters.MessageListAdapter;
import chuan.messengertry.MainActivity;
import chuan.messengertry.Message;

import static chuan.messengertry.MainActivity.adapter;
import static chuan.messengertry.MainActivity.messageList;

/**
 * Created by chuan on 2/11/2019.
 */

public class MessageRequest extends AsyncTask<String,Void,Void> {

    String data = "";
    Activity activity;

    public MessageRequest(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public Void doInBackground(String...params)
    {
        try
        {
            URL url = new URL ("http://primapps.utem.edu.my/api/SubscribedTopics");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(1000);

            Uri.Builder builder = new Uri.Builder().appendQueryParameter("token","dev7thIp37s:APA91bHdFsZoXpYuIEC6H7CT93hKF17M5igMKxEKJX3UJYJg-opPCyHRTSgdgaR2qDsOhXndCwxhp7-ruZOEhiK2VWk2OA7Q6REjSJeMCfp4pUdS6lsoHRcTE22ZEMY5DuuNOU0qN_sB");
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
            bufferedWriter.write(query);
            bufferedWriter.flush();
            bufferedWriter.close();
            os.close();
            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            if(line != null)
            {
                line = bufferedReader.readLine();
                data += line;
            }
            is.close();



        }
        catch (Exception e)
        {
            Log.e("123", "doInBackground: " + e.toString());
        }


        return null;
    }

    @Override
    public void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);


        try {
            JSONObject jo = new JSONObject(data);
            JSONObject jo2 = jo.getJSONObject("data");
            JSONObject jo3 = jo2.getJSONObject("classes_K100007404");
            JSONObject jo4 = jo2.getJSONObject("school_P100168084");
            JSONObject jo5 = jo2.getJSONObject("student_P100168084");

            Date date3 = new SimpleDateFormat("yyyy-MM-dd").parse(jo3.getString("addDate"));
            Date date4 = new SimpleDateFormat("yyyy-MM-dd").parse(jo4.getString("addDate"));
            Date date5 = new SimpleDateFormat("yyyy-MM-dd").parse(jo5.getString("addDate"));


            Collections.sort(messageList);
            adapter.notifyDataSetChanged();

            /*messageList.removeAll(messageList);

            messageList.add(new Message("1", date3));
            messageList.add(new Message("2", date4));
            messageList.add(new Message("3", date5));
            messageList.add(new Message("4", date3));
            messageList.add(new Message("5", date4));
            messageList.add(new Message("6", date5));*/


            Log.e("111", "onPostExecute: " + data );
        }
        catch (Exception e)
        {
            Log.e("123", "doInBackground: " + e.toString());
        }
    }
}
