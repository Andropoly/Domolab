package ch.epfl.andropoly.domolab;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import JsonUtilisties.myJsonReader;
import MQTTsender.MqttDomolab;

public class Domolab extends Application {
    private static String TAG = "---DomolabApp---";

    // mqtt private variables
    private static Domolab mContext;
    private static MqttDomolab mqttDomolab;
    private static Boolean mqttCreated = false;

    // database private variables
    private static FirebaseDatabase database;
    private static DatabaseReference profileRef;
    private static DatabaseReference profileGetRef;

    public static BooleanVariable DatabaseChanged = new BooleanVariable();
    public static boolean isListening = false;

    // Home global variables
    public static String HomeName_db;
    public static String userID_db;
    public static ArrayList<String> mqttSettings_db = new ArrayList<>();
    public static String roomsString_db;
    public static String favsString_db;
    public static String devicesString_db;
    public static JSONArray roomsArray_db;
    public static JSONArray favsArray_db;
    public static JSONObject devicesObject_db;

    //JSON files related variables
    public static String MyRoomFile = "my_rooms.json";
    public static String MyFavFile = "my_favs.json";
    public static String MyDevFile = "my_devices.json";
    public static String savedCredentialsFile = "savedCredentials.json";
    public static String mqttSettingsFile = "mqttCurrentSettings.json";
    public static String MyHomenameFile = "currentHouse.json";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mqttSettings_db.add("");
        mqttSettings_db.add("");
        mqttSettings_db.add("");
    }

    public static void creatMqtt(String username, String password, String serverURI){
        mqttDomolab = new MqttDomolab(mContext, username, password, serverURI);
        mqttCreated = true;
    }
    public static boolean mqttIsCreated(){
        return mqttCreated;
    }

    public static MqttDomolab getMqttDomolab(){return  mqttDomolab;}

    public static Domolab getContext() {
        return mContext;
    }

    public static void setDatabaseApp(FirebaseDatabase DATABASE, String profileKey) {
        database = DATABASE;
        profileGetRef = database.getReference("profiles");
        profileRef = profileGetRef.child(profileKey).getRef();
        setListenerDatabase();
    }

    public static  FirebaseDatabase getDatabase() {return database; }

    public static  DatabaseReference getprofileGetRef() {return profileGetRef; }

    public static  DatabaseReference getprofileRef() {return profileRef; }

    private static void setListenerDatabase() {
        Log.d(TAG, "set listener");
        isListening = true;
        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HomeName_db = dataSnapshot.child("HomeName").getValue(String.class);
                userID_db = dataSnapshot.child("userID").getValue(String.class);

                String mqttUsername = dataSnapshot.child("MQTT").child("username").getValue(String.class);
                String mqttPassword = dataSnapshot.child("MQTT").child("password").getValue(String.class);
                String mqttServer = dataSnapshot.child("MQTT").child("serverURI").getValue(String.class);

                mqttSettings_db.set(0, mqttUsername);
                mqttSettings_db.set(1, mqttPassword);
                mqttSettings_db.set(2, mqttServer);

                roomsString_db = dataSnapshot.child("Rooms").getValue(String.class);
                favsString_db = dataSnapshot.child("Favorites").getValue(String.class);
                devicesString_db = dataSnapshot.child("Devices").getValue(String.class);

                try {
                    roomsArray_db = new JSONArray(roomsString_db);
                    favsArray_db = new JSONArray(favsString_db);
                    devicesObject_db = new JSONObject(devicesString_db);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "finished changing values");

                saveDataInJsonFiles();

                DatabaseChanged.setBoolean(!DatabaseChanged.isBoolean());
                isListening = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DatabaseChanged.setBoolean(!DatabaseChanged.isBoolean());
                isListening = false;
            }
        });
    }

    public static boolean isAppListening() {return isListening;    }

    private static void saveDataInJsonFiles() {
        JSONObject JSONHomeName_db = new JSONObject();
        try {
            JSONHomeName_db.put("HouseName", HomeName_db);
            JSONObject JSONMqttSettings_db = new JSONObject();
            JSONMqttSettings_db.put("MQTTUsername", mqttSettings_db.get(0));
            JSONMqttSettings_db.put("MQTTPassword", mqttSettings_db.get(1));
            JSONMqttSettings_db.put("MQTTServer", mqttSettings_db.get(2));

            myJsonReader.jsonWriteFileInternal(getContext(), MyHomenameFile , JSONHomeName_db);
            myJsonReader.jsonWriteFileInternal(getContext(), MyRoomFile , roomsArray_db);
            myJsonReader.jsonWriteFileInternal(getContext(), MyFavFile , favsArray_db);
            myJsonReader.jsonWriteFileInternal(getContext(), mqttSettingsFile , JSONMqttSettings_db);
            myJsonReader.jsonWriteFileInternal(getContext(), MyDevFile, devicesObject_db);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "modified internal files");
    }

}


