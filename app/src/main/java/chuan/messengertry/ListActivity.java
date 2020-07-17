package chuan.messengertry;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chuan.messengertry.Adapters.MessageListAdapter;
import chuan.messengertry.Adapters.SubsribedListAdapter;

public class ListActivity extends AppCompatActivity {

    public static List<String> subList2;
    public static SubsribedListAdapter adapter;
    public static ListView ll;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        pref = getSharedPreferences("pref", 0);
        final Set<String> subList = new HashSet<String>(pref.getStringSet("subscribedList", new HashSet<String>()));

        getSupportActionBar().setTitle("Subscribed Rooms");


        if(subList != null)
        {
            Toast.makeText(this,"Click on the room to change displayed notification.",Toast.LENGTH_SHORT).show();
            subList2 = new ArrayList<>(subList);

            editor = pref.edit();



            ll = (ListView) findViewById(R.id.llsub);

            Collections.sort(subList2);
            adapter = new SubsribedListAdapter(this, subList2);
            ll.setAdapter(adapter);

            ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String room = subList2.get(position);

                    Log.e("123",room);

                    if(room.contains("%1"))
                    {
                        subList2.remove(room);
                        subList.remove(room);
                        subList2.add(room.substring(0,room.length()-1) + "2");
                        subList.add(room.substring(0,room.length()-1) + "2");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(room);
                        FirebaseMessaging.getInstance().subscribeToTopic(room.substring(0,room.length()-1) + "2");
                        Toast.makeText(ListActivity.this, "Only room name will be displayed in notification !", Toast.LENGTH_SHORT).show();
                    }
                    else if(room.contains("%2"))
                    {
                        subList2.remove(room);
                        subList.remove(room);
                        subList2.add(room.substring(0,room.length()-1) + "3");
                        subList.add(room.substring(0,room.length()-1) + "3");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(room);
                        FirebaseMessaging.getInstance().subscribeToTopic(room.substring(0,room.length()-1) + "3");
                        Toast.makeText(ListActivity.this, "Room name and sender will be displayed in notification !", Toast.LENGTH_SHORT).show();
                    }
                    else if(room.contains("%3"))
                    {
                        subList2.remove(room);
                        subList.remove(room);
                        subList2.add(room.substring(0,room.length()-1) + "1");
                        subList.add(room.substring(0,room.length()-1) + "1");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(room);
                        FirebaseMessaging.getInstance().subscribeToTopic(room.substring(0,room.length()-1) + "1");
                        Toast.makeText(ListActivity.this, "Notification will be sent without including room name and sender !", Toast.LENGTH_SHORT).show();
                    }

                    editor.putStringSet("subscribedList",subList).apply();
                    Collections.sort(subList2);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        else
        {
            Toast.makeText(this,"No room was subscribed yet, subscribe a room to get notification.",Toast.LENGTH_SHORT).show();
        }


    }
}
