package com.oyou;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class GeoTest {

	@Test(expected=IllegalArgumentException.class)
	public void testGetFeatureJSONStringString() {
		GeoJSONConverUtil.getFeatureJSON(null, null);
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetFeatureJSONString() {
		GeoJSONConverUtil.getFeatureJSON(null,"lineString");
	}
	@Test(expected=IllegalArgumentException.class)
	public void testGetFeatureJSON() {
		System.out.println("testGetFeatureJSON:"+GeoJSONConverUtil.getFeatureJSON("116.364433,39.9678116;116.361763,39.9677925;","lineString"));
	}
	@Test
	public void testGetFeatureColletion(){
		List<String> clist=new ArrayList<String>();
		clist.add("116.364433,39.9678116;116.361763,39.9677925;");
		clist.add("116.364433,39.9678116;116.361763,39.9677925;116.361763,39.9607925;116.361763,39.960925;116.364433,39.9678116;");
		List<String> tlist=new ArrayList<String>();
		tlist.add("LineString");
		tlist.add("Polygon");
		
		String feacolle=GeoJSONConverUtil.getFeatureCollectionJSON(clist,tlist);
	}
	@Test
	public void testGetFeatureColletionWithProperties(){
		List<String> clist=new ArrayList<String>();
		clist.add("116.364433,39.9678116;116.361763,39.9677925;");
		clist.add("116.364433,39.9678116;116.361763,39.9677925;116.361763,39.9607925;116.361763,39.960925;116.364433,39.9678116;");
		List<String> tlist=new ArrayList<String>();
		tlist.add("LineString");
		tlist.add("Polygon");
		List<Map<String, Object>> plist=new ArrayList<Map<String, Object>>();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("p1", "dd");
		map.put("p2", "ddd");
		plist.add(map);
		plist.add(null);
		String feacolle=GeoJSONConverUtil.getFeatureCollectionJSON(clist,tlist,plist);
		System.out.println(feacolle);
		
	}
	
}
