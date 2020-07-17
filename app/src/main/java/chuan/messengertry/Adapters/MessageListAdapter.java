package chuan.messengertry.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.SimpleTimeZone;

import chuan.messengertry.Message;
import chuan.messengertry.R;

/**
 * Created by chuan on 2/11/2019.
 */

public class MessageListAdapter extends BaseAdapter {

    Activity activity;
    List<Message> messageList;

    public MessageListAdapter(Activity activity,List<Message> messageList)
    {
        this.activity = activity;
        this.messageList = messageList;
    }

    @Override
    public int getCount()
    {
        return messageList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View convertView3 = activity.getLayoutInflater().inflate(R.layout.layout_holder_you,null);

        View convertView2 = activity.getLayoutInflater().inflate(R.layout.layout_holder_me,null);

        View convertView4 = activity.getLayoutInflater().inflate(R.layout.layout_holder_date,null);

        ArrayList<View> viewlist = new ArrayList<>();
        viewlist.add(convertView3);
        viewlist.add(convertView2);

        try
        {
            convertView = viewlist.get(messageList.get(position).leftright);

            TextView tv = convertView.findViewById(R.id.tv_chat_text);
            TextView tvTime = convertView.findViewById(R.id.tv_time);

            Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(messageList.get(position).DateTime);

            DateFormat df = new SimpleDateFormat("HH:mm");
            String date = df.format(date1);

            DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            String date2 = df2.format(date1);

            if(messageList.get(position).showDate == 1)
            {
                TextView tvDate = convertView4.findViewById(R.id.tv_date);
                tvDate.setText(date2);

                return convertView4;
            }

            tv.setText(messageList.get(position).Subject);
            tvTime.setText(date + " -" + messageList.get(position).sender);

        }
        catch (Exception e)
        {
            Log.e("123", "getView: " + e.toString() );
        }
        return convertView;
    }

}
