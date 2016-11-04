package be.ulb.plnmonitor;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import be.ulb.plnmonitor.datacontroller.PLNDataController;
import be.ulb.plnmonitor.object.LOCKSSBox;
import be.ulb.plnmonitor.object.PLN;

import org.apache.wicket.markup.html.basic.Label;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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

public class GlobalNetworkStatusPage extends BasePage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5392655035733062380L;
	// private static final long serialVersionUID = 1L;

	public GlobalNetworkStatusPage(final PageParameters parameters) {
		super(parameters);
		add(new SummaryPanel("summaryPanel", super.plnDataController));

		add(new Label("script","function drawmap() {\n"+
"			d3.select(window).on(\"mousemove\", mousemove).on(\"mouseup\", mouseup);\n"+
"\n"+
"			var origrotate = [ 30, -50, 0 ]\n"+
"\n"+
"			var w = window, d = document, e = d.documentElement, g = d\n"+
"					.getElementById('safeplnmap'), x = g.clientWidth, y = 600;\n"+
"\n"+
"			var width = x, height = y;\n"+
"\n"+
"			var tip = d3.tip().attr('class', 'd3-tip').offset([ 0, 0 ]).html(\n"+
"					function(d) {\n"+
"						if (d.down == false) {\n"+
"							return \"<span style='color:green'>\" + d.sourceName\n"+
"									+ \" &#65515 \" + d.targetName\n"+
"									+ \" is UP </span>\";\n"+
"						} else {\n"+
"							return \"<span style='color:red'>\" + d.sourceName\n"+
"									+ \" &#65515 \" + d.targetName\n"+
"									+ \" is DOWN </span>\";\n"+
"						}\n"+
"					})\n"+
"\n"+
"			var proj = d3.geo.orthographic().translate(\n"+
"					[ width / 2, height / 2 ]).clipAngle(90).scale(600).rotate(\n"+
"					origrotate);\n"+
"\n"+
"			var sky = d3.geo.orthographic()\n"+
"					.translate([ width / 2, height / 2 ]).clipAngle(90).scale(\n"+
"							proj.scale() * 1.05).rotate(origrotate);\n"+
"\n"+
"			var path = d3.geo.path().projection(proj).pointRadius(2);\n"+
"\n"+
"			var swoosh = d3.svg.line().x(function(d) {\n"+
"				return d[0]\n"+
"			}).y(function(d) {\n"+
"				return d[1]\n"+
"			}).interpolate(\"cardinal\").tension(.0);\n"+
"\n"+
"			var links = [], links_dead = [], arcLines = [], arclines_dead;\n"+
"\n"+
"			//Setup zoom behavior\n"+
"			var zoom = d3.behavior.zoom(true).translate(\n"+
"					[ width / 2, height / 2 ]).scale(proj.scale()).scaleExtent(\n"+
"					[ 200, 1200 ]).on(\"zoom\", move);\n"+
"\n"+
"			var svg = d3.select(\"#safeplnmap\").append(\"svg\").attr(\"width\",\n"+
"					width).attr(\"height\", height).on(\"mousedown\", mousedown)\n"+
"					.call(zoom);\n"+
"\n"+
"			queue().defer(d3.json, \"world-110m.json\").defer(d3.json,\n"+
"					\"places.json\").await(ready);\n"+
"\n"+
"			jump();\n"+
"\n"+
"			function ready(error, world, places) {\n"+
"				var ocean_fill = svg.append(\"defs\").append(\"radialGradient\")\n"+
"						.attr(\"id\", \"ocean_fill\").attr(\"cx\", \"75%\").attr(\"cy\",\n"+
"								\"25%\");\n"+
"				ocean_fill.append(\"stop\").attr(\"offset\", \"5%\").attr(\n"+
"						\"stop-color\", \"#fff\");\n"+
"				ocean_fill.append(\"stop\").attr(\"offset\", \"100%\").attr(\n"+
"						\"stop-color\", \"#ababab\");\n"+
"\n"+
"				var globe_highlight = svg.append(\"defs\").append(\n"+
"						\"radialGradient\").attr(\"id\", \"globe_highlight\").attr(\n"+
"						\"cx\", \"75%\").attr(\"cy\", \"25%\");\n"+
"				globe_highlight.append(\"stop\").attr(\"offset\", \"5%\").attr(\n"+
"						\"stop-color\", \"#ffd\").attr(\"stop-opacity\", \"0.6\");\n"+
"				globe_highlight.append(\"stop\").attr(\"offset\", \"100%\").attr(\n"+
"						\"stop-color\", \"#ba9\").attr(\"stop-opacity\", \"0.2\");\n"+
"\n"+
"				var globe_shading = svg.append(\"defs\").append(\"radialGradient\")\n"+
"						.attr(\"id\", \"globe_shading\").attr(\"cx\", \"55%\").attr(\n"+
"								\"cy\", \"45%\");\n"+
"				globe_shading.append(\"stop\").attr(\"offset\", \"30%\").attr(\n"+
"						\"stop-color\", \"#fff\").attr(\"stop-opacity\", \"0\")\n"+
"				globe_shading.append(\"stop\").attr(\"offset\", \"100%\").attr(\n"+
"						\"stop-color\", \"#505962\").attr(\"stop-opacity\", \"0.3\")\n"+
"\n"+
"				var drop_shadow = svg.append(\"defs\").append(\"radialGradient\")\n"+
"						.attr(\"id\", \"drop_shadow\").attr(\"cx\", \"50%\").attr(\"cy\",\n"+
"								\"50%\");\n"+
"				drop_shadow.append(\"stop\").attr(\"offset\", \"20%\").attr(\n"+
"						\"stop-color\", \"#000\").attr(\"stop-opacity\", \".5\")\n"+
"				drop_shadow.append(\"stop\").attr(\"offset\", \"100%\").attr(\n"+
"						\"stop-color\", \"#000\").attr(\"stop-opacity\", \"0\")\n"+
"\n"+
//"				svg.append(\"ellipse\").attr(\"cx\", 440).attr(\"cy\", 450).attr(\n"+
//"						\"rx\", proj.scale() * .90)\n"+
//"						.attr(\"ry\", proj.scale() * .25).attr(\"class\",\n"+
//"								\"noclicks\").style(\"fill\", \"url(#drop_shadow)\");\n"+
"\n"+
"				svg.append(\"circle\").attr(\"cx\", width / 2).attr(\"cy\",\n"+
"						height / 2).attr(\"r\", proj.scale()).attr(\"class\",\n"+
"						\"noclicks\").style(\"fill\", \"url(#ocean_fill)\");\n"+
"\n"+
"				svg.append(\"path\").datum(\n"+
"						topojson.object(world, world.objects.land)).attr(\n"+
"						\"class\", \"land noclicks\").attr(\"d\", path);\n"+
"\n"+
"				svg.append(\"circle\").attr(\"cx\", width / 2).attr(\"cy\",\n"+
"						height / 2).attr(\"r\", proj.scale()).attr(\"class\",\n"+
"						\"noclicks\").style(\"fill\", \"url(#globe_highlight)\");\n"+
"\n"+
"				svg.append(\"circle\").attr(\"cx\", width / 2).attr(\"cy\",\n"+
"						height / 2).attr(\"r\", proj.scale()).attr(\"class\",\n"+
"						\"noclicks\").style(\"fill\", \"url(#globe_shading)\");\n"+
"\n"+
"				svg.append(\"g\").attr(\"class\", \"points\").selectAll(\"text\").data(\n"+
"						places.features).enter().append(\"path\").attr(\"class\",\n"+
"						\"point\").attr(\"d\", path);\n"+
"\n"+
"				svg.append(\"g\").attr(\"class\", \"labels\").selectAll(\"text\").data(\n"+
"						places.features).enter().append(\"text\").attr(\"class\",\n"+
"						\"label\").text(function(d) {\n"+
"					return d.properties.name\n"+
"				})\n"+
"\n"+
"				// spawn links between cities as source/target coord pairs\n"+
"				places.features\n"+
"						.forEach(function(a) {\n"+
"							places.features\n"+
"									.forEach(function(b) {\n"+
"										if (a !== b) {\n"+
"											if ((a.properties.name != \"Montreal\" && b.properties.name != \"Montreal\")) {\n"+
"												links\n"+
"														.push({\n"+
"															source : a.geometry.coordinates,\n"+
"															target : b.geometry.coordinates,\n"+
"															sourceName : a.properties.name,\n"+
"															targetName : b.properties.name,\n"+
"															down : false\n"+
"														});\n"+
"											} else {\n"+
"												links_dead\n"+
"														.push({\n"+
"															source : a.geometry.coordinates,\n"+
"															target : b.geometry.coordinates,\n"+
"															sourceName : a.properties.name,\n"+
"															targetName : b.properties.name,\n"+
"															down : true\n"+
"														});\n"+
"											}\n"+
"										}\n"+
"									});\n"+
"						});\n"+
"\n"+
"				// build geoJSON features from links array\n"+
"				links.forEach(function(e, i, a) {\n"+
"					var feature = {\n"+
"						\"type\" : \"Feature\",\n"+
"						\"geometry\" : {\n"+
"							\"type\" : \"LineString\",\n"+
"							\"coordinates\" : [ e.source, e.target ]\n"+
"						}\n"+
"					}\n"+
"					arcLines.push(feature)\n"+
"				})\n"+
"\n"+
"				// build geoJSON features from links array\n"+
"				links_dead.forEach(function(e, i, a) {\n"+
"					var feature = {\n"+
"						\"type\" : \"Feature\",\n"+
"						\"geometry\" : {\n"+
"							\"type\" : \"LineString\",\n"+
"							\"coordinates\" : [ e.source, e.target ]\n"+
"						}\n"+
"					}\n"+
"					//arcLines.push(feature)\n"+
"				})\n"+
"\n"+
"				svg.append(\"g\").attr(\"class\", \"arcs\").selectAll(\"path\").data(\n"+
"						arcLines).enter().append(\"path\").attr(\"class\", \"arc\")\n"+
"						.attr(\"d\", path)\n"+
"\n"+
"				svg.append(\"g\").attr(\"class\", \"flyers\").selectAll(\"path\").data(\n"+
"						links).enter().append(\"path\").attr(\"class\", \"flyer\")\n"+
"						.attr(\"d\", function(d) {\n"+
"							return swoosh(flying_arc(d))\n"+
"						}).on('mouseover', tip.show).on('mouseout', tip.hide)\n"+
"\n"+
"				svg.append(\"g\").attr(\"class\", \"flyers\").selectAll(\"path\").data(\n"+
"						links_dead).enter().append(\"path\").attr(\"class\",\n"+
"						\"flyer_dead\").attr(\"d\", function(d) {\n"+
"					return swoosh(flying_arc(d))\n"+
"				}).on('mouseover', tip.show).on('mouseout', tip.hide)\n"+
"\n"+
"				svg.call(tip);\n"+
"				refresh();\n"+
"			}\n"+
"\n"+
"			function flying_arc(pts) {\n"+
"				var source = pts.source, target = pts.target;\n"+
"				var sourceName = pts.sourceName, targetName = pts.targetName;\n"+
"				var down = pts.down;\n"+
"\n"+
"				var mid = location_along_arc(source, target, .5);\n"+
"				var result = [ proj(source), sky(mid), proj(target) ]\n"+
"				return result;\n"+
"			}\n"+
"\n"+
"			function refresh() {\n"+
"\n"+
"				svg.selectAll(\".land\").attr(\"d\", path);\n"+
"				svg.selectAll(\".point\").attr(\"d\", path);\n"+
"				//svg.selectAll(\".circle\").attr(\"r\", proj.scale());\n"+
"				//svg.selectAll(.\"ellipse\").attr(\"r\", proj.scale());\n"+
"\n"+
"				svg.selectAll(\".arc\").attr(\"d\", path).attr(\"opacity\",\n"+
"						function(d) {\n"+
"							return fade_at_edge(d)\n"+
"						})\n"+
"\n"+
"				svg.selectAll(\".flyer\").attr(\"d\", function(d) {\n"+
"					return swoosh(flying_arc(d))\n"+
"				}).attr(\"opacity\", function(d) {\n"+
"					return fade_at_edge(d)\n"+
"				})\n"+
"\n"+
"				svg.selectAll(\".flyer_dead\").attr(\"d\", function(d) {\n"+
"					return swoosh(flying_arc(d))\n"+
"				}).attr(\"opacity\", function(d) {\n"+
"					return fade_at_edge(d)\n"+
"				})\n"+
"				position_labels();\n"+
"			}\n"+
"\n"+
"			function position_labels() {\n"+
"				var centerPos = proj.invert([ width / 2, height / 2 ]);\n"+
"\n"+
"				var arc = d3.geo.greatArc();\n"+
"\n"+
"				svg\n"+
"						.selectAll(\".label\")\n"+
"						.attr(\n"+
"								\"text-anchor\",\n"+
"								function(d) {\n"+
"									var x = proj(d.geometry.coordinates)[0];\n"+
"									return x < width / 2 - 20 ? \"end\"\n"+
"											: x < width / 2 + 20 ? \"middle\"\n"+
"													: \"start\"\n"+
"								})\n"+
"						.attr(\n"+
"								\"transform\",\n"+
"								function(d) {\n"+
"									var loc = proj(d.geometry.coordinates), x = loc[0], y = loc[1];\n"+
"									var offset = x < width / 2 ? -5 : 5;\n"+
"									return \"translate(\" + (x + offset) + \",\"\n"+
"											+ (y - 2) + \")\"\n"+
"								}).style(\"display\", function(d) {\n"+
"							var d = arc.distance({\n"+
"								source : d.geometry.coordinates,\n"+
"								target : centerPos\n"+
"							});\n"+
"							return (d > 1.57) ? 'none' : 'inline';\n"+
"						})\n"+
"\n"+
"			}\n"+
"\n"+
"			function fade_at_edge(d) {\n"+
"				var centerPos = proj.invert([ width / 2, height / 2 ]), arc = d3.geo\n"+
"						.greatArc(), start, end;\n"+
"				// function is called on 2 different data structures..\n"+
"				if (d.source) {\n"+
"					start = d.source, end = d.target;\n"+
"				} else {\n"+
"					start = d.geometry.coordinates[0];\n"+
"					end = d.geometry.coordinates[1];\n"+
"				}\n"+
"\n"+
"				var start_dist = 1.57 - arc.distance({\n"+
"					source : start,\n"+
"					target : centerPos\n"+
"				}), end_dist = 1.57 - arc.distance({\n"+
"					source : end,\n"+
"					target : centerPos\n"+
"				});\n"+
"\n"+
"				var fade = d3.scale.linear().domain([ -.1, 0 ])\n"+
"						.range([ 0, .1 ])\n"+
"				var dist = start_dist < end_dist ? start_dist : end_dist;\n"+
"\n"+
"				return fade(dist)\n"+
"			}\n"+
"\n"+
"			function location_along_arc(start, end, loc) {\n"+
"				var interpolator = d3.geo.interpolate(start, end);\n"+
"				return interpolator(loc)\n"+
"			}\n"+
"\n"+
"			// modified from http://bl.ocks.org/1392560\n"+
"			var m0, o0;\n"+
"			function mousedown() {\n"+
"				m0 = [ d3.event.pageX, d3.event.pageY ];\n"+
"				o0 = proj.rotate();\n"+
"				d3.event.preventDefault();\n"+
"			}\n"+
"			function mousemove() {\n"+
"				if (m0) {\n"+
"					var m1 = [ d3.event.pageX, d3.event.pageY ], o1 = [\n"+
"							o0[0] + (m1[0] - m0[0]) / 6,\n"+
"							o0[1] + (m0[1] - m1[1]) / 6 ];\n"+
"					o1[1] = o1[1] > 50 ? 50 : o1[1] < -50 ? -50 : o1[1];\n"+
"					proj.rotate(o1);\n"+
"					sky.rotate(o1);\n"+
"					refresh();\n"+
"				}\n"+
"			}\n"+
"			function mouseup() {\n"+
"				if (m0) {\n"+
"					mousemove();\n"+
"					m0 = null;\n"+
"				}\n"+
"			}\n"+
"\n"+
"			function move() {\n"+
"				if (d3.event) {\n"+
"					var scale = d3.event.scale;\n"+
"					var origin = [ d3.event.translate[0] * -1,\n"+
"							d3.event.translate[1] ];\n"+
"\n"+
"					proj.scale(scale);\n"+
"					sky.scale(scale * 1.05);\n"+
"					//backgroundCircle.attr('r', scale);\n"+
"					//path.pointRadius(2 * scale / scale0);\n"+
"					svg.selectAll(\"circle\").attr(\"r\", scale);\n"+
"					svg.selectAll(\"ellipse\").attr(\"r\", scale);\n"+
"\n"+
"					refresh();\n"+
"				}\n"+
"			}\n"+
"\n"+
"			function jump() {\n"+
"\n"+
"				svg.call(zoom.scale(200).event);\n"+
"				svg.transition().duration(5000).call(\n"+
"						zoom.scale(900).scaleExtent([ 200, 1200 ]).event);\n"+
"			}\n"+
"\n"+
"		}\n").setEscapeModelStrings(false));
		

	}

}