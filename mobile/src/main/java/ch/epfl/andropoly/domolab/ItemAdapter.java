package ch.epfl.andropoly.domolab;

import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final List<Pair<Object, String>> list_rooms = Arrays.asList(
            Pair.create(null, "Kitchen"),
            Pair.create(null, "Room"),
            Pair.create(null, "Restroom"),
            Pair.create(null, "Living room")
    );

    @Override
    public int getItemCount() {
        return list_rooms.size();
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
        Pair<Object, String> pair = list_rooms.get(position);
        holder.display(pair);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private final Button roomButton;
        private Pair<String, String> currentPair;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            roomButton = (Button) itemView.findViewById(R.id.btn_room);

            roomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO
                }
            });
        }

        public void display(Pair<Object, String> pair) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ImageView imgButton = (ImageView) itemView.findViewById(R.id.img_room);
                TextView txt_room = (TextView) itemView.findViewById(R.id.txt_room);
                txt_room.setText(pair.second);

                switch (pair.second) {
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
