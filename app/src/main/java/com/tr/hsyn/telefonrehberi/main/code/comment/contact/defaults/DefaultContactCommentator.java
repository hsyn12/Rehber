package com.tr.hsyn.telefonrehberi.main.code.comment.contact.defaults;


import android.view.View;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.counter.Counter;
import com.tr.hsyn.daytimes.DayTime;
import com.tr.hsyn.nextension.Extension;
import com.tr.hsyn.nextension.NumberExtention;
import com.tr.hsyn.nextension.WordExtension;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.scaler.Scaler;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.code.mislister.MisterLister;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.Group;
import com.tr.hsyn.telefonrehberi.main.code.comment.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.code.comment.contact.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.code.comment.contact.dialog.MostCallDialog;
import com.tr.hsyn.telefonrehberi.main.code.comment.contact.dialog.MostCallItemViewData;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.ShowCallsDialog;
import com.tr.hsyn.text.Span;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.Unit;
import com.tr.hsyn.xlog.xlog;

import java.util.List;


/**
 * Varsayılan kişi yorumcusu<br>
 * Kişi ile ilgili en genel konuları yorumlar.
 */
public class DefaultContactCommentator extends ContactCommentator {
	
	//protected final String  DEFAULT_DATE_FORMAT = "d.M.yyyy";
	
	public DefaultContactCommentator(ContactCommentStore commentStore) {
		
		super(commentStore);
	}
	
	private void historyQuantityComment() {
		
		//- 10'a 3 ölçek
		//- orta değer (10, 10 * 3] aralığı
		Scaler  scaler = Scaler.createNewScaler(10, 3f);
		int     scale  = scaler.getQuantity(history.size());
		Spanner name   = new Spanner();
		
		if (contact.getName() != null && !PhoneNumbers.isPhoneNumber(contact.getName(), true))
			name.append(contact.getName(), Spans.bold())
					.append(Stringx.format("'%s ait ", WordExtension.getWordExt(contact.getName(), Extension.TYPE_TO)));
		else name.append("Kişiye ait ");
		
		ShowCallsDialog      showCallsDialog = new ShowCallsDialog(store.getActivity(), history);
		View.OnClickListener listener        = View -> showCallsDialog.show();
		
		if (scaler.isMin(scale))
			comment.append(name).append("sadece ");
		
		comment.append(Stringx.format("%s", store.sizeCall(history.size())), Spans.click(listener, getColor(com.tr.hsyn.rescolors.R.color.orange_500)), Spans.underline())
				.append(" kaydı var. ");
	}
	
	private void mostCallComments() {
		
		if (calls != null) {
			
			//- Telefon numarası bir anahtar gibi kullanılacak
			String phoneNumber = PhoneNumbers.formatNumber(history.get(0).getNumber(), 10);
			
			//- Telefon numarasına karşı, numaraya ait arama kayıtlarından oluşan bir liste
			List<Group<Call>> groups = MisterLister.group(calls, c -> PhoneNumbers.formatNumber(c.getNumber(), 10));
			
			int differentPerson = groups.size();
			
			//- Bu kişinin arama kayıtları, tüm arama kayıtlarının yüzde kaçı oluyor?
			int percent = (history.size() * 100) / calls.size();
			
			if (percent > 0)
				comment.append(Stringx.format("Bu kayıtlar, tüm arama kayıtlarının yüzde %d'%s oluyor. ", percent, NumberExtention.getNumberExt(percent, NumberExtention.TYPE_DAY)));
			
			comment.append(Stringx.format("Tüm arama kayıtları %d farklı kişiden oluşuyor ", differentPerson));
			
			
			//- Bay Sayman. Listedeki elemanları sayacak
			Counter<Group<Call>> counter = new Counter<>(groups);
			
			//- Listeyi, kayıt sayısına göre çoktan aza doğru sırala
			groups.sort((x, y) -> Integer.compare(y.size(), x.size()));
			
			
			//- en çok kaydı olan eleman
			Group<Call> winner = groups.get(0);
			
			//- Aynı arama kaydı sayısına sahip kişi sayısı
			//- Yani en çok arama kaydına sahip kaç kişi olduğunu buluyoruz
			long count = counter.count(winner, Group::size);
			
			//- Bu kişi en çok arama kaydına sahip kişi mi?
			if (PhoneNumbers.equals(phoneNumber, winner.getValue().getNumber())) {
				
				var viewData = Lister.map(groups, e -> {
					
					var name = e.getValue().getName();
					
					if (name == null) name = e.getValue().getNumber();
					
					return new MostCallItemViewData(name, e.size());
				});
				View.OnClickListener clickListener = v -> new MostCallDialog(store.getActivity(), viewData);
				
				if (count == 1) {
					
					comment.append(store.thisContact())
							.append(" ")
							.append(store.theMostCallLog(), Spans.click(clickListener))
							.append(" ")
							.append(store.contactHas())
							.append(". ");
					
					xlog.d("Bu kişi en fazla arama kaydına sahip kişi : %s", groups);
				}
				else {
					
					comment.append(store.thisContact())
							.append(" ")
							.append(store.theMostCallLog(), Spans.click(clickListener))
							.append(" ")
							.append(store.hasOneOfThem((int) count))
							.append(". ");
					
					xlog.d("Bu kişi en fazla arama kaydına sahip %d kişiden biri : %s", count, groups);
				}
			}
		}
		else {
			
			comment.append("Arama kayıtları alınamadı");
		}
	}
	
	private void addFirstLastCallComment() {
		
		if (history.size() > 1) {
			
			history.sort((x, y) -> Long.compare(y.getTime(), x.getTime()));
			
			//- Kişinin en eski arama kaydı
			Call   firstCall     = history.get(history.size() - 1);
			Call   lastCall      = history.get(0);
			var    duration      = Time.toDuration(lastCall.getTime() - firstCall.getTime());
			Unit[] durationUnits = {Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.HOUR, Unit.MINUTE};
			
			Span[] textSpans = {
					Spans.bold(),
					Spans.foreground(Colors.getPrimaryColor())
			};
			
			comment.append("İlk arama kaydının tarihi ")
					.append(Stringx.format("%s. ", Time.toString(firstCall.getTime())), textSpans)
					.append("Son arama kaydı ise ")
					.append(Stringx.format("%s. ", Time.toString(lastCall.getTime())), textSpans);
			
			comment.append("Bu iki tarih arasında geçen zaman tam olarak ")
					.append(Stringx.format("%s. ", DayTime.toString(store.getActivity(), duration, durationUnits)), textSpans);
			
			xlog.d("Kişiye ait ilk arama kaydının tarihi : %s [%d]", Time.toString(firstCall.getTime()), firstCall.getTime());
			
			
		}
	}
	
	@Override
	public void commentContact() {
		
		boolean noHistory = history.isEmpty();
		
		if (noHistory) {
			
			xlog.d("Kişiye ait bir arama kaydı yok");
			
			noHistoryComment();
		}
		
		if (!noHistory) {
			
			historyQuantityComment();
			mostCallComments();
		}
	}
	
	private void noHistoryComment() {
		
		if (contactName.isContact()) {
			
			
		}
		
	}
	
	
}
