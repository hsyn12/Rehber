package com.tr.hsyn.telefonrehberi.main.code.comment.contact.defaults;


import android.view.View;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.counter.Counter;
import com.tr.hsyn.nextension.NameExtension;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.scaler.Scaler;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.code.mislister.MisterLister;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.Group;
import com.tr.hsyn.telefonrehberi.main.code.comment.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.code.comment.contact.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.code.comment.contact.dialog.MostCallDialog;
import com.tr.hsyn.telefonrehberi.main.code.comment.contact.dialog.MostCallItemViewData;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.telefonrehberi.main.dev.atom.Ranker;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.Unit;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import github.nisrulz.packagehunter.PackageHunter;


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
					.filter(call -> PhoneNumbers.containsNumber(ContactKey.GETTER.getNumbers(contact), call.getNumber()))
					.collect(Collectors.toList());
		else return new ArrayList<>(0);
	}
	
	@NotNull
	@Override
	public CharSequence commentate(@NotNull Contact subject) {
		
		contact      = subject;
		this.history = getContactHistory();
		
		Spanner comment = new Spanner();
		
		comment.append(commentSavedDate())
				.append(commentHistory());
		
		return comment;
	}
	
	@NotNull
	@Override
	public CharSequence commentSavedDate() {
		
		Spanner comment = new Spanner();
		
		comment.append(onInstallDate());
		
		return comment;
	}
	
	@NotNull
	@Override
	public CharSequence commentHistory() {
		
		Spanner comment = new Spanner();
		
		
		boolean noHistory = history.isEmpty();
		
		if (noHistory) {
			
			xlog.d("Kişiye ait bir arama kaydı yok");
			
			//todo birşeyler ekle
			comment.append(store.noHistory());
		}
		
		if (!noHistory) {
			//- Geçmişe bir göz atalım
			
			//- 10'a 3 ölçek
			//- orta değer (10, 10 * 3] aralığı
			var scaler = Scaler.createNewScaler(10, 3f);
			int scale  = scaler.getQuantity(history.size());
			
			if (scaler.isMin(scale)) {
				
				comment.append(store.historySizeOnly(history.size()));
			}
			else {
				
				comment.append(store.historySize(history.size()));
			}
			
			
			if (calls != null) {
				
				String               phoneNumber = PhoneNumbers.formatNumber(history.get(0).getNumber(), 10);
				List<Group<Call>>    groups      = MisterLister.group(calls, c -> PhoneNumbers.formatNumber(c.getNumber(), 10));
				Counter<Group<Call>> counter     = new Counter<>(groups);
				
				groups.sort((x, y) -> Integer.compare(y.size(), x.size()));
				
				Group<Call> winner = groups.get(0);//- en çok olan eleman
				long        count  = counter.count(winner, Group::size);
				
				if (PhoneNumbers.equals(phoneNumber, winner.getValue().getNumber())) {
					
					var                  viewData      = Lister.map(groups, e -> new MostCallItemViewData(e.getValue().getName(), e.size()));
					View.OnClickListener clickListener = v -> new MostCallDialog(store.getActivity(), viewData);
					
					if (count == 1) {
						
						comment.append(store.thisContact())
								.append(" ")
								.append(store.theMostCall(), Spans.click(clickListener))
								.append(" ")
								.append(store.contactHas())
								.append(". ");
						
						xlog.d("Bu kişi en fazla arama kaydına sahip kişi : %s", groups);
					}
					else {
						
						comment.append(store.thisContact())
								.append(" ")
								.append(store.theMostCall(), Spans.click(clickListener))
								.append(" ")
								.append(store.hasOneOfThem((int) count));
						
						xlog.d("Bu kişi en fazla arama kaydına sahip %d kişiden biri : %s", count, groups);
					}
				}
			}
		}
		
		
		if (false) {
			
			history.sort((x, y) -> Long.compare(y.getTime(), x.getTime()));
			
			//- Kişinin en eski arama kaydı
			Call first    = history.get(history.size() - 1);
			long distance = first.getTime();
			xlog.d("Kişiye ait ilk arama kaydının tarihi : %s [%d]", Time.toString(first.getTime()), first.getTime());
			
			if (distance < 0L) {
				
				//! Kişiye ait ilk arama kaydı, kayıt zamanından eski.
				//- Yani kullanıcının eski bir dostu.
				//- Elbette çok da eski olamaz çünkü arama kaydı kapasitesi buna izin vermez.
				//- Ama bizden eski olduğu için 'eski dost' kategorisine giriyor bizim için.
				
				xlog.d("Kişiye ait ilk arama kaydı, kayıt zamanından eski");
			}
			else {
				
				//! Kişiye ait ilk arama kaydı, kayıt zamanından yeni.
				
				xlog.d("Kişiye ait ilk arama kaydı, kayıt zamanından yeni");
			}
			
			xlog.d("Kişinin kayıt tarihi ile ilk arama kaydı tarihi arasında geçen zaman : %s", Time.toDuration(Math.abs(distance)));
		}
		
		return comment;
	}
	
	@NotNull
	private CharSequence onInstallDate() {
		
		final Spanner comment       = new Spanner();
		PackageHunter packageHunter = new PackageHunter(store.getActivity());
		long          installDate   = packageHunter.getFirstInstallTimeForPkg(store.getActivity().getPackageName());
		long          savedDate     = 0;
		
		comment.append(store.savedDate());
		
		if (savedDate != 0L) {
			
			comment.append(Stringx.format(" %s", Time.toString(savedDate, DEFAULT_DATE_FORMAT), Spans.bold()))
					.append(". ");
		}
		else {
			
			comment.append(" ").append(store.unknown()).append(". ");
		}
		
		
		xlog.d("Uygulamanın yüklenme zamanı : %s [%d]", Time.toString(installDate), installDate);
		xlog.d("Kişinin kayıt zamanı        : %s [%d]", Time.toString(savedDate), savedDate);
		xlog.d("Geçen süre                  : %s", Time.toDuration(savedDate - installDate));
		
		var contacts = Over.Contacts.getContacts();
		
		if (contacts == null) {
			
			xlog.d("Kişi listesine ulaşılamıyor");
			return comment;
		}
		
		
		List<Long> allDates = new ArrayList<>();
		
		//- Tüm kayıt tarihlerini aldık
		
		//- Çokluğu hesapla
		Ranker<Long>          ranker = new Ranker<>(allDates);
		Map<Long, Long>       range  = ranker.makeRank();
		Map.Entry<Long, Long> max    = Ranker.getMostRank(range);
		
		if (max.getKey() != -1L) {
			
			//- Elimizde bilgi var demek
			
			if (range.size() == 1) {
				
				//- Tüm kişiler uygulama yüklendiğinde kaydedilmiş.
				//- Açıkçası en çok beklenen durum bu.
				
				xlog.d("Tüm kişilerin kayıt tarihi aynı");
				
				//- Kişilerin kayıt zamanı aynı olduğu için daha ileri bir araştırma yapmaya gerek yok
				
			}
			else {
				
				//- Burada kişilerin yüklenme zamanlarını kontrol etmemiz gerek,
				//- çünkü farklı kayıt zamanları var.
				
				var savedDates = range.keySet();
				
				for (var d : savedDates) {
					
					//- Kimler ne zaman ve ne çoklukta kaydedilmiş
					xlog.d("%d kişi '%s' tarihinde kayıt edilmiş", range.get(d), Time.toString(d));
				}
				
				//- Aynı tarihte kaydedilen en çok kişi sayısı, tüm kişilerin yüzde kaçına denk geliyor
				long percent = (max.getValue() * 100) / contacts.size();
				
				xlog.d("Kişilerin %s'%s '%s' tarihinde kayıt edilmiş", "%" + percent, NameExtension.getHers(percent), Time.toString(max.getKey()));
				
				//- Bu yüzde değeri yüksekse, kişiler toplu şekilde uygulama yüklenirken kaydedilmiş demektir.
				//- Ki olması gereken de bu çünkü uygulama her halükarda sonradan yükleniyor.
				//- Burada amaç uygulamanın yüklenme zamanı kişinin kayıt zamanı mı oluyor onu bulmak.
				//- Eğer öyleyse,
				//- yani şuanki kişi uygulama yüklenirken kaydedilmiş ise,
				//- bu kişinin genel olarak kayıtlı olduğu süreyi bilemeyiz.
				//- Çünkü kişi biz yokken varmış.
				//- Eğer kişi sonradan kaydedilmişse,
				//- kayıtlı olduğu süreyi ve bu süre içindeki tüm iletişimi
				//- inceleyerek daha emin sonuçlar üretebiliriz.
				
				
				if (percent >= 70) {
					
					//- %70 büyük bir oran.
					//- Bu da, uygulamanın yüklenme zamanına (büyük olasılıkla) yakın bir zamandır.
					//- Kısa bir hatırlatma;
					//- olay şöyle; uygulama yüklenir, ilk açıldığında rehber izinleri sorulur,
					//- izinler onaylandığı an kişiler veri tabanına kaydedilir.
					//- Tabi uygulama yüklenir yüklenmez açılmayabilir,
					//- belki aylarca sonra açar kullanıcı.
					//- Ama bu bir sorun değil.
					//- Önemli olan uygulama ilk yüklemeyi gerçekleştirdiğinde mi bu kişi kaydedildi,
					//- yoksa sonradan mı kaydedildi bunu bilmek.
					//- Yani kişinin ömrünü tam olarak bilmek.
					//- Kişi bizden önce kaydedilmişse bunu bilemeyiz tabiki.
					
					xlog.d("Bu yüzde değeri kişilerin uygulama yüklenirken kayıt edildiğini gösteriyor");
					
					
					//- Şuanki kişi %70'lik çoğunluk içinde mi?
					if (savedDate == max.getKey()) {
						
						//! Evet, şuanki kişi uygulama yüklendiğinde kayıt edilmiş.
						//- Bakalım kullanıcı uygulamayı yükledikten ne kadar sonra kişiler kayıt edilmiş.
						//- Bir başka değişle, kullanıcı uygulamayı yükledikten ne kadar süre sonra ilk kez açmış.
						//- Elbette uygulamayı yüzlerce kez açmış ama izinleri kabul etmemiş olabilir (düşük bir olasılık) ve
						//- bu durumda mesela uygulama 1 yıl önce yüklenmiş ve
						//- izinler 6 ay önce kabul edilmiş olabilir.
						//- Bu biraz uç bir örnek ancak olasılık dahilinde ve etrafta çok manyak var.
						
						var timeInfo = Time.toDuration(max.getKey() - installDate);
						
						timeInfo.getDurations().stream()
								.filter(t -> t.getUnit().equals(Unit.YEAR)).findFirst()
								.ifPresent(year -> {
									
									//- İşte sana bir manyak
									
									xlog.w("Uygulamanın yüklenmesinin üzerinden %d yıl sonra kişiler kaydedilmiş", year.getValue());
									
									//- Elbette buraya düşmemiz çok çok düşük bir olasılık
								});
						
						
						//- Eğer bu süre, (yani uygulamanın yüklenip izinlerin verilmesi arasında geçen süre)
						//- uzun denebilecek bir süre ise kullanıcının çok meşkul yada
						//- unutkan biri olduğunu söyleyebiliriz.
						//- Ama bu kesin bir bilgi olmayabilir çünkü çoğumuz böyle bir durum yaşamışızdır.
						//- Şahsen meşkuliyetten değildi benimki.
						//- Ama bir parça unutkan olduğumu itiraf edebilirim.
						//- Ve önceliklerin ihtiyaçlar doğrultusunda olduğunu da hatırlamalıyız.
						
						//- Artık kişinin ilk yükleme zamanında kaydildiğini biliyoruz.
						
						xlog.d("Kişi uygulama yüklenirken kaydedilmiş");
					}
					else {
						
						//! Hayır, şuanki kişi sonradan kaydedilmiş
						//- Bizi en çok ilgilendiren yer işte burası.
						
						xlog.d("Kişi sonradan kaydedilmiş");
					}
				}
			}
		}
		
		
		return comment;
	}
	
	
}
