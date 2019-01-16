package MQTTsender;

import android.content.Context;
import android.util.JsonReader;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import JsonUtilisties.myJsonReader;

public class MqttDevice {

    public static void mqttAllOff(Context activityContext, MqttDomolab mqttDomolab){
        try {
            JSONArray myDevices = myJsonReader.jsonArrFromFileInternal(activityContext, "my_devices.json");
        //TODO: finish this task

        } catch (IOException e) {
            Toast.makeText(activityContext, "No device in your home", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (JSONException e) {
            Toast.makeText(activityContext, "Issue while loading your devices", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    public static void mqttSendDevice(Context activityContext, MqttDomolab mqttDomolab, String roomName, String devicesName) {
        try {
            JSONObject myDevices = myJsonReader.jsonObjFromFileInternal(activityContext, "my_devices.json");
            JSONObject device =  myDevices.getJSONObject(roomName)
                    .getJSONObject(devicesName)
                    .getJSONObject("Inputs");

            String topic = roomName +"/"+devicesName;

            mqttDomolab.sendJsonToTopic(device, topic);

        } catch (IOException e) {
            Toast.makeText(activityContext, "No device in your home", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (JSONException e) {
            Toast.makeText(activityContext, "Issue while loading this device", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (NotConnectedException e) {
            Toast.makeText(activityContext, "Your MQTT is not connected properly", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
