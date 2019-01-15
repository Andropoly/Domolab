package ch.epfl.andropoly.domolab;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import MQTTsender.MqttDomolab;
import MQTTsender.NotConnectedException;

public class TestActivity extends AppCompatActivity {
    MqttDomolab mqttDomolab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mqttDomolab =Domolab.getMqttDomolab();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button button = findViewById(R.id.testButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject obj = new JSONObject();

                try {
                    obj.put("434343",3.2);
                    obj.put("3434343",3.2);
                    mqttDomolab.sendJsonToTopic(obj,"test");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                    Toast.makeText(Domolab.getContext(), "Not connected",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
