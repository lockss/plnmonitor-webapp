package org.lockss.plnmonitor.webpages.AUstatus;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;


import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.lockss.plnmonitor.datacontroller.PLNDataController;
import org.lockss.plnmonitor.object.AU;
import org.lockss.plnmonitor.webpages.BasePage;
import org.lockss.plnmonitor.webpages.BoxStatus.BoxInfoPage;
import org.apache.wicket.markup.html.basic.Label;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;


/////////////////////////////////////////////////////////////////////
//Webpage displaying all AUs names available in all boxes
/////////////////////////////////////////////////////////////////////

public class AUDetails extends BasePage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5392655035733062380L;
	//private static final long serialVersionUID = 1L;
	
	public AUDetails(final PageParameters parameters, final PLNDataController plnDataController) {
		super(parameters);
		 
	    List<String> allAUNames = new ArrayList<String>();
	    		
		try {
			//TODO: adapt to PLN ID
			Iterator<String> allAUs = plnDataController.getPlnDAO().getAllAUsInPLN(0).iterator();
			
			while (allAUs.hasNext()){
				String curAUName = allAUs.next();
				if (! allAUNames.contains(curAUName)){
					allAUNames.add(curAUName);
				}
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        
		Iterator<String> allAUNamesit = allAUNames.iterator();
		
		RepeatingView AUrepeating = new RepeatingView("AUrepeating");
        add(AUrepeating);
                
        AbstractItem AUitem = null;

		while (allAUNamesit.hasNext()){
			String 	AUName = allAUNamesit.next();
			
			Iterator<AU> allAUs = null;
			try {
				allAUs = plnDataController.getPlnDAO().getAUsAcrossBoxes(0, AUName).iterator();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		    int index = 0;
	        while (allAUs.hasNext()){
	        	final AU au = allAUs.next();
	        	String ID = AUrepeating.newChildId();
	        	
	        	AUitem = new AbstractItem(ID);
	        	
	        	String boxName = null;
	        	try {
	    			//TODO: adapt to PLN ID
	    				boxName = plnDataController.getPlnDAO().getBoxName(au.getLockssBoxId());
	    		
	    		} catch (SQLException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	        	
	        	final String boxNameLink=boxName;
				
	        	Link<String> linkBox = new Link<String>("BoxName") {

	        		private static final long serialVersionUID = 1L;

					@Override
	                public void onClick() {
	                	setResponsePage(new BoxInfoPage(parameters, plnDataController, boxNameLink));
	                }
	            };
	            linkBox.setBody(Model.of(boxName));
	            linkBox.add(new AttributeAppender("style", "cursor: pointer;"));
	        	AUitem.add(linkBox);
	        	
	        	Link<String> linkAU = new Link<String>("AUName") {
	                /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
	                public void onClick() {
	                	setResponsePage(new AUAcrossBoxesDisplayPage(parameters, plnDataController, au.getName()));
	                }
	            };
	            linkAU.setBody(Model.of(au.getName()));
	            linkAU.add(new AttributeAppender("style", "cursor: pointer;"));
	        	AUitem.add(linkAU);

	        	AUitem.add(new Label("AUlisttdbyear", au.getTdbYear()));
	        	AUitem.add(new Label("AUlistaccesstype", au.getAccessType()));
	        	AUitem.add(new Label("AUlistcontentsize", FileUtils.byteCountToDisplaySize(au.getContentSize())));
	        	AUitem.add(new Label("AUlisttdbpublisher", au.getTdbPublisher()));
	        	AUitem.add(new Label("AUlistlastpollresult", au.getLastPollResult()));
	        	AUrepeating.add(AUitem);
	        	
	            final int idx = index;
	            AUitem.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>()
	            {
	                private static final long serialVersionUID = 1L;

	                @Override
	                public String getObject()
	                {
	                    return (idx % 2 == 1) ? "even" : "odd";
	                }
	            }));

	            index++;
		        }
	        }
		}

}



