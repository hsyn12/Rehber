package com.tr.hsyn.telefonrehberi.main.activity.call;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class MostCallsAdapter extends RecyclerView.Adapter<MostCallsAdapter.Holder> {

    private final List<MostCallsItemData> list;
    private final ItemIndexListener       selectListener;

    public MostCallsAdapter(@NotNull List<MostCallsItemData> list, @NotNull ItemIndexListener selectListener) {

        this.list           = list;
        this.selectListener = selectListener;

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.most_calls_item_view, parent, false), selectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {

        var item = list.get(position);

        holder.txtName.setText(item.getCallName());
        holder.txtType.setText(item.getTypeName());
        holder.imgRank.setImageDrawable(item.getRank());
        holder.imgType.setImageDrawable(item.getType());

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    static final class Holder extends RecyclerView.ViewHolder {

        TextView  txtName;
        TextView  txtType;
        ImageView imgType;
        ImageView imgRank;

        public Holder(@NotNull View itemView, @NotNull ItemIndexListener selectListener) {

            super(itemView);

            txtName = itemView.findViewById(R.id.most_calls_item_text_name);
            txtType = itemView.findViewById(R.id.most_calls_item_text_type);
            imgType = itemView.findViewById(R.id.most_calls_item_img_type);
            imgRank = itemView.findViewById(R.id.most_calls_item_img_order_number);

            itemView.setBackgroundResource(Colors.getRipple());
            itemView.setOnClickListener(v -> selectListener.onItemIndex(getAdapterPosition()));
        }
    }

}
