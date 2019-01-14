package ch.epfl.andropoly.domolab;

import android.app.Application;

import MQTTsender.MqttDomolab;

public class Domolab extends Application {

    private static Domolab mContext;
    private static MqttDomolab mqttDomolab;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static void creatAndConnectMqtt(String username, String password, String serverURI){
        mqttDomolab = new MqttDomolab(mContext, username, password, serverURI);
    }

    public static MqttDomolab getMqttDomolab(){return  mqttDomolab;}

    public static Domolab getContext() {
        return mContext;
    }
}
