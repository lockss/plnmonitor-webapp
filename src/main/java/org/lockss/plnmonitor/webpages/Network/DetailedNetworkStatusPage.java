package org.lockss.plnmonitor.webpages.Network;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.lockss.plnmonitor.datacontroller.PLNDataController;
import org.lockss.plnmonitor.object.LOCKSSBox;
import org.lockss.plnmonitor.object.LOCKSSBoxData;
import org.lockss.plnmonitor.object.Person;
import org.lockss.plnmonitor.webpages.BasePage;
import org.lockss.plnmonitor.webpages.SummaryPanel;
import org.lockss.plnmonitor.webpages.Members.PersonDisplayPage;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;

import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;

public class DetailedNetworkStatusPage extends BasePage implements IHeaderContributor {
	/**
	 * 
	 */
	private static final ArrayList<String> colors = new ArrayList<String>(Arrays.asList("aero",
                "purple",
                "red",
                "blue",
                "green",
                "orange"));
	
	private static final ArrayList<String> bgColors = new ArrayList<String>(Arrays.asList("#BDC3C7",
                "#9B59B6",
                "#E74C3C",
                "#3498DB",
                "#26B99A",
                "#FFA500"
                ));
	
	private static final  ArrayList<String> hoverColors = new ArrayList<String>(Arrays.asList("#CFD4D8",
            "#B370CF",
            "#E95E4F",
            "#49A9EA",
            "#36CAAB",
            "#FFA500"
            ));
	
	private static final long serialVersionUID = 5392655035733062380L;
	// private static final long serialVersionUID = 1L;

