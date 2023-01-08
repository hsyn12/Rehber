package com.tr.hsyn.telefonrehberi.main.code.story.contact;


import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.perfectsort.PerfectSort;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.Contacts;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.Dates;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Account;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.telefonrehberi.main.code.database.contact.ContactDatabase;
import com.tr.hsyn.telefonrehberi.main.code.database.contact.DBContact;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.use.Bool;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.paperdb.Paper;


/**
 * Rehber Yöneticisi.<br>
 * Rehber üzerinde yapılacak tüm işlemler bu sınıf üzerinden yürütülür.
 */
@Keep
public class ContactStory implements ContactDataStory {
	
	private final Context   context;
	private final DBContact database;
	
	public ContactStory(@NotNull Context context, @NotNull DBContact database) {
		
		this.database = database;
		this.context  = context;
	}
	
	@Override
	public DBContact getDatabase() {
		
		return database;
	}
	
	@Override
	public List<Contact> load() {
		
		//- Önce veri tabanını yükle
		ContactsHolder contacts = new DatabaseContacts(database);
		
		//- Veri tabanı boşsa sistemden yükle
		if (contacts.isEmpty()) {
			
			contacts = new SystemContacts(context.getContentResolver());
			
			if (contacts.isEmpty()) xlog.d("Rehber boş");
			else firstLoad(contacts);
		}
		else regularLoad(contacts);
		
		return contacts.getContacts();
	}
	
	private void firstLoad(@NotNull ContactsHolder contacts) {
		
		xlog.w("First load");
		contacts.sort();
		
		//- Evet bu ilk yükleme
		//- Çünkü veri tabanı boş sistem dolu
		
		var time = Time.currentMillis();
		//! Bu, sistem kayıtları.
		//! Bu yüzden tarihleri ekliyoruz veri tabanına yazarken
		contacts.setDates(time);
		
		Runny.run(() -> {
			
			var count = database.add(contacts.getContacts());
			xlog.d("%d kişi kaydedildi [size=%d]", count, contacts.getContacts().size());
			
			//- Backup
			Paper.book("ContactsBackup").write(String.valueOf(Time.currentMillis()), contacts);
		}, false);
	}
	
	private void regularLoad(@NotNull ContactsHolder contacts) {
		
		contacts.sort();
		
		//! Veri tabanı boş değil
		//! Yani daha önce yükleme yapılmış
		//! Sadece bu durumda güncelleme kontrolü yapıyoruz
		Runny.run(() -> checkContacts(contacts), false);
	}
	
	/**
	 * Android kayıtları ile uygulamanın kendi kayıtları arasındaki farklar kontrol edilir.
	 */
	private void checkContacts(@NotNull ContactsHolder contacts) {
		
		//! Yükleme işleminin mantığına göre
		//! Bu metot çağrılıyorsa veri tabanı boş değil demektir
		//! Zaten veri tabanı boş olsa
		//! karşılaştırma yapacak bir kayıt yok demektir
		
		var date           = Time.currentMillis();
		var systemContacts = loadFromSystem();
		
		if (systemContacts.isEmpty()) {
			
			makeAllDeleted(contacts.getContacts(), date);
		}
		else {
			
			xlog.d("Kişiler inceleniyor");
			
			var r = context.getContentResolver();
			
			//- Veri tabanında olup da sistemde olmayanlar
			var deleted = Lister.difference(contacts.getContacts(), systemContacts);
			//- Sistemde olup da veri tabanında olmayanlar
			var newContacts = Lister.difference(systemContacts, contacts.getContacts());
			
			Bool.of(deleted.isEmpty())
					.ifTrue(() -> xlog.d("Silinen bir kişi yok"))
					.ifFalse(() -> {
						
						xlog.d("Silinen %d kişi var [%s]", deleted.size(), deleted.stream().map(Contact::getName).collect(Collectors.toList()));
						
						Lister.loop(contacts.getContacts(), c -> c.getDates().setDeletedDate(date));
						database.update(deleted);
						
						Blue.box(Key.DELETED_CONTACTS, deleted);
					});
			
			Bool.of(newContacts.isEmpty())
					.ifTrue(() -> xlog.d("Yeni bir kişi yok"))
					.ifFalse(() -> {
						
						xlog.d("%d yeni kişi kaydedilecek", newContacts.size());
						Lister.loop(newContacts, c -> c.setDates(Dates.newDates(date)));
						var count = database.add(newContacts);
						xlog.d("%d yeni kişi eklendi [size=%d]", count, newContacts.size());
						
						Blue.box(Key.NEW_CONTACTS, newContacts);
					});
			
			//- Eklenen eklendi, silinen silindi
			//- Sıra geldi değişiklikleri kontrol etmeye
			checkChanges(systemContacts, contacts.getContacts());
		}
		
		Blue.box(Key.CONTACT_LIST_UPDATED, Time.currentMillis() - date);
	}
	
