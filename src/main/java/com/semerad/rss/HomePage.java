package com.semerad.rss;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.semerad.rss.service.AccountService;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class HomePage extends WebPage {

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

	private StatelessForm getSignUpForm() {
		final StatelessForm signupForm = new StatelessForm("signupForm") {
			@Override
			protected void onSubmit() {
				onSignupSubmit();
			}
		};
		signupForm.setDefaultModel(new CompoundPropertyModel(this));
		signupForm.add(new TextField("signupUsername"));
		signupForm.add(new PasswordTextField("signupPassword"));
		return signupForm;
	}

	private StatelessForm getLoginForm() {
		final StatelessForm loginForm = new StatelessForm("loginForm") {
			@Override
			protected void onSubmit() {
				onLoginSubmit();
			}

		};
		loginForm.setDefaultModel(new CompoundPropertyModel(this));
		loginForm.add(new TextField("loginUsername"));
		loginForm.add(new PasswordTextField("loginPassword"));
		return loginForm;
	}

	protected void onSignupSubmit() {
		if (StringUtils.isEmpty(signupUsername) || StringUtils.isEmpty(signupPassword)) {
			return;
		}
		if (accountService.create(signupUsername, signupPassword)) {
			signInAndContinue(signupUsername, signupPassword);
		} else {
			error("User already exists");
		}

	}

	protected void onLoginSubmit() {
		if (StringUtils.isEmpty(loginUsername) || StringUtils.isEmpty(loginPassword)) {
			return;
		}
		signInAndContinue(loginUsername, loginPassword);
	}

	protected void signInAndContinue(final String user, final String pass) {
		final boolean authResult = AuthenticatedWebSession.get().signIn(user, pass);
		if (authResult) {
			continueToOriginalDestination();
		} else {
			error("Wrong username or password");
		}
	}

}
