package ch.epfl.andropoly.domolab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;

import MQTTsender.MqttDomolab;

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

        mqttDomolab = new MqttDomolab(getApplicationContext(), mUsername, mPwd, mServerAddr);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mqttDomolab.sendMsgToTopic("test button","test");
                Intent intent = new Intent(Tes2Activity.this, TestActivity.class);
                intent.putExtra("mqttclient", (Serializable) mqttDomolab);
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
                mqttDomolab.sendMsgToTopic("test","test");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

}
