package org.lockss.plnmonitor.webpages.Members;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.lockss.plnmonitor.datacontroller.PLNDataController;
import org.lockss.plnmonitor.object.Institution;
import org.lockss.plnmonitor.object.Person;
import org.lockss.plnmonitor.webpages.BasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.repeater.RepeatingView;

import java.sql.SQLException;
import java.util.Iterator;

public class AllMembers extends BasePage {
	private static final long serialVersionUID = 1L;

	public AllMembers(final PageParameters parameters) {
		super(parameters);
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
