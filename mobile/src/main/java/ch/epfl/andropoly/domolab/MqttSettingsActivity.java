package ch.epfl.andropoly.domolab;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import JsonUtilisties.myJsonReader;

public class MqttSettingsActivity extends AppCompatActivity {
    private String mHouseName;
    private String mMqttServerURI;
    private String mMqttUsername;
    private String mMqttPassword;

    private EditText mHouse;
    private EditText mServer;
    private EditText mUsername;
    private EditText mPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button saveButton = (Button)findViewById(R.id.saveSettingsButton);
        Button cancelButton = (Button)findViewById(R.id.cancelSettingButton);



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * On save clicked, the value of the text are stored into a json file for further used.
                 */

                mHouse = (EditText) findViewById(R.id.houseTextEdit);
                mServer = (EditText)findViewById(R.id.mqttServerTextEdit);
                mUsername = (EditText)findViewById(R.id.mqttUsernameTextEdit);
                mPassword = (EditText) findViewById(R.id.houseTextEdit);

                JSONObject obj1 = new JSONObject();
                JSONObject obj2 = new JSONObject();

                mHouseName = mHouse.getText().toString();
                mMqttServerURI = mServer.getText().toString();
                mMqttUsername = mUsername.getText().toString();
                mMqttPassword = mPassword.getText().toString();

                boolean cancel = false;
                View focusView = null;

                if(isServerURIValid(mMqttServerURI)){

                    try {
                        obj1.put("HouseName", mHouseName);
                        obj2.put("MQTTServer",mMqttServerURI);
                        obj2.put("MQTTUsername",mMqttUsername);
                        obj2.put("MQTTPassword",mMqttPassword);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    myJsonReader.jsonWriteFileInternal(MqttSettingsActivity.this, "currentHouse.json", obj1);
                    myJsonReader.jsonWriteFileInternal(MqttSettingsActivity.this, "mqttCurrentSettings.json", obj2);

                    Intent intent = new Intent(MqttSettingsActivity.this, Tes2Activity.class);
                    startActivity(intent);
                }else{
                    mServer.setError("Your server address is missing :");
                    focusView = mServer;
                    focusView.requestFocus();

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private boolean isServerURIValid(String serverURI) {
        //TODO: Replace this with your own logic
        return serverURI.contains(":");
    }


}
