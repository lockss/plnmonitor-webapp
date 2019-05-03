package org.lockss.plnmonitor.webpages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.lockss.plnmonitor.datacontroller.PLNDataController;
import org.lockss.plnmonitor.webpages.BoxStatus.BoxInfoPage;

class BoxMenuItemPanel extends Panel { 
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BoxMenuItemPanel(String id, String label, final PageParameters parameters, final PLNDataController plnDataController, final String boxName) { 
	    super(id); 

	    
	    Link menuLink = new Link("link") {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void onClick() {
            	setResponsePage(new BoxInfoPage(parameters, plnDataController, boxName));
            }
        };
        menuLink.add(new Label("label", label));
        add(menuLink);
	  } 
	} 
