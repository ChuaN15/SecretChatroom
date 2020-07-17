package chuan.messengertry.Adapters;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by chuan on 2/13/2019.
 */

public class FireMessage extends AsyncTask<String,Void,Void> {
    private final String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    private JSONObject Body;

    public FireMessage(String title,String body) throws JSONException {
        Body = new JSONObject();
        JSONObject notification = new JSONObject();
        notification.put("title", title);
        notification.put("body", body);
        Body.put("notification", notification);
    }

    @Override
    public Void doInBackground(String...params)
    {
        try
        {

        Body.put("condition", "'"+params[0]+"' in topics");

        URL url = new URL(API_URL_FCM);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(true);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(1000);

        conn.setRequestProperty("Authorization", "key=AAAAicOgXm0:APA91bH5yG_QOTTdmUL5XkpTMYvaT0-" +
                "Ser_ksitRSppRroj_cIDd4aFrXtqEjXBhZxqXZM93lAeAeePIS6h0HFxSPfn4Ke0o_3sOtMdQxLjf34qR_jctnYVpN7IwEu1FA1hPJ-nJEpdn");
        conn.setRequestProperty("Content-Type", "application/json");

        Log.e("123", "sendPushNotification: " + Body.toString());

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(Body.toString());
            wr.flush();

            conn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            StringBuilder builder = new StringBuilder();
            while ((output = br.readLine()) != null) {
                builder.append(output);
            }
            System.out.println(builder);

        } catch (Exception e) {
            Log.e("123", "sendPushNotification: " + e.toString());
        }
        return null;
    }

}