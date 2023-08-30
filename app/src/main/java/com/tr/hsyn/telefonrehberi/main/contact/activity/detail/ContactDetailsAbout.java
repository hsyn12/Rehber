package com.tr.hsyn.telefonrehberi.main.contact.activity.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.UiThread;

import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.execution.Work;
import com.tr.hsyn.gate.AutoGate;
import com.tr.hsyn.gate.Gate;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.comment.Moody;
import com.tr.hsyn.telefonrehberi.main.contact.comment.commentator.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.contact.comment.commentator.ContactCommentator;
import com.tr.hsyn.telefonrehberi.main.contact.comment.defaults.DefaultContactCommentator;
import com.tr.hsyn.vanimator.ViewAnimator;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

/**
 * This class interested contact information based on the call history.
 */
public class ContactDetailsAbout extends ContactDetailsMenu {
	
	/**
	 * Gate used to block input while showing about
	 */
	private final Gate      gateAbout = AutoGate.newGate(2000L);
	/**
	 * Indicates whether the 'about view' is opened
	 */
	private       boolean   isOpenAboutView;
	/**
	 * The view for the 'about view'
	 */
	private       ViewGroup view_about_content;
	/**
	 * The text view for the 'about view'
	 */
	private       TextView  text_about;
	/**
	 * Indicates whether the comment needs to be updated
	 */
	private       boolean   reComment = true;
	
	/**
	 * Creates a new instance of a {@link ContactCommentator}
	 * based on the current mood of the app.
	 *
	 * @param activity the activity object
	 *
	 * @return a new instance of a {@link ContactCommentator}
	 */
	private static @NotNull ContactCommentator createCommentator(@NotNull Activity activity) {
		
		Moody                        moody = Moody.getMood();
		@NotNull ContactCommentStore store = ContactCommentStore.createCommentStore(activity, moody);
		
		switch (moody) {
			
			case DEFAULT:
				
				DefaultContactCommentator commentator = new DefaultContactCommentator(store);
				xlog.d("Default Commentator");
				return commentator;
			case HAPPY:
				xlog.d("Not yet happy");
		}
		
		xlog.d("Wrong moody : %d", moody.ordinal());
		return new DefaultContactCommentator(store);
	}
	
	/**
	 * Activity codes must be start from here after <code>super</code> call.<br>
	 * Because the call history of the contact must be updated before any actions are performed.<br>
	 * İf no call history means no comment is generated.<br>
	 * İf no any phone number belongs to the contact means no comment is generated
	 * and no 'about' view is displayed.
	 */
	@Override
	protected void onHistoryLoad() {
		// This must be the first call in the onHistoryLoad method
		// because the call history must be updated before all.
		super.onHistoryLoad();
		
		// The contact must have one call at least
		if (history != null && history.getSize() > 0) {
			
			reComment = true;
			setupCommentViews();
		}
	}
	
	/**
	 * Sets up the comment views.
	 */
	@SuppressLint("InflateParams")
	private void setupCommentViews() {
		
		view_about_content =
			(ViewGroup) getLayoutInflater()
				            .inflate(
					            R.layout.contact_about_content,
					            mainContainer,
					            false);
		text_about         = findView(view_about_content, R.id.text_about);
		text_about.setMovementMethod(new LinkMovementMethod());
		
		int       ripple            = Colors.getRipple();
		View      view_about_header = view_about_content.findViewById(R.id.about_header);
		ImageView image_about_icon  = findView(view_about_content, R.id.about_icon);
		
		Colors.setTintDrawable(
			image_about_icon.getDrawable(),
			Colors.lighter(Colors.getPrimaryColor(), 0.2f));
		view_about_header.setBackgroundResource(ripple);
		
		addToDetailView(view_about_content);
		view_about_header.setOnClickListener(this::onClickHeader);
	}
	
	/**
	 * Called when the 'about view' is clicked.
	 *
	 * @param view the view
	 */
	private void onClickHeader(View view) {
		
		if (history == null) {
			
			xlog.i("No call history yet");
			return;
		}
		
		if (gateAbout.enter()) {
			comment();
		}
	}
	
	/**
	 * Called when the 'about view' is each time clicked.
	 * If the call history is updated, the comment is updated.
	 * If the call history is not updated, only calls {@link #animateAboutView()}.
	 */
	private void comment() {
		
		if (reComment) {
			
			reComment = false;
			
			Work.on(this::getCommentator)
				.onSuccess(this::onCommentatorReady)
				.execute();
		}
		else {
			
			animateAboutView();
		}
	}
	
	/**
	 * Creates a new instance of a {@link ContactCommentator}.
	 *
	 * @return the {@link ContactCommentator}
	 */
	@NotNull
	private ContactCommentator getCommentator() {
		
		return createCommentator(this);
	}
	
	/**
	 * Called when the {@link ContactCommentator} is ready.
	 *
	 * @param commentator the {@link ContactCommentator}
	 */
	@UiThread
	private void onCommentatorReady(@NotNull ContactCommentator commentator) {
		
		commentator.commentOn(contact, comment -> {
			text_about.setText(comment);
			animateAboutView();
		});
	}
	
	/**
	 * Animate the 'about view'.
	 */
	@UiThread
	private void animateAboutView() {
		
		if (isOpenAboutView) {
			
			isOpenAboutView = false;
			
			ViewAnimator.on(text_about).alpha(1, 0).pivotY(0).scaleY(1, 0).translationY(0, -150).duration(200).onStop(() -> text_about.setVisibility(View.GONE)).start();
			
			ViewAnimator.on(findView(view_about_content, R.id.expand_indicator)).rotation(180, 0).duration(500).start();
			
		}
		else {
			
			isOpenAboutView = true;
			text_about.setVisibility(View.VISIBLE);
			
			ViewAnimator.on(text_about).alpha(0, 1).pivotY(0).scaleY(0, 1).translationY(-150, 0).duration(200).start();
			
			ViewAnimator.on(findView(view_about_content, R.id.expand_indicator)).rotation(0, 180).duration(500).start();
		}
	}
	
}
