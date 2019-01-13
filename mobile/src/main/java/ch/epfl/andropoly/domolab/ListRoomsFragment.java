package ch.epfl.andropoly.domolab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListRoomsFragment .OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListRoomsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListRoomsFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    private List<String> list_rooms = Arrays.asList(
            "Kitchen","Room","Restroom","Living room","Kitchen","Room","Restroom","Living room"
    );

    public ListRoomsFragment() {
        // Required empty public constructor
    }
    public static ListRoomsFragment newInstance(String param1, String param2) {
        ListRoomsFragment fragment = new ListRoomsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_rooms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //private RecordingAdapter adapter;
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_rooms);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ItemAdapter(list_rooms));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
