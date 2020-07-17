package chuan.messengertry;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        getSupportActionBar().hide();
    }

    public void createRoom(View v)
    {
        final EditText et = (EditText)findViewById(R.id.editText);
        final EditText et2 = (EditText)findViewById(R.id.etNickname);

        if(et2.getText().toString().isEmpty() || et.getText().toString().isEmpty())
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(RoomActivity.this);
            alertDialog.setTitle("Failed to create room !");
            alertDialog.create();
            alertDialog.setMessage("Please fill in the room name and your nickname !");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();

            return;
        }
        else if(et2.getText().toString().contains("%"))
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(RoomActivity.this);
            alertDialog.setTitle("Failed to create room !");
            alertDialog.create();
            alertDialog.setMessage("Room name is not allowed to include % symbol !");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();

            return;
        }
        else if(et2.getText().toString().length() > 20)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(RoomActivity.this);
            alertDialog.setTitle("Failed to create room !");
            alertDialog.create();
            alertDialog.setMessage("Room name cannot more than 20characters !");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();

            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(RoomActivity.this);
        progressDialog.setMessage("Creating...");
        progressDialog.create();
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tripsRef = rootRef.child("Room");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String roomName = ds.getValue(String.class);
                    if(et.getText().toString().equals(roomName))
                    {
                        list.add(roomName);
                    }
                }

                if(list.size() > 0)
                {
                    progressDialog.dismiss();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RoomActivity.this);
                    alertDialog.setTitle("Failed to create room !");
                    alertDialog.create();
                    alertDialog.setMessage("The room name was taken, please use register with another room name !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
                else
                {
                    progressDialog.dismiss();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Room");

                    String userId = myRef.push().getKey();

                    myRef.child(userId).setValue(et.getText().toString());

                    SharedPreferences pref = getSharedPreferences("pref",0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Room",et.getText().toString()).apply();
                    editor.putString("Nickname",et2.getText().toString()).apply();

                    Intent intent = new Intent(RoomActivity.this,MainActivity.class);
                    RoomActivity.this.startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        tripsRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void joinRoom(View view)
    {
        final EditText et = (EditText)findViewById(R.id.editText);
        final EditText et2 = (EditText)findViewById(R.id.etNickname);

        if(et2.getText().toString().isEmpty() || et.getText().toString().isEmpty())
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(RoomActivity.this);
            alertDialog.setTitle("Failed to join room !");
            alertDialog.create();
            alertDialog.setMessage("Please fill in the room name and your nickname !");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();

            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(RoomActivity.this);
        progressDialog.setMessage("Joining...");
        progressDialog.create();
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tripsRef = rootRef.child("Room");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String roomName = ds.getValue(String.class);
                    if(et.getText().toString().equals(roomName))
                    {
                        list.add(roomName);
                    }
                }

                if(list.size() < 1)
                {
                    progressDialog.dismiss();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RoomActivity.this);
                    alertDialog.setTitle("Failed to join room !");
                    alertDialog.setMessage("No room was found based on the room name !");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.create();
                    alertDialog.show();
                }
                else
                {
                    progressDialog.dismiss();
                    SharedPreferences pref = getSharedPreferences("pref",0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Room",et.getText().toString()).apply();
                    editor.putString("Nickname",et2.getText().toString()).apply();

                    Intent intent = new Intent(RoomActivity.this,MainActivity.class);
                    RoomActivity.this.startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        tripsRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void goToList(View v)
    {
        Intent intent = new Intent(RoomActivity.this,ListActivity.class);
        this.startActivity(intent);
    }
}
