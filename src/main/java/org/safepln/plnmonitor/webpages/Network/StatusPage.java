//package be.ulb.plnmonitor;
//
//import org.apache.wicket.markup.html.link.Link;
//import org.apache.wicket.markup.html.list.AbstractItem;
//import org.apache.wicket.markup.html.panel.FeedbackPanel;
//import org.apache.wicket.markup.html.panel.Panel;
//
//
//import org.apache.wicket.request.mapper.parameter.PageParameters;
//
//import be.ulb.plnmonitor.datacontroller.PLNDataController;
//import be.ulb.plnmonitor.object.LOCKSSBox;
//import be.ulb.plnmonitor.object.PLN;
//
//import org.apache.wicket.markup.html.basic.Label;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.apache.wicket.AttributeModifier;
//import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.export.CSVDataExporter;
//import org.apache.wicket.extensions.markup.html.repeater.data.table.export.ExportToolbar;
//import org.apache.wicket.markup.repeater.Item;
//import org.apache.wicket.markup.repeater.RepeatingView;
//import org.apache.wicket.model.AbstractReadOnlyModel;
//import org.apache.wicket.model.IModel;
//import org.apache.wicket.model.Model;
//import org.apache.wicket.model.PropertyModel;
//
//
//
//public class StatusPage extends BasePage {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 5392655035733062380L;
//	//private static final long serialVersionUID = 1L;
//	
//	public StatusPage(final PageParameters parameters, PLNDataController plnDataController) {
//		super(parameters);
//		 
//	      Iterator<LOCKSSBox> plnBoxes = plnDataController.getPlnDAO().getPlnList().get(0).getPlnBoxes().iterator();
//
//	        RepeatingView repeating = new RepeatingView("repeating");
//	        add(repeating);
//
//	        int index = 0;
//	        while (plnBoxes.hasNext())
//	        {
//	            AbstractItem item = new AbstractItem(repeating.newChildId());
//
//	            repeating.add(item);
//	            LOCKSSBox box = plnBoxes.next();
//
//	            item.add(new Label("boxId", String.valueOf(box.getBoxId())));
//	            item.add(new Label("country", box.getCountry()));
//	            item.add(new Label("ipAddress", box.getIpAddress()));
//	            item.add(new Label("uiport", box.getUiport()));
//	            item.add(new Label("activeAUs", box.getLockssBoxData().get(0).getActiveAUs()));
//	            item.add(new Label("techAdmin", box.getTechAdmin().getFirstName() + " " + box.getTechAdmin().getName()));
//	            item.add(new Label("orgAdmin", box.getOrgAdmin().getFirstName() + " " + box.getOrgAdmin().getName()));
//	             
//	            final int idx = index;
//	            item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>()
//	            {
//	                private static final long serialVersionUID = 1L;
//
//	                @Override
//	                public String getObject()
//	                {
//	                    return (idx % 2 == 1) ? "even" : "odd";
//	                }
//	            }));
//
//	            index++;
//	        }
//	}
//
//
///**
// * 
// */
////class ActionPanel extends Panel
////{
////    /**
////	 * 
////	 */
////	private static final long serialVersionUID = -8354021933953685121L;
////
////	/**
////     * @param id
////     *            component id
////     * @param model
////     *            model for contact
////     */
////    public ActionPanel(String id, IModel<Contact> model)
////    {
////        super(id, model);
////        add(new Link("select")
////        {
////            /**
////			 * 
////			 */
////			private static final long serialVersionUID = 1L;
////
////			@Override
////            public void onClick()
////            {
////                selected = (Contact)getParent().getDefaultModelObject();
////            }
////        });
////    }
//}
///**
// * @return selected contact
// */
////public Contact getSelected()
////{
////    return selected;
////}
////
/////**
//// * sets selected contact
//// * 
//// * @param selected
//// */
////public void setSelected(Contact selected)
////{
////    addStateChange();
////    this.selected = selected;
////}
////
/////**
//// * @return string representation of selected contact property
//// */
////public String getSelectedContactLabel()
////{
////    if (selected == null)
////    {
////        return "No Contact Selected";
////    }
////    else
////    {
////        return selected.getFirstName() + " " + selected.getLastName();
////    }
////}
////}
//
//
