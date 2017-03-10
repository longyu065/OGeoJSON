package com.oyou.gis;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;



/**
 * @author tkrui
 *
 */
public class GeoJSONConverUtil {
	
	public static void main(String[] args) {
	//	String geometry=getFeatureJSON("116.364433,39.9678116;116.361763,39.9677925;", "Point",null);
		String geometry2=getFeatureJSON("116.364433,39.9678116;116.361763,39.9677925", "LineString");
//		String geometry2=getFeatureJSON("116.364733,39.9678116;", "MultiPoint",null);
//		String geometry3=getFeatureJSON("116. 3,39.9;116.4,39.9;116.4,39.7;116.3,39.7;116.3,39.9&116.31,39.89;116.37,39.89;116.37,39.76;116.31,39.76;116.31,39.89", "Polygon",null);
		String geometry4=getFeatureJSON("116.3,39.9;116.4,39.9;116.4,39.7;116.3,39.7;116.3,39.9", "MultiLineString",null);
		String geometry5=getFeatureJSON("116.3,39.9;116.4,39.9;116.4,39.7;116.3,39.7;116.3,39.9", "Polygon",null);
		
		List list=new ArrayList<Object>();

		list.add(geometry5);
		String feacolle=getFeatureCollectionJSON("dd",list);
		System.out.println(feacolle);
	}
	/**
	 * @param coordinates 坐标串 116.364433,39.9678116;116.361763,39.9677925;
	 * @param type type 坐标类型 支持Polygon;Point;MultiPointLineString;
	 * <p><span>Point</span>116.364433,39.9678116;</p>
	 * <p><span>MutilPoint</span>116.361763,39.9677925;</p>
	 * <p><span>LineString</span>116.364433,39.9678116;116.361763,39.9677925;</p>
	 * <p><span>MutilLineString</span>116.3,39.9;116.4,39.9;116.4,39.7;116.3,39.7;116.3,39.9&116.31,39.89;116.37,39.89;116.37,39.76;116.31,39.76;116.31,39.89</p>
	 * <p><span>Polygon</span>
	 * 116.3,39.9;116.4,39.9;116.4,39.7;116.3,39.7;116.3,39.9&116.31,39.89;116.37,39.89;116.37,39.76;116.31,39.76;116.31,39.89
	 * <br>& 前为外环后其余皆为内环空
	 * </p>
	 * @return  返回GeoJSON 字符串 形式 {"geometry":{"coordinates":[[116.364433,39.9678116],[116.361763,39.9677925]],"type":"LineString"},"type":"Feature"}
	 */
	public static String getFeatureJSON(String coordinates, String type){
		return getFeatureJSON(coordinates,type,null);
	}
	/**
	 * @param coordinates 坐标串 116.364433,39.9678116;116.361763,39.9677925;
	 * @param type type 坐标类型 支持Polygon;Point;MultiPointLineString;
	 * <p><span>Point</span>116.364433,39.9678116;</p>
	 * <p><span>MutilPoint</span>116.361763,39.9677925;</p>
	 * <p><span>LineString</span>116.364433,39.9678116;116.361763,39.9677925;</p>
	 * <p><span>MutilLineString</span>116.3,39.9;116.4,39.9;116.4,39.7;116.3,39.7;116.3,39.9&116.31,39.89;116.37,39.89;116.37,39.76;116.31,39.76;116.31,39.89</p>
	 * <p><span>Polygon</span>
	 * 116.3,39.9;116.4,39.9;116.4,39.7;116.3,39.7;116.3,39.9&116.31,39.89;116.37,39.89;116.37,39.76;116.31,39.76;116.31,39.89
	 * <br>& 前为外环后其余皆为内环空
	 * </p>
	 * @param properties map类型
	 * @return  返回GeoJSON 字符串 形式 {"geometry":{"coordinates":[[116.364433,39.9678116],[116.361763,39.9677925]],"type":"LineString"},"type":"Feature"}
	 */
	public static String getFeatureJSON(String coordinates, String type,Map<String, Object> properties) {
		
		if (coordinates==null||coordinates.equals("")||type==null||type.equals("")) {
			throw new IllegalArgumentException("argument must have  a value");
		}
		
		JSONObject jsonObject=new JSONObject(true);
		JSONObject geometryJsonObject=new JSONObject(true);

		if (type.equals("Point")||type.equals("MultiPoint")) {
			
			if (coordinates.split("&").length>1) {
				throw  new IllegalArgumentException("coordinates is not a Point or MultiPoint array string;");
			}
			
			String[] positions=coordinates.replaceAll("&","").split(";");
			//判断coordinates 含有多个点自动转换为MultiPoint
			if (positions.length>1) {
				JSONArray multiPointArray=new JSONArray();
				type="MultiPoint";
				System.err.println("Warnning:The coordinates contains multiple coordinate,convert type to MultiPoint");
				for (String position : positions) {
					multiPointArray.add(converCoordinateTOJSONArray(position));
				}
				geometryJsonObject.put("coordinates",multiPointArray);
			}else {
				type="Point";
				JSONArray pointArray=converCoordinateTOJSONArray(positions[0]);
				geometryJsonObject.put("coordinates",pointArray);
				System.err.println("Warnning:The coordinates contains one position,convert type to Point");
			}
		}
		else if (type.equals("LineString")) {//For type "LineString", the "coordinates" member must be an array of two or more positions.

			if (coordinates.split("&").length>1||coordinates.replaceAll("&","").split(";").length<2) {
				throw  new IllegalArgumentException("coordinates is not a LineString type array string;");
			}
			
			String[] positions=coordinates.replaceAll("&","").split(";");
			JSONArray lineArray=new JSONArray();
			for (String position : positions) {
				lineArray.add(converCoordinateTOJSONArray(position));
			}
			
			geometryJsonObject.put("coordinates",lineArray);
		}else if (type.equals("MultiLineString")) {
			
			String[] coordinate=coordinates.split("&");
			
			JSONArray multiLineArray=new JSONArray();
			
			for (String coordi : coordinate) {
				JSONArray	lineArrayArray=new JSONArray();
				String [] positions=coordi.toString().split(";");
				if(positions.length==1){
					throw  new IllegalArgumentException("coordinates is not a MultiLineString type array string;");
				}
				for (String position : positions) {
					lineArrayArray.add(converCoordinateTOJSONArray(position));
				}
				multiLineArray.add(lineArrayArray);		
			}
			
			geometryJsonObject.put("coordinates",multiLineArray);
		}else if (type.equals("Polygon")||type.equals("MultiPolygon")) {
			String[] multiPolygonCoordinate=coordinates.split("#");
			
			if (multiPolygonCoordinate.length>1) {
				type="MultiPolygon";
				JSONArray multiPolygonArray=new JSONArray();
				for (String polygoncoordi : multiPolygonCoordinate) {
					multiPolygonArray.add((JSON.parseObject(GeoJSONConverUtil.getFeatureJSON(polygoncoordi, "Polygon")).getJSONObject("geometry")).getJSONArray("coordinates"));
				}
				geometryJsonObject.put("coordinates",multiPolygonArray);
			}else {
				type="Polygon";
				String[] coordinate=coordinates.replace("#", "").split("&");
				JSONArray polygonArray=new JSONArray();
				for (String coordi : coordinate) {
					if (coordi==null||coordi.equals("")) {
						continue;
					}
					JSONArray	positionsArray=new JSONArray();
					String [] positions=coordi.toString().split(";");
				
					if(positions.length<1){
						continue;
					}
					for (String position : positions) {
						positionsArray.add(converCoordinateTOJSONArray(position));
					}
					polygonArray.add(positionsArray);		
				}
				geometryJsonObject.put("coordinates",polygonArray);
			}
			
			
		}else {
			throw new IllegalArgumentException("type is not correct");
		}
		geometryJsonObject.put("type",type);
		jsonObject.put("type", "Feature");
		

		if (properties!=null) {
			jsonObject.put("properties",new JSONObject(properties));
		}
		jsonObject.put("geometry", geometryJsonObject);
		return jsonObject.toJSONString();
	}
	/**
	 * @param features 单个geojson类型String的集合
	 * @return featureCollection
	 */
	public static String getFeatureCollectionJSON(String name,List<String> features){
		if (features==null||features.size()==0) {
			throw new IllegalArgumentException("list must contain a value");
		}
		JSONObject geoJSON=new JSONObject(true);
		geoJSON.put("name", name==null?"":name);
		geoJSON.put("type", "FeatureCollection");
		JSONArray featuresArray=new JSONArray();
		for (String feature : features) {
			try {
				featuresArray.add(JSON.parse(feature));
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException("list must contain a json value");
			}
		}
		geoJSON.put("features", featuresArray);
		return geoJSON.toJSONString();
	}
	/**
	 * @param coordinate
	 * @param type
	 * @return
	 */
	public static String getFeatureCollectionJSON(String name,List<String> coordinates, List<String> types){
		return getFeatureCollectionJSON(name,coordinates, types,null);
	}
	/**
	 * @param coordinate  坐标串 116.364433,39.9678116;116.361763,39.9677925;
	 * @param type
	 * @param properties
	 * @return
	 */
	public static String getFeatureCollectionJSON(String name,List<String> coordinates, List<String> types,List<Map<String, Object>> properties){
		
		if (coordinates==null||coordinates.size()==0||types==null||types.size()==0) {
			throw new IllegalArgumentException("list must contain a value");
		}
		else if(coordinates.size()!=types.size()||(properties!=null&&coordinates.size()!=types.size())) {
			throw new IllegalArgumentException("coordinate and type must have the same size");
		}
		
		JSONObject geoJSON=new JSONObject(true);
		geoJSON.put("name", name==null?"":name);
		geoJSON.put("type", "FeatureCollection");
		JSONArray featuresArray=new JSONArray();
		for (int i = 0; i < coordinates.size(); i++) {
			try {
				featuresArray.add(JSON.parse(getFeatureJSON(coordinates.get(i), types.get(i), properties==null?null:properties.get(i))));
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException("list must contain a json value");
			}
		}
		geoJSON.put("features", featuresArray);
		return geoJSON.toJSONString();
	}

