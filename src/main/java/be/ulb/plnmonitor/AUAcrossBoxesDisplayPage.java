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

public class AUAcrossBoxesDisplayPage extends BasePage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5392655035733062380L;
	// private static final long serialVersionUID = 1L;

	public AUAcrossBoxesDisplayPage(final PageParameters parameters) {
		super(parameters);
	}

	public AUAcrossBoxesDisplayPage(final PageParameters parameters, PLNDataController plnDataController,
			String auName) {
		super(parameters);

		List<AU> auInBoxes = null;
		// TODO: adapt PLN identifier in query

		try {
			auInBoxes = plnDataController.getPlnDAO().getAUsAcrossBoxes(0, auName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		add(new Label("auName", auName));

		Iterator<AU> auInBoxesIterator = auInBoxes.iterator();

		RepeatingView auTableRepeating = new RepeatingView("auAcrossBoxesDisplayTablePanel");
		add(auTableRepeating);

		while (auInBoxesIterator.hasNext()) {
			AU au = auInBoxesIterator.next();
			
			auTableRepeating.add(new AUAcrossBoxesDisplayTablePanel(auTableRepeating.newChildId(),parameters, plnDataController,au));


		}
	}
}

