package be.ulb.plnmonitor;

import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.UrlResourceReference;

import be.ulb.plnmonitor.datacontroller.PLNDataController;
import be.ulb.plnmonitor.object.LOCKSSBox;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;

public class HomePage extends BasePage {
	private static final long serialVersionUID = 1L;

	private List<LOCKSSBox> plnBoxes;

	public HomePage(final PageParameters parameters) {
		super(parameters);      
		//plnBoxes = plnDataController.getPlnDAO().getPlnList().get(0).getPlnBoxes().iterator();   
	};

	public HomePage(final PageParameters parameters, PLNDataController plnDataController) {
		super(parameters);      
		plnBoxes = plnDataController.getPlnDAO().getPlnList().get(0).getPlnBoxes();   
	};

//	@Override
//	public void renderHead(IHeaderResponse response) {
//		super.renderHead(response);
//		if (plnBoxes != null){
//			String nodes = "var nodes = new vis.DataSet([ ";
//			Iterator<LOCKSSBox> plnBoxesIt = plnBoxes.iterator(); 
//			int count=0;
//			LOCKSSBox box = null;
//			for (int i=0; plnBoxesIt.hasNext(); i++) {
//				box = plnBoxesIt.next();
//				if (i != 0) nodes = nodes + ", \n"; 
//				nodes = nodes + "{id: " + i + ",\n label : '" + box.getName() + "', \n title : '" + box.getIpAddress();
//				nodes = nodes + "', \n group:0, \n" + "	color : {background : 'white', border : 'green'}\n";
//				nodes = nodes + "}";
//				count = i;
//			}
//			nodes = nodes + " ]); \n";
//
//			String edges = "var edges = new vis.DataSet([ ";
//			for (int i = 0; i < count; i++) {
//				for (int j = 0; j< count-i; j++) {
//					if  (i==0 && j==0) {} 
//					else { 
//							edges = edges + ",\n";
//					}
//					edges = edges + "{\n";
//					edges = edges + "	from: " + i + ",\n";
//					edges = edges + "	to: " + j + ",\n";
//					edges = edges + "	color : {\n";
//					edges = edges +	"		color : 'red' \n";
//					edges = edges +	"	}\n";
//					edges = edges + "}";
//				}
//			}
//			edges = edges + " ]); \n";
//
//			String networkJS = nodes + edges + new String ("var container = document.getElementById('mynetwork');"+
//					"var data = {"+
//					"	nodes : nodes,"+
//					"	edges : edges"+
//					"};"+
//					"var options = {"+
//					"   layout:{randomSeed:2}," + 
//					"	nodes : {"+
//					"		shape : 'box',"+
//					"		size : 10,"+
//					"		font : {"+
//					"			size : 16,"+
//					"			strokeWidth : 3"+
//					"		},"+
//					"		borderWidth : 2,"+
//					"		shadow : true"+
//					"	},"+
//					"	edges : {"+
//					"		width : 1,"+
//					"		length : 100,"+
//					"		smooth : {"+
//					"			type : 'dynamic',"+
//					"			roundness : 0.1"+
//					"		},"+
////					"		physics: false,"+
//					"		shadow : true"+
//					"	}"+
//					"};"+
//					"var network = new vis.Network(container, data, options);");
//
//			response.render(OnDomReadyHeaderItem.forScript(networkJS));
//		}
//
//	}
}
