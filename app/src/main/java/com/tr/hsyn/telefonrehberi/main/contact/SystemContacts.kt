package com.tr.hsyn.telefonrehberi.main.contact

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.tr.hsyn.contactdata.ContactData
import com.tr.hsyn.content.Contents
import com.tr.hsyn.perfectsort.PerfectSort
import com.tr.hsyn.phone_numbers.PhoneNumbers
import com.tr.hsyn.telefonrehberi.main.contact.data.bank.system.ContactColumns
import com.tr.hsyn.telefonrehberi.main.contact.data.bigPic
import com.tr.hsyn.telefonrehberi.main.contact.data.emails
import com.tr.hsyn.telefonrehberi.main.contact.data.events
import com.tr.hsyn.telefonrehberi.main.contact.data.groups
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
}