package org.safepln.plnmonitor.webpages.BoxStatus;


import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.safepln.plnmonitor.datacontroller.PLNDataController;
import org.safepln.plnmonitor.object.LOCKSSBox;
import org.safepln.plnmonitor.object.Person;
import org.safepln.plnmonitor.webpages.BasePage;
import org.safepln.plnmonitor.webpages.Configuration.EditBoxPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;

import java.sql.SQLException;
import java.util.Iterator;

import org.apache.wicket.AttributeModifier;

public class BoxConfigurationPage extends BasePage {
	private static final long serialVersionUID = 1L;

	public BoxConfigurationPage(final PageParameters parameters) {
		super(parameters);

		//add(new Label("version", getApplication().getFrameworkSettings().getVersion()));

		// TODO Add your page's components here

    }


	public BoxConfigurationPage(final PageParameters parameters, PLNDataController plnDataController) {
		super(parameters);
	      
		
		Iterator<LOCKSSBox> plnBoxes=null;
		try {
			plnBoxes = plnDataController.getPlnDAO().getAllBoxesInfo(0).iterator();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	        RepeatingView repeating = new RepeatingView("repeating");
	        add(repeating);

	        int index = 0;
	        while (plnBoxes.hasNext())
	        {
	            AbstractItem item = new AbstractItem(repeating.newChildId());

	            repeating.add(item);
	            LOCKSSBox box = plnBoxes.next();

	            item.add(new Label("boxId", String.valueOf(box.getBoxId())));
	            item.add(new Label("country", box.getCountry()));
	            item.add(new Label("ipAddress", box.getIpAddress()));
	            item.add(new Label("uiport", box.getUiport()));
	            
	            if (box.getLockssBoxData()!= null && (box.getLockssBoxData().size()!=0)) {
	            	item.add(new Label("activeAUs", box.getLockssBoxData().get(0).getActiveAUs()));
				}
				else {
					item.add(new Label("activeAUs", ""));				
				}
	            
	            
	            Person orgAdmin = null;
	            Person techAdmin = null;
	            try {
	            	orgAdmin = plnDataController.getPlnDAO().getPerson(box.getOrgAdmin());
	            	techAdmin = plnDataController.getPlnDAO().getPerson(box.getTechAdmin());
	            }
	            catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	            }
	            
	            item.add(new Label("techAdmin", techAdmin.getFirstName() + " " + techAdmin.getName()));
	            item.add(new Label("orgAdmin", orgAdmin.getFirstName() + " " + orgAdmin.getName()));
	             
	            item.add(new Link("editBox", Model.of("Edit box")){  // idem as previous comment

					private static final long serialVersionUID = 1L;

					public void onClick()
					{
						setResponsePage(new EditBoxPage(parameters));
					}
				});
	            
	            final int idx = index;
	            item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>()
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
