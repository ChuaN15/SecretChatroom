package chuan.messengertry.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chuan.messengertry.Message;
import chuan.messengertry.R;

/**
 * Created by chuan on 2/11/2019.
 */

public class SubsribedListAdapter extends BaseAdapter {

    Activity activity;
    List<String> subList;

    public SubsribedListAdapter(Activity activity, List<String> subList)
    {
        this.activity = activity;
        this.subList = subList;
    }

    @Override
    public int getCount()
    {
        return subList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return subList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.subscribed_room_row, parent, false);
        }

            ImageView ivSymbol = convertView.findViewById(R.id.tvExamPercentage);
        TextView tvRoomName = convertView.findViewById(R.id.tvRoomName);
        TextView tvShort = convertView.findViewById(R.id.tvShort);

        String room = subList.get(position).toString();

        tvRoomName.setText(room.substring(0,room.length()-2));
        tvShort.setText(room.substring(0,1));

        if(room.contains("%1"))
        {
            ivSymbol.setImageResource(R.drawable.red_circle);
        }
        else if(room.contains("%2"))
        {
            ivSymbol.setImageResource(R.drawable.blue_circle);
        }
        else
        {
            ivSymbol.setImageResource(R.drawable.green_circle);
        }

        return convertView;
    }

}
