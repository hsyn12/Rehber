package com.tr.hsyn.telefonrehberi.dev.fastadapter;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.selection.ItemIndexListener;

import java.util.List;


public abstract class FastAdapter<T> extends RecyclerView.Adapter<FastAdapter.FastHolder> {

    protected List<T> list;

    public FastAdapter(List<T> list) {

        this.list = list;
    }

    protected abstract FastHolder createHolder(@NonNull ViewGroup parent, int viewType);

    protected abstract void bindHolder(@NonNull FastHolder holder, int position);

    @NonNull
    @Override
    public FastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return createHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull FastHolder holder, int position) {

        bindHolder(holder, position);
    }

    @Override
    public int getItemCount() {

        return list.size();
    }


    public abstract static class FastHolder extends RecyclerView.ViewHolder {

        public FastHolder(@NonNull View itemView, ItemIndexListener selectListener) {

            super(itemView);

            var root = getRoot();

            root.setBackgroundResource(Colors.getRipple());
            root.setOnClickListener(v -> {
                if (selectListener != null) selectListener.onItemIndex(getAdapterPosition());
            });
        }

        protected abstract View getRoot();
    }

}
