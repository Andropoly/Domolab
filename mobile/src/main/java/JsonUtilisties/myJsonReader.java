package JsonUtilisties;

import android.content.Context;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static JSONObject jsonFromFile(Context activityContext, String filename) throws IOException, JSONException {
        return new JSONObject(stringFromFile(activityContext, filename));
    }
}
