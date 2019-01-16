package JsonUtilisties;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ch.epfl.andropoly.domolab.Domolab;

import static ch.epfl.andropoly.domolab.Domolab.MyDevFile;

public class myJsonReader {
    private static String stringFromFile(Context activityContext, String filename) throws IOException {
        InputStream is = activityContext.getAssets().open(filename);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String json = new String(buffer, "UTF-8");
        return json;

    }
    private static String stringFromFileInternal(Context activityContext, String filename) throws IOException {

        FileInputStream is = activityContext.openFileInput(filename);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String json = new String(buffer, "UTF-8");
        return json;
    }

    public static JSONObject jsonObjFromFileAsset(Context activityContext, String filename) throws IOException, JSONException {
        /**
         *  Create a json object from a json file in the asset resources file. This is used to get
         *  an object for the devices and room list
         */
        return new JSONObject(stringFromFile(activityContext, filename));
    }
    public static JSONObject jsonObjFromFileInternal(Context activityContext, String filename) throws IOException, JSONException {
        /**
         *  Create a json object from a json file internally stored. This is used to get the stored
         *  values and states of the home
         */
        return new JSONObject(stringFromFileInternal(activityContext, filename));
    }
    public static JSONArray jsonArrFromFileAsset(Context activityContext, String filename) throws IOException, JSONException {
        /**
         *  Create a json array from a json file in the asset resources file. This is used to get
         *  an object for the devices and room list
         */
        return new JSONArray(stringFromFile(activityContext, filename));
    }
    public static JSONArray jsonArrFromFileInternal(Context activityContext, String filename) throws IOException, JSONException {
        /**
         *  Create a json array from a json file internally stored. This is used to get the stored
         *  values and states of the home
         */
        return new JSONArray(stringFromFileInternal(activityContext, filename));
    }


