package com.tr.hsyn.xbox;


import com.tr.hsyn.key.Key;
import com.tr.hsyn.xbox.definition.Hotel;
import com.tr.hsyn.xbox.definition.Reception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;


/**
 * <h2>Otel Rosa</h2>
 * <p>
 * Rosa 35 yaşında, esmer, 1.65 boyunda ve daha önce hiç evlenmemiş işkolik bir kadın.
 * Ancak evlenmemesinin sebebi işkolik olması değil, ilişkilerinde
 * aradığı samimiyeti ve güveni bulamaması. Önceki yıllarda yaşadığı psikolojik
 * sorunlardan dolayı defalarca evini tekrdip otellerde ve daha önce hiç
 * bilmediği tanımadığı yerlerde kalmış ve
 * bu ona özgürlük denen şeyin, istediğin zaman istediğin yere gidebilmek
 * olduğunu düşündürmüş. Kendisi gibi özgürlük heveslilerine yol göstermesi ve
 * yardımcı olması için bu oteli kurmuş.<br><br>
 * <p>
 * Otelde yer bulamama diye bir sorun kesinlikle yok.
 * Ancak anahtar numaranı iyi seçmelisin çünkü bir başkasını odasından edebilirsin.
 * Bu yüzden bilenen anahtarlar için {@link com.tr.hsyn.key.Key} sınıfına gözatmanı önemle rica ederim.<br><br>
 * <p>
 * Otelin misafirlerinden biriyle buluşmak için {@link Lobby}'ye girebilirsin.
 * {@code Lobby} iki şekilde kullanılabilir,
 *
 * <ul>
 *    <li>Bakıp çıkmak ({@link #peek(com.tr.hsyn.key.Key, Object)})</li>
 *    <li>Beklemek ({@link #meet(com.tr.hsyn.key.Key, Consumer)})</li>
 * </ul>
 * <p>
 * Bir misafire bakıp çıkmak demek, eğer misafir oradaysa görüşmek, değilse çıkıp gitmek demektir.<br>
 * Beklemek ise, misafir orada değilse gelene kadar beklemek demektir. Ancak istenildiği zaman {@code Lobby}'den
 * çıkıp gidilebilir yinede ({@link #cancelMeeting(com.tr.hsyn.key.Key)}).<br><br>
 * <p>
 * Misafirler otelden istedikleri zaman ayrılabilirler ({@link #exitFromHotel(com.tr.hsyn.key.Key)}).<br>
 * Otelden ayrılmak, çıkılan odanın tamamen temizleneceği anlamına gelir ve
 * aynı odaya yeni bir misafir gelene kadar oda boş kalır ({@code null}).
 *
 * @author hsyn 2 Ocak 2023 Pazartesi 13:25
 */
public class Rosa extends Hotel {
	
	private final Lobby lobby = new Lobby();
	
	public Rosa(@NotNull Reception reception) {
		
		super(reception);
	}
	
	@Nullable
	@Override
	public <T> T room(com.tr.hsyn.key.@NotNull Key key) {
		
		return reception.place(key);
	}
	
	@Nullable
	@Override
	public <T> T room(com.tr.hsyn.key.@NotNull Key key, @NotNull T object) {
		
		return reception.place(key, object);
	}
	
	@Override
	public boolean exist(@NotNull Key key) {
		
		return reception.exist(key);
	}
	
	@Override
	public <T> boolean peek(com.tr.hsyn.key.@NotNull Key key, @NotNull T object) {
		
		return lobby.lookFor(key, object);
	}
	
	@Override
	public <T> void meet(com.tr.hsyn.key.@NotNull Key key, @NotNull Consumer<T> consumer) {
		
		lobby.waitFor(key, consumer);
	}
	
	@Override
	public <T> T exitFromHotel(com.tr.hsyn.key.@NotNull Key key) {
		
		return reception.exit(key);
	}
	
	@Override
	public <T> T cancelMeeting(com.tr.hsyn.key.@NotNull Key key) {
		
		return lobby.exit(key);
	}
	
	@Override
	public void closeTheHotel() {
		
		cancelAllMeetings();
		reception.close();
	}
	
	@Override
	public void cancelAllMeetings() {
		
		lobby.close();
	}
	
}
