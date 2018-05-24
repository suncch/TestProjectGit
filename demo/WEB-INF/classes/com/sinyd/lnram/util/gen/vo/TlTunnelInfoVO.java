package com.sinyd.lnram.util.gen.vo;

import java.io.Serializable;

/*
 * @version 2.0
 */

public class TlTunnelInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
  	private String id;
  		
  	private String tunnel_name;
  		
  	private String tunnel_code;
  		
  	private String construction_company;
  		
  	private String tunnel_length;
  		
  	private String tunnel_height;
  		
  	private String traffic_time;
  		
  	private String tunnel_direction;
  		
  	private java.math.BigDecimal longitude;
  		
  	private java.math.BigDecimal latitude;
  		
  	private String office_code;
  		
  	private String create_timestamp;
  		
    
	public String getId () {
		return id;
	}
    public void setId ( String obj ) {
		id = obj;
	}
	public String getTunnel_name () {
		return tunnel_name;
	}
    public void setTunnel_name ( String obj ) {
		tunnel_name = obj;
	}
	public String getTunnel_code () {
		return tunnel_code;
	}
    public void setTunnel_code ( String obj ) {
		tunnel_code = obj;
	}
	public String getConstruction_company () {
		return construction_company;
	}
    public void setConstruction_company ( String obj ) {
		construction_company = obj;
	}
	public String getTunnel_length () {
		return tunnel_length;
	}
    public void setTunnel_length ( String obj ) {
		tunnel_length = obj;
	}
	public String getTunnel_height () {
		return tunnel_height;
	}
    public void setTunnel_height ( String obj ) {
		tunnel_height = obj;
	}
    public String getTraffic_time () {
		return traffic_time;
	}
	public void setTraffic_time ( String obj ) {
		traffic_time = obj;
	}
	public String getTunnel_direction () {
		return tunnel_direction;
	}
    public void setTunnel_direction ( String obj ) {
		tunnel_direction = obj;
	}
	public java.math.BigDecimal getLongitude () {
		return longitude;
	}
    public void setLongitude ( java.math.BigDecimal obj ) {
		longitude = obj;
	}
	public java.math.BigDecimal getLatitude () {
		return latitude;
	}
    public void setLatitude ( java.math.BigDecimal obj ) {
		latitude = obj;
	}
	public String getOffice_code () {
		return office_code;
	}
    public void setOffice_code ( String obj ) {
		office_code = obj;
	}
    public String getCreate_timestamp () {
		return create_timestamp;
	}
	public void setCreate_timestamp ( String obj ) {
		create_timestamp = obj;
	}
}