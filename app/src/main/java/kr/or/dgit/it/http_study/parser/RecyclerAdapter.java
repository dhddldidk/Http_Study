package kr.or.dgit.it.http_study.parser;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kr.or.dgit.it.http_study.R;

public class RecyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<Item> items;

    public RecyclerAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Item item = items.get(position);
        viewHolder.itemName.setText(item.getItemName());
        viewHolder.makerName.setText(item.getMakerName());
        viewHolder.itemPrice.setText(item.getItemPrice()+"");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        TextView makerName;
        TextView itemPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            makerName = itemView.findViewById(R.id.makerName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
        }
    }
}
