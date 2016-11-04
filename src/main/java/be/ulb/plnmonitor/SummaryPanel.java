package be.ulb.plnmonitor;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import be.ulb.plnmonitor.datacontroller.PLNDataController;
import be.ulb.plnmonitor.object.Person;

class SummaryPanel extends Panel {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	public SummaryPanel(String id, final PLNDataController plnDataController) {
		super(id);

		long totalSize = 0;
		int nbAUs = 0;
		int nbBoxes = 0;
		long lastPoll = 0;
		long minAvailableCapacity = 0;
		long lastUpdate = 0;
		
		long curTime = new Date().getTime();
		try {
			totalSize = plnDataController.getPlnDAO().getTotalSize(0);
			nbAUs =  plnDataController.getPlnDAO().getNbOfAUsInPLN(0);
			nbBoxes = plnDataController.getPlnDAO().getNbBoxes();
			lastPoll = TimeUnit.MILLISECONDS.toHours(curTime - plnDataController.getPlnDAO().getLastCompletedPoll());
			minAvailableCapacity = plnDataController.getPlnDAO().getminAvailableCapacity();
			lastUpdate = TimeUnit.MILLISECONDS.toHours(curTime - plnDataController.getPlnDAO().getLastUpdate());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		add(new Label("totSize", FileUtils.byteCountToDisplaySize(totalSize)).setEscapeModelStrings(false));
		add(new Label("nbAUs", nbAUs));
		add(new Label("nbBoxes",nbBoxes));
		add(new Label("lastPoll",lastPoll + "h"));
		add(new Label("minAvailableCapacity",FileUtils.byteCountToDisplaySize(minAvailableCapacity)).setEscapeModelStrings(false));
		add(new Label("lastUpdate",lastUpdate + "h"));
		
	}
}
