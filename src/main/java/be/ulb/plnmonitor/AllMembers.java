package be.ulb.plnmonitor;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import be.ulb.plnmonitor.datacontroller.PLNDataController;
import be.ulb.plnmonitor.object.Institution;
import be.ulb.plnmonitor.object.LOCKSSBox;
import be.ulb.plnmonitor.object.PLN;
import be.ulb.plnmonitor.object.Person;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;

import java.sql.SQLException;
import java.util.Iterator;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebPage;

public class AllMembers extends BasePage {
	private static final long serialVersionUID = 1L;

	public AllMembers(final PageParameters parameters) {
		super(parameters);

		// add(new Label("version",
		// getApplication().getFrameworkSettings().getVersion()));

		// TODO Add your page's components here

	}

	public AllMembers(final PageParameters parameters, PLNDataController plnDataController) {
		super(parameters);

		RepeatingView repeating = new RepeatingView("MembersRepeating");
		add(repeating);

		Iterator<Institution> institutions = null;
		try {
			institutions = plnDataController.getPlnDAO().getAllInstitutions().iterator();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (institutions.hasNext()) {
			Iterator<Person> personsFromInstitution = null;
			Institution curInstitution = institutions.next();
			long institutionId = curInstitution.getInstitutionId();
			try {
				personsFromInstitution = plnDataController.getPlnDAO().getPersonsFromInstitution(institutionId)
						.iterator();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			AbstractItem item = new AbstractItem(repeating.newChildId());
			repeating.add(item);
			item.add(new Label("InstitutionRepeating", curInstitution.getName()));
			
			Person techAdmin = null;
			Person orgAdmin = null;
			while (personsFromInstitution.hasNext()) {
				Person curPerson = personsFromInstitution.next();
	
				if (curPerson.isTechAdmin()) {
					techAdmin = new Person(curPerson);
				} else if (curPerson.isOrgAdmin()) {
					orgAdmin = new Person(curPerson);
					
				}

			}
			
			item.add(new MemberPanel("OrgMembersRepeating", parameters, plnDataController, orgAdmin));
			item.add(new MemberPanel("TechMembersRepeating", parameters, plnDataController, techAdmin));

		}

	}

}
