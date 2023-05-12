package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.defaults;


import android.view.View;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.counter.Counter;
import com.tr.hsyn.daytimes.DayTime;
import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.nextension.NumberExtension;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.code.android.Res;
import com.tr.hsyn.telefonrehberi.code.android.dialog.ShowCall;
import com.tr.hsyn.telefonrehberi.code.call.CallOver;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.dialog.MostCallDialog;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment.dialog.MostCallItemViewData;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data.History;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.Group;
import com.tr.hsyn.telefonrehberi.main.code.comment.dialog.ShowCallsDialog;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.text.Span;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.text.Spans;
import com.tr.hsyn.time.Duration;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.Unit;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * This class implements the ContactCommentator interface and provides default behavior for commenting on a contact.
 * It retrieves the call history of the contact and generates comments based on that history.
 */
public class DefaultContactCommentator implements ContactCommentator {

    /**
     * The comment object.
     * All generated comments by the commentator will be appended into this object
     */
    protected final Spanner comment = new Spanner();
    /**
     * The list of all call log calls
     */
    protected final List<Call> calls;
    /**
     * Call history of the contact
     */
    protected History history;
    /**
     * The current contact
     */
    protected Contact contact;
    /**
     * The comment store
     */
    protected ContactCommentStore commentStore;
    //protected final String DEFAULT_DATE_FORMAT = "d.M.yyyy";

