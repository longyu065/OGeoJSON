package com.oyou;


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

	/**
	 * @param coordinate 坐标串 116.364433,39.9678116;116.361763,39.9677925;
	 * @param type 坐标类型 支持Polygon;Point;LineString;
	 * @return 返回GeoJSON 字符串 形式 {"geometry":{"coordinates":[[116.364433,39.9678116],[116.361763,39.9677925]],"type":"LineString"},"type":"Feature"}
	 */
	public static String getFeatureJSON(String coordinate, String type){
		return getFeatureJSON(coordinate,type,null);
	}
	/**
	 * @param coordinate 坐标串 116.364433,39.9678116;116.361763,39.9677925;
	 * @param type type 坐标类型 支持Polygon;Point;LineString;
	 * @param properties
	 * @return  返回GeoJSON 字符串 形式 {"geometry":{"coordinates":[[116.364433,39.9678116],[116.361763,39.9677925]],"type":"LineString"},"type":"Feature"}
	 */
	public static String getFeatureJSON(String coordinate, String type,Map<String, Object> properties) {
		if (coordinate==null||coordinate.equals("")||type==null||type.equals("")) {
			throw new IllegalArgumentException("argument must have  a value");
		}
		String[] coordinates=coordinate.split(";");
		JSONObject jsonObject=new JSONObject(true);
		JSONObject geometryJsonObject=new JSONObject(true);
		geometryJsonObject.put("type",type);
		
		StringBuilder pgString=new StringBuilder("");
		if (type.equals("Polygon")) {
			pgString.append("[[");
			for (String string : coordinates) {
				pgString.append("["+string+"],");
			}
			geometryJsonObject.put("coordinates",JSON.parseArray(pgString.substring(0,pgString.length()-1)+"]]"));
		}
		else if (type.equals("Point")) {
			for (String string : coordinates) {
				pgString.append("["+string+"],");
			}
			geometryJsonObject.put("coordinates",JSON.parseArray(pgString.substring(0,pgString.length()-1)+"]]"));
		}
		else if (type.equals("LineString")) {
			pgString.append("[");
			for (String string : coordinates) {
				pgString.append("["+string+"],");
			}
			geometryJsonObject.put("coordinates",JSON.parseArray(pgString.substring(0,pgString.length()-1)+"]"));
		}else {
			throw new IllegalArgumentException("type is not correct");
		}
		
		jsonObject.put("type", "Feature");
		jsonObject.put("geometry", geometryJsonObject);

		if (properties!=null) {
			jsonObject.put("properties",new JSONObject(properties));
		}
		return jsonObject.toJSONString();
	}
	/**
	 * @param features 单个geojson类型String的集合
	 * @return featureCollection
	 */
	public static String getFeatureCollectionJSON(List<String> features){
		if (features==null||features.size()==0) {
			throw new IllegalArgumentException("list must contain a value");
		}
		JSONObject geoJSON=new JSONObject(true);
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
	public static String getFeatureCollectionJSON(List<String> coordinates, List<String> types){
		return getFeatureCollectionJSON(coordinates, types,null);
	}
	/**
	 * @param coordinate  坐标串 116.364433,39.9678116;116.361763,39.9677925;
	 * @param type
	 * @param properties
	 * @return
	 */
	public static String getFeatureCollectionJSON(List<String> coordinates, List<String> types,List<Map<String, Object>> properties){
		
		if (coordinates==null||coordinates.size()==0||types==null||types.size()==0) {
			throw new IllegalArgumentException("list must contain a value");
		}
		else if(coordinates.size()!=types.size()||(properties!=null&&coordinates.size()!=types.size())) {
			throw new IllegalArgumentException("coordinate and type must have the same size");
		}
		
		JSONObject geoJSON=new JSONObject();
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
	public static void main(String[] args) {
		String geometry=getFeatureJSON("116.364433,39.9678116;116.361763,39.9677925;", "LineString",null);
		List list=new ArrayList<Object>();
		list.add(geometry);
		list.add(geometry);
		String feacolle=getFeatureCollectionJSON(list);
		
	}
}
