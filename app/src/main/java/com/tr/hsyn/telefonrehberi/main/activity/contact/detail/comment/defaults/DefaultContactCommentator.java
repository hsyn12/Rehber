package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.defaults;


import android.view.View;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.counter.Counter;
import com.tr.hsyn.daytimes.DayTime;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.nextension.Extension;
import com.tr.hsyn.nextension.NumberExtention;
import com.tr.hsyn.nextension.WordExtension;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.scaler.Scaler;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.code.android.Res;
import com.tr.hsyn.telefonrehberi.code.android.dialog.ShowCall;
import com.tr.hsyn.telefonrehberi.code.call.CallOver;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.dialog.MostCallDialog;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.dialog.MostCallItemViewData;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.Group;
import com.tr.hsyn.telefonrehberi.main.code.comment.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.ShowCallsDialog;
import com.tr.hsyn.text.Span;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.time.Duration;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.Unit;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * This class implements the ContactCommentator interface and provides default behavior for commenting on a contact.
 * It retrieves the call history of the contact and generates comments based on that history.
 */
public class DefaultContactCommentator implements ContactCommentator {
	
	protected final Spanner             comment = new Spanner();
	protected       ContactCommentStore commentStore;
	protected       List<Call>          history;
	protected       List<Call>          calls;
	protected       Contact             contact;
	//protected final String  DEFAULT_DATE_FORMAT = "d.M.yyyy";
	
	/**
	 * Constructs a new DefaultContactCommentator object with the given comment store.
	 *
	 * @param commentStore the comment store to be used by this commentator
	 */
	public DefaultContactCommentator(ContactCommentStore commentStore) {
		
		this.commentStore = commentStore;
		history           = getHistory();
		calls             = getCalls();
	}
	
	/**
	 * Returns the current contact being commented on.
	 *
	 * @return the current contact
	 */
	@Override
	public Contact getContact() {
		
		return contact;
	}
	
	/**
	 * Returns the comment store being used by this commentator.
	 *
	 * @return the comment store
	 */
	@Override
	public ContactCommentStore getCommentStore() {
		
		return commentStore;
	}
	
	/**
	 * Generates a comment on the specified contact.
	 *
	 * @param contact the contact to comment on
	 * @return the generated comment as a CharSequence
	 */
	@Override
	public @NotNull CharSequence commentOn(@NotNull Contact contact) {
		
		this.contact = contact;
		commentOnContact();
		
		return comment;
	}
	
	/**
	 * Generates comments based on the call history of the current contact.
	 * Invokes private methods to generate individual comments.
	 * This is the first method called by the commentator in {@link #commentOn(Contact)} method.
	 */
	private void commentOnContact() {
		
		xlog.d("History : %d", history.size());
		
		boolean noHistory = history.isEmpty();
		
		if (noHistory) {
			
			xlog.d("Kişiye ait bir arama kaydı yok");
			
			noHistoryComment();
		}
		
		if (!noHistory) {
			
			xlog.d("There are some history : %d", history.size());
			
			Runny.run(() -> {
				
				historyQuantityComment();
				
				//mostCallComments();
			});
			
		}
		
	}
	
	/**
	 * Generates a comment about the quantity of call history for the contact.
	 * Appends the generated comment to the Comment object managed by this commentator.
	 */
	private void historyQuantityComment() {
		//- 10'a 3 ölçek
		//- orta değer (10, 10 * 3] aralığı
		Scaler  scaler     = Scaler.createNewScaler(10, 3f);
		int     scale      = scaler.getQuantity(history.size());
		Spanner name       = new Spanner();
		int     clickColor = getColor(com.tr.hsyn.rescolors.R.color.orange_500);
		
		if (contact.getName() != null && !PhoneNumbers.isPhoneNumber(contact.getName()))
			name.append(contact.getName(), Spans.bold(), Spans.foreground(getColor(R.color.purple_500)))
					.append(Stringx.format("'%s ait ", WordExtension.getWordExt(contact.getName(), Extension.TYPE_TO)));
		else name.append("Kişiye ait ");
		
		comment.append(name);
		
		ShowCallsDialog showCallsDialog = new ShowCallsDialog(commentStore.getActivity(), history);
		
		View.OnClickListener listener = View -> showCallsDialog.show();
		
		if (scaler.isMin(scale))
			comment.append("sadece ");
		
		comment.append(Stringx.format("%s", commentStore.sizeCall(history.size())), Spans.click(listener, clickColor), Spans.underline())
				.append(" kaydı var. ");
		
		
		if (history.size() == 1) {
			
			Call                 call       = history.get(0);
			String               callType   = commentStore.getActivity().getString(R.string.incomming_call);
			Duration             timeBefore = Time.howLongBefore(call.getTime());
			ShowCall             showCall   = new ShowCall(commentStore.getActivity(), call);
			View.OnClickListener listener1  = View -> showCall.show();
			
			comment.append("Bu arama", Spans.click(listener1, clickColor))
					.append(Stringx.format(" %d %s önce gerçekleşmiş bir %s.", timeBefore.getValue(), timeBefore.getUnit(), callType.toLowerCase()));
			
		}
		else {
			
			Call     lastCall   = history.get(0);
			Call     firstCall  = history.get(history.size() - 1);
			String   callType   = Res.getCallType(commentStore.getActivity(), lastCall.getType());
			Duration timeBefore = Time.howLongBefore(lastCall.getTime());
			ShowCall showCall   = new ShowCall(commentStore.getActivity(), lastCall);
			
			View.OnClickListener listener1 = View -> showCall.show();
			
			comment.append("Son arama", Spans.click(listener1, clickColor))
					.append(Stringx.format(" %d %s önce gerçekleşmiş bir %s.", timeBefore.getValue(), timeBefore.getUnit(), callType.toLowerCase()));
		}
		
		
	}
	
