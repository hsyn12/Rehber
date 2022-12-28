package com.tr.hsyn.roomstorm.sd;


import com.tr.hsyn.roomstorm.FieldType;

import java.util.List;


public abstract class ListSerializer<T> extends AbsSerializer<T> {
	
	@Override
	public FieldType getSerializedFieldType() {
		
		return FieldType.LIST;
	}
	
	public abstract String serialize(List<T> list);
	
	public abstract List<T> deserialize(String value);
}
