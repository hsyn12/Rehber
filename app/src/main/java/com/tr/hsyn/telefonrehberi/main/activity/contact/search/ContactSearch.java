package com.tr.hsyn.telefonrehberi.main.activity.contact.search;


import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tr.hsyn.activity.ActivityView;
import com.tr.hsyn.bungee.Bungee;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.DigiGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.page.SwipeListener;
import com.tr.hsyn.searchview.MaterialSearchView;
import com.tr.hsyn.searchview.OnSearchViewListener;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.code.Phone;
import com.tr.hsyn.telefonrehberi.code.ResourceUtil;
import com.tr.hsyn.telefonrehberi.code.android.ui.swipe.ContactSwipeCallBack;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.ContactDetails;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * <h1>ContactSearch</h1>
 * <p>
 * Kişilerde arama yapmayı sağlar.
 */
public class ContactSearch extends ActivityView implements OnSearchViewListener, SwipeListener {
	
	private final Gate                 gateSelection = AutoGate.newGate(1000L);
	private final DigiGate             gateFilter    = (DigiGate) DigiGate.newGate(1500L).loop();
	protected     List<Contact>        contacts      = new ArrayList<>(Objects.requireNonNull(Blue.getObject(Key.CONTACTS)));
	protected     RecyclerView         list;
	protected     ProgressBar          progressBar;
	protected     View                 emptyView;
	protected     MaterialSearchView   searchView;
	protected     ContactSearchAdapter adapter;
	protected     String               searchText;
	
	@Override
	protected int getLayoutId() {
		
		return R.layout.activity_contact_search;
	}
	
	@Override
	protected void onCreate() {
		
		list        = findView(R.id.list_search_contacts);
		progressBar = findView(R.id.progress_search_contacts);
		emptyView   = findView(R.id.empty);
		searchView  = findView(R.id.search_contacts);
		
		searchView.setOnSearchViewListener(this);
		
		updateBinaryNumbers();
		
		list.setAdapter(adapter = new ContactSearchAdapter(contacts, this::onItemSelected));
		
		updateEmpty();
		
		adapter.setFilterStartListener(() -> progressBar.setVisibility(View.VISIBLE));
		adapter.setFilterCompleteListener(() -> {
			
			updateEmpty();
			progressBar.setVisibility(View.GONE);
		});
		
		Colors.changeStatusBarColor(this);
		Colors.setProgressColor(progressBar);
		
		setupList();
		
		searchView.showSearch();
	}
	
	protected void onItemSelected(int index) {
		
		if (gateSelection.enter()) {
			
			var selected = adapter.getFilteredContacts().get(index);
			
			xlog.d("Selected : %s", selected.getName());
			
			Blue.box(Key.SELECTED_CONTACT, selected);
			openContactDetails();
		}
	}
	
	protected void openContactDetails() {
		
		startActivity(new Intent(this, ContactDetails.class));
		Bungee.slideRight(this);
	}
	
	protected void updateEmpty() {
		
		emptyView.setVisibility(getAdapterCalls().isEmpty() ? View.VISIBLE : View.GONE);
	}
	
	protected List<Contact> getAdapterCalls() {
		
		return adapter.getFilteredContacts();
	}
	
	private void setupList() {
		
		ContactSwipeCallBack swipeCallBack = new ContactSwipeCallBack(ItemTouchHelper.RIGHT, this, ResourceUtil.getBitmap(this, R.drawable.telephone_call));
		swipeCallBack.setBgColor(Colors.getPrimaryColor());
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallBack);
		itemTouchHelper.attachToRecyclerView(list);
	}
	
	/**
	 * Listeyi birden fazla numarası olanlar için güncelle.
	 * Tüm liste tek numaralı olacak, fazla numarası olanlar her numara için eklenecek.
	 */
	private void updateBinaryNumbers() {
		
		for (int i = 0; i < contacts.size(); ) {
			
			List<String> numbers = contacts.get(i).getData(ContactKey.NUMBERS);
			
			//- Biden fazla numarası olanlar
			if (numbers != null && numbers.size() > 1) {
				
				//- Adamımız
				var contact = contacts.remove(i);
				
				for (var number : numbers) {
					
					var _contact = new Contact(contact);
					
					ContactKey.setNumbers(_contact, Lister.listOf(number));
					contacts.add(i++, _contact);
				}
			}
			else i++;
		}

		/*var any = contacts.stream().anyMatch(c -> c.getNumbers() != null && c.getNumbers().size() > 1);

		if (any) {

			xlog.d("Hala birden fazla numarası olanlar var");
		}*/
		
	}
	
	@Override
	public void onSearchViewShown() {
		
	}
	
	@Override
	public void onSearchViewClosed() {
		
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		
		Bungee.zoomFast(this);
	}
	
	@Override
	public boolean onQueryTextSubmit(String query) {
		
		return true;
	}
	
	@Override
	public void onQueryTextChange(String newText) {
		
		if (newText == null) return;
		
		searchText = newText;
		
		gateFilter.enter(this::filter);
	}
	
	private void filter() {
		
		adapter.onTextChanged(searchText);
	}
	
	@Override
	public void onSwipe(int index) {
		
		adapter.notifyItemChanged(index);
		
		var contact = getAdapterCalls().get(index);
		
		xlog.d("Swiped : %s", contact.getName());
		
		List<String> numbers = contact.getData(ContactKey.NUMBERS);
		
		if (numbers != null && !numbers.isEmpty()) {
			
			Phone.makeCall(this, numbers.get(0));
		}
		else {
			
			Show.tost(this, "Numara yok");
		}
	}
	
	
}
