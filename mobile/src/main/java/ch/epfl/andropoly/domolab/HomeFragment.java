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

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static JsonUtilisties.myJsonReader.jsonArrFromFileAsset;
import static JsonUtilisties.myJsonReader.jsonArrFromFileInternal;
import static JsonUtilisties.myJsonReader.jsonWriteFileInternal;

public class HomeFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    List<String> list_type = new ArrayList<String>();
    List<String> list_name = new ArrayList<String>();
    public static RecyclerView.Adapter mHomeAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_home, container, false);

        //private RecordingAdapter adapter;
        LinearLayoutManager manager = new LinearLayoutManager(layout.getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.list_favorites);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mHomeAdapter);

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

    private void setHomeAdapter(){
        JSONArray fav_list = new JSONArray();
        JSONObject fav_obj = new JSONObject();

        list_name.clear();
        list_type.clear();

        try {
            if(mHomeAdapter == null) {
                fav_list = jsonArrFromFileAsset(getActivity(), "data.json");
                jsonWriteFileInternal(getActivity(), "data_modified.json", fav_list);
            } else
                fav_list = jsonArrFromFileInternal(getActivity(), "data_modified.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Fill array with JSON array
        for(int i=0; i<fav_list.length(); i++){
            try {
                fav_obj = fav_list.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                list_type.add(fav_obj.getString("type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                list_name.add(fav_obj.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        list_name.add("New");
        list_type.add("New");

        if(mHomeAdapter == null)
            mHomeAdapter = new ItemAdapter(list_type, list_name, getResources().getString(R.string.add_room));
    }

    public void updateAdapter(){
        setHomeAdapter();
        mHomeAdapter.notifyDataSetChanged();
    }
}