	private void makeAllDeleted(@NotNull List<Contact> contacts, long date) {
		
		//- Veri tabanı boş değil ama sistem kayıtları boş
		//! Yani tüm kişiler sistemden silinmiş
		
		xlog.d("Tüm kişiler silinmiş olarak işaretleniyor");
		
		Runny.run(() -> {
			
			contacts.forEach(c -> {
				c.getDates().setDeletedDate(date);
				database.update(c, String.valueOf(c.getContactId()));
			});
			
			//! Tüm kişiler silindi
			Blue.box(Key.DELETED_CONTACTS, contacts);
		}, false);
	}
	
	private void checkChanges(@NotNull List<? extends Contact> systemContacts, @NotNull List<? extends Contact> databaseContacts) {
		
		Lister.loop(systemContacts.size(), i -> {
			
			var systemContact = systemContacts.get(i);
			
			int index = databaseContacts.indexOf(systemContact);
			
			if (index != -1) {
				
				boolean update = false;
				
				var databaseContact = databaseContacts.get(index);
				var info            = new ContactUpdateInfo();
				
				if (!Objects.equals(systemContact.getName(), databaseContact.getName())) {
					
					update = true;
					info.setName(databaseContact.getName(), systemContact.getName());
					
					xlog.d("İsim değişikliği : [%s] --> [%s]", databaseContact.getName(), systemContact.getName());
				}
				
				if (!Lister.equals(systemContact.getNumbers(), databaseContact.getNumbers())) {
					
					update = true;
					info.setNumber(Stringx.joinToString(databaseContact.getNumbers()), Stringx.joinToString(systemContact.getNumbers()));
					xlog.d("Telefon numarası değişikliği : [%s] --> [%s]", databaseContact.getNumbers(), systemContact.getNumbers());
				}
				
				if (update) {
					
					databaseContact.getDates().setUpdatedDate(info.getTime());
					systemContact.getDates().setUpdatedDate(info.getTime());
					
					updateFromDatabase(databaseContact);
					
					info.setId(systemContact.getContactId());
					
					new UpdatedContacts().add(info);
				}
			}
		});
	}
	
	@Override
	public List<Contact> loadFromDatabase() {
		
		return database.queryAll(ContactDatabase.DELETED_DATE + "=0");
	}
	
	@Override
	public List<Contact> loadFromSystem() {
		
		return Contacts.getContacts(context.getContentResolver());
	}
	
	@Override
	public boolean addIntoDatabase(@NotNull Contact item) {
		
		return database.add(item);
	}
	
	@Override
	public boolean deleteFromDatabase(@NotNull Contact item) {
		
		return database.delete(item);
	}
	
	@Override
	public int deleteFromDatabase(@NotNull List<? extends Contact> items) {
		
		return database.delete(items);
	}
	
	@Override
	public boolean deleteFromSystem(@NonNull Contact item) {
		
		return deleteContact(context.getContentResolver(), String.valueOf(item.getContactId()));
	}
	
	@Override
	public int delete(@NonNull List<? extends Contact> items) {
		
		return (int) items.stream().filter(c -> deleteFromSystem(c) && deleteFromDatabase(c)).count();
	}
	
	@Override
	public boolean delete(@NonNull Contact item) {
		
		var deleted = deleteFromSystem(item);
		
		if (deleted) {
			
			return deleteFromDatabase(item);
		}
		else {
			
			xlog.d("Kişi sistemden silinemedi");
		}
		
		return false;
	}
	
	@Override
	public boolean updateFromDatabase(@NonNull Contact item) {
		
		return database.update(item, String.valueOf(item.getContactId()));
	}
	
	@Override
	public int updateFromDatabase(@NonNull List<? extends Contact> items) {
		
		return database.update(items);
	}
	
	@Override
	public boolean updateFromSystem(@NonNull Contact item) {
		
		return false;
	}
	
	@Override
	public boolean addIntoSystem(@NonNull Contact item) {
		
		if (item.getName() == null || item.getNumbers() == null) {
			
			xlog.w("Gerekli bilgiler sağlanamadı : %s", item);
			return false;
		}
		
		var result = addContact(context.getContentResolver(), item.getName(), item.getNumbers());
		return result != null && result[0].count != 0;
	}
	
	@Override
	public int addIntoDatabase(@NotNull List<? extends Contact> items) {
		
		return database.add(items);
	}
	
	@Override
	public int deleteFromSystem(@NotNull List<? extends Contact> items) {
		
		return deleteContacts(
				context.getContentResolver(),
				items.stream()
						.map(Contact::getContactId)
						.map(String::valueOf)
						.collect(Collectors.toList()));
	}
	
