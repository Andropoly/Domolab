package JsonUtilisties;

import android.content.Context;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ch.epfl.andropoly.domolab.HomeActivity;

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
}
