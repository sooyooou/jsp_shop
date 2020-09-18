package shop.customer;

import java.sql.*;

public class CustomerDataBean {
	
	private String id;
	private String passwd;
	private String name;
	private Timestamp reg_data;
	private String tel;
	private String address;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getReg_data() {
		return reg_data;
	}
	public void setReg_data(Timestamp reg_data) {
		this.reg_data = reg_data;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
