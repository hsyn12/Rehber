package com.tr.hsyn.telefonrehberi.main.contact

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.tr.hsyn.collection.Lister
import com.tr.hsyn.contactdata.ContactData
import com.tr.hsyn.content.Contents
import com.tr.hsyn.perfectsort.PerfectSort
import com.tr.hsyn.phone_numbers.PhoneNumbers
import com.tr.hsyn.telefonrehberi.main.contact.data.bank.system.ContactColumns
import com.tr.hsyn.telefonrehberi.main.contact.data.bigPic
import com.tr.hsyn.telefonrehberi.main.contact.data.emails
import com.tr.hsyn.telefonrehberi.main.contact.data.events
import com.tr.hsyn.telefonrehberi.main.contact.data.groups
import com.tr.hsyn.telefonrehberi.main.contact.data.handler.MimeTypeHandler
import com.tr.hsyn.telefonrehberi.main.contact.data.note
import com.tr.hsyn.telefonrehberi.main.contact.data.numbers
import com.tr.hsyn.telefonrehberi.main.contact.data.pic
import tr.xyz.contact.Contact
import tr.xyz.contact.ContactId

object SystemContacts {

	val ContactId.entityUri: Uri get() = Contents.getContactEntityUri(this.id)
	fun Uri.contactCursor(resolver: ContentResolver): Cursor? = resolver.query(this,
		ContactColumns.PROJECTION,
		null,
		null,
		null)

	fun Uri.entityCursor(resolver: ContentResolver): Cursor? = resolver.query(this,
		null,
		null,
		null,
		null)
	/**
	 * Gets the contacts from the given content resolver.
	 *
	 * @param resolver The content resolver
	 * @return A list of contacts
	 */
	fun getContacts(resolver: ContentResolver): List<Contact> {

		val cursor =
			ContactsContract.Contacts.CONTENT_URI.contactCursor(resolver) ?: return ArrayList(0)

		// region Setup indexes of contact column
		val contactIdCol = cursor.getColumnIndex(ContactColumns.PROJECTION[0])
		val nameCol = cursor.getColumnIndex(ContactColumns.PROJECTION[1])
		val picCol = cursor.getColumnIndex(ContactColumns.PROJECTION[2])
		val bigPicCol = cursor.getColumnIndex(ContactColumns.PROJECTION[3])
		// endregion

		val contacts: MutableList<Contact> = ArrayList(cursor.count)

		cursor.use {
			while (cursor.moveToNext()) {
				val contact = Contact(cursor.getLong(contactIdCol), cursor.getString(nameCol))
				contact.pic = cursor.getString(picCol)
				contact.bigPic = cursor.getString(bigPicCol)
				setContact(resolver, contact)
				contacts.add(contact)
			}
		}

		// xlog.d("Found %d contacts", contacts.size());
		contacts.sortWith(PerfectSort.stringComparator(Contact::name)) // Sorting by names
		return contacts
	}

	private fun setContact(contentResolver: ContentResolver, contact: Contact) {
		val cursor = contact.contactId.entityUri.entityCursor(contentResolver) ?: return
		cursor.use {_setContactDetails(it, contact)}
	}

	/**
	 * Sets the contact information for the given contact.
	 *
	 * @param cursor The cursor to read the contact information from.
	 * @param contact The contact to set the information for.
	 */
	@SuppressLint("Range")
	private fun _setContactDetails(cursor: Cursor, contact: Contact) {
		val data1Col = cursor.getColumnIndex(ContactColumns.DATA_COLUMNS[0])
		val mimeTypeCol = cursor.getColumnIndex(ContactColumns.DATA_COLUMNS[1])
		val emails: MutableList<String> = java.util.ArrayList(2)
		val numbers: MutableList<String> = java.util.ArrayList(2)
		val groups: MutableList<String> = java.util.ArrayList(2)
		val events: MutableList<ContactData> = java.util.ArrayList(2)
		do {
			val data1 = cursor.getString(data1Col) ?: continue

			when (cursor.getString(mimeTypeCol)) {
				ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE           -> emails.add(data1)
				ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE           -> addNumbers(cursor,
					data1Col,
					numbers)

				ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE -> groups.add(data1)
				ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE           -> addEvents(cursor,
					data1,
					events)

				ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE            -> contact.note =
					data1
			}
		}
		while (cursor.moveToNext())
		if (numbers.isNotEmpty()) contact.numbers = numbers
		if (emails.isNotEmpty()) contact.emails = emails
		if (groups.isNotEmpty()) contact.groups = groups
		if (events.isNotEmpty()) contact.events = events
	}

	fun addNumbers(cursor: Cursor, data1Column: Int, numbers: MutableList<String>) {
		val rowNumber = cursor.getString(data1Column)
		val number = PhoneNumbers.formatNumber(rowNumber, PhoneNumbers.MINIMUM_NUMBER_LENGTH)
		val notExist = numbers.stream()
			.noneMatch {num: String -> PhoneNumbers.equals(number, num)}
		if (notExist) numbers.add(rowNumber)
	}

