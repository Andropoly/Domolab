package ch.epfl.andropoly.domolab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    private String mUsername = "ywcheuja";
    private String mPwd = "XD9nWtB5CRKl";
    private MqttConnectOptions mOptions = new MqttConnectOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mOptions.setUserName(mUsername);
        mOptions.setPassword(mPwd.toCharArray());
        mOptions.setCleanSession(true);


        String clientId = MqttClient.generateClientId();
        final MqttAndroidClient client =
                new MqttAndroidClient(Domolab.getContext(), "tcp://m21.cloudmqtt.com:16233",
                        clientId);

        try {
            IMqttToken token = client.connect(mOptions);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(Domolab.getContext(), "Successfully connected to the MQTT Broker",
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(Domolab.getContext(), "Failed to connect to the MQTT Broker",
                            Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }



        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String topic = "test";
                String payload = "the payload";
                byte[] encodedPayload = new byte[0];
                try {
                    IMqttToken token = client.connect();
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
                try {
                    client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(TestActivity.this, Tes2Activity.class);
                startActivity(intent);

            }
        });
    }

}


