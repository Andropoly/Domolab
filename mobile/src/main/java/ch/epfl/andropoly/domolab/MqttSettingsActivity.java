package ch.epfl.andropoly.domolab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import JsonUtilisties.myJsonReader;

public class MqttSettingsActivity extends AppCompatActivity {

    private String mHouseName;
    private String mMqttServerURI;
    private String mMqttUsername;
    private String mMqttPassword;
    private ArrayList<String> mqttSettings;

    private EditText mHouse;
    private EditText mServer;
    private EditText mUsername;
    private EditText mPassword;

    private String userID;
    private String profileKey;
    private FirebaseDatabase database;
    private DatabaseReference profileGetRef;
    private DatabaseReference profileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button saveButton = (Button)findViewById(R.id.saveSettingsButton);
        Button cancelButton = (Button)findViewById(R.id.cancelSettingButton);

        mHouse = (EditText) findViewById(R.id.houseTextEdit);
        mServer = (EditText) findViewById(R.id.mqttServerTextEdit);
        mUsername = (EditText) findViewById(R.id.mqttUsernameTextEdit);
        mPassword = (EditText) findViewById(R.id.mqttPasswordTextEdit);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userID = intent.getExtras().getString("USERID");
            profileKey = intent.getExtras().getString("PROFILEKEY");

            if(intent.getExtras().size() > 2) {
                mqttSettings = intent.getExtras().getStringArrayList("MQTTSETTINGS");
                mHouseName = intent.getExtras().getString("HOMENAME");

                mHouse.setText(mHouseName);
                mUsername.setText(mqttSettings.get(0));
                mPassword.setText(mqttSettings.get(1));
                mServer.setText(mqttSettings.get(2));
            }

            database = FirebaseDatabase.getInstance();
            profileGetRef = database.getReference("profiles");
            profileRef = profileGetRef.child(profileKey);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * On save clicked, the value of the text are stored into a json file for further used.
                     */

                    JSONObject obj1 = new JSONObject();
                    JSONObject obj2 = new JSONObject();

                    mHouseName = mHouse.getText().toString();
                    mMqttServerURI = mServer.getText().toString();
                    mMqttUsername = mUsername.getText().toString();
                    mMqttPassword = mPassword.getText().toString();

                    View focusView = null;

                    if (isServerURIValid(mMqttServerURI)) {

                        try {
                            obj1.put("HouseName", mHouseName);
                            obj2.put("MQTTServer", mMqttServerURI);
                            obj2.put("MQTTUsername", mMqttUsername);
                            obj2.put("MQTTPassword", mMqttPassword);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        myJsonReader.jsonWriteFileInternal(MqttSettingsActivity.this, "currentHouse.json", obj1);
                        myJsonReader.jsonWriteFileInternal(MqttSettingsActivity.this, "mqttCurrentSettings.json", obj2);

                        profileRef.runTransaction( new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                mutableData.child("HomeName").setValue(mHouseName);
                                mutableData.child("MQTT").child("username").setValue(mMqttUsername);
                                mutableData.child("MQTT").child("password").setValue(mMqttPassword);
                                mutableData.child("MQTT").child("serverURI").setValue(mMqttServerURI);
                                return Transaction.success(mutableData);
                            }
                            @Override
                            public void onComplete (@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot)
                            {
                                Intent intent = new Intent(MqttSettingsActivity.this, HomeActivity.class);
                                intent.putExtra("USERID", userID);
                                intent.putExtra("PROFILEKEY", profileKey);
                                startActivity(intent);
                            }
                        });
                    } else {
                        mServer.setError("Your server address is missing :");
                        focusView = mServer;
                        focusView.requestFocus();

                    }
                }
            });
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MqttSettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isServerURIValid(String serverURI) {
        //TODO: Replace this with your own logic
        return serverURI.contains(":");
    }


}
