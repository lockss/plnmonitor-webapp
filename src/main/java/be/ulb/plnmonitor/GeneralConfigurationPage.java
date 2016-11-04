package be.ulb.plnmonitor;


import org.apache.wicket.request.mapper.parameter.PageParameters;

import be.ulb.plnmonitor.datacontroller.PLNDataController;
import be.ulb.plnmonitor.object.LOCKSSBox;
import be.ulb.plnmonitor.object.PLN;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;

import java.sql.SQLException;
import java.util.Iterator;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebPage;

public class GeneralConfigurationPage extends BasePage {
	private static final long serialVersionUID = 1L;

	public GeneralConfigurationPage(final PageParameters parameters) {
		super(parameters);

		//add(new Label("version", getApplication().getFrameworkSettings().getVersion()));

		// TODO Add your page's components here

    }
	
	public GeneralConfigurationPage(final PageParameters parameters, PLNDataController plnDataController) {
		super(parameters);
		 
	      Iterator<PLN> plns=null;
		try {
			plns = plnDataController.getPlnDAO().getPLNs().iterator();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	        RepeatingView repeating = new RepeatingView("genconfigrepeating");
	        add(repeating);

	        int index = 0;
	        while (plns.hasNext())
	        {
	            AbstractItem item = new AbstractItem(repeating.newChildId());

	            repeating.add(item);
	            PLN pln = plns.next();

	            item.add(new Label("plnName", String.valueOf(pln.getName())));
	            item.add(new Label("configURL", String.valueOf(pln.getConfigUrl())));
	             
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
