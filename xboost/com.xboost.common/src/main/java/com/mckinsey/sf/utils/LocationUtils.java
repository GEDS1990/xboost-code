package com.mckinsey.sf.utils;

public class LocationUtils {
    private static double EARTH_RADIUS = 6378.137;  
  
    private static double rad(double d) {  
        return d * Math.PI / 180.0;  
    }  
  
    /** 
     * 通过经纬度获取距离(单位：米) 
     * @param lat1 
     * @param lng1 
     * @param lat2 
     * @param lng2 
     * @return 
     */  
    public static double getDistance(double lat1, double lng1, double lat2,  
                                     double lng2) {  
        double radLat1 = rad(lat1);  
        double radLat2 = rad(lat2);  
        double a = radLat1 - radLat2;  
        double b = rad(lng1) - rad(lng2);  
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  
                + Math.cos(radLat1) * Math.cos(radLat2)  
                * Math.pow(Math.sin(b / 2), 2)));  
        s = s * EARTH_RADIUS;  
        s = Math.round(s * 10000d) / 10000d;  
        s = s*1000;  
        return s;  
    }  
    
    /*public static void main(String[] args){
    	ArrayList<Location> locs = new ArrayList<Location>();
    	
    	String splitBy = ",";
        String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/com/alivia/location/69网点和经纬度.csv"));
			line = br.readLine();
			while((line = br.readLine()) !=null){
				String[] row = line.split(splitBy);
				String loc = row[0];
				String lonStr = row[1];
				String latStr = row[2];
				
				double lon = Double.parseDouble(lonStr);
				double lat = Double.parseDouble(latStr);
				
				Location location = new Location(loc,lon,lat);
				locs.add(location);
				
			}
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String fileName = "src/main/resources/com/alivia/location/distance.csv";
		
		OutputStream out = null;
		@SuppressWarnings("resource")
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("dist");
		int count = 0;
		try {
			Row row = sheet.createRow(count++);
			row.createCell(0).setCellValue("from");
			row.createCell(1).setCellValue("to");
			row.createCell(2).setCellValue("dist");
			row.createCell(3).setCellValue("time");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		for(int i=0;i<locs.size();i++){
			System.out.print(i);
			Location loc1 = locs.get(i);
			for(int j=0; j<locs.size();j++){
				
				Location loc2 = locs.get(j);
				
				double dist = getDistance(loc1.getLat(),loc1.getLon(),loc2.getLat(),loc2.getLon());
				Row row = sheet.createRow(count++);
				row.createCell(0).setCellValue(loc1.getLoc());
				row.createCell(1).setCellValue(loc2.getLoc());
				row.createCell(2).setCellValue(dist);
				row.createCell(3).setCellValue(dist);
			}
		}
		try {
			out = new FileOutputStream(fileName);
			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
    }*/
}  