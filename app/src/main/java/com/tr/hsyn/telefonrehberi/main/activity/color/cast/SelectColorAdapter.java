package com.tr.hsyn.telefonrehberi.main.activity.color.cast;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.R;

import java.util.List;


@SuppressWarnings({"MethodReturnOfConcreteClass", "MethodParameterOfConcreteClass"})
public class SelectColorAdapter extends RecyclerView.Adapter<SelectColorAdapter.ViewHolder> {

    private List<ColorModel>  colors;
    private ItemIndexListener clickListener;
    private int               selectedIndex;

    public SelectColorAdapter(List<ColorModel> colors) {

        this.colors = colors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.color_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        ColorModel color = colors.get(i);

        holder.color.setBackgroundColor(color.getColor());
        holder.colorName.setText(color.getName());

        if (selectedIndex == holder.getAdapterPosition()) {

            holder.itemView.setBackgroundResource(com.tr.hsyn.rescolors.R.color.call_filter_item_selected_color);
        }
        else {

            holder.itemView.setBackgroundResource(Colors.getRipple());
        }
    }

    @Override
    public int getItemCount() {

        return colors.size();
    }
	
	/*@Override
	public void onViewRecycled(@NonNull ViewHolder holder) {
		
		super.onViewRecycled(holder);
		
		if (selectedIndex == holder.getAdapterPosition()) {
			
			holder.itemView.setBackgroundResource(R.color.call_filter_item_selected_color);
		}
		else {
			
			holder.itemView.setBackgroundResource(Colors.getRipple());
		}
	}*/

    public void setSelectedIndex(int selectedIndex) {

        this.selectedIndex = selectedIndex;
    }

    public SelectColorAdapter setClickListener(ItemIndexListener listener, int selectedIndex) {

        clickListener      = listener;
        this.selectedIndex = selectedIndex;
        return this;
    }

    final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View     color;
        TextView colorName;

        ViewHolder(@NonNull View itemView) {

            super(itemView);


            color     = itemView.findViewById(R.id.view);
            colorName = itemView.findViewById(R.id.color);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int selected = getAdapterPosition();

            if (selected != selectedIndex)
                if (clickListener != null) {clickListener.onItemIndex(getAdapterPosition());}
        }
    }
}
