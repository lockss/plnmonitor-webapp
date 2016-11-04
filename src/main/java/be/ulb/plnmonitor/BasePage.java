package be.ulb.plnmonitor;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import be.ulb.plnmonitor.datacontroller.PLNDataController;
import be.ulb.plnmonitor.datacontroller.UserSession;
import be.ulb.plnmonitor.object.AU;

public class BasePage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1368912539112543508L;
	final UserSession userSession = getMySession();
    
	final protected PLNDataController plnDataController = new PLNDataController(userSession);
	
	public BasePage(final PageParameters parameters) {
		super(parameters);
	    //plnDataController = new PLNDataController(userSession);
	    //plnDataController.loadPLNStatus();

		// TODO Add your page's components here
		add(new Link("globalNetworkStatusPage"){
			private static final long serialVersionUID = 2759176826285891095L;

					public void onClick() {
						
						//TODO: remove all load PLN Status() everywhere
						//plnDataController.loadPLNStatus();
		        		setResponsePage(new GlobalNetworkStatusPage(parameters));
		            }
		});
		

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
		
		add(new Link("loginPage"){
			public void onClick() {
				//plnDataController.loadPLNStatus();
        		setResponsePage(new Login(parameters));
            }
		});
		
		add(new Link("allMembers"){
			public void onClick() {
				//plnDataController.loadPLNStatus();
        		setResponsePage(new AllMembers(parameters, plnDataController));
            }
		});
		
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
        
//        for (final String currentBoxName: boxesName) {
//        	Link menuLink = new Link(boxMenuRepeating.newChildId()) {
//                /**
//				 * 
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//                public void onClick() {
//                	setResponsePage(new BoxInfoPage(parameters, plnDataController, currentBoxName));
//                }
//            };
//        	menuLink.add(new AttributeAppender("style", "color: rgba(255, 255, 255, 0.75); padding-left:50; margin-box:0; font-size: 12px; box-sizing: border-box; "));
//            //menuLink.setRenderBodyOnly(true);
//        	menuLink.setBody(Model.of(currentBoxName));
//        	menuLink.add(new Label("menuLink", "hello"));
//        	boxMenuRepeating.add(menuLink);
//
//        }
        
        for (final String currentBoxName: boxesName) {        	
            boxMenuRepeating.add(new MenuItemPanel(boxMenuRepeating.newChildId(), currentBoxName, parameters, plnDataController, currentBoxName)); 
        }


	}
        

		

    public UserSession getMySession(){
    	return (UserSession)getSession();
    }

}


 