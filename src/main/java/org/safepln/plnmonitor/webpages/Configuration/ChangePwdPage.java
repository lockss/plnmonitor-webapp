package org.safepln.plnmonitor.webpages.Configuration;

import java.sql.SQLException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.safepln.plnmonitor.datacontroller.PLNDataController;
import org.safepln.plnmonitor.webpages.BasePage;

public class ChangePwdPage extends BasePage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3435752520758334049L;

	public ChangePwdPage(PageParameters parameters, PLNDataController plnDataController) {
		super(parameters);
		
		String userName = new String("Unknown");
		if (this.getMySession() != null) {
			if (this.getMySession().getUser() != null) {
				userName = this.getMySession().getUser().getUsername();
			}
		}
		add(new Label("userName", userName));
		
	}

}
