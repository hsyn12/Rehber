package com.tr.hsyn.telefonrehberi.main.activity.color;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.xtoolbar.Toolbarx;
import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.colors.Rehber;
import com.tr.hsyn.selection.ItemIndexListener;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.activity.color.cast.SelectColorDialog;
import com.tr.hsyn.xlog.xlog;


public class ColorsActivity extends AppCompatActivity implements ItemIndexListener {
	
	public static final String      SELECTED_COLOR = "selected_color";
	private             ProgressBar progressBar;
	private             TextView    colorsDescription;
	private             Toolbar     toolbar;
	private             int[]       colors;
	private             String[]    colorNames;
	private             int         colorIndex;
	private             int         primaryColor;
	private             Button      btnSelectColor;
	private             Button      btnApplyColor;
	private             int         selectedColor;
	private             String      selectedColorName;
	private             ViewGroup   root;
	private             int         lastColor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_colors);
		
		root              = findViewById(R.id.colors_activity_root);
		primaryColor      = Rehber.ColorSelection.getSelected(this);
		colorIndex        = Rehber.Color.getColorIndex(this, primaryColor);
		colors            = Rehber.Color.getColors(this);
		colorNames        = Rehber.Color.getColorNames(this);
		btnSelectColor    = findViewById(R.id.button_select_color);
		btnApplyColor     = findViewById(R.id.button_apply_color);
		progressBar       = findViewById(R.id.progress_colors);
		colorsDescription = findViewById(R.id.colors_description);
		toolbar           = findViewById(R.id.toolbar_colors);
		
		progressBar.setVisibility(View.GONE);
		
		Toolbarx.setToolbar(this, toolbar);
		
		toolbar.setNavigationOnClickListener(v -> {
			
			//- Geri dönme işi seçilen renk uygulanmadan yapılırsa olay iptal edilmiş olur
			setResult(RESULT_CANCELED);
			onBackPressed();
		});
		btnSelectColor.setOnClickListener(this::onClickSelect);
		btnApplyColor.setOnClickListener(v -> applyColor());
		
		selectedColor = primaryColor;
		changeColor();
		
	}
	
	private void onClickSelect(@NonNull View view) {
		
		view.setEnabled(false);
		
		new SelectColorDialog(this, colorNames, colors, colorIndex, this);
	}
	
	private void applyColor() {
		
		Rehber.ColorSelection.setSelected(this, selectedColor);
		
		Intent intent = new Intent();
		intent.putExtra(SELECTED_COLOR, selectedColor);
		
		//- Renk uygulanırsa işlem tamamlanmış olur
		setResult(RESULT_OK, intent);
		
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		Bungee.slideDown(this);
	}
	
	@Override
	public void onItemIndex(int position) {
		
		
		btnSelectColor.setEnabled(true);
		
		if (position != -1) {// -1 seçim yapılmadan dialog kapandı demek
			
			colorIndex        = position;
			selectedColor     = colors[position];
			selectedColorName = colorNames[position];
			lastColor         = primaryColor;
			primaryColor      = selectedColor;
			
			xlog.d("Selected Color : %d - %s", position, selectedColorName);
			
			changeColor();
		}
	}
	
	private void changeColor() {
		
		Colors.runColorAnimation(
				lastColor,
				selectedColor,
				i -> {
					root.setBackgroundColor(i);
					toolbar.setBackgroundColor(i);
				});
		
		
		Colors.setStatusBarColor(getWindow(), selectedColor);
		
		//- Bunu yaparsan görünmez
		//Colors.setIndeterminateProgressColor(progressBar, selectedColor);
		
		
	}
}