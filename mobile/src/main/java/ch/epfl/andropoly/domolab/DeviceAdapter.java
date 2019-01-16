package ch.epfl.andropoly.domolab;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ItemViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private List<String> mList_name;
    private List<String> mList_type;
    private List<Integer> mList_state;

    DeviceAdapter(List<String> name, List<String> type, List<Integer> state){
        this.mList_name = name;
        this.mList_type = type;
        this.mList_state = state;
    }

    @Override
    public int getItemCount() {
        return mList_name.size();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.device_layout, parent, false);

        return new DeviceAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.display(mList_name.get(position), mList_type.get(position), mList_state.get(position));
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemViewHolder(@NonNull final View itemView) {
            super(itemView);

            // Add click function on the image
            ImageView img_device = (ImageView) itemView.findViewById(R.id.img_device);
            img_device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Click image",
                            Toast.LENGTH_SHORT).show();
                }
            });

            // Add click function on the text
            TextView txt_device = (TextView) itemView.findViewById(R.id.txt_device);
            txt_device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Click text",
                            Toast.LENGTH_SHORT).show();
                }
            });

            // Add toggle function on the switch
            Switch sw_device = (Switch) itemView.findViewById(R.id.sw_device);
            sw_device.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Toggle switch.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        void display(String name, String type, int state) {
            Context context = itemView.getContext();
            int id = context.getResources().getIdentifier(type, "drawable", context.getPackageName());

            // Set the image of the device
            ImageView img_device = (ImageView) itemView.findViewById(R.id.img_device);
            img_device.setImageResource(id);

            // Set the text of the device
            TextView txt_device = (TextView) itemView.findViewById(R.id.txt_device);
            txt_device.setText(name);

            // Set the state of the switch button
            Switch sw_device = (Switch) itemView.findViewById(R.id.sw_device);

            if(state == 0)
                sw_device.setChecked(false);
            else
                sw_device.setChecked(true);
        }
    }
}
