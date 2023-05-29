package com.tr.hsyn.telefonrehberi.main.call.data;


public interface DBCalls {
	
	//- Arama kayıtlarının temel kolonları
	String NAME             = "name";
	String NUMBER           = "number";
	String DATE             = "date";
	String TYPE             = "type";
	String DURATION         = "duration";
	//==============================================================
	//==============================================================
	//- Ekstra kolonlar
	String CONTACT_ID       = "contact_id";
	String DELETED_DATE     = "deleted_date";
	String RINGING_DURATION = "ringing_duration";
	String NOTE             = "note";
	String EXTRA            = "extra";
	String LABELS           = "mabels";
	//==============================================================
	//==============================================================
	
}
