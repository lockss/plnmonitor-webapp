package org.safepln.plnmonitor.webpages.AUstatus;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.safepln.plnmonitor.datacontroller.PLNDataController;
import org.safepln.plnmonitor.object.AU;
import org.safepln.plnmonitor.object.LOCKSSBox;
import org.safepln.plnmonitor.object.LOCKSSBoxData;
import org.safepln.plnmonitor.webpages.BasePage;
import org.apache.commons.io.FileUtils;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.AbstractItem;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.RepeatingView;

/////////////////////////////////////////////////////////////////////
// Webpage displaying AU info for one specific AU in one specific box
/////////////////////////////////////////////////////////////////////

public class AUAcrossBoxesDisplayPage extends BasePage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5392655035733062380L;
	// private static final long serialVersionUID = 1L;

	public AUAcrossBoxesDisplayPage(final PageParameters parameters) {
		super(parameters);
	}

	public AUAcrossBoxesDisplayPage(final PageParameters parameters, PLNDataController plnDataController,
			String auName)  {
		super(parameters);

		List<AU> auInBoxes = null;
		// TODO: adapt PLN identifier in query

		try {
			auInBoxes = plnDataController.getPlnDAO().getAUsAcrossBoxes(0, auName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		add(new Label("auName", auName));

		// display summary panel
		
		RepeatingView repeatingAgreement = new RepeatingView("repeatingAgreement");
		add(repeatingAgreement);
		
		RepeatingView repeatingAUSizes = new RepeatingView("repeatingAUSizes");
		add(repeatingAUSizes);
		
		for (AU au : auInBoxes) {

			
			AbstractItem agreement_item = new AbstractItem(repeatingAgreement.newChildId());
			AbstractItem size_item = new AbstractItem(repeatingAUSizes.newChildId());

			String boxName = "";
			//get corresponding box Name
			try {
				boxName = plnDataController.getPlnDAO().getBoxName(au.getLockssBoxId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			agreement_item.add(new Label("Agreementbox", boxName));
			
			WebMarkupContainer template = new WebMarkupContainer("repeatingAgreementWidth");
			agreement_item.add(template);

			String barcolor = "bg-green";
			
			if (au.getRecentPollAgreement() == 0) {
	        	barcolor = "bg-gray";
	        }
	        else if (au.getRecentPollAgreement() < 0.7) {
	        	barcolor = "bg-red"; 	
	        }
	        else if  (au.getRecentPollAgreement() < 0.95) {
	        	barcolor = "bg-orange"; 	
	        }
	        else if (au.getRecentPollAgreement() <= 1.0){
	        	barcolor = "bg-green"; 	
	        }
	        
			template.add(new AttributeAppender("style", "width:" + String.format("%d", Math.round(100*au.getRecentPollAgreement())) + "%;"));
			template.add(new AttributeAppender("class", " " + barcolor));

			agreement_item.add(new Label("repeatingAgreementValue", String.format("%.1f", 100*au.getRecentPollAgreement()) + "% ").setEscapeModelStrings(false));
			agreement_item.add(new Label("agreementPercentageValue", String.format("%.1f", 100*au.getRecentPollAgreement()) + "% ").setEscapeModelStrings(false));
			repeatingAgreement.add(agreement_item);

			size_item.add(new Label("AUsizebox", boxName));
			
			size_item.add(new Label("repeatingAUsize", FileUtils.byteCountToDisplaySize(au.getContentSize())).setEscapeModelStrings(false));
			repeatingAUSizes.add(size_item);
			
		}
		
		
		

	
			
			
	
		
		
		
		// display panels for each AU with detailed info 
		Iterator<AU> auInBoxesIterator = auInBoxes.iterator();

		RepeatingView auTableRepeating = new RepeatingView("auAcrossBoxesDisplayTablePanel");
		add(auTableRepeating);

		while (auInBoxesIterator.hasNext()) {
			AU au = auInBoxesIterator.next();
			
			auTableRepeating.add(new AUAcrossBoxesDisplayTablePanel(auTableRepeating.newChildId(),parameters, plnDataController,au));


		}
	}
}