	public DetailedNetworkStatusPage(final PageParameters parameters, final PLNDataController plnDataController) {
		super(parameters);
		add(new SummaryPanel("summaryPanel", super.plnDataController));

		List<LOCKSSBox> plnBoxes = null;
		try {
			// TODO: get real PLN ID - not hard coded
			plnBoxes = plnDataController.getPlnDAO().getAllBoxesInfo(0);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RepeatingView repeatingUsage = new RepeatingView("repeatingUsage");
		add(repeatingUsage);

		for (LOCKSSBox box : plnBoxes) {

			long used = 0;
			long size = 0;
			List<LOCKSSBoxData> listBoxData = box.getLockssBoxData();
			for (LOCKSSBoxData boxData : listBoxData) {
				used += boxData.getUsed() * 1024; //expressed in 1k-blocks -> need to multiply by 1024				
				size += boxData.getSize() * 1024 ;//expressed in 1k-blocks -> need to multiply by 1024		
			}
			int percentageUsed = 0;
			if (size != 0) {
				percentageUsed= (int) ((used * 100) / size);
			}
			
			AbstractItem item = new AbstractItem(repeatingUsage.newChildId());

			item.add(new Label("institution", box.getName()));
			WebMarkupContainer template = new WebMarkupContainer("repeatingWidth");
			item.add(template);

			template.add(new AttributeAppender("style", "width:" + percentageUsed + "%;"));

			item.add(new Label("repeatingPercentage", percentageUsed + "% Complete").setEscapeModelStrings(false));
			item.add(new Label("repeatingCapacity", FileUtils.byteCountToDisplaySize(size))
					.setEscapeModelStrings(false));
			repeatingUsage.add(item);
		}

		HashMap<String, Long> AUSizePerInstitution = new HashMap<String, Long>();

		try {
			// TODO: get real PLN ID - not hard coded
			AUSizePerInstitution = plnDataController.getPlnDAO().getAUSizePerInstitution();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RepeatingView repeatingAUInstitution = new RepeatingView("repeatingAUInstitution");
		add(repeatingAUInstitution);
		
		Iterator<String> colorsIterator = colors.listIterator();
		Iterator<String> bgColorsIterator = bgColors.listIterator();
		Iterator<String> hoverColorsIterator = hoverColors.listIterator();
		
		String institutionsLabels = "[";
		String colorLabels = "[";
		String hoverColorLabels = "[";
		String bgColorsLabels = "[";
		List<Long> institutionsData = new ArrayList<Long>();
		Long totalData = (long) 0;
		for (String institution : AUSizePerInstitution.keySet()) {
			if ((institution != null) && !(institution.equals(""))) {
				AbstractItem item = new AbstractItem(repeatingUsage.newChildId());

				item.add(new Label("AUInstitution", institution));
				institutionsLabels = institutionsLabels + "\"" + institution + "\",";
				
				String curColor = colorsIterator.next();
				item.add(new WebMarkupContainer("colorInstitution").add(new AttributeAppender("class", " " + curColor)));
				colorLabels = colorLabels + "\""+ curColor + "\",";
				bgColorsLabels = bgColorsLabels + "\"" + bgColorsIterator.next() + "\",";
				hoverColorLabels = hoverColorLabels + "\"" + hoverColorsIterator.next() + "\",";
				item.add(new Label("sizeAUforInstitution",
						FileUtils.byteCountToDisplaySize(AUSizePerInstitution.get(institution))));
				totalData += AUSizePerInstitution.get(institution);
				institutionsData.add(AUSizePerInstitution.get(institution));
				repeatingAUInstitution.add(item);
			}
		}
		institutionsLabels = institutionsLabels.substring(0,institutionsLabels.length() - 1) + "]";
		
		String institutionsDataLabel= "[";
		for (Long data : institutionsData) {
			institutionsDataLabel = institutionsDataLabel + (int) (data*100 /totalData) + ",";
		}
		institutionsDataLabel = institutionsDataLabel.substring(0,institutionsDataLabel.length() - 1) + "]";
		colorLabels = colorLabels.substring(0, colorLabels.length()-1) + "]";
		hoverColorLabels = hoverColorLabels.substring(0, hoverColorLabels.length()-1) + "]";
		bgColorsLabels = bgColorsLabels.substring(0, bgColorsLabels.length()-1) + "]";
		RepeatingView repeating = new RepeatingView("repeating");
		add(repeating);

		int index = 0;
		Iterator<LOCKSSBox> lockssBoxIterator = plnBoxes.iterator();
		while (lockssBoxIterator.hasNext()) {
			AbstractItem item = new AbstractItem(repeating.newChildId());

			repeating.add(item);
			LOCKSSBox box = lockssBoxIterator.next();

			Person orgAdmin = null;
			Person techAdmin = null;
			try {
				orgAdmin = plnDataController.getPlnDAO().getPerson(box.getOrgAdmin());
				techAdmin = plnDataController.getPlnDAO().getPerson(box.getTechAdmin());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
			
			String daemonVersion = "";
			if(box.getDaemon_full_version()!= null) {
				
				Pattern pattern = Pattern.compile(".*fullVersion=(.*?),.*");
				Matcher matcher = pattern.matcher(box.getDaemon_full_version());
				if (matcher.find())	{
					daemonVersion=matcher.group(1);
				}
			}
			item.add(new Label("daemonVersion", daemonVersion));
			
			final long orgAdminId = box.getOrgAdmin();
			Link<String> orgManlink = new Link<String>("orgAdmin") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(new PersonDisplayPage(parameters, plnDataController, orgAdminId));
				}
			};
			orgManlink.setBody(Model.of(orgAdmin.getFirstName() + " " + orgAdmin.getName()));
			orgManlink.add(new AttributeAppender("style", "cursor: pointer;"));
			item.add(orgManlink);

			final long techAdminId = box.getTechAdmin();

			Link<String> techManlink = new Link<String>("techAdmin") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(new PersonDisplayPage(parameters, plnDataController, techAdminId));
				}
			};
			techManlink.setBody(Model.of(techAdmin.getFirstName() + " " + techAdmin.getName()));
			techManlink.add(new AttributeAppender("style", "cursor: pointer;"));
			item.add(techManlink);

			
			final int idx = index;
			item.add(AttributeModifier.replace("class", new AbstractReadOnlyModel<String>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject() {
					return (idx % 2 == 1) ? "even" : "odd";
				}
			}));

			index++;
		}

		add(new Label("script"," $(document).ready(function(){" +
		        "var options = {"+
		        "       legend: false,"+
		        "          responsive: false"+
		        "        };"+
		        "        new Chart(document.getElementById(\"canvas1\"), {"+
		        "          type: 'doughnut',"+
		        "          tooltipFillColor: \"rgba(51, 51, 51, 0.55)\","+
		        "          data: {"+
		        "            labels: "+institutionsLabels + "," +
		        "            datasets: [{"+
		        "              data: "+institutionsDataLabel + ","+
		        "              backgroundColor: "+ bgColorsLabels +","+
		        "              hoverBackgroundColor: " + hoverColorLabels +
		        "            }]"+
		        "          },"+
		        "          options: options"+
		        "        });"+
		        "      });").setEscapeModelStrings(false));
	
	}

	
	
}
