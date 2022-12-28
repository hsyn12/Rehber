package com.tr.xyz.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Bu sınıf en temel fragment sınıfımız.
 */
public abstract class Fragment extends androidx.fragment.app.Fragment {
	
	/**
	 * Fragment için görünüm dosyasını döndür.
	 *
	 * @return Görünüm dosyası
	 */
	@LayoutRes
	protected abstract int getLayoutId();
	
	/**
	 * Bu sınıfın yaptığı en temel iş, görseli oluşturmak.
	 * Geri kalan her şey alt sınıflara ait.
	 *
	 * @param inflater           inflater
	 * @param container          container
	 * @param savedInstanceState savedInstanceState
	 * @return view
	 */
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		return inflater.inflate(getLayoutId(), container, false);
	}
}
