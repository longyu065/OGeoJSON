第一次做开源，一个小的坐标串到GeoJSON的工具。欢迎各位提出建议，才能不断成长。  
根据GeoJSON标准规范

##示例
```
List<String> clist=new ArrayList<String>();
		clist.add("116.364433,39.9678116;116.361763,39.9677925;");
		clist.add("116.364433,39.9678116;116.361763,39.9677925;116.361763,39.9607925;116.361763,39.960925;116.364433,39.9678116;");
		List<String> tlist=new ArrayList<String>();
		tlist.add("LineString");
				tlist.add("Polygon");
		String feacolle=GeoJSONConverUtil.getFeatureCollectionJSON(clist,tlist);
``` 
##带属性数据
```
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
		

```
###返回结果
``` 
{"features":[{"geometry":{"coordinates":[[116.364433,39.9678116],[116.361763,39.9677925]],"type":"LineString"},"type":"Feature","properties":{"p1":"dd","p2":"ddd"}},{"geometry":{"coordinates":[[[116.364433,39.9678116],[116.361763,39.9677925],[116.361763,39.9607925],[116.361763,39.960925],[116.364433,39.9678116]]],"type":"Polygon"},"type":"Feature"}],"type":"FeatureCollection"}
```