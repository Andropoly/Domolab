package ch.epfl.andropoly.domolab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import JsonUtilisties.myJsonReader;
import MQTTsender.AlreadyConnectecException;
import MQTTsender.MqttDomolab;
import MQTTsender.NotConnectedException;

public class HomeActivity extends AppCompatActivity implements PopupAddingRoom.PopupAddingRoomListener, PopupEditRoom.PopupEditRoomListener {
    private final String TAG = "----------" + this.getClass().getSimpleName();

    private final int SETTINGS = 1;
    // Databse variable
    private String userID;
    private String profileKey;
    private DatabaseReference profileGetRef;
    private DatabaseReference profileRef;
    private FirebaseDatabase database;

    private HomeFragment homeFragment;
    private ListRoomsFragment listRoomsFragment;

    // MQTT variable
    private MqttDomolab mqttDomolab;
    private String mServerAddr;
    private String mUsername;
    private String mPwd;

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
        }

        Domolab.DatabaseChanged.setListener(new BooleanVariable.ChangeListener() {
            @Override
            public void onChange() {
                setLayoutWithDatabase();
            }
        });

        testAndCreatMqttClient();
        Domolab.MqttChanged.setListener(new BooleanVariable.ChangeListener() {
            @Override
            public void onChange() {
                mqttDomolab.setAllHouse();
            }
        });

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
                settingsIntent.putExtra("MQTTSETTINGS", Domolab.mqttSettings_db);
                settingsIntent.putExtra("HOMENAME", Domolab.HomeName_db);
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
        myJsonReader.jsonWriteFileInternal(HomeActivity.this, Domolab.savedCredentialsFile, JSONCredentials);

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
        Toast.makeText(HomeActivity.this, "Welcome to your home: " + Domolab.HomeName_db + " !",
                Toast.LENGTH_LONG).show();
        Log.d(TAG, "Welcome "+ userID + " to your home: " + Domolab.HomeName_db + " !");
        Log.d(TAG, "Rooms info are contained in " + Domolab.roomsArray_db);
        Log.d(TAG, "Favs info are contained in " + Domolab.favsArray_db);
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

    private void testAndCreatMqttClient(){
        /**
         * Checking if the MQTT client is already set in the MQTT settings activity.
         * If not set it with the stored data from last time
         */
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
            } catch (AlreadyConnectecException e) {
                Toast.makeText(HomeActivity.this, "Couldn't connect to MQTT", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
