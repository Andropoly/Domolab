package ch.epfl.andropoly.domolab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import static android.support.constraint.Constraints.TAG;

public class PopupEditRoom extends AppCompatDialogFragment {
    private Spinner typeRoom;
    private EditText editRoomName;
    private PopupEditRoomListener mListener;

    private String btn_type_room;
    private String btn_room_name;

    /*public PopupEditRoom(String name, String type){
        btn_room_name = name;
        btn_type_room = type;
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_room, null);

        builder.setView(view)
                .setTitle("Adding room")
                .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
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

                        ItemRecording.addList_rooms(type_room, room_name);
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

        return builder.create();
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
