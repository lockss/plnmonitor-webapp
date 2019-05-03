package org.lockss.plnmonitor.webpages;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.authroles.authentication.panel.SignInPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.lockss.plnmonitor.datacontroller.PLNDataController;
import org.lockss.plnmonitor.object.AU;

public final class SignInPage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1368912539112543508L;

	public SignInPage(final PageParameters parameters) {
		//super(parameters);
		this.setStatelessHint(false);
		//add(new SignInPanel("mySignInPanel"));
		MySignInPanel mySignInPanel=new MySignInPanel("mySignInPanel");
		mySignInPanel.setParameters(parameters);

		add (mySignInPanel);
   		add(new Link("infoPage"){
			private static final long serialVersionUID = 2759176826285891095L;

					public void onClick() {
						setResponsePage(new InfoPage(parameters));
		            }
		});
	}
	
}