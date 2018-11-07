package org.safepln.plnmonitor.webpages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.safepln.plnmonitor.datacontroller.PLNDataController;
import org.safepln.plnmonitor.webpages.AUstatus.AUfromPublisherPage;
import org.safepln.plnmonitor.webpages.BoxStatus.BoxInfoPage;

class AUMenuItemPanel extends Panel { 
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AUMenuItemPanel(String id, String label, final PageParameters parameters, final PLNDataController plnDataController, final String publisherName) { 
	    super(id); 

	    
	    Link menuLink = new Link("link") {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void onClick() {
            	setResponsePage(new AUfromPublisherPage(parameters, plnDataController, publisherName));
            }
        };
        menuLink.add(new Label("label", label));
        add(menuLink);
	  } 
	} 
