package ch.epfl.andropoly.domolab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        HomeFragment homeFragment = HomeFragment.newInstance("Hey", "Hello");
        ListRoomsFragment listRoomsFragment = ListRoomsFragment.newInstance("Hey", "Hello");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.list_rooms_fragment, listRoomsFragment)
                .addToBackStack(null)
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_fragment, homeFragment)
                .addToBackStack(null)
                .commit();

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            //TODO: Save user id
        }

        /*//private RecordingAdapter adapter;
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

        recyclerView.setAdapter(new ItemAdapter(list_rooms, false));*/
    }
}
