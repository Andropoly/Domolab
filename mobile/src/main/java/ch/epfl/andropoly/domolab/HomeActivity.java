package ch.epfl.andropoly.domolab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    public enum ROOM {LIVING, KITCHEN, BEDROOM}

    private final String TAG = this.getClass().getSimpleName();

    //private RecordingAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            //TODO: Save user id
        }

        recyclerView = (RecyclerView) findViewById(R.id.list_rooms);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ItemAdapter());

        recyclerView = (RecyclerView) findViewById(R.id.list_favorites);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ItemAdapter());
    }

    /*private class RecordingAdapter extends ArrayAdapter<ItemRecording> {
        private int item_layout;

        RecordingAdapter(Activity activity, int item_layout) {
            super(activity, item_layout);
            this.item_layout = item_layout;
        }

        @NonNull
        @Override
        @SuppressLint("StringFormatInvalid")
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //Reference to the row View
            String txt_button = "";
            View item = convertView;

            if (item == null) {
                //Inflate it from layout
                item = LayoutInflater.from(getContext()).inflate(item_layout, parent, false);
            }

            Drawable image = getResources().getDrawable(R.drawable.penguin);
            ROOM activity = ROOM.LIVING;

            switch (activity) {
                case LIVING:
                    image = getResources().getDrawable(R.drawable.sofa);
                    txt_button = "Living room";
                    break;
                case KITCHEN:
                    image = getResources().getDrawable(R.drawable.table);
                    txt_button = "Kitchen";
                    break;
                case BEDROOM:
                    image = getResources().getDrawable(R.drawable.bed);
                    txt_button = "Bedroom";
                    break;
            }

            ImageView historyActivityImage = item.findViewById(R.id.item_btn_room);
            historyActivityImage.setImageDrawable(image);

            if (getItem(position) != null) {
                ((TextView) item.findViewById(R.id.item_btn_room)).setText(txt_button);
            } else {
                Log.v(TAG, "getItem(position) is null");
            }

            return item;
        }
    }*/
}
