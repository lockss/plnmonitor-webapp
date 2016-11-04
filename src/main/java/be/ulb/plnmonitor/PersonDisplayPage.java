package be.ulb.plnmonitor;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import be.ulb.plnmonitor.datacontroller.PLNDataController;
import be.ulb.plnmonitor.object.LOCKSSBox;
import be.ulb.plnmonitor.object.PLN;
import be.ulb.plnmonitor.object.Person;

import org.apache.wicket.markup.html.basic.Label;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

public class PersonDisplayPage extends BasePage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5392655035733062380L;
	// private static final long serialVersionUID = 1L;

	public PersonDisplayPage(final PageParameters parameters, final PLNDataController plnDataController, final long personId) {
		super(parameters);

		Person person = null;
		try {
			// TODO: get real PLN ID - not hard coded
			person = plnDataController.getPlnDAO().getPerson(personId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		add(new Label("memberName", person.getFirstName() + " " + person.getName()));
//		add(new Label("phone", person.getPhone()));
//		add(new Label("email", person.getEmailAddress()));
//		add(new Label("institute", "Institute"));
//		add(new Label("address", "Address"));
//		add(new Label("skype", "skype account"));
//		add(new Label("github", "github account"));
		add(new MemberPanel("memberPanel", parameters, plnDataController, person));
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
}
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
