package ch.epfl.andropoly.domolab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static JsonUtilisties.myJsonReader.jsonArrFromFileAsset;
import static JsonUtilisties.myJsonReader.jsonArrFromFileInternal;

public class ListRoomsFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    public static RecyclerView.Adapter mRoomAdapter;

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
        View layout = inflater.inflate(R.layout.fragment_list_rooms, container, false);

        this.setRoomAdapter(true);

        //private RecordingAdapter adapter;
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.list_rooms);

        recyclerView.setLayoutManager(new LinearLayoutManager(layout.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(mRoomAdapter);

        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setRoomAdapter(boolean asset){
        String filename;
        JSONArray fav_list = new JSONArray();
        JSONObject room_obj = new JSONObject();
        List<String> list_type = new ArrayList<String>();
        List<String> list_name = new ArrayList<String>();

        try {
            if(asset)
                fav_list = jsonArrFromFileAsset(getActivity(), "data.json");
            else
                fav_list = jsonArrFromFileInternal(Domolab.getContext(), "data_modified.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Fill array with JSON array
        for(int i=0; i<fav_list.length(); i++){
            try {
                room_obj = fav_list.getJSONObject(i);
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

        list_type.add("New");
        list_name.add("New");

        Log.e(TAG, String.valueOf(list_name));

        mRoomAdapter = new ItemAdapter(list_type, list_name, getResources().getString(R.string.add_room));
    }

    public void updateAdapter(){
        setRoomAdapter(false);
        mRoomAdapter.notifyDataSetChanged();
    }
}
