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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import JsonUtilisties.myJsonReader;

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

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONObject obj = null;
                JSONObject obj2 = null;
                try {
                    obj = myJsonReader.jsonFromFileAsset( Tes2Activity.this,"test.json");
                    myJsonReader.jsonWriteFileInternal(Tes2Activity.this,"coucou.json", obj);
                    obj2 = myJsonReader.jsonFromFileInternal( Tes2Activity.this,"coucou.json");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    mqttDomolab.sendJSONToTopic(obj2,"test");
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }
                mqttDomolab.disconnect();
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
