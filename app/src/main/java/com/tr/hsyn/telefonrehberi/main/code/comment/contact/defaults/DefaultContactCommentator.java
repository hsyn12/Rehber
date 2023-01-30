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
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.text.Span;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.Unit;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Varsayılan kişi yorumcusu<br>
 * Kişi ile ilgili en genel konuları yorumlar.
 */
public class DefaultContactCommentator implements ContactCommentator {
	
	protected final String              DEFAULT_DATE_FORMAT = "d.M.yyyy";
	protected final ContactCommentStore store;
	protected final List<Call>          calls;
	protected       List<Call>          history;
	protected       Contact             contact;
	private final   Spanner             comment             = new Spanner();
	
	public DefaultContactCommentator(ContactCommentStore commentStore) {
		
		this.store = commentStore;
		calls      = Over.CallLog.Calls.getCalls();
	}
	
	@NotNull
	private List<Call> getContactHistory() {
		
		var calls = Over.CallLog.Calls.getCalls();
		
		if (calls != null)
			return calls
					.stream()
					.filter(call -> PhoneNumbers.containsNumber(contact.getData(ContactKey.NUMBERS), call.getNumber()))
					.collect(Collectors.toList());
		else return new ArrayList<>(0);
	}
	
	@NotNull
	@Override
	public CharSequence commentate(@NotNull Contact subject) {
		
		contact      = subject;
		this.history = getContactHistory();
		
		Spanner comment = new Spanner();
		
		comment.append(commentHistory());
		
		return comment;
	}
	
	protected int getColor(int id) {
		
		return store.getActivity().getColor(id);
	}
	
	private void historyQuantityComment() {
		
		//- 10'a 3 ölçek
		//- orta değer (10, 10 * 3] aralığı
		var scaler = Scaler.createNewScaler(10, 3f);
		int scale  = scaler.getQuantity(history.size());
		
		if (scaler.isMin(scale)) {
			
			comment.append(store.historySizeOnly(history.size()));
		}
		else {
			
			ShowCallsDialog      showCallsDialog = new ShowCallsDialog(store.getActivity(), history, null, null);
			View.OnClickListener listener        = View -> showCallsDialog.show();
			
			var name = contact.getName();
			
			if (name != null && !PhoneNumbers.isPhoneNumber(name, true)) {
				
				name = Stringx.toTitle(name);
				comment.append(Stringx.format("%s", name), Spans.bold())
						.append(Stringx.format("'%s ait ", WordExtension.getWordExt(name, Extension.TYPE_TO)));
				
				
			}
			else {
				
				comment.append("Kişiye ait ");
				
			}
			
			
			comment.append(Stringx.format("%s", store.sizeCall(history.size())), Spans.click(listener, getColor(com.tr.hsyn.rescolors.R.color.orange_500)), Spans.underline())
					.append(" kaydı var");
		}
		
		comment.append(". ");
	}
	
	private void noHistoryComment() {
		
		comment.append(store.noHistory()).append(". ");
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
			
			comment.append(Stringx.format("Bu kayıtlar, tüm arama kayıtlarının yüzde %d'%s oluyor. ", percent, NumberExtention.getNumberExt(percent, Extension.TYPE_IN_TO)));
			
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
	
	@NotNull
	@Override
	public CharSequence commentHistory() {
		
		boolean noHistory = history.isEmpty();
		
		if (noHistory) {
			
			xlog.d("Kişiye ait bir arama kaydı yok");
			
			noHistoryComment();
		}
		
		if (!noHistory) {
			
			historyQuantityComment();
			mostCallComments();
			//addFirstLastCallComment();
		}
		
		return comment;
	}
	
	
}
