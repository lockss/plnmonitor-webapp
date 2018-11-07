package org.safepln.plnmonitor.webpages.Configuration;


import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.safepln.plnmonitor.webpages.BasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

public class ConfigurationPage extends BasePage {
	private static final long serialVersionUID = 1L;

	public ConfigurationPage(final PageParameters parameters) {
		super(parameters);

		//add(new Label("version", getApplication().getFrameworkSettings().getVersion()));

		// TODO Add your page's components here

    }
}
