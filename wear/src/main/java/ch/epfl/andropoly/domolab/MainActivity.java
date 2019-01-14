package ch.epfl.andropoly.domolab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    public static final String ACTION_RECEIVE_ROOM_INFO = "RECEIVE_ROOM_INFO";
    public static final String ROOM_NAME = "ROOM_NAME";
    private TextView mTextView;
    private ConstraintLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_layout_main);

        mTextView = (TextView) findViewById(R.id.HelloText);

        mLayout = findViewById(R.id.exampleLayout);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                TextView textView = findViewById(R.id.RoomText);

                String roomName = intent.getStringExtra(ROOM_NAME);
                textView.setText(roomName);

            }
        }, new IntentFilter(ACTION_RECEIVE_ROOM_INFO));

        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        updateDisplay();
    }

    private void updateDisplay() {
        if(isAmbient()){
            mLayout.setBackgroundColor(getResources().getColor(android.R.color.black,getTheme()));
        }else{
            mLayout.setBackgroundColor(getResources().getColor(android.R.color.white,getTheme()));
        }
    }
}

