package ch.epfl.andropoly.domolab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static JsonUtilisties.myJsonReader.jsonArrFromFileAsset;
import static JsonUtilisties.myJsonReader.jsonArrFromFileInternal;
import static JsonUtilisties.myJsonReader.jsonObjFromFileAsset;
import static JsonUtilisties.myJsonReader.jsonWriteFileInternal;
import static android.support.constraint.Constraints.TAG;

public class PopupAddingRoom extends AppCompatDialogFragment {
    private Spinner typeRoom;
    private EditText editRoomName;
    private PopupAddingRoomListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_room, null);

        builder.setView(view)
                .setTitle("Adding room")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e(TAG, String.valueOf(typeRoom));
                        String type_room = typeRoom.getSelectedItem().toString();
                        String room_name = editRoomName.getText().toString();

                        addRoom(type_room, room_name);
                        mListener.updateRoomAdapter();
                    }
                });

        editRoomName = view.findViewById(R.id.edit_txt_room);
        typeRoom = view.findViewById(R.id.spn_room_type);

        fillSpinner(view);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (PopupAddingRoomListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement PopupAddingRoomListener");
        }
    }

    private void addRoom(String type, String name){
        JSONArray room_def = new JSONArray();
        JSONObject new_room = new JSONObject();

        // Recover list from JSON file
        try {
            room_def = jsonArrFromFileInternal(getActivity(), "data_modified.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            new_room.put("type", type);
            new_room.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        room_def.put(new_room);
        jsonWriteFileInternal(getActivity(), "data_modified.json", room_def);
    }

    private void fillSpinner(View view){
        JSONObject room_def = new JSONObject();
        JSONArray room_list = new JSONArray();
        List<String> spinnerArray =  new ArrayList<String>();

        // Recover list from JSON file
        try {
            room_def = jsonObjFromFileAsset(getActivity(), getString(R.string.file_rooms_definition));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Fill array with JSON object
        try {
            room_list = room_def.getJSONArray(getString(R.string.json_list_rooms));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0; i<room_list.length(); i++){
            try {
                spinnerArray.add(room_list.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Fill the spinner by setting an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, spinnerArray);

        Log.e(TAG, String.valueOf(spinnerArray));
        Log.e(TAG, String.valueOf(adapter));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) view.findViewById(R.id.spn_room_type);
        Log.e(TAG, String.valueOf(sItems));
        sItems.setAdapter(adapter);
    }

    public interface PopupAddingRoomListener {
        void updateRoomAdapter();
    }
}
