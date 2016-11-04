package be.ulb.plnmonitor;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import be.ulb.plnmonitor.datacontroller.PLNDataController;
import be.ulb.plnmonitor.object.AU;
import be.ulb.plnmonitor.object.LOCKSSBox;
import be.ulb.plnmonitor.object.PLN;
import be.ulb.plnmonitor.object.Person;

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
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.CSVDataExporter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.ExportToolbar;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/////////////////////////////////////////////////////////////////////
// Webpage displaying AU info for one specific AU in one specific box
/////////////////////////////////////////////////////////////////////

class AUAcrossBoxesDisplayTablePanel extends Panel {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	
	public AUAcrossBoxesDisplayTablePanel(final String id, final PageParameters parameters, final PLNDataController plnDataController, final AU au) {
		super(id);
			String boxName = new String();
			try {
				boxName = plnDataController.getPlnDAO().getBoxName(au.getLockssBoxId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			add(new Label("tablename", boxName));
			RepeatingView auRepeating = new RepeatingView("AUrepeating");
			add(auRepeating);
			
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

			AbstractItem item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "AU Name"));
			item.add(new Label("AUvalue", au.getName()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Access Type"));
			item.add(new Label("AUvalue", au.getAccessType()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "AU LOCKSS ID"));
			item.add(new Label("AUvalue", au.getAu_LOCKSS_Id()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Crawl Pool"));
			item.add(new Label("AUvalue", au.getCrawlPool()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Crawl Proxy"));
			item.add(new Label("AUvalue", au.getCrawlProxy()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Crawl Window"));
			item.add(new Label("AUvalue", au.getCrawlWindow()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Last Crawl Result"));
			item.add(new Label("AUvalue", au.getLastCrawlResult()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Last Poll Result"));
			item.add(new Label("AUvalue", au.getLastPollResult()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Plugin Name"));
			item.add(new Label("AUvalue", au.getPluginName())); 

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Publishing Platform"));
			item.add(new Label("AUvalue", au.getPublishingPlatform()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Repository Path"));
			item.add(new Label("AUvalue", au.getRepositoryPath()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Subscription Status"));
			item.add(new Label("AUvalue", au.getSubscriptionStatus()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Substance State"));
			item.add(new Label("AUvalue", au.getSubstanceState()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Title Database Publisher"));
			item.add(new Label("AUvalue", au.getTdbPublisher()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Title Database Year"));
			item.add(new Label("AUvalue", au.getTdbYear()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Volume"));
			item.add(new Label("AUvalue", au.getVolume()));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Content Size"));
			item.add(new Label("AUvalue", FileUtils.byteCountToDisplaySize(au.getContentSize())));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Info Created At"));
			item.add(new Label("AUvalue", format.format(au.getCreatedAt())));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Info Updated At"));
			item.add(new Label("AUvalue", format.format(au.getUpdatedAt())));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "AU Creation Time"));
			item.add(new Label("AUvalue", format.format(au.getCreationTime())));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Disk Usage"));
			item.add(new Label("AUvalue", FileUtils.byteCountToDisplaySize(au.getDiskUsage())));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Last Completed Crawl"));
			item.add(new Label("AUvalue", format.format(au.getLastCompletedCrawl())));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Last Completed Poll"));
			item.add(new Label("AUvalue", format.format(au.getLastCompletedPoll())));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "AU Last Crawl"));
			item.add(new Label("AUvalue", format.format(au.getLastCrawl())));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Recent Poll Agreement"));
			item.add(new Label("AUvalue", format.format(au.getRecentPollAgreement())));

			item = new AbstractItem(auRepeating.newChildId());
			auRepeating.add(item);
			item.add(new Label("AUitem", "Available from publisher"));
			item.add(new Label("AUvalue", au.isAvailableFromPublisher() ? "yes" : "no"));



		}
	}

/**
 * 
 */
// class ActionPanel extends Panel
// {
// /**
// *
// */
// private static final long serialVersionUID = -8354021933953685121L;
//
// /**
// * @param id
// * component id
// * @param model
// * model for contact
// */
// public ActionPanel(String id, IModel<Contact> model)
// {
// super(id, model);
// add(new Link("select")
// {
// /**
// *
// */
// private static final long serialVersionUID = 1L;
//
// @Override
// public void onClick()
// {
// selected = (Contact)getParent().getDefaultModelObject();
// }
// });
// }

/**
 * @return selected contact
 */
// public Contact getSelected()
// {
// return selected;
// }
//
/// **
// * sets selected contact
// *
// * @param selected
// */
// public void setSelected(Contact selected)
// {
// addStateChange();
// this.selected = selected;
// }
//
/// **
// * @return string representation of selected contact property
// */
// public String getSelectedContactLabel()
// {
// if (selected == null)
// {
// return "No Contact Selected";
// }
// else
// {
// return selected.getFirstName() + " " + selected.getLastName();
// }
// }
// }
