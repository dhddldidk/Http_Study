package kr.or.dgit.it.http_study.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kr.or.dgit.it.http_study.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<Item> items;

    private OnSelectedItem onSelectedItem;

    public RecyclerAdapter(List<Item> items, OnSelectedItem onSelectedItem) {
        this.items = items;
        this.onSelectedItem = onSelectedItem;
    }

    public interface OnSelectedItem {
        void onItemSelected(Item item, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemTitle.setText(items.get(position).getTitle());
        holder.itemDetail.setText(items.get(position).getDetail());
        holder.itemImage.setImageResource(items.get(position).getImg());
        holder.itemCheck.setChecked(items.get(position).isChecked());
        holder.itemCheck.setVisibility(items.get(position).getVisible());
        holder.itemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                items.get(position).setChecked(isChecked);
            }
        });

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());

                for(Item i : items){
                    i.setVisible(View.GONE);
                }
                notifyDataSetChanged();
            }
        });

        //카드뷰 자체 선택 시
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectedItem.onItemSelected(items.get(position), position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                for(Item i : items){
                    if (i.getVisible()==View.GONE) {
                        i.setVisible(View.VISIBLE);
                    }else{
                        i.setVisible(View.GONE);
                    }
                }
                notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;
        TextView itemDetail;
        CheckBox itemCheck;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemDetail =(TextView) itemView.findViewById(R.id.item_detail);
            itemCheck = (CheckBox) itemView.findViewById(R.id.item_check);
        }

    }

}
