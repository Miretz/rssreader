package com.semerad.rss;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

public class WicketApplication extends AuthenticatedWebApplication {

	@Override
	public Class<? extends WebPage> getHomePage() {
		return DashboardPage.class;
	}

	@Override
	public void init() {
		super.init();

		// enable spring injection
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return HomePage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return BasicAuthenticationSession.class;
	}
}
