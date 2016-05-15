package com.semerad.rss;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;

import com.semerad.rss.service.AccountService;

public class HomePage extends WebPage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private AccountService accountService;

	private String loginUsername;
	private String loginPassword;

	private String signupUsername;
	private String signupPassword;

	public HomePage(final PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		// add account service to basic session via setter
		final BasicAuthenticationSession basicSession = (BasicAuthenticationSession) AuthenticatedWebSession.get();
		basicSession.setAccountService(accountService);

		add(getLoginForm());
		add(new FeedbackPanel("loginFeedback"));

		add(getSignUpForm());
		add(new FeedbackPanel("signupFeedback"));
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
	private StatelessForm getSignUpForm() {
		final StatelessForm signupForm = new StatelessForm("signupForm") {
			@Override
			protected void onSubmit() {
				if (Strings.isEmpty(signupUsername) || Strings.isEmpty(signupPassword)) {
					return;
				}

				// create a new user account
				accountService.create(signupUsername, signupPassword);

				final boolean authResult = AuthenticatedWebSession.get().signIn(signupUsername, signupPassword);
				if (authResult) {
					continueToOriginalDestination();
				} else {
					error("Error during sign up");
				}

			}

		};
		signupForm.setDefaultModel(new CompoundPropertyModel(this));
		signupForm.add(new TextField("signupUsername"));
		signupForm.add(new PasswordTextField("signupPassword"));
		return signupForm;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	private StatelessForm getLoginForm() {
		final StatelessForm loginForm = new StatelessForm("loginForm") {
			@Override
			protected void onSubmit() {
				if (Strings.isEmpty(loginUsername)) {
					return;
				}

				final boolean authResult = AuthenticatedWebSession.get().signIn(loginUsername, loginPassword);
				if (authResult) {
					continueToOriginalDestination();
				} else {
					error("Wrong username or password");
				}
			}
		};
		loginForm.setDefaultModel(new CompoundPropertyModel(this));
		loginForm.add(new TextField("loginUsername"));
		loginForm.add(new PasswordTextField("loginPassword"));
		return loginForm;
	}
}
