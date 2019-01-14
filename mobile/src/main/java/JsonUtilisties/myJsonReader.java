package JsonUtilisties;

import android.content.Context;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public static JSONObject jsonFromFileAsset(Context activityContext, String filename) throws IOException, JSONException {
        return new JSONObject(stringFromFile(activityContext, filename));
    }
    public static JSONObject jsonFromFileInternal(Context activityContext, String filename) throws IOException, JSONException {
        return new JSONObject(stringFromFileInternal(activityContext, filename));
    }

    public static void jsonWriteFileInternal(Context activityContext, String filename, JSONObject obj){
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
