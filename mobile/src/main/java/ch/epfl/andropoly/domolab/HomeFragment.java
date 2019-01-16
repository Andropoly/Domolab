package ch.epfl.andropoly.domolab;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import static ch.epfl.andropoly.domolab.Domolab.MyFavFile;

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

        final TextView TempView = layout.findViewById(R.id.tempView);
        String temp = Double.toString(Domolab.temp);
        TempView.setText(temp+"°");

        Domolab.TempChanged.setListener(new BooleanVariable.ChangeListener() {
            @Override
            public void onChange() {
                String temp = Double.toString(Domolab.temp);
                TempView.setText(temp+"°");

            }
        });

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
            fav_list = jsonArrFromFileInternal(getActivity(), MyFavFile);
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
                list_type.add(fav_obj.getString("Type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                list_name.add(fav_obj.getString("Name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        list_name.add("New");
        list_type.add("New");

        if(mHomeAdapter == null)
            mHomeAdapter = new ItemAdapter(list_type, list_name, getResources().getString(R.string.add_favorite));
        else
            mHomeAdapter.notifyDataSetChanged();
    }

    public void updateAdapter(){
        setHomeAdapter();
    }
}
