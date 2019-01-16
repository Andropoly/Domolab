package ch.epfl.andropoly.domolab;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import Dialog.DialogAddFavorite;
import Dialog.DialogAddRoom;
import Dialog.DialogEditRoom;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private List<String> mList_type;
    private List<String> mList_name;
    private String mItem;

    ItemAdapter(List<String> type, List<String> name, String item){
        this.mList_type = type;
        this.mList_name = name;
        this.mItem = item;
    }

    @Override
    public int getItemCount() {
        return mList_type.size();
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
        holder.display(mList_type.get(position), mList_name.get(position));
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private Button roomButton;


        ItemViewHolder(final View itemView) {
            super(itemView);
            roomButton = (Button) itemView.findViewById(R.id.btn_room);

            roomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView txt_room = (TextView) itemView.findViewById(R.id.txt_room);

                    int name_idx = mList_name.indexOf(txt_room.getText().toString());
                    String type = mList_type.get(name_idx);

                    RoomFragment roomFragment = RoomFragment.newInstance(type, txt_room.getText().toString());
                    AppCompatActivity activity = (AppCompatActivity) itemView.getContext();

                    if(txt_room.getText().toString().equals("New")) {
                        openDialogAddingItem(activity, mItem);
                    } else {
                        // Check if the fragment is already set to not setting it twice
                        if (activity.getSupportFragmentManager().getBackStackEntryCount() != 0)
                            activity.getSupportFragmentManager().popBackStack();

                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.home_fragment, roomFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });

            roomButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TextView txt_room = (TextView) itemView.findViewById(R.id.txt_room);
                    AppCompatActivity activity = (AppCompatActivity) itemView.getContext();

                    if(!txt_room.getText().toString().equals("New")) {
                        openDialogEditingItem(activity, mItem, txt_room.getText().toString());
                    }

                    return false;
                }
            });
        }

        private void openDialogAddingItem(AppCompatActivity activity, String item) {
            if(item.equals(activity.getResources().getString(R.string.add_room))) {
                DialogAddRoom dialogAddRoom = new DialogAddRoom();
                dialogAddRoom.show(activity.getSupportFragmentManager(), "Dialog add room");
            } else if(item.equals(activity.getResources().getString(R.string.add_favorite))) {
                DialogAddFavorite dialogAddFavorite = new DialogAddFavorite();
                dialogAddFavorite.show(activity.getSupportFragmentManager(), "Dialog add favorite");
            }
        }

        private void openDialogEditingItem(AppCompatActivity activity, String item, String name) {
            int name_idx = mList_name.indexOf(name);
            String type = mList_type.get(name_idx);

            FragmentManager fragment = activity.getSupportFragmentManager();

            if(item.equals(activity.getResources().getString(R.string.add_room))) {
                DialogEditRoom dialogEditRoom = DialogEditRoom.newInstance(name, type);
                dialogEditRoom.show(fragment, "Dialog edit room");
            } else if(item.equals(activity.getResources().getString(R.string.add_favorite))){
                DialogEditRoom dialogEditRoom = DialogEditRoom.newInstance(name, type);
                dialogEditRoom.show(fragment, "Dialog edit favorite");
            }
        }

        void display(String type, String name) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ImageView imgButton = (ImageView) itemView.findViewById(R.id.img_room);
                TextView txt_room = (TextView) itemView.findViewById(R.id.txt_room);
                txt_room.setText(name);

                switch (type) {
                    case ("Kitchen"):
                        imgButton.setImageResource(R.drawable.table);
                        break;
                    case ("Bedroom"):
                        imgButton.setImageResource(R.drawable.bed);
                        break;
                    case ("Bathroom"):
                        imgButton.setImageResource(R.drawable.bathroom);
                        break;
                    case ("Living room"):
                        imgButton.setImageResource(R.drawable.sofa);
                        break;
                    case ("New"):
                        imgButton.setImageResource(R.drawable.add);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
