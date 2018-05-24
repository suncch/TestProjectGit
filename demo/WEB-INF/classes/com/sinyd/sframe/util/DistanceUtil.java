package com.sinyd.sframe.util;

/**
 * 用来计算百度地图上两经纬度点之间的距离
 * 
 * @author Administrator
 * 
 */
public class DistanceUtil {
	/**
	 * 计算两点之间距离
	 * 
	 * @param start_lat
	 * @param start_lon
	 * @param end_lat
	 * @param end_lon
	 * @return 米
	 */
	public static double getDistance(double start_lon, double start_lat,
			double end_lon, double end_lat) {
		double lat1 = (Math.PI / 180) * start_lat;
		double lat2 = (Math.PI / 180) * end_lat;

		double lon1 = (Math.PI / 180) * start_lon;
		double lon2 = (Math.PI / 180) * end_lon;

		// double Lat1r = (Math.PI/180)*(gp1.getLatitudeE6()/1E6);
		// double Lat2r = (Math.PI/180)*(gp2.getLatitudeE6()/1E6);
		// double Lon1r = (Math.PI/180)*(gp1.getLongitudeE6()/1E6);
		// double Lon2r = (Math.PI/180)*(gp2.getLongitudeE6()/1E6);

		// 地球半径
		double R = 6371;

		// 两点间距离 km，如果想要米的话，结果*1000就可以了
		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
				* Math.cos(lat2) * Math.cos(lon2 - lon1))
				* R;
		return d * 1000;
	}

	static double DEF_PI = 3.14159265359; // PI
	static double DEF_2PI = 6.28318530712; // 2*PI
	static double DEF_PI180 = 0.01745329252; // PI/180.0
	static double DEF_R = 6370693.5; // radius of earth

	public static double getShortDistance(double lon1, double lat1,
			double lon2, double lat2) {
		double ew1, ns1, ew2, ns2;
		double dx, dy, dew;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 经度差
		dew = ew1 - ew2;
		// 若跨东经和西经180 度，进行调整
		if (dew > DEF_PI)
			dew = DEF_2PI - dew;
		else if (dew < -DEF_PI)
			dew = DEF_2PI + dew;
		dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
		dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
		// 勾股定理求斜边长
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}

	public static void main(String[] args) {
		// 测试阜新和沈阳之间的距离并与百度地图测量工具的结果对比
		double res = getDistance(121.629058, 42.063125, 123.395198, 41.84339);
		double res2 = getShortDistance(121.784357, 42.117672,
				121.776452, 42.116227);
		System.out.println(res);
		System.out.println(res2);
	}
}
