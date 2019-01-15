package ch.epfl.andropoly.domolab;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TestWearServiceActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mButton;
    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_wear_service);

        mEditText = findViewById(R.id.editTextRoomName);
        mButton = findViewById(R.id.sendNameButton);


    }

    public void sendRoomNameCallback(View view) {
        roomName = mEditText.getText().toString();
        if (roomName == "") {
            roomName = "void";
        }

        Intent intentWear = new Intent(TestWearServiceActivity.this,WearService.class);
        intentWear.setAction(WearService.ACTION_SEND.ROOM_NAME_SEND.name());
        intentWear.putExtra(WearService.ROOM_NAME,roomName);
        startService(intentWear);
    }
}
