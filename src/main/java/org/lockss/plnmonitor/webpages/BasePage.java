package org.lockss.plnmonitor.webpages;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.lockss.plnmonitor.NewAuthenticatedWebSession;
import org.lockss.plnmonitor.datacontroller.PLNDataController;
import org.lockss.plnmonitor.webpages.AUstatus.AUDetails;
import org.lockss.plnmonitor.webpages.AUstatus.AUOverview;
import org.lockss.plnmonitor.webpages.BoxStatus.BoxConfigurationPage;
import org.lockss.plnmonitor.webpages.Configuration.ChangePwdPage;
import org.lockss.plnmonitor.webpages.Configuration.GeneralConfigurationPage;
import org.lockss.plnmonitor.webpages.Members.AllMembers;
import org.lockss.plnmonitor.webpages.Network.DetailedNetworkStatusPage;
import org.lockss.plnmonitor.webpages.Network.GlobalNetworkStatusPage;

/**
 * This page is only accessible by a user in the ADMIN role.
 */
@AuthorizeInstantiation("ADMIN")

public class BasePage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1368912539112543508L;
	final NewAuthenticatedWebSession userSession = getMySession();
    
	final protected PLNDataController plnDataController = new PLNDataController(userSession);
	
	public BasePage(final PageParameters parameters) {
		super(parameters);
	    
//		add(new Link("globalNetworkStatusPage"){
//			private static final long serialVersionUID = 2759176826285891095L;
//
//					public void onClick() {
//						
//						//TODO: remove all load PLN Status() everywhere
//						//plnDataController.loadPLNStatus();
//		        		setResponsePage(new GlobalNetworkStatusPage(parameters));
//		            }
//		});
		

		add(new Link("detailedNetworkStatusPage"){
            /**
			 * 
			 */
			private static final long serialVersionUID = -6490486240576052467L;

			public void onClick() {
				//plnDataController.loadPLNStatus();
        		setResponsePage(new DetailedNetworkStatusPage(parameters, plnDataController));
            }
		});

		add(new Link("generalConfigurationPage"){
            /**
			 * 
			 */
			private static final long serialVersionUID = 6001051763895895979L;

			public void onClick() {
				//plnDataController.loadPLNStatus();
        		setResponsePage(new GeneralConfigurationPage(parameters, plnDataController));
            }
		});

		add(new Link("boxConfigurationPage"){
            /**
			 * 
			 */
			private static final long serialVersionUID = -103004139170927593L;

			public void onClick() {
        		setResponsePage(new BoxConfigurationPage(parameters, plnDataController));
            }
		});

		
		add(new Link("auOverview"){
			public void onClick() {
				//plnDataController.loadPLNStatus();
        		setResponsePage(new AUOverview(parameters, plnDataController));
            }
		});
		
		add(new Link("auDetails"){
			public void onClick() {
				//plnDataController.loadPLNStatus();
        		setResponsePage(new AUDetails(parameters, plnDataController));
            }
		});
	
		
		// AU Repeating Menu 
		
		List<String> AUProducerNames = null;
		try {
			AUProducerNames = plnDataController.getPlnDAO().getAUPublisherNames();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO: fix CSS issue with menu not aligning properly
		
		RepeatingView AUMenuRepeating = new RepeatingView("AUMenuRepeating");
        add(AUMenuRepeating);
        
        
        for (final String currentAUProducerName: AUProducerNames) {        	
        	AUMenuRepeating.add(new AUMenuItemPanel(AUMenuRepeating.newChildId(), currentAUProducerName, parameters, plnDataController, currentAUProducerName)); 
        }
		
		add(new Link("logoutPage"){
			public void onClick() {
				//plnDataController.loadPLNStatus();
				getSession().invalidate();
        		setResponsePage(new InfoPage(parameters));
            }
		});
		
		add(new Link("changePwdPage"){
			public void onClick() {
        		setResponsePage(new ChangePwdPage(parameters, plnDataController));
            }
		});
		
		
		
		add(new Link("allMembers"){
			public void onClick() {
				//plnDataController.loadPLNStatus();
        		setResponsePage(new AllMembers(parameters, plnDataController));
            }
		});
		
		
		// Box repeating menu
		
		List<String> boxesName = null;
		try {
			boxesName = plnDataController.getPlnDAO().getBoxesNames();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO: fix CSS issue with menu not aligning properly
		
		RepeatingView boxMenuRepeating = new RepeatingView("boxMenuRepeating");
        add(boxMenuRepeating);
        
        
        for (final String currentBoxName: boxesName) {        	
            boxMenuRepeating.add(new BoxMenuItemPanel(boxMenuRepeating.newChildId(), currentBoxName, parameters, plnDataController, currentBoxName)); 
        }

	}
        

    public NewAuthenticatedWebSession getMySession(){
    	return (NewAuthenticatedWebSession)getSession();
    }

}


 