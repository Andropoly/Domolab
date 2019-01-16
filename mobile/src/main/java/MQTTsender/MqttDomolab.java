package MQTTsender;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import ch.epfl.andropoly.domolab.Domolab;
import ch.epfl.andropoly.domolab.LoginActivity;

public class MqttDomolab{
    public MqttAndroidClient mqttAndroidClient;

    private String mServerUri = "tcp://m21.cloudmqtt.com:16233";

    private final String clientId = MqttClient.generateClientId();
    private final String mSubscriptionTopic = "sensor/+";

    private String mUsername = "ywcheuja";
    private String mPassword = "XD9nWtB5CRKl";

    public MqttDomolab(Context context, String username, String password, String serverUri){
        mUsername = username;
        mPassword = password;
        mServerUri = serverUri;

        mqttAndroidClient = new MqttAndroidClient(context, mServerUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("mqtt", s);
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Mqtt", mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        try {
            connect();
        } catch (AlreadyConnectecException e) {
            e.printStackTrace();
        }
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    private void connect() throws AlreadyConnectecException{
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(mUsername);
        mqttConnectOptions.setPassword(mPassword.toCharArray());

        if (mqttAndroidClient.isConnected()){
            throw new AlreadyConnectecException();
        } else {
            try {

                mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

                        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                        disconnectedBufferOptions.setBufferEnabled(true);
                        disconnectedBufferOptions.setBufferSize(100);
                        disconnectedBufferOptions.setPersistBuffer(false);
                        disconnectedBufferOptions.setDeleteOldestMessages(false);
                        mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                        try {
                            subscribeToTopic(mSubscriptionTopic);
                        } catch (NotConnectedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.w("Mqtt", "Failed to connect to: " + mServerUri + exception.toString());
                    }
                });


            } catch (MqttException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void disconnect() {
        try {
            mqttAndroidClient.disconnect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void subscribeToTopic(String subscriptionTopic) throws NotConnectedException{
        if (mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.w("Mqtt", "Subscribed!");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.w("Mqtt", "Subscribed fail!");
                    }
                });

            } catch (MqttException e) {
                System.err.println("Exception whilst subscribing");
                e.printStackTrace();
            }
        } else {
            throw new NotConnectedException();
        }
    }

    public void unbscribeFromTopic(String subscriptionTopic) throws NotConnectedException{
        if (mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.unsubscribe(subscriptionTopic, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.w("Mqtt", "Unsubscribed");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.w("Mqtt", "Unsubscribed fail!");
                    }
                });
            } catch (MqttException e) {
                System.err.println("Exception whilst subscribing");
                e.printStackTrace();
            }
        } else{
            throw new NotConnectedException();
        }
    }

    public void sendMsgToTopic(String payload, String topic) throws NotConnectedException {
        if (mqttAndroidClient.isConnected()) {
            byte[] encodedPayload = new byte[0];
            try {
                IMqttToken token = mqttAndroidClient.connect();
                encodedPayload = payload.getBytes("UTF-8");
                MqttMessage message = new MqttMessage(encodedPayload);
                mqttAndroidClient.publish(topic, message, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.w("Mqtt", "Message send");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.w("Mqtt", "Message couldn't be send!");
                    }
                });
            } catch (UnsupportedEncodingException | MqttException e) {
                System.err.println("Exception whilst sending");
                e.printStackTrace();
            }
        } else {
            throw new NotConnectedException();
        }
    }

    public void sendJsonToTopic(JSONObject obj, String topic) throws NotConnectedException{

        if (mqttAndroidClient.isConnected()) {
            byte[] encodedPayload = new byte[0];
            try {

                IMqttToken token = mqttAndroidClient.connect();
                try {
                    encodedPayload = obj.toString().getBytes("utf-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                MqttMessage message = new MqttMessage(encodedPayload);
                mqttAndroidClient.publish(topic, message, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.w("Mqtt", "Json send");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.w("Mqtt", "Message couldn't be send!");
                    }
                });
            } catch (MqttException e) {
                System.err.println("Exception whilst sending");
                e.printStackTrace();
            }
        } else {
            throw new NotConnectedException();
        }

    }

    public void sendJsonToTopic(JSONArray obj, String topic) throws NotConnectedException{

        if (mqttAndroidClient.isConnected()) {
            byte[] encodedPayload = new byte[0];
            try {

                IMqttToken token = mqttAndroidClient.connect();
                try {
                    encodedPayload = obj.toString().getBytes("utf-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                MqttMessage message = new MqttMessage(encodedPayload);
                mqttAndroidClient.publish(topic, message, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.w("Mqtt", "Json send");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.w("Mqtt", "Message couldn't be send!");
                    }
                });
            } catch (MqttException e) {
                System.err.println("Exception whilst sending");
                e.printStackTrace();
            }
        } else {
            throw new NotConnectedException();
        }

    }
}

