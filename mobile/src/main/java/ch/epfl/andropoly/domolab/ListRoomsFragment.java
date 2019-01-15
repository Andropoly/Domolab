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
    public static ListRoomsFragment newInstance() {
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

    public void setRoomAdapter(){
        JSONArray room_list = new JSONArray();
        JSONObject room_obj = new JSONObject();

        list_name.clear();
        list_type.clear();

        try {
            if(mRoomAdapter == null) {
                room_list = jsonArrFromFileAsset(getActivity(), "data.json");
                jsonWriteFileInternal(getActivity(), "data_modified.json", room_list);
            } else
                room_list = jsonArrFromFileInternal(getActivity(), "data_modified.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Fill array with JSON array
        for(int i=0; i<room_list.length(); i++){
            try {
                room_obj = room_list.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                list_type.add(room_obj.getString("type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                list_name.add(room_obj.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        list_name.add("New");
        list_type.add("New");

        if(mRoomAdapter == null)
            mRoomAdapter = new ItemAdapter(list_type, list_name, getResources().getString(R.string.add_room));
    }

    public void updateAdapter(){
        setRoomAdapter();
        mRoomAdapter.notifyDataSetChanged();
    }
}
