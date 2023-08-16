package com.tr.hsyn.telefonrehberi.main.dev;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.bool.Bool;
import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.main.data.ContactLog;
import com.tr.hsyn.use.Use;
import com.tr.hsyn.xbox.Blue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tr.xyz.contact.Contact;


/**
 * <h2>Over</h2>
 * <p>
 * It provides a method
 * of accessing some code that needs to be accessed from anywhere in the program.
 * The class runs entirely on the {@link Blue} class.
 * When using the {@code Blue} class, the key of the object must be known.
 * However,
 * this class ({@code Over})
 * offers more clear and understandable methods
 * instead of using the object key codes defined in the {@link Key} class.
 * <pre>{@code
 * List<Contact> contacts = Blue.getObject(Key.CONTACTS);
 * List<Contact> contacts =  Over.Contact.getContacts();
 * // Both return the same object
 * }</pre>
 *
 * @see Blue
 * @see Key
 */
public interface Over {

	/**
	 * Program home directory
	 */
	interface App {

		/**
		 * @return Application context
		 */
		static Context getContext() {return Blue.getObject(Key.CONTEXT);}


	}

	interface Content {

		/**
		 * @return {@code true} if the Contacts and calls are loaded
		 */
		static boolean loadComplete() {

			var callsLoaded    = com.tr.hsyn.telefonrehberi.main.call.data.CallLog.isLoaded();
			var contactsLoaded = ContactLog.isLoaded();

			return callsLoaded && contactsLoaded;
		}

		/**
		 * Contacts directory
		 */
		interface Contacts {

			static boolean isLoaded() {

				return Blue.getObject(Key.CONTACT_LOG) != null;
			}

			static List<Contact> getContacts() {

				return Blue.getObject(Key.CONTACTS);
			}

			/**
			 * @return the last selected contact from the contact list
			 */
			static Contact getSelectedContact() {

				return Blue.getObject(Key.SELECTED_CONTACT);
			}

			/**
			 * Sets a flag indicating that the Contacts list needs to be refreshed.
			 */
			static void refreshContacts() {

				Blue.box(Key.REFRESH_CONTACTS, true);
			}

			/**
			 * There is a list of deleted contacts at {@link Key#DELETED_CONTACTS} key.
			 * And this method adds the deleted contacts to the deleted contact list.
			 *
			 * @param contacts contacts to be added to the deleted contact list
			 */
			static void addDeleted(Contact... contacts) {

				List<Contact> contactList = getDeleted();

				if (contactList == null) {

					contactList = new ArrayList<>();
					Blue.box(Key.DELETED_CONTACTS, contactList);
				}

				contactList.addAll(Arrays.asList(contacts));
			}

			/**
			 * @return deleted contact list at {@link Key#DELETED_CONTACTS}
			 */
			static List<Contact> getDeleted() {

				return Blue.getObject(Key.DELETED_CONTACTS);
			}
		}

		/**
		 * Call log directory
		 */
		interface CallLog {

			/**
			 * Indicates whether there are call logs.
			 *
			 * @return {@code true} if call records are set, otherwise {@code false}
			 */
			static boolean isLoaded() {

				return Blue.exist(Key.CALL_LOG_CALLS);
			}

			/**
			 * Returns the call logs manager.
			 *
			 * @return the call logs manager
			 */
			static Story<Call> getCallLogManager() {

				return Blue.getObject(Key.CALL_STORY);
			}

			/**
			 * Records a flag
			 * indicating that there is a permanent change in the call logs and needs to be refreshed.
			 */
			static void refreshCallLog() {

				Blue.box(Key.REFRESH_CALL_LOG, true);
			}

			/**
			 * Arama kayıtlarında kalıcı bir değişiklik olmuş ise {@code true} döner
			 * ve verilen yeni değeri set eder.
			 * Verilen yeni değerin set edilmesi için herhangi bir koşul yoktur,
			 * dönen değer her ne olursa olsun verilen değer set edilir.
			 *
			 * @param newValue Yeni değer
			 *
			 * @return Arama kayıtlarında güncelleme olmuş ise {@code true}
			 */
			static boolean needRefreshCallLog(@Nullable Boolean newValue) {

				boolean b = needRefreshCallLog();

				Blue.box(Key.REFRESH_CALL_LOG, newValue);

				return b;
			}

			/**
			 * Ama kayıtlarında kalıcı bir değişiklik yapıldığında,
			 * bu işlemi yapan kod kayıtların değiştiğini ve yenilenmesi gerektiğini bildiren bir bilgi kaydeder.
			 * Bu metot kaydedilen bu bilgiyi kontrol eder ve yenilenme gerekliliği varsa {@code true} döndürür.
			 * Metot sadece kontrol eder, bilgiyi değiştirmez.
			 *
			 * @return Arama kayıtlarının yenilenmesi gerekiyorsa {@code true}
			 */
			static boolean needRefreshCallLog() {

				Boolean b = Blue.getObject(Key.REFRESH_CALL_LOG);

				return b != null && b;
			}

			/**
			 * Arama kayıtları listesi
			 */
			interface Calls {

				static Bool isUpdated(@NonNull Bool newValue) {

					var u = isUpdated();

					if (!u.equals(newValue))
						Blue.box(Key.CALL_LOG_UPDATED, newValue.bool());

					return u;
				}

				/**
				 * @return {@code true} if calls updated
				 */
				@NonNull
				static Bool isUpdated() {

					return Bool.of(Blue.getObject(Key.CALL_LOG_UPDATED));
				}

				/**
				 * Listenin durumunu değiştir.
				 *
				 * @param state Yeni durum
				 */
				static void setUpdated(@NonNull Bool state) {

					Blue.box(Key.CALL_LOG_UPDATED, state.getObject());
				}

				/**
				 * @return all call log calls
				 */
				@Nullable
				static List<com.tr.hsyn.calldata.Call> getCalls() {

					return Blue.getObject(Key.CALL_LOG_CALLS);
				}

				static void setCalls(List<com.tr.hsyn.calldata.Call> calls) {

					Blue.box(Key.CALL_LOG_CALLS, calls);
				}

				/**
				 * Global arama kayıtları listesi editörü.
				 * Sadece elindeki liste üzerinde değişiklik yapar.
				 * Veri tabanı yada sistem kayıtları ile hiçbir ilgisi yoktur.
				 */
				interface Editor {

					static void add(com.tr.hsyn.calldata.Call call) {

						Use.ifNotNull(getCalls(), calls -> {

							calls.add(call);
							Over.Content.CallLog.Calls.setUpdated(Bool.TRUE);
						});
					}

					static void set(int index, com.tr.hsyn.calldata.Call call) {

						Use.ifNotNull(getCalls(), calls -> calls.set(index, call));
					}

					/**
					 * Arama kayıtlarını siler.
					 *
					 * @param calls Ana listeden silinecek kayıtlar
					 *
					 * @return Silinen kayıt sayısı
					 */
					static int delete(com.tr.hsyn.calldata.Call... calls) {

						List<com.tr.hsyn.calldata.Call> callList = getCalls();
						int                             deleted  = 0;

						if (callList == null || calls == null || calls.length == 0) return deleted;

						//noinspection ForLoopReplaceableByForEach
						for (int i = 0; i < calls.length; i++) {

							int index = callList.indexOf(calls[ i ]);

							if (index != -1) {

								callList.remove(index);
								deleted++;
							}
						}

						if (deleted != 0) Over.Content.CallLog.Calls.setUpdated(Bool.TRUE);

						return deleted;
					}
				}
			}


		}
	}


}