	/**
	 * @param position  the default regex is dot 
	 * 		<br> example: 116.364433,39.9678116
	 * @return JSONArray
	 */
	private static JSONArray converCoordinateTOJSONArray(String position){
		return converCoordinateTOJSONArray(position,null);
	}
	/**
	 * @param position  
	 * @return JSONArray
	 */
	/**
	 * @param position  example: 116.364433,39.9678116
	 * @param regex the default regex is dot <i>","</i>
	 * @return
	 */
	private static JSONArray converCoordinateTOJSONArray(String position,String regex){
	
		if (position==null) {
			throw new NullPointerException("position is null"); 
		}
		if (regex==null) {
			regex=",";
		}
		String [] co=position.split(regex);
		if (co.length!=2) {
			throw new IllegalArgumentException("cant't parse parse lng,lat:"+position);
		}
		
		
		
		JSONArray resultArray=new JSONArray();
		try {
			BigDecimal bigDecimal0=new BigDecimal(co[0]);
			BigDecimal bigDecimal1=new BigDecimal(co[1]);

			resultArray.add(bigDecimal0);
			resultArray.add(bigDecimal1);
			
		} catch (NumberFormatException e) {
			throw new NumberFormatException("cant't parse lng,lat:"+position);
		}
		
		return resultArray;
	}
}