	/**
	 * Generates a comment about the contact's most frequent contacts based on their call history.
	 * Appends the generated comment to the Comment object managed by this commentator.
	 */
	private void mostCallComments() {
		
		if (calls != null) {
			
			//- Telefon numarası bir anahtar gibi kullanılacak
			String phoneNumber = PhoneNumbers.formatNumber(history.get(0).getNumber(), 10);
			
			//- Telefon numarasına karşı, numaraya ait arama kayıtlarından oluşan bir liste
			List<Group<Call>> groups = CallOver.groupByNumber((x, y) -> Integer.compare(y.size(), x.size()));
			
			//- Arama kayıtlarındaki kişi sayısı
			int differentPerson = groups.size();
			
			//todo Arama kayıtlarını oluşturan kişi sayısı çok düşükse
			// yapılacak inceleme pek kayda değer olmayabilir.
			
			//- Bu kişinin arama kayıtları, tüm arama kayıtlarının yüzde kaçı oluyor?
			int percent = (history.size() * 100) / calls.size();
			
			if (percent > 0)
				comment.append(Stringx.format("Bu kayıtlar, tüm arama kayıtlarının yüzde %d'%s oluyor. ", percent, NumberExtention.getNumberExt(percent, NumberExtention.TYPE_DAY)));
			
			//todo yüzde değeri 1'e ulaşmıyorsa ne olacak
			
			//comment.append(Stringx.format("Tüm arama kayıtları %d farklı kişiden oluşmakta. ", differentPerson));
			
			//- Bay Sayman. Listedeki elemanları sayacak
			Counter<Group<Call>> counter = new Counter<>(groups);
			
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
				View.OnClickListener clickListener = v -> new MostCallDialog(commentStore.getActivity(), viewData);
				
				int color = getColor(com.tr.hsyn.rescolors.R.color.orange_500);
				
				if (count == 1) {
					
					comment.append(commentStore.thisContact())
							.append(" ")
							.append(commentStore.theMostCallLog(), Spans.click(clickListener, color))
							.append(" ")
							.append(commentStore.contactHas())
							.append(". ");
					
					xlog.d("Bu kişi en fazla arama kaydına sahip kişi : %s", groups);
				}
				else {
					
					comment.append(commentStore.thisContact())
							.append(" ")
							.append(commentStore.theMostCallLog(), Spans.click(clickListener, color))
							.append(" ")
							.append(commentStore.hasOneOfThem((int) count))
							.append(". ");
					
					xlog.d("Bu kişi en fazla arama kaydına sahip %d kişiden biri : %s", count, groups);
				}
			}
		}
		else {
			
			comment.append("Arama kayıtları alınamadı");
		}
		
		
		xlog.i("Most comments completed");
	}
	
	/**
	 * Generates a comment about the first and last call made to the contact.
	 * Appends the generated comment to the Comment object managed by this commentator.
	 */
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
					.append(Stringx.format("%s. ", DayTime.toString(commentStore.getActivity(), duration, durationUnits)), textSpans);
			
			xlog.d("Kişiye ait ilk arama kaydının tarihi : %s [%d]", Time.toString(firstCall.getTime()), firstCall.getTime());
			
			
		}
	}
	
	
	/**
	 * Generates a comment when there is no call history for the contact.
	 * Appends the generated comment to the Comment object managed by this commentator.
	 */
	private void noHistoryComment() {
		
		comment.append("Bu kişi ile hiçbir iletişimin yok. ");
	}
	
	
}