    public static void jsonWriteFileInternal(Context activityContext, String filename, JSONObject obj){
        /**
         * Write the Json object into a internally stored file so it can be accessed later.
         * The file is in private mode to avoid bothering and being read by others
         */

        FileOutputStream outputStream;
        try {
            outputStream = activityContext.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(obj.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void jsonWriteFileInternal(Context activityContext, String filename, JSONArray obj){
        /**
         * Write the Json array into a internally stored file so it can be accessed later.
         * The file is in private mode to avoid bothering and being read by others
         */

        FileOutputStream outputStream;
        try {
            outputStream = activityContext.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(obj.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNameExist(JSONArray array, String key, String value){
        /**
         * Check if the value is already existing for a given key.
         * Return false if it is not already present and true otherwise.
         */
        for(int i=0; i<array.length(); i++) {
            try {
                if (array.getJSONObject(i).get(key).equals(value)) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void deleteRoom(Context context, String filename, String key, String value){
        /**
         * Delete the room from the JSON filename by checking if the value is used with the key.
         */
        JSONArray room_list = new JSONArray();

        // Recover list from JSON file
        try {
            room_list = jsonArrFromFileInternal(context, filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0; i<room_list.length(); i++) {
            try {
                if (room_list.getJSONObject(i).get(key).equals(value)) {
                    room_list.remove(i);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Update database global variable
        Domolab.roomsArray_db = room_list;
        jsonWriteFileInternal(context, filename, room_list);
    }

    public static void editRoom(Context context, String filename, String key_type, String key_name,
                          String value_type, String value_name, String btn_room_name){
        /**
         * Edit the room from the JSON filename by checking if the value_name is used with the key_name.
         * Set the new values for:
         * -    key_type -> value_type
         * -    key_name -> value_name
         */
        JSONArray room_list = new JSONArray();

        // Recover list from JSON file
        try {
            room_list = jsonArrFromFileInternal(context, filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0; i<room_list.length(); i++) {
            try {
                if (room_list.getJSONObject(i).get(key_name).equals(btn_room_name)) {
                    if(isNameExist(room_list, key_name, value_name) && !btn_room_name.equals(value_name))
                        room_list.getJSONObject(i).put(key_name, value_name + "'");
                    else
                        room_list.getJSONObject(i).put(key_name, value_name);

                    room_list.getJSONObject(i).put(key_type, value_type);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Update database global variable
        Domolab.roomsArray_db = room_list;
        if(!btn_room_name.equals(value_name))
            updateDeviceRoomName(btn_room_name, value_name);

        jsonWriteFileInternal(context, filename, room_list);
    }

    static private void updateDeviceRoomName(String btn_room_name, String value_name){
        JSONObject room_new_name_obj = new JSONObject();

        try {
            room_new_name_obj = Domolab.devicesObject_db.getJSONObject(btn_room_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Domolab.devicesObject_db.put(value_name, room_new_name_obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Update database global variable
        Domolab.devicesObject_db.remove(btn_room_name);
    }

    public static void editInputDevices(Context context, String roomName, String deviceName,
                                String inputType, String value) throws  JSONException {
        /**
         * Edit a device string value in a particular room, name, and input type.
         */

        String filename = MyDevFile;
        JSONObject myDevices = null;

        try {
            myDevices = jsonObjFromFileInternal(context, filename);
        } catch (IOException e) {
            Log.e("EditDeviceValue", "The devices json file does not exist or we do not have the right to read it");
            e.printStackTrace();
        }
        myDevices.getJSONObject(roomName).getJSONObject(deviceName).getJSONObject("Inputs").put(inputType,value);

        jsonWriteFileInternal(context, filename, myDevices);
    }
    public static void editInputDevices(Context context, String roomName, String deviceName,
                                        String inputType, int value) throws  JSONException {
        /**
         * Edit a device int value in a particular room, name, and input type.
         */

        String filename = MyDevFile;
        JSONObject myDevices = null;


        try {
            myDevices = jsonObjFromFileInternal(context, filename);
        } catch (IOException e) {
            Log.e("EditDeviceValue", "The devices json file does not exist or we do not have the right to read it");

            e.printStackTrace();
        }
        myDevices.getJSONObject(roomName).getJSONObject(deviceName).getJSONObject("Inputs").put(inputType,value);
        jsonWriteFileInternal(context, filename, myDevices);
    }
    public static void editInputDevices(Context context, String roomName, String deviceName,
                                        String inputType, float value) throws  JSONException {
        /**
         * Edit a device float value in a particular room, name, and input type.
         */

        String filename = MyDevFile;
        JSONObject myDevices = null;

        try {
            myDevices = jsonObjFromFileInternal(context, filename);
        } catch (IOException e) {
            Log.e("EditDeviceValue", "The devices json file does not exist or we do not have the right to read it");
            e.printStackTrace();
        }
        myDevices.getJSONObject(roomName).getJSONObject(deviceName).getJSONObject("Inputs").put(inputType,value);


        jsonWriteFileInternal(context, filename, myDevices);
    }
    public static JSONObject toggleDevices(Context context, String roomName, String deviceName
                                        ) throws JSONException {
        /**
         * Edit a toggle device value to toggle this device in a particular room, name,.
         */

        String filename = MyDevFile;
        JSONObject myDevices = null;

        try {
            myDevices = jsonObjFromFileInternal(context, filename);
        } catch (IOException e) {
            Log.e("EditDeviceValue", "The devices json file does not exist or we do not have the right to read it");
            e.printStackTrace();
        }
        int state = myDevices.getJSONObject(roomName).getJSONObject(deviceName).getJSONObject("Inputs").getInt("Toggle");
        if (state>0){
            myDevices.getJSONObject(roomName).getJSONObject(deviceName).getJSONObject("Inputs").put("Toggle",0);
        } else {
            myDevices.getJSONObject(roomName).getJSONObject(deviceName).getJSONObject("Inputs").put("Toggle",1);
        }

        jsonWriteFileInternal(context, filename, myDevices);
        return myDevices;
    }


}