    /**
     * Constructs a new DefaultContactCommentator object with the given comment store.
     *
     * @param commentStore the comment store to be used by this commentator
     */
    public DefaultContactCommentator(ContactCommentStore commentStore, List<Call> calls) {

        this.commentStore = commentStore;
        this.calls = calls;
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
        this.history = contact.getData(ContactKey.HISTORY);

        //if history null or empty, no need to go any further
        if (history!=null)
            if (history.isEmpty()) comment.append(commentStore.noHistory());
            else commentOnContact();
        else comment.append(commentStore.historyNotFound());

        return comment;
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
     * Generates comments based on the call history of the current contact.
     * Invokes private methods to generate individual comments.
     * This is the first method called by the commentator in {@link #commentOn(Contact)} method.
     */
    private void commentOnContact() {

        // Here we start to generate the comment.

        // Call history must not be 'null' and empty at this point
        if (history==null || history.isEmpty())
            throw new IllegalArgumentException("Call history must not be null or empty");


        if (history.size()==1) {
            commentOnSingleCall();
            return;
        }


        xlog.d("Accessed the call history [contact='%s', size=%d]", contact.getName(), history.size());

        Runny.run(() -> {

            //noinspection Convert2MethodRef
            historyQuantityComment();

            //mostCallComments();
        });

    }

    private void commentOnSingleCall() {

        comment.append(commentStore.singleCall()).append(". ");
        commentOnTheSingleCall(history.get(0));
    }

    private void commentOnTheSingleCall(@NotNull Call call) {

        int clickColor = getClickColor();
        Duration timeBefore = Time.howLongBefore(call.getTime());
        String callType = Res.getCallType(commentStore.getActivity(), call.getType());
        ShowCall showCall = new ShowCall(commentStore.getActivity(), call);
        View.OnClickListener listener1 = View -> showCall.show();

        //bu arama 3 gün önce gerçekleşen bir arama
        comment.append("Bu arama", Spans.click(listener1, clickColor))
                .append(Stringx.format(" %d %s önce gerçekleşmiş bir ", timeBefore.getValue(), timeBefore.getUnit()))
                .append(Stringx.format("%s", callType.toLowerCase()), Spans.bold())
                .append(". ");
    }

    /**
     * Generates a comment about the quantity of call history for the contact.
     * Appends the generated comment to the {@link #comment} object managed by this commentator.
     */
    private void historyQuantityComment() {

        // Now, we are sure that the call history size has been more than one.
        // The call history of the current contact has two calls at least.

        int clickColor = getClickColor();

        // We want to inform the user about the quantity of call history.
        // For example, 'The contact has 2 calls'

        String _name = contact.getName()!=null && !PhoneNumbers.isPhoneNumber(contact.getName()) ? contact.getName():Stringx.toTitle(getString(R.string.word_contact));
        ShowCallsDialog showCallsDialog = new ShowCallsDialog(commentStore.getActivity(), history.getHistory());
        View.OnClickListener listener = View -> showCallsDialog.show();
        comment.append(_name, Spans.bold(), Spans.foreground(getTextColor()));

        if (commentStore.isTurkishLanguage()) {


        } else {

            comment.append(" ")
                    .append(getString(R.string.word_has))
                    .append(Stringx.format("%s", getString(R.string.word_calls_with_size, history.size())), Spans.click(listener, clickColor), Spans.underline());
        }


        this.comment.append(commentOnTheLastCall());
        this.comment.append(aboutLastCallType());
    }

    @Override
    public @NotNull CharSequence commentOnTheLastCall() {

        Spanner comment = new Spanner();
        int clickColor = getColor(com.tr.hsyn.rescolors.R.color.orange_500);

        Call lastCall = history.getLastCall();
        Call firstCall = history.getFirstCall();
        String callType = Res.getCallType(commentStore.getActivity(), lastCall.getType());
        Duration timeBefore = Time.howLongBefore(lastCall.getTime());
        ShowCall showCall = new ShowCall(commentStore.getActivity(), lastCall);

        View.OnClickListener listener1 = View -> showCall.show();

        comment.append("Son arama", Spans.click(listener1, clickColor))
                .append(" kaydı ")
                .append(Stringx.format(" %d %s önce gerçekleşmiş bir ", timeBefore.getValue(), timeBefore.getUnit()))
                .append(Stringx.format("%s", callType.toLowerCase()), Spans.bold())
                .append(". ");

        return comment;
    }

    /**
     * @return comment about last call type
     */
    private @NotNull CharSequence aboutLastCallType() {

        Spanner comment = new Spanner();
        Call lastCall = history.getLastCall();
        List<Integer> types = new ArrayList<>();
        int type = lastCall.getType();

        types.add(type);

        switch (type) {//@off
            case CallType.INCOMING:
                types.add(CallType.INCOMING_WIFI);
                break;
            case CallType.OUTGOING:
                types.add(CallType.OUTGOING_WIFI);
                break;
            case CallType.INCOMING_WIFI:
                types.add(CallType.INCOMING);
                break;
            case CallType.OUTGOING_WIFI:
                types.add(CallType.OUTGOING);
                break;
        }//@on

        var callTypes = Lister.toIntArray(types);
        var typedCalls = history.getCalls(callTypes);
        var typeStr = Res.getCallType(commentStore.getActivity(), type);

        if (typedCalls.size()==1) {

            comment.append("Ve bu ")
                    .append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
                    .append(" kişiye ait tek ")
                    .append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
                    .append(". ");
        } else {

            var calls = history.getCalls(callTypes);
            ShowCallsDialog showCallsDialog = new ShowCallsDialog(commentStore.getActivity(), calls, history.getContact().getName(), Stringx.format("%d %s", calls.size(), typeStr));

            View.OnClickListener listener = View -> showCallsDialog.show();

            comment.append("Ve bu ")
                    .append(Stringx.format("%s", typeStr.toLowerCase()), Spans.bold())
                    .append(" kişiye ait ")
                    .append(Stringx.format("%d %s", typedCalls.size(), typeStr.toLowerCase()), Spans.click(listener, getClickColor()), Spans.underline())
                    .append("dan biri. ");
        }

        return comment;
    }

    /**
     * Generates a comment when there is no call history for the contact.
     * Appends the generated comment to the {@link #comment} object managed by this commentator.
     */
    private void noHistoryComment() {

        comment.append("Bu kişi ile hiçbir iletişimin yok. ");
    }

    /**
     * Generates a comment about the contact's most frequent contacts based on their call history.
     * Appends the generated comment to the Comment object managed by this commentator.
     */
    private void mostCallComments() {

        if (calls!=null) {

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
                comment.append(Stringx.format("Bu kayıtlar, tüm arama kayıtlarının yüzde %d'%s oluyor. ", percent, NumberExtension.getNumberExt(percent, NumberExtension.TYPE_DAY)));

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

                    if (name==null) name = e.getValue().getNumber();

                    return new MostCallItemViewData(name, e.size());
                });
                View.OnClickListener clickListener = v -> new MostCallDialog(commentStore.getActivity(), viewData);

                int color = getColor(com.tr.hsyn.rescolors.R.color.orange_500);

                if (count==1) {

                    comment.append(commentStore.thisContact())
                            .append(" ")
                            .append(commentStore.theMostCallLog(), Spans.click(clickListener, color))
                            .append(" ")
                            .append(commentStore.contactHas())
                            .append(". ");

                    xlog.d("Bu kişi en fazla arama kaydına sahip kişi : %s", groups);
                } else {

                    comment.append(commentStore.thisContact())
                            .append(" ")
                            .append(commentStore.theMostCallLog(), Spans.click(clickListener, color))
                            .append(" ")
                            .append(commentStore.hasOneOfThem((int) count))
                            .append(". ");

                    xlog.d("Bu kişi en fazla arama kaydına sahip %d kişiden biri : %s", count, groups);
                }
            }
        } else {

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

            //- Kişinin en eski arama kaydı
            Call firstCall = history.getFirstCall();
            Call lastCall = history.getLastCall();
            var duration = Time.toDuration(lastCall.getTime() - firstCall.getTime());
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


}
