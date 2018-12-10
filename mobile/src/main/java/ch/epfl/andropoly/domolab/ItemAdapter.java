package ch.epfl.andropoly.domolab;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        private final Button imgButton;
        private Pair<String, String> currentPair;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            imgButton = ((Button) itemView.findViewById(R.id.imageButton));

            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO
                }
            });
        }

        public void display(Pair<Object, String> pair) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                switch (pair.second) {
                    case ("Kitchen"):
                        imgButton.setBackgroundResource(R.drawable.table);
                        break;
                    case ("Room"):
                        imgButton.setBackgroundResource(R.drawable.bed);
                        break;
                    case ("Restroom"):
                        imgButton.setBackgroundResource(R.drawable.restroom);
                        break;
                    case ("Living room"):
                        imgButton.setBackgroundResource(R.drawable.sofa);
                        break;
                    default:
                        break;
                }
            }

            imgButton.setText(pair.second);
        }
    }
}
