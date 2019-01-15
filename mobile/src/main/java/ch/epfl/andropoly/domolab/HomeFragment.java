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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //private RecordingAdapter adapter;
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_favorites);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ItemAdapter(list_fav));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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
