package com.tr.hsyn.telefonrehberi.main.activity.call.backup.dialog;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.code.android.dialog.Dialog;

import java.util.List;


public class DialogBackupOptions implements ItemIndexListener {

    private final ItemIndexListener selectListener;
    private final AlertDialog       dialog;

    public DialogBackupOptions(@NonNull Activity activity, @NonNull List<String> options, ItemIndexListener selectListener) {

        this.selectListener = selectListener;

        View         layout       = Dialog.inflate(activity, R.layout.call_backup_options);
        RecyclerView recyclerView = layout.findViewById(R.id.list_backup_options);

        //layout.setBackgroundColor(Colors.lighter(Colors.getPrimaryColor(), .3f));
        recyclerView.setAdapter(new BackupOptionsAdapter(options, this));

        dialog = Dialog.createDialog(activity, layout);
    }

    public void show() {

        dialog.show();
    }

    public void hide() {

        dialog.dismiss();
    }

    @Override
    public void onItemIndex(int position) {

        Runny.run(dialog::dismiss, 600L);

        if (selectListener != null) selectListener.onItemIndex(position);
    }

    public static final class BackupOptionsAdapter extends RecyclerView.Adapter<BackupOptionsAdapter.Holder> {

        private final List<String>      options;
        private final ItemIndexListener selectListener;

        public BackupOptionsAdapter(List<String> options, ItemIndexListener selectListener) {

            this.options        = options;
            this.selectListener = selectListener;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.backup_option, parent, false), selectListener);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

            holder.option.setText(options.get(position));
        }

        @Override
        public int getItemCount() {

            return options.size();
        }

        public static final class Holder extends RecyclerView.ViewHolder {

            private final TextView option;

            public Holder(@NonNull View itemView, ItemIndexListener selectListener) {

                super(itemView);

                option = itemView.findViewById(R.id.text_name_option);

                itemView.setBackgroundResource(Colors.getRipple());
                itemView.setOnClickListener(v -> {
                    if (selectListener != null) selectListener.onItemIndex(getAdapterPosition());
                });
            }
        }
    }

}
