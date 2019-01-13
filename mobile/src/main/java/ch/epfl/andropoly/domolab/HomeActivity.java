package ch.epfl.andropoly.domolab;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager;

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

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            //TODO: Save user id
        }

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

        /*ConstraintLayout.LayoutParams params = new
                ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // Set the height by params
        params.height=200;

        // set height of RecyclerView
        holder.setLayoutParams(params);
*/
        recyclerView.setAdapter(new ItemAdapter(list_rooms, false));
    }
}
