package ch.epfl.andropoly.domolab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

public class RoomFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    static private String type_room;
    static private String room_name;

    public RoomFragment() {
        // Required empty public constructor
    }

    static public RoomFragment newInstance(String type, String name) {
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        TextView device = (TextView) view.findViewById(R.id.txt_device);
        device.setText(room_name);

        ImageView img_device = (ImageView) view.findViewById(R.id.image_device);

        switch(type_room){
            case ("Kitchen"):
                img_device.setImageResource(R.drawable.table);
                break;
            case ("Bedroom"):
                img_device.setImageResource(R.drawable.bed);
                break;
            case ("Bathroom"):
                img_device.setImageResource(R.drawable.bathroom);
                break;
            case ("Living room"):
                img_device.setImageResource(R.drawable.sofa);
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
