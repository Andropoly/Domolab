package ch.epfl.andropoly.domolab;

import android.os.Build;
import android.support.annotation.NonNull;
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
        View view;

        view = inflater.inflate(R.layout.item_layout, parent, false);

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
                    Log.e(TAG, String.valueOf(txt_room));

                    if(txt_room.getText().equals("Kitchen")) {
                        RoomFragment roomFragment = RoomFragment.newInstance(txt_room.getText().toString());
                        AppCompatActivity activity = (AppCompatActivity) itemView.getContext();

                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.home_fragment, roomFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }

        void display(String item) {
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
