package ch.epfl.andropoly.domolab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private List<String> list_rooms = Arrays.asList(
            "Kitchen","Room","Restroom","Living room","Kitchen","Room","Restroom","Living room"
    );

    private List<String> list_fav = Arrays.asList(
            "Kitchen","Restroom","Restroom","Restroom"
    );
    private String userID;
    private String profileKey;
    private DatabaseReference profileRef;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            userID = intent.getExtras().getString("USERID");
            profileKey = intent.getExtras().getString("PROFILEKEY");
        }

        database = FirebaseDatabase.getInstance();
        profileRef = database.getReference("profiles");
        profileRef.child(profileKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String homename_db = dataSnapshot.child("HomeName").getValue(String.class);
                String userID_db = dataSnapshot.child("userID").getValue(String.class);
                setLayoutWithDatabase(homename_db, userID_db);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Empty
            }
        });

        //private RecordingAdapter adapter;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_rooms);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ItemAdapter(list_rooms, true));

        recyclerView = (RecyclerView) findViewById(R.id.list_favorites);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ItemAdapter(list_fav, true));

        Log.e(TAG, Integer.toString(recyclerView.getHeight()));

        int numberOfColumns = 2;
        StaggeredGridLayoutManager grid_layout = new StaggeredGridLayoutManager(numberOfColumns,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.grid_options);
        recyclerView.setLayoutManager(grid_layout);

        recyclerView.setAdapter(new ItemAdapter(list_rooms, false));



    }

    private void setLayoutWithDatabase(String homename, String userid) {
        Toast.makeText(HomeActivity.this, "Welcome to your home: " + homename + " !",
                Toast.LENGTH_LONG).show();
        Log.d(TAG, "Welcome to your home: " + homename + " !");
    }
}