	@Override
	public int addIntoSystem(@NotNull List<? extends Contact> items) {
		
		ArrayList<ContentProviderOperation> ops                   = new ArrayList<>();
		int                                 rawContactInsertIndex = 0;
		
		
		for (var contact : items) {
			
			ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
					        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
					        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
					        .build());
			
			
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
					        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
					        .build());
			
			if (contact.getNumbers() != null)
				for (var number : contact.getNumbers())
					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
							        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
							        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
							        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
							        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
							        .build());
		}
		
		
		try {
			return context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops)[0].count;
		}
		catch (RemoteException | OperationApplicationException e) {
			e.printStackTrace();
		}
		finally {
			
			ops.clear();
		}
		
		return 0;
	}
	
	/**
	 * Sistem rehberine kişi ekler.
	 *
	 * @param resolver resolver
	 * @param name     name
	 * @param numbers  numbers
	 * @return ContentProviderResult dizisi
	 */
	@Nullable
	private static ContentProviderResult[] addContact(@NonNull ContentResolver resolver, @NotNull String name, @NotNull Iterable<String> numbers) {
		
		ArrayList<ContentProviderOperation> ops                   = new ArrayList<>();
		int                                 rawContactInsertIndex = 0;
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				        .build());
		
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
				        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
				        .build());
		
		for (var number : numbers)
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
					        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
					        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
					        .build());
		
		
		try {
			return resolver.applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch (RemoteException | OperationApplicationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Nullable
	private static ContentProviderResult[] addContact(@NonNull ContentResolver resolver, Account account, @NonNull String name, @NotNull Iterable<String> numbers) {
		
		ArrayList<ContentProviderOperation> ops                   = new ArrayList<>();
		int                                 rawContactInsertIndex = 0;
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, account == null ? null : account.getType())
				        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, account == null ? null : account.getName())
				        .build());
		
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
				        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
				        .build());
		
		for (var number : numbers)
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
					        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
					        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
					        .build());
		
		
		try {
			return resolver.applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch (RemoteException | OperationApplicationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Sistem kayıtlarından kişiyi sil.
	 *
	 * @param contentResolver ContentResolver
	 * @param contactId       contact id
	 * @return Silme başarılı olursa {@code true}.
	 */
	private static boolean deleteContact(@NotNull final ContentResolver contentResolver, @NotNull final String contactId) {
		
		Uri      contactUri  = ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
		String   whereClause = ContactsContract.RawContacts.CONTACT_ID + " = ?";
		String[] args        = {contactId};
		
		return contentResolver.delete(contactUri, whereClause, args) > 0;
	}
	
	/**
	 * Kişileri sistem kayıtlarından siler.
	 *
	 * @param contentResolver contentResolver
	 * @param contactIds      Silinecek kişilere ait contact id listesi
	 * @return Silinen kişi sayısı
	 */
	private static int deleteContacts(@NotNull final ContentResolver contentResolver, @NotNull final List<String> contactIds) {
		
		Uri    contactUri  = ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
		String whereClause = Stringx.format("%s in (%s)", ContactsContract.RawContacts.CONTACT_ID, Stringx.joinToString(contactIds));
		
		return contentResolver.delete(contactUri, whereClause, null);
	}
	
	private interface ContactsHolder {
		
		List<Contact> getContacts();
		
		default boolean isEmpty() {
			
			return getContacts().isEmpty();
		}
		
		default void sort() {
			
			getContacts().sort(PerfectSort.comparator(Contact::getName));
		}
		
		default void setDates(long time) {
			
			Lister.loop(getContacts(), c -> c.setDates(Dates.newDates(time)));
		}
	}
	
	private static final class DatabaseContacts implements ContactsHolder {
		
		private final List<Contact> contacts;
		
		private DatabaseContacts(@NotNull DBContact database) {
			
			contacts = load(database);
		}
		
		@Override
		public List<Contact> getContacts() {
			
			return contacts;
		}
		
		@NotNull
		private List<Contact> load(@NotNull DBContact database) {
			
			return database.queryAll(ContactDatabase.DELETED_DATE + "=0");
		}
		
	}
	
	private static final class SystemContacts implements ContactsHolder {
		
		private final List<Contact> contacts;
		
		public SystemContacts(@NotNull ContentResolver contentResolver) {
			
			contacts = load(contentResolver);
		}
		
		@Override
		public List<Contact> getContacts() {
			
			return contacts;
		}
		
		@NotNull
		private List<Contact> load(@NotNull ContentResolver contentResolver) {
			
			return Contacts.getContacts(contentResolver);
		}
	}
	
	
}
