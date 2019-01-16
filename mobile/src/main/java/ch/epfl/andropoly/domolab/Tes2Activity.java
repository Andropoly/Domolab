package ch.epfl.andropoly.domolab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import JsonUtilisties.myJsonReader;

import MQTTsender.MqttDevice;
import MQTTsender.MqttDomolab;
import MQTTsender.NotConnectedException;

public class Tes2Activity extends AppCompatActivity {
    MqttDomolab mqttDomolab;

    private String mUsername = "ywcheuja";
    private String mPwd = "XD9nWtB5CRKl";
    private String mServerAddr = "tcp://m21.cloudmqtt.com:16233";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tes2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Domolab.creatAndConnectMqtt( mUsername, mPwd, mServerAddr);
        mqttDomolab =Domolab.getMqttDomolab();

        JSONObject obj3 = new JSONObject();
        try {
            obj3 = myJsonReader.jsonObjFromFileInternal(Tes2Activity.this, "mqttCurrentSettings.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONObject obj = null;
                JSONObject obj2 = null;
                JSONArray myObj = null;
                try {
                    obj = myJsonReader.jsonObjFromFileAsset( Tes2Activity.this,"testMyDevices.json");
                    myJsonReader.jsonWriteFileInternal(Tes2Activity.this,"myDevices.json", obj);
                    MqttDevice.mqttSendDevice(Tes2Activity.this, mqttDomolab, "Parents Bedroom", "Right light");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*try {
                    //obj.ge
                    mqttDomolab.sendJsonToTopic(obj.getJSONObject("Light"),"test");
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                //mqttDomolab.disconnect();
                Intent intent = new Intent(Tes2Activity.this, TestActivity.class);
                //intent.putExtra("mqttclient", mqttDomolab);
                startActivity(intent);
            }
        });
    }

    private void startMqtt(){
        mqttDomolab.setCallback(new MqttCallbackExtended() {

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

}
