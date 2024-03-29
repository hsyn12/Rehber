package com.tr.hsyn.telefonrehberi.main.contact.comment.topics;

import android.app.Activity;
import android.view.View;

import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.call.data.Key;
import com.tr.hsyn.telefonrehberi.main.call.data.RankMap;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostDurationData;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.MostDurationDialog;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.comment.ContactComment;
import com.tr.hsyn.telefonrehberi.main.data.ContactLog;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.Unit;
import com.tr.hsyn.time.duration.DurationGroup;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import tr.xyz.contact.Contact;

public class CallDurationComment implements ContactComment {
	
	private final CallLog                  callLog = getCallLogs();
	private final Spanner                  comment = new Spanner("\n");
	private       Activity                 activity;
	private       Consumer<ContactComment> callback;
	private       Contact                  contact;
	private       boolean                  isTurkish;
	
	@Override
	public boolean isTurkish() {
		
		return isTurkish;
	}
	
	@Override
	public Activity getActivity() {
		
		return activity;
	}
	
	@Override
	public @NotNull CharSequence getComment() {
		
		return comment;
	}
	
	@Override
	public Topic getTopic() {
		
		return Topic.CALL_DURATION;
	}
	
	@Override
	public void createComment(@NotNull Contact contact, @NotNull Activity activity, @NotNull Consumer<ContactComment> callback, boolean isTurkish) {
		
		// region Initialization
		this.contact   = contact;
		this.callback  = callback;
		this.activity  = activity;
		this.isTurkish = isTurkish;
		
		if (callLog == null) {
			
			xlog.d("callCollection is null");
			returnComment();
			return;
		}
		// endregion
		RankMap                rankMap      = callLog.rankByDuration();
		int                    rank         = rankMap.getRank(contact);
		CallRank               thisRank     = rankMap.getCallRank(rank, contact);
		List<MostDurationData> durationList = createDurationList(rankMap, ContactLog.getLogOrEmpty().getWithNumber());
		String                 title        = getString(R.string.title_speaking_durations);
		String                 subtitle     = getString(R.string.size_contacts, durationList.size());
		View.OnClickListener   listener     = view -> new MostDurationDialog(getActivity(), durationList, title, subtitle).show();
		
		if (thisRank != null) {
			
			long totalDuration = thisRank.getTotalDuration() * 1000L;
			
			//+ if exist any speaking
			if (totalDuration > 0L) {
				
				DurationGroup duration = Time.toDuration(totalDuration);
				//+ The object that to convert the duration to string.
				DurationGroup.Stringer stringer = DurationGroup.Stringer.builder()
					                                  .formatter(duration1 -> fmt("%d %s", duration1.getValue(), makePlural(duration1.getUnit().toString(), duration1.getValue())))//_ the formatted string to be used.
					                                  .units(Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.HOUR, Unit.MINUTE)//_ the units should be used.
					                                  .zeros(false);//_ the zero durations should not be used.
				
				
				if (isTurkish) {
					
					comment.append("Bu arama geçmişi süresi boyunca bu kişi ile aranızda toplam ")
						.append(fmt("%s", stringer.durations(duration.getDurations()).toString()), getTextStyle())
						.append(" konuşma gerçekleşti. Ve bu süre ile ")
						.append("en çok konuştuğun", getClickSpans(listener))
						.append(" kişiler listesinde ")
						.append(fmt("%s", rank), getTextStyle())
						.append(". sırada. ");
				}
				else {
					
					comment.append("During this call history duration, there was a total of ")
						.append(fmt("%s", stringer.durations(duration.getDurations()).toString()), getTextStyle())
						.append(" of conversation between you and this contact. And this duration is in the ")
						.append(fmt("%s", rank), getTextStyle())
						.append(". order in the list of ")
						.append("the people you talk to the most", getClickSpans(listener))
						.append(" . ");
				}
			}
			else {//+ no speaking
				
				if (isTurkish) comment.append("Bu arama geçmişi süresi boyunca bu kişi ile aranızda hiç konuşma olmamış. ");
				else comment.append("During this call history duration, there was no conversation between you and this contact. ");
			}
		}
		
		returnComment();
	}
	
	@Override
	public @NotNull Consumer<ContactComment> getCallback() {
		
		return callback;
	}
	
	/**
	 * Creates a list of most duration items.
	 *
	 * @param rankMap the map of rank
	 *
	 * @return the list of most duration items
	 */
	@NotNull
	private List<MostDurationData> createDurationList(@NotNull RankMap rankMap, List<Contact> contacts) {
		
		List<MostDurationData> list = new ArrayList<>();
		
		int rank = 1;
		while (true) {
			
			List<CallRank> rankList = rankMap.getRank(rank);
			
			if (rankList.isEmpty()) break;
			
			for (CallRank callRank : rankList) {
				
				if (callRank.getTotalDuration() == 0) continue;
				
				String name = callRank.getName();
				
				if (name == null || name.trim().isEmpty())
					name = getContactName(callRank.getCalls().get(0).getNumber(), contacts);
				
				long contactId = contact.getId();
				
				MostDurationData data = new MostDurationData(name, Time.formatSeconds((int) callRank.getTotalDuration()), rank);
				
				if (contactId == Key.getContactId(callRank.getCalls().get(0)))
					data.setSelected(true);
				
				list.add(data);
			}
			
			rank++;
		}
		
		return list;
	}
	
	/**
	 * Returns the name of the contact.
	 *
	 * @param number the phone number
	 *
	 * @return the name or the number
	 */
	@NotNull
	private String getContactName(@NotNull String number, List<Contact> contacts) {
		
		if (contacts == null || contacts.isEmpty())
			return number;
		
		for (Contact contact : contacts) {
			
			List<String> numbers = com.tr.hsyn.telefonrehberi.main.contact.data.ContactKeyKt.getNumbers(contact);
			
			if (numbers == null || numbers.isEmpty()) continue;
			
			
			if (PhoneNumbers.existsNumber(numbers, number)) {
				
				String name = contact.getName();
				return name != null && !name.trim().isEmpty() ? name : number;
			}
		}
		
		return number;
	}
}
