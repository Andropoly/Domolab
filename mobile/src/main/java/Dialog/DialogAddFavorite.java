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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.andropoly.domolab.HomeActivity;
import ch.epfl.andropoly.domolab.R;

import static JsonUtilisties.myJsonReader.isNameExist;
import static JsonUtilisties.myJsonReader.jsonArrFromFileAsset;
import static JsonUtilisties.myJsonReader.jsonArrFromFileInternal;
import static JsonUtilisties.myJsonReader.jsonObjFromFileAsset;
import static JsonUtilisties.myJsonReader.jsonWriteFileInternal;

public class DialogAddFavorite extends AppCompatDialogFragment {
    private Spinner mRoomName;
    private Spinner mDeviceName;
    private DialogAddFavoriteListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_favorite, null);

        builder.setView(view)
                .setTitle("Add favorite")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(mDeviceName.getSelectedItem() == null)
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                        else {
                            String room_name = mRoomName.getSelectedItem().toString();
                            String device_name = mDeviceName.getSelectedItem().toString();

                            //addFavorite(room_name, device_name);
                            Toast.makeText(getContext(), "Add favorite", Toast.LENGTH_LONG).show();
                            mListener.updateFavoriteAdapter();
                        }
                    }
                });

        mRoomName = view.findViewById(R.id.spn_favorite_room);
        mDeviceName = view.findViewById(R.id.spn_favorite_device);

        fillSpinnerRooms(view);

        mRoomName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fillSpinnerDevices(selectedItemView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (DialogAddFavoriteListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DialogAddFavoriteListener");
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

    private void fillSpinnerRooms(View view){
        JSONArray devices_list = new JSONArray();
        List<String> spinnerArray =  new ArrayList<String>();

        // Recover list from JSON file
        try {
            devices_list = jsonArrFromFileInternal(getActivity(), "my_rooms.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0; i<devices_list.length(); i++){
            try {
                spinnerArray.add(devices_list.getJSONObject(i).getString("Name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Fill the spinner by setting an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) view.findViewById(R.id.spn_favorite_room);
        sItems.setAdapter(adapter);
    }

    private void fillSpinnerDevices(View view){
        Toast.makeText(getContext(), "Fill devices spinner", Toast.LENGTH_LONG).show();
    }

    public interface DialogAddFavoriteListener {
        void updateFavoriteAdapter();
    }
}
