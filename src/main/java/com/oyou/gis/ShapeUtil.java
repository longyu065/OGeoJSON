package com.oyou.gis;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.oyou.util.FileReaderWriter;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class ShapeUtil {

	/**
	 * @param filePath
	 */
	public void readShapeToGeoJSON(String srcfilePath,String destFilaPath,String destName) {
		if (srcfilePath == null || srcfilePath.lastIndexOf(".shp") < 0) {
			throw new IllegalArgumentException(
					"filePath argument is null or invaild");
		}
		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
		try {
			ShapefileDataStore sds = (ShapefileDataStore) dataStoreFactory
					.createDataStore(new File(srcfilePath).toURI().toURL());
			sds.setCharset(Charset.forName("UTF-8"));
			SimpleFeatureSource featureSource = sds.getFeatureSource();
			SimpleFeatureIterator itertor = featureSource.getFeatures()
					.features();
			
			List<String> c = new ArrayList<String>();
			List<Map<String, Object>> properties = new ArrayList<Map<String, Object>>();
			List<String> types = new ArrayList<String>();
			
			while (itertor.hasNext()) {

				SimpleFeature feature = itertor.next();
				
				Iterator<Property> it = feature.getProperties().iterator();
				
				Map<String, Object> proerties = new LinkedHashMap<String, Object>();
				
			
				while (it.hasNext()) {
					Property pro = it.next();
					if (pro.getValue() instanceof MultiPolygon) {
						types.add("MultiPolygon");
						String polygonString = pro.getValue().toString();
						polygonString = polygonString
								.substring(polygonString.indexOf("("),
										polygonString.lastIndexOf(")") + 1).replace(")), ((", "#").replace("), (", "&").replace("(", "")
								.replace(")", "").replace(" ", ",")
								.replace(",,", ";");
						c.add(polygonString);
					}else if (pro.getValue() instanceof Point) {
						types.add("Point");
					String	pointString = pro.getValue().toString();
					pointString = pointString
								.substring(pointString.indexOf("("),
										pointString.lastIndexOf(")") + 1).replace(")), ((", "#").replace("), (", "&").replace("(", "")
								.replace(")", "").replace(" ", ",")
								.replace(",,", ";");
						c.add(pointString);
					}else if (pro.getValue() instanceof MultiPoint) {
						types.add("MultiPoint");
						String multiPoint = pro.getValue().toString();
						multiPoint = multiPoint
								.substring(multiPoint.indexOf("("),
										multiPoint.lastIndexOf(")") + 1).replace(")), ((", "#").replace("), (", "&").replace("(", "")
								.replace(")", "").replace(" ", ",")
								.replace(",,", ";");
						c.add(multiPoint);
					}else if (pro.getValue() instanceof  LineString) {
						
						types.add("LineString");
						String lineString = pro.getValue().toString();
						lineString = lineString
								.substring(lineString.indexOf("("),
										lineString.lastIndexOf(")") + 1).replace(")), ((", "#").replace("), (", "&").replace("(", "")
								.replace(")", "").replace(" ", ",")
								.replace(",,", ";");
						c.add(lineString);
					}else if (pro.getValue() instanceof  MultiLineString) {
						
						types.add("MultiLineString");
						String multiLineString = pro.getValue().toString();
						multiLineString = multiLineString
								.substring(multiLineString.indexOf("("),
										multiLineString.lastIndexOf(")") + 1).replace(")), ((", "#").replace("), (", "&").replace("(", "")
								.replace(")", "").replace(" ", ",")
								.replace(",,", ";");
						c.add(multiLineString);
					}
					else{
						proerties.put(pro.getName().toString(), pro.getValue());
					}
				}
				properties.add(proerties);
			}
			itertor.close();
			String geojson = GeoJSONConverUtil.getFeatureCollectionJSON(destName,c,
					types, properties);
			System.out.println(geojson);
			FileReaderWriter.writeIntoFile(geojson, destFilaPath+"/"+destName+".geojson", false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new ShapeUtil().readShapeToGeoJSON("F:\\tianchi\\tianchi_line1.shp","F:\\tianchi","tinchi1");
	
	}

}
