package Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import ch.epfl.andropoly.domolab.R;

import static JsonUtilisties.myJsonReader.isNameExist;
import static JsonUtilisties.myJsonReader.jsonArrFromFileInternal;
import static JsonUtilisties.myJsonReader.jsonObjFromFileAsset;
import static JsonUtilisties.myJsonReader.jsonWriteFileInternal;

public class DialogAddRoom extends AppCompatDialogFragment {
    private Spinner mTypeRoom;
    private EditText mEditRoomName;
    private DialogAddRoomListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_room, null);

        builder.setView(view)
                .setTitle("Add room")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String type_room = mTypeRoom.getSelectedItem().toString();
                        String room_name = mEditRoomName.getText().toString();

                        addRoom(type_room, room_name);
                        mListener.updateRoomAdapter();
                    }
                });

        mTypeRoom = view.findViewById(R.id.spn_room_type);
        mEditRoomName = view.findViewById(R.id.edit_txt_room);

        fillSpinner(view);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (DialogAddRoomListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DialogAddRoomListener");
        }
    }

    private void addRoom(String type, String name){
        JSONArray room_list = new JSONArray();
        JSONObject new_room = new JSONObject();

        // Recover list from JSON file
        try {
            room_list = jsonArrFromFileInternal(getActivity(), "my_rooms.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(isNameExist(room_list, "Name", name))
                new_room.put("Name", name + "'");
            else
                new_room.put("Name", name);

            new_room.put("Type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        room_list.put(new_room);
        jsonWriteFileInternal(getActivity(), "my_rooms.json", room_list);
    }

    private void fillSpinner(View view){
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
    }

    public interface DialogAddRoomListener {
        void updateRoomAdapter();
    }
}