	@SuppressLint("Range") fun addEvents(cursor: Cursor,
	                                     data1: String?,
	                                     events: MutableList<in ContactData>) {
		val type = cursor.getInt(cursor.getColumnIndex(ContactColumns.DATA_COLUMNS[2]))
		events.add(ContactData.newData(data1, type))
	}



	/**
	 * Creates a new contact intent.
	 *
	 * @return the new contact intent
	 */
	fun createNewContactIntent(): Intent {
		val intent = Intent(ContactsContract.Intents.Insert.ACTION)
		intent.type = ContactsContract.RawContacts.CONTENT_TYPE
		intent.putExtra("finishActivityOnSaveCompleted", true)
		return intent
	}

	/**
	 * Creates an Intent to add a new contact to the system contact book.
	 *
	 * @param name The name of the new contact
	 * @param number The phone number of the new contact.
	 * @return An Intent to add a new contact to the system contact book.
	 */
	fun createNewContactIntent(name: String, number: String): Intent {
		val intent = createNewContactIntent()
		intent.putExtra(ContactsContract.Intents.Insert.NAME, name)
		intent.putExtra(ContactsContract.Intents.Insert.PHONE, number)
		return intent
	}

	private fun setContactDetails(contentResolver: ContentResolver,
	                              contact: Contact,
	                              handler: MimeTypeHandler) {
		val uri = Contents.getContactEntityUri(contact.id)
		val cursor = contentResolver.query(uri, null, null, null, null) ?: return
		if (!cursor.moveToFirst()) {
			cursor.close()
			return
		}
		val data1Col = cursor.getColumnIndex(ContactColumns.DATA_COLUMNS[0])
		val mimeTypeCol = cursor.getColumnIndex(ContactColumns.DATA_COLUMNS[1])
		val data2Col = cursor.getColumnIndex(ContactColumns.DATA_COLUMNS[2])
		do {
			val data1 = cursor.getString(data1Col) ?: continue
			val mimeType = cursor.getString(mimeTypeCol)
			val data2 = cursor.getString(data2Col)
			handler.handleMimeType(mimeType, data1, data2)

			/* switch (mimeType) {

				case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
					emails.add(data1);
					break;
				case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
					addNumbers(cursor, data1Col, numbers);
					break;
				case ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE:
					groups.add(data1);
					break;
				case ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE:
					addEvents(cursor, data1, events);
					break;
				case ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE:
					contact.setData(ContactKey.NOTE, data1);
					break;
			} */
		}
		while (cursor.moveToNext())
		cursor.close()
		handler.applyResult(contact)
	}

	/**
	 * Verilen contact id değerine ait kişi için belirli bir mimetype ile
	 * kaydedilmiş bilgileri toplar.<br></br> Bununla bir kişinin telefon
	 * numaraları veya email adresleri alınabilir.<br></br>
	 *
	 * ```
	 * `List<String> getMailAddresses(@NotNull ContentResolver contentResolver, long contactId) {
	 * return getByMimeType(contentResolver, contactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
	 * }
	 * `
	 * ```
	 * *
	 *
	 * @param contentResolver contentResolver
	 * @param contactId contactId
	 * @param mimeType mimeType
	 * @return list of data
	 */
	fun getByMimeType(contentResolver: ContentResolver,
	                  contactId: Long,
	                  mimeType: String): List<String> {
		val uri = Contents.getContactEntityUri(contactId)
		val cursor =
			contentResolver.query(uri,
				ContactColumns.DATA_COLUMNS,
				ContactColumns.DATA_COLUMNS[1] + "=?",
				arrayOf(mimeType),
				null)
		if (cursor != null) {
			val data1Col = cursor.getColumnIndex(ContactColumns.DATA_COLUMNS[0])
			val mimeTypeCol = cursor.getColumnIndex(ContactColumns.DATA_COLUMNS[1])
			val data: MutableList<String> = java.util.ArrayList(cursor.count)
			while (cursor.moveToNext()) {
				val _mimeType = cursor.getString(mimeTypeCol)
				if (mimeType == _mimeType) {
					val data1 = cursor.getString(data1Col)
					if (data1 != null) data.add(data1)
				}
			}
			cursor.close()
			return data
		}
		return java.util.ArrayList(0)
	}

	fun getContactId(context: Context, phoneNumber: String): Long {
		if (phoneNumber.trim {it <= ' '}.isEmpty()) return 0
		val resolver = context.contentResolver
		val cursor = resolver.query(
			ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			Lister.arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID),
			null,
			null,
			null,
			null
		) ?: return 0
		if (cursor.count == 0) {
			cursor.close()
			return 0
		}
		var id: Long = 0
		val idCol = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
		val numberCol = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
		while (cursor.moveToNext()) {
			val number = cursor.getString(numberCol)
			if (PhoneNumbers.equals(phoneNumber, number)) {
				id = cursor.getLong(idCol)
				break
			}
		}
		cursor.close()
		return id
	}
}