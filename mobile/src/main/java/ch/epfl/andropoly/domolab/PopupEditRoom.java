package ch.epfl.andropoly.domolab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatDialogFragment;
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

import static JsonUtilisties.myJsonReader.deleteRoom;
import static JsonUtilisties.myJsonReader.editRoom;
import static JsonUtilisties.myJsonReader.jsonObjFromFileAsset;

public class PopupEditRoom extends AppCompatDialogFragment {
    private Spinner typeRoom;
    private EditText editRoomName;
    private PopupEditRoomListener mListener;

    public PopupEditRoom(){
        // Empty constructor required for DialogFragment
    }

    static public PopupEditRoom newInstance(String name, String type){
        PopupEditRoom fragment = new PopupEditRoom();
        Bundle args = new Bundle();

        args.putString("btn_type_room", type);
        args.putString("btn_room_name", name);
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        assert getArguments() != null;
        String btn_type_room = getArguments().getString("btn_type_room");
        final String btn_room_name = getArguments().getString("btn_room_name");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_room, null);

        builder.setView(view)
                .setTitle("Editing room")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteRoom(getActivity(), "my_rooms.json","Name", btn_room_name);
                        mListener.updateRoomAdapter();
                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String type_room = typeRoom.getSelectedItem().toString();
                        String room_name = editRoomName.getText().toString();

                        editRoom(getActivity(), "my_rooms.json", "Type", "Name", type_room, room_name, btn_room_name);
                        mListener.updateRoomAdapter();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });

        editRoomName = view.findViewById(R.id.edit_txt_room);
        typeRoom = view.findViewById(R.id.spn_room_type);

        fillItems(view, btn_type_room, btn_room_name);

        return builder.create();
    }

    private void fillItems(View view, String btn_type_room, String btn_room_name){
        JSONObject room_def = new JSONObject();
        JSONArray room_list = new JSONArray();
        List<String> spinnerArray =  new ArrayList<String>();

        // Recover list from JSON file
        try {
            room_def = jsonObjFromFileAsset(getActivity(), "rooms.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Fill array with JSON object
        try {
            room_list = room_def.getJSONArray("List");
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

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) view.findViewById(R.id.spn_room_type);
        sItems.setAdapter(adapter);

        // Fill items of the dialog with existing ones
        // Fill spinner
        for(int i=0; i<room_list.length(); i++) {
            try {
                if (room_list.getString(i).equals(btn_type_room)) {
                    sItems.setSelection(i);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Fill edittext
        EditText txt_name = (EditText) view.findViewById(R.id.edit_txt_room);
        txt_name.setText(btn_room_name);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (PopupEditRoomListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement PopupEditingRoomListener");
        }
    }

    public interface PopupEditRoomListener {
        void updateRoomAdapter();
    }
}
