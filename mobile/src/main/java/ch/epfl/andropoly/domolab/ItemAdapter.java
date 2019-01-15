package ch.epfl.andropoly.domolab;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private List<String> mList_item;

    ItemAdapter(List<String> list){
        mList_item = list;
    }

    @Override
    public int getItemCount() {
        return mList_item.size();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_layout, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.display(mList_item.get(position));
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final Button roomButton;


        ItemViewHolder(final View itemView) {
            super(itemView);
            roomButton = (Button) itemView.findViewById(R.id.btn_room);

            roomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView txt_room = (TextView) itemView.findViewById(R.id.txt_room);

                    int name_idx = mList_name.indexOf(txt_room.getText().toString());
                    String type = mList_type.get(name_idx);

                    RoomFragment roomFragment = RoomFragment.newInstance(type, txt_room.getText().toString());
                    AppCompatActivity activity = (AppCompatActivity) itemView.getContext();

                    // Check if the fragment is already set to not setting it twice
                    AppCompatActivity activity = (AppCompatActivity) itemView.getContext();

                    if(!txt_room.getText().toString().equals("New")) {
                        openDialogEditingItem(activity, mItem, txt_room.getText().toString());
                    }

                    if(activity.getSupportFragmentManager().getBackStackEntryCount() != 0)
                        activity.getSupportFragmentManager().popBackStack();

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.home_fragment, roomFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        private void openDialogAddingItem(AppCompatActivity activity, String item) {
            if(item.equals(activity.getResources().getString(R.string.add_room))) {
                PopupAddingRoom popupAddingRoom = new PopupAddingRoom();
                popupAddingRoom.show(activity.getSupportFragmentManager(), "Popup adding room");
            }
        }

        private void openDialogEditingItem(AppCompatActivity activity, String item, String name) {
            if(item.equals(activity.getResources().getString(R.string.add_room))) {
                int name_idx = mList_name.indexOf(name);
                String type = mList_type.get(name_idx);

                FragmentManager fragment = activity.getSupportFragmentManager();
                PopupEditRoom popupEditRoom = PopupEditRoom.newInstance(name, type);

                popupEditRoom.show(fragment, "Popup editing room");
            }
        }

        void display(String type, String name) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ImageView imgButton = (ImageView) itemView.findViewById(R.id.img_room);
                TextView txt_room = (TextView) itemView.findViewById(R.id.txt_room);
                txt_room.setText(item);

                switch (item) {
                    case ("Kitchen"):
                        imgButton.setImageResource(R.drawable.table);
                        break;
                    case ("Room"):
                        imgButton.setImageResource(R.drawable.bed);
                        break;
                    case ("Restroom"):
                        imgButton.setImageResource(R.drawable.restroom);
                        break;
                    case ("Living room"):
                        imgButton.setImageResource(R.drawable.sofa);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
