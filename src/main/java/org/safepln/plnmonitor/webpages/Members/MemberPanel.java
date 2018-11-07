package org.safepln.plnmonitor.webpages.Members;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.safepln.plnmonitor.datacontroller.PLNDataController;
import org.safepln.plnmonitor.object.Person;

class MemberPanel extends Panel {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	public MemberPanel(String id, final PageParameters parameters, final PLNDataController plnDataController,
			final Person person) {
		super(id);

		// Link menuLink = new Link("link") {
		// /**
		// *
		// */
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public void onClick() {
		// setResponsePage(new BoxInfoPage(parameters, plnDataController,
		// boxName));
		// }
		// };
		// menuLink.add(new Label("label", label));
		// add(menuLink);
		if (person != null) {
			String memberType = new String();
			if (person.isOrgAdmin()) {
				memberType.concat("Organizational Admin");
			}
			if (person.isTechAdmin()) {
				memberType.concat("Technical Admin");
			}
			add(new Label("MemberType", person.isOrgAdmin()? "Organizational Administrator":"Technical Administrator"));
			add(new Label("MemberName", person.getFirstName() + " " + person.getName()));
			add(new Label("MemberPhone", person.getPhone()));
			add(new Label("MemberEmail", person.getEmailAddress()));
			add(new Label("MemberInstitution", person.getInstitutionId()));
			//add(new Label("MemberEmailTo", "onClick=\").setEscapeModelStrings(false));	
			
		
			add(new Button("MemberEmailTo", Model.of("Send mail")) { // idem as previous comment

				private static final long serialVersionUID = 1L;

				@Override
				protected String getOnClickScript()
				{
					return "window.location.href =\"mailto:"+person.getEmailAddress()+"\";";
				}

				@Override
				public void onSubmit()
				{
					 //this.info("Cancel was pressed!");
					//this.window.location.href='mailto:"+ person.getEmailAddress()+ "';\"";
				}
			});
		} else {
			add(new Label("MemberType", "not defined"));
			add(new Label("MemberName", "none"));
			add(new Label("MemberPhone", ""));
			add(new Label("MemberEmail", ""));
			add(new Label("MemberInstitution", ""));
			add(new Label("MemberEmailTo", ""));
		}
	}
}
