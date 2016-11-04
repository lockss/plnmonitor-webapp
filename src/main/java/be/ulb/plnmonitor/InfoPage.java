package be.ulb.plnmonitor;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class InfoPage extends BasePage {

    public InfoPage(final PageParameters parameters) {
        super(parameters);
        
    }
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
         String initMapJS = new String ("function initMap() {" +        		 
			"	var uluru = { "+
			"		lat : -25.363,"+
			"		lng : 131.044" +
			"	};" +
			"	var map = new google.maps.Map(document.getElementById('map'), {"+
			"		zoom : 4,"+
			"		center : uluru"+
			"	});" +

			"	var contentString = '<div id=\"content\">' "+
			"			+ '<div id=\"siteNotice\">'"
			+"			+ '</div>'"
			+"			+ '<h1 id=\"firstHeading\" class=\"firstHeading\">Uluru</h1>'"
			+"			+ '<div id=\"bodyContent\">'"
			+"			+ '<p><b>Uluru</b>, also referred to as <b>Ayers Rock</b>, is a large '"
			+"			+ 'sandstone rock formation in the southern part of the '"
			+"			+ 'Northern Territory, central Australia. It lies 335&#160;km (208&#160;mi) '"
			+"			+ 'south west of the nearest large town, Alice Springs; 450&#160;km '"
			+"			+ '(280&#160;mi) by road. Kata Tjuta and Uluru are the two major '"
			+"			+ 'features of the Uluru - Kata Tjuta National Park. Uluru is '"
			+"			+ 'sacred to the Pitjantjatjara and Yankunytjatjara, the '"
			+"			+ 'Aboriginal people of the area. It has many springs, waterholes, '"
			+"			+ 'rock caves and ancient paintings. Uluru is listed as a World '"
			+"			+ 'Heritage Site.</p>'"
			+"			+ '<p>Attribution: Uluru, <a href=\"https://en.wikipedia.org/w/index.php?title=Uluru&oldid=297882194\">'"
			+"			+ 'https://en.wikipedia.org/w/index.php?title=Uluru</a> '"
			+"			+ '(last visited June 22, 2009).</p>' + '</div>'"
			+"			+ '</div>';"

			+"	var infowindow = new google.maps.InfoWindow({"
			+"		content : contentString,"
			+"		maxWidth : 200"
			+"	});"

			+"	var marker = new google.maps.Marker({"
			+"		position : uluru,"
			+"		map : map,"
			+"		title : 'Uluru (Ayers Rock)'"
			+"	});"
			+"	marker.addListener('click', function() {"
			+"		infowindow.open(map, marker);"
			+"	});"
			+"};");
			
			response.render(JavaScriptHeaderItem.forScript(initMapJS, "initMap"));
    } 
}