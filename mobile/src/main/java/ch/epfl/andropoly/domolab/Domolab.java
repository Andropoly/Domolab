package ch.epfl.andropoly.domolab;

import android.app.Application;

import MQTTsender.MqttDomolab;

public class Domolab extends Application {

    private static Domolab mContext;
    private static MqttDomolab mqttDomolab;
    private static Boolean mqttCreated = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
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
}
