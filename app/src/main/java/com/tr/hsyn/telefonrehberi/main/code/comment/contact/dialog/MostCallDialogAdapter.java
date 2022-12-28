package com.tr.hsyn.telefonrehberi.main.code.comment.contact.dialog;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.textdrawable.TextDrawable;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class MostCallDialogAdapter extends RecyclerView.Adapter<MostCallDialogAdapter.Holder> {

    private final List<MostCallItemViewData> mostCallItemViewDataList;

    public MostCallDialogAdapter(List<MostCallItemViewData> mostCallItemViewDataList) {

        this.mostCallItemViewDataList = mostCallItemViewDataList;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.most_calls_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {

        var item  = mostCallItemViewDataList.get(position);
        int color = Colors.COLOR_GENERATOR.getRandomColor();

        holder.name.setText(item.getName());
        holder.count.setImageDrawable(TextDrawable.builder().buildRound(String.valueOf(item.getCallSize()), color));

    }

    @Override
    public int getItemCount() {

        return mostCallItemViewDataList.size();
    }

    public static final class Holder extends RecyclerView.ViewHolder {

        TextView  name;
        ImageView count;

        public Holder(@NonNull @NotNull View itemView) {

            super(itemView);

            TextView  txtType = itemView.findViewById(R.id.most_calls_item_text_type);
            ImageView type    = itemView.findViewById(R.id.most_calls_item_img_type);
            name  = itemView.findViewById(R.id.most_calls_item_text_name);
            count = itemView.findViewById(R.id.most_calls_item_img_order_number);

            txtType.setText(name.getContext().getString(R.string.call_record));

            type.setImageDrawable(AppCompatResources.getDrawable(name.getContext(), R.drawable.all_calls));
            itemView.setBackgroundResource(Colors.getRipple());
        }
    }
}
