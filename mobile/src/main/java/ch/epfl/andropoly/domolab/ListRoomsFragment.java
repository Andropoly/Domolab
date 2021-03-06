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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static JsonUtilisties.myJsonReader.jsonArrFromFileAsset;
import static JsonUtilisties.myJsonReader.jsonArrFromFileInternal;
import static JsonUtilisties.myJsonReader.jsonWriteFileInternal;
import static ch.epfl.andropoly.domolab.Domolab.MyRoomFile;

public class ListRoomsFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    List<String> list_type = new ArrayList<String>();
    List<String> list_name = new ArrayList<String>();
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

        setRoomAdapter();

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

    public void setRoomAdapter(){
        JSONArray room_list = new JSONArray();
        JSONObject room_obj = new JSONObject();

        list_name.clear();
        list_type.clear();

        try {
            room_list = jsonArrFromFileInternal(getActivity(), MyRoomFile);
        }
        catch (IOException e) {
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
                list_type.add(room_obj.getString("Type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                list_name.add(room_obj.getString("Name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        list_name.add("New");
        list_type.add("New");

        if(mRoomAdapter == null)
            mRoomAdapter = new ItemAdapter(list_type, list_name, getResources().getString(R.string.add_room));
        else
            mRoomAdapter.notifyDataSetChanged();
    }

    public void updateAdapter(){
        setRoomAdapter();
        ItemAdapter.updateDatabase(getActivity(), this.getResources().getString(R.string.add_room));
    }
}
