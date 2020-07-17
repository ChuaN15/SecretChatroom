package chuan.messengertry;

import android.app.NotificationManager;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chuan.messengertry.Adapters.FireMessage;
import chuan.messengertry.Adapters.MessageListAdapter;
import chuan.messengertry.Requests.MessageRequest;

public class MainActivity extends AppCompatActivity {

    public static List<Message> messageList;
    public static MessageListAdapter adapter;
    public static ListView ll;
    DatabaseReference mDatabase;
    DatabaseReference rootRef;
    DatabaseReference tripsRef;
    ValueEventListener valueEventListener;
    ChildEventListener childEventListener;
    String room;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Menu menu;
    DateFormat df2;
    boolean isDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference("Message");

        pref = getSharedPreferences("pref", 0);

        room = pref.getString("Room","");
        editor = pref.edit();

        rootRef = FirebaseDatabase.getInstance().getReference();
        tripsRef = rootRef.child("Message");


        getSupportActionBar().setTitle(room);

        ll = (ListView) findViewById(R.id.ll);
        messageList = new ArrayList<Message>();

        adapter = new MessageListAdapter(this, messageList);
        MainActivity.ll.setAdapter(adapter);

        df2 = new SimpleDateFormat("dd/MM/yyyy");

        loadMessage();

    }


    public void sendMessage(View v)
    {
        String sender = pref.getString("Nickname","");

        EditText et = (EditText)findViewById(R.id.et_message);
        Date today = new Date();

        String userId = mDatabase.push().getKey();

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(today);

        Message message = new Message(et.getText().toString(),date,room,sender,0,0);

        mDatabase.child(userId).setValue(message);

        try
        {
            new FireMessage("SECRET ROOM NOTIFICATION","You had received new message from subscribed secret room !").execute(room + "%1");
            new FireMessage("SECRET ROOM NOTIFICATION","You had received new message from " + room + "!").execute(room + "%2");
            new FireMessage(room,sender + ": " + et.getText().toString()).execute(room + "%3");
        }
        catch (Exception e)
        {
        }

        et.setText("");
    }

    public void loadMessage()
    {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Message message = dataSnapshot.getValue(Message.class);

                Log.e("123", "onChildAdded: " + "please" );
                if (room.equals(message.Room)) {
                    if(messageList.size() > 0)
                    {
                        try {
                            Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(message.DateTime);
                            Date date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(messageList.get(messageList.size() - 1).DateTime);
                            if(!df2.format(date1).equals(df2.format(date2)))
                            {
                                Message message2 = new Message("",message.DateTime,room,"",0,1);
                                messageList.add(message2);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {

                        }

                        Log.e("123", "onChildAdded: " + "mtfk yeah2" );

                        if(messageList.get(messageList.size() - 1).sender.equals(""))
                        {
                            if(!message.sender.equals(messageList.get(messageList.size() - 2).sender))
                            {

                                if(messageList.get(messageList.size() - 2).leftright == 0)
                                {
                                    message.leftright = 1;
                                }
                                else
                                {
                                    message.leftright = 0;
                                }
                            }
                            else
                            {
                                message.leftright = messageList.get(messageList.size() - 2).leftright;
                            }
                        }
                        else
                        {
                            if(!message.sender.equals(messageList.get(messageList.size() - 1).sender))
                            {

                                if(messageList.get(messageList.size() - 1).leftright == 0)
                                {
                                    message.leftright = 1;
                                }
                                else
                                {
                                    message.leftright = 0;
                                }
                            }
                            else
                            {
                                message.leftright = messageList.get(messageList.size() - 1).leftright;
                            }
                        }
                    }
                    else
                    {
                        Message message2 = new Message("",message.DateTime,room,"",0,1);
                        messageList.add(message2);
                        adapter.notifyDataSetChanged();
                        Log.e("123", "onChildAdded: " + "please2" );
                    }
                    messageList.add(message);
                    adapter.notifyDataSetChanged();

                    Log.e("123", "onChildAdded: " + "mtfk yeah" );

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };




        tripsRef.addChildEventListener(childEventListener);

        messageList.clear();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;

        Set<String> fetch = new HashSet<String>(pref.getStringSet("subscribedList", new HashSet<String>()));

        if(fetch != null)
        {
            if(fetch.contains(room+ "%1") || fetch.contains(room+ "%2") || fetch.contains(room+ "%3"))
            {
                menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.unsubscribe));
            }
            else
            {
                menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.subsribe));
            }
        }

        return true;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        tripsRef.removeEventListener(childEventListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        SharedPreferences pref = getSharedPreferences("pref",0);
        SharedPreferences.Editor editor = pref.edit();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {

            Set<String> fetch = new HashSet<String>(pref.getStringSet("subscribedList", new HashSet<String>()));

            if(fetch == null)
            {
                FirebaseMessaging.getInstance().subscribeToTopic(room + "%1");
                fetch = new ArraySet<>();
                fetch.add(room + "%1");
                menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.unsubscribe));
                Toast.makeText(this,"Successfully subscribed " + room,Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(fetch.contains(room+ "%1") || fetch.contains(room+ "%2") || fetch.contains(room+ "%3"))
                {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(room+ "%1");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(room+ "%2");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(room+ "%3");
                    fetch.remove(room+ "%1");
                    fetch.remove(room+ "%2");
                    fetch.remove(room+ "%3");
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.subsribe));
                    Toast.makeText(this,"Successfully unsubscribed " + room,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FirebaseMessaging.getInstance().subscribeToTopic(room+ "%1");
                    fetch.add(room + "%1");
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.unsubscribe));
                    Toast.makeText(this,"Successfully subscribed " + room,Toast.LENGTH_SHORT).show();
                }
            }
            editor.putStringSet("subscribedList",fetch).apply();
        }

        return super.onOptionsItemSelected(item);
    }
}
