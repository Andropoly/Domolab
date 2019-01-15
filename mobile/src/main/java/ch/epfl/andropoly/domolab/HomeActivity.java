package ch.epfl.andropoly.domolab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import JsonUtilisties.myJsonReader;

import static JsonUtilisties.myJsonReader.jsonObjFromFileInternal;

public class HomeActivity extends AppCompatActivity {
    private final String TAG = "------" + this.getClass().getSimpleName() + "------";

    private List<String> list_rooms = Arrays.asList(
            "Kitchen","Room","Restroom","Living room","Kitchen","Room","Restroom","Living room"
    );

    private List<String> list_fav = Arrays.asList(
            "Kitchen","Restroom","Restroom","Restroom"
    );

    // Databse variable
    private String userID;
    private String profileKey;
    private DatabaseReference profileRef;
    private FirebaseDatabase database;

    //private GenericTypeIndicator<ArrayList<String>> ArrayListStringType = new GenericTypeIndicator<ArrayList<String>>() {};
    private String homename_db;
    private String userID_db;
    //private ArrayList<String> listofrooms_db;
    //private ArrayList<String> listoffav_db;
    private String roomsString_db;
    private String favsString_db;
    private JSONArray roomsArray_db;
    private JSONArray favsArray_db;


    // Main code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        HomeFragment homeFragment = HomeFragment.newInstance("Hey", "Hello");
        ListRoomsFragment listRoomsFragment = ListRoomsFragment.newInstance();
        String fragmentTAG = getResources().getString(R.string.home_page);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTAG);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_rooms_fragment, listRoomsFragment)
                .commit();

        if(fragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_fragment, homeFragment, fragmentTAG)
                    .commit();
        }

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
                homename_db = dataSnapshot.child("HomeName").getValue(String.class);
                userID_db = dataSnapshot.child("userID").getValue(String.class);
                //listofrooms_db = dataSnapshot.child("listOfRooms").getValue(ArrayListStringType);
                //listoffav_db = dataSnapshot.child("listOfFav").getValue(ArrayListStringType);
                roomsString_db = dataSnapshot.child("Rooms").getValue(String.class);
                favsString_db = dataSnapshot.child("Favorites").getValue(String.class);

                try {
                    roomsArray_db = new JSONArray(roomsString_db);
                    favsArray_db = new JSONArray(favsString_db);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setLayoutWithDatabase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Empty
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.NeedHelpId:
                Toast.makeText(HomeActivity.this, "Really Nigga -_- you need help for that ?", Toast.LENGTH_LONG).show();
                return true;
            case R.id.AddRoomId:
                Toast.makeText(HomeActivity.this, "Added Room", Toast.LENGTH_LONG).show();
                return true;
            case R.id.AddFavId:
                Toast.makeText(HomeActivity.this, "Added Favorite", Toast.LENGTH_LONG).show();
                return true;
            case R.id.AboutUsId:
                Toast.makeText(HomeActivity.this, "We are the JAVA gods", Toast.LENGTH_LONG).show();
                return true;
            case R.id.LogOutId:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        JSONObject JSONCredentials = new JSONObject();

        myJsonReader.jsonWriteFileInternal(HomeActivity.this, "savedCredentials.json", JSONCredentials);
        Intent LogoutIntent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(LogoutIntent);
    }

    private void setLayoutWithDatabase() {
        Toast.makeText(HomeActivity.this, "Welcome to your home: " + homename_db + " !",
                Toast.LENGTH_LONG).show();
        Log.d(TAG, "Welcome "+ userID_db + " to your home: " + homename_db + " !");
        //Log.d(TAG, "Your rooms: " + listofrooms_db);
        //Log.d(TAG, "Your favorites: " + listoffav_db);
        Log.d(TAG, "Rooms info are contained in " + roomsArray_db);
        Log.d(TAG, "Favs info are contained in " + favsArray_db);
    }

    @Override
    public void onBackPressed() {
        // TODO: When HomeFragment is active, propose to logout instead of doing nothing
        if(this.getSupportFragmentManager().getBackStackEntryCount() != 0)
            super.onBackPressed();
    }
}
