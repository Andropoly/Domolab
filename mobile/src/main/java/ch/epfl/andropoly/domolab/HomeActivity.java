package ch.epfl.andropoly.domolab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import JsonUtilisties.myJsonReader;
import MQTTsender.AlreadyConnectecException;
import MQTTsender.MqttDomolab;
import MQTTsender.NotConnectedException;

public class HomeActivity extends AppCompatActivity implements PopupAddingRoom.PopupAddingRoomListener, PopupEditRoom.PopupEditRoomListener {
    private final String TAG = this.getClass().getSimpleName();

    private final int SETTINGS = 1;
    // Databse variable
    private String userID;
    private String profileKey;
    private DatabaseReference profileGetRef;
    private DatabaseReference profileRef;
    private FirebaseDatabase database;

    private HomeFragment homeFragment;
    private ListRoomsFragment listRoomsFragment;

    //private GenericTypeIndicator<ArrayList<String>> ArrayListStringType = new GenericTypeIndicator<ArrayList<String>>() {};
    private String homename_db;
    private String userID_db;
    private ArrayList<String> mqttSettings_db = new ArrayList<>();
    //private ArrayList<String> listofrooms_db;
    //private ArrayList<String> listoffav_db;
    private String roomsString_db;
    private String favsString_db;
    private JSONArray roomsArray_db;
    private JSONArray favsArray_db;

    MqttDomolab mqttDomolab;

    private String mUsername = "ywcheuja";
    private String mPwd = "XD9nWtB5CRKl";
    private String mServerAddr = "tcp://m21.cloudmqtt.com:16233";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setFragments();

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            // used for accessing the proper profile in the database
            userID = intent.getExtras().getString("USERID");
            profileKey = intent.getExtras().getString("PROFILEKEY");

            // gets the proper references for accessing the profile
            database = FirebaseDatabase.getInstance();
            profileGetRef = database.getReference("profiles");
            profileRef = profileGetRef.child(profileKey).getRef();
            //profileRef = database.getReference("profiles");
            //profileRef.child(profileKey).addValueEventListener(new ValueEventListener() {

            // gathers the data contained in the profile in the database
            profileRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    homename_db = dataSnapshot.child("HomeName").getValue(String.class);
                    userID_db = dataSnapshot.child("userID").getValue(String.class);

                    String mqttUsername = dataSnapshot.child("MQTT").child("username").getValue(String.class);
                    String mqttPassword = dataSnapshot.child("MQTT").child("password").getValue(String.class);
                    String mqttServer = dataSnapshot.child("MQTT").child("serverURI").getValue(String.class);
                    mqttSettings_db.add(mqttUsername);
                    mqttSettings_db.add(mqttPassword);
                    mqttSettings_db.add(mqttServer);

                    //listofrooms_db = dataSnapshot.child("listOfRooms").getValue(ArrayListStringType);
                    //listoffav_db = dataSnapshot.child("listOfFav").getValue(ArrayListStringType);
                    roomsString_db = dataSnapshot.child("Rooms").getValue(String.class);
                    favsString_db = dataSnapshot.child("Favorites").getValue(String.class);

                    try {
                        roomsArray_db = new JSONArray(roomsString_db);
                        favsArray_db = new JSONArray(favsString_db);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // sets the layout with regard to the gathered data
                    setLayoutWithDatabase();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Empty
                }
            });
        }
        if (Domolab.mqttIsCreated()){
            mqttDomolab = Domolab.getMqttDomolab();
        } else {
            try {

                JSONObject obj = myJsonReader.jsonObjFromFileInternal(HomeActivity.this, "mqttCurrentSettings.json");
                mServerAddr = obj.getString("MQTTServer");
                mUsername = obj.getString("MQTTUsername");
                mPwd = obj.getString("MQTTPassword");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Toast.makeText(HomeActivity.this, "Not MQTT settings", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Domolab.creatMqtt(mUsername, mPwd, mServerAddr);
            mqttDomolab = Domolab.getMqttDomolab();
            try {
                mqttDomolab.connect();
                mqttDomolab.sendMsgToTopic("An app is connected", "status");
            } catch (AlreadyConnectecException e) {
                Toast.makeText(HomeActivity.this, "Couldn't connect to MQTT", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.NeedHelpId:
                Toast.makeText(HomeActivity.this, "Really Nigga -_- you need help for that ?", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.AddRoomId:
                Toast.makeText(HomeActivity.this, "Added Room", Toast.LENGTH_LONG).show();
                PopupAddingRoom popupAddingRoom = new PopupAddingRoom();
                popupAddingRoom.show(this.getSupportFragmentManager(), "Popup adding room");
                return true;
            case R.id.AddFavId:
                Toast.makeText(HomeActivity.this, "Added Favorite", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.SettingsId:
                Intent settingsIntent = new Intent(HomeActivity.this, MqttSettingsActivity.class);
                settingsIntent.putExtra("USERID", userID);
                settingsIntent.putExtra("PROFILEKEY", profileKey);
                settingsIntent.putExtra("MQTTSETTINGS", mqttSettings_db);
                settingsIntent.putExtra("HOMENAME", homename_db);
                startActivityForResult(settingsIntent, SETTINGS);
                return true;
            case R.id.AboutUsId:
                Toast.makeText(HomeActivity.this, "We are the JAVA gods", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.LogOutId:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        JSONObject JSONCredentials = new JSONObject();

        // deletes the saved credentials
        myJsonReader.jsonWriteFileInternal(HomeActivity.this, "savedCredentials.json", JSONCredentials);

        // goes back to the loginActivity
        Intent LogoutIntent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(LogoutIntent);
    }

    private void setFragments(){
        homeFragment = HomeFragment.newInstance();
        listRoomsFragment = ListRoomsFragment.newInstance();
        String fragmentTAG = getResources().getString(R.string.home_page);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTAG);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_rooms_fragment, listRoomsFragment)
                .commit();

        if(fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_fragment, homeFragment)
                    .commit();
        }
    }
    
    private void setLayoutWithDatabase() {
        Toast.makeText(HomeActivity.this, "Welcome to your home: " + homename_db + " !",
                Toast.LENGTH_LONG).show();
        Log.d(TAG, "Welcome "+ userID_db + " to your home: " + homename_db + " !");
        //Log.d(TAG, "Your rooms: " + listofrooms_db);
        //Log.d(TAG, "Your favorites: " + listoffav_db);
        Log.d(TAG, "Rooms info are contained in " + roomsArray_db);
        Log.d(TAG, "Favs info are contained in " + favsArray_db);
    }

    @Override
    public void onBackPressed() {
        // TODO: When HomeFragment is active, propose to logout instead of doing nothing
        if(this.getSupportFragmentManager().getBackStackEntryCount() != 0)
            super.onBackPressed();
    }

    public void allOffCallback(View view) {

        Intent intent = new Intent(HomeActivity.this, Tes2Activity.class);
        startActivity(intent);
    }

    @Override
    public void updateRoomAdapter() {
        listRoomsFragment.updateAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTINGS) {
            // do nothing
        }
    }
}
