package com.tr.hsyn.colors;


import com.tr.hsyn.holder.Holder;

import java.lang.ref.WeakReference;


class RegistersHolder {
	
	static WeakReference<Holder<ColorRegisters>> colorRegistersHolder = new WeakReference<>(Holder.newHolder());
}
