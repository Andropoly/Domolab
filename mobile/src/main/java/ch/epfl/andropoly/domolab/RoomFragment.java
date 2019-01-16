package ch.epfl.andropoly.domolab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static JsonUtilisties.myJsonReader.jsonObjFromFileInternal;
import static ch.epfl.andropoly.domolab.Domolab.MyDevFile;

public class RoomFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    static private String type_room;
    static private String room_name;

    List<String> list_name = new ArrayList<String>();
    List<String> list_type = new ArrayList<String>();
    List<Integer> list_state = new ArrayList<Integer>();
    public static RecyclerView.Adapter mDeviceAdapter;

    public RoomFragment() {
        // Required empty public constructor
    }

    static public RoomFragment newInstance(String type, String name) {
        RoomFragment fragment = new RoomFragment();

        type_room = type;
        room_name = name;

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
        View layout = inflater.inflate(R.layout.fragment_room, container, false);

        setDeviceAdapter();
        TextView txt_room_name = (TextView) layout.findViewById(R.id.txt_room);
        txt_room_name.setText(room_name);

        //private RecordingAdapter adapter;
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.list_devices);

        recyclerView.setLayoutManager(new LinearLayoutManager(layout.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mDeviceAdapter);

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

    public void setDeviceAdapter(){
        JSONArray device_list_room = new JSONArray();
        JSONObject all_device_obj = new JSONObject();

        list_name.clear();
        list_type.clear();
        list_state.clear();

        try {
            all_device_obj = jsonObjFromFileInternal(getContext(), MyDevFile).getJSONObject(room_name);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Fill list_name by taking name from JSONObject
        try {
            device_list_room = all_device_obj.getJSONArray("List");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(device_list_room.length() != 0) {
            for (int i = 0; i < device_list_room.length(); i++) {
                try {
                    list_name.add(device_list_room.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Fill list_type and list_state with the corresponding name
            for (int i = 0; i < device_list_room.length(); i++) {
                try {
                    list_type.add(all_device_obj.getJSONObject(list_name.get(i)).getString("Type"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    list_state.add(all_device_obj.getJSONObject(list_name.get(i)).getJSONObject("Inputs").getInt("Toggle"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        mDeviceAdapter = new DeviceAdapter(list_name, list_type, list_state, room_name);
    }

    public void updateAdapter(){
        setDeviceAdapter();
    }
}
