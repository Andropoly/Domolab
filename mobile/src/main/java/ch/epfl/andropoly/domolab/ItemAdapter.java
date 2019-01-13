package ch.epfl.andropoly.domolab;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private List<String> list_item;
    private boolean square;

    ItemAdapter(List<String> list, boolean ratio){
        list_item = list;
        square = ratio;
    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if(square)
            view = inflater.inflate(R.layout.item_height_layout, parent, false);
        else
            view = inflater.inflate(R.layout.item_width_layout, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.display(list_item.get(position));
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private final Button roomButton;


        ItemViewHolder(final View itemView) {
            super(itemView);
            roomButton = (Button) itemView.findViewById(R.id.btn_room);

            roomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO
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
