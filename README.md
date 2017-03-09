第一次做开源，一个小的坐标串到GeoJSON的工具。欢迎各位提出建议，才能不断成长。  
根据GeoJSON标准规范

示例
`List<String> clist=new ArrayList<String>();
		clist.add("116.364433,39.9678116;116.361763,39.9677925;");
		clist.add("116.364433,39.9678116;116.361763,39.9677925;116.361763,39.9607925;116.361763,39.960925;116.364433,39.9678116;");
		List<String> tlist=new ArrayList<String>();
		tlist.add("LineString");
		tlist.add("Polygon");
		
		String feacolle=GeoJSONConverUtil.getFeatureCollectionJSON(clist,tlist);
`