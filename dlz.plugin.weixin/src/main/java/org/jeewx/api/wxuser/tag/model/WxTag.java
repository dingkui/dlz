package org.jeewx.api.wxuser.tag.model;

public class WxTag {
	void doNothing(){new java.util.ArrayList<>().forEach(a->{});}
	
	Integer id;
	String name;
	int count;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "WxTag [id=" + id + ", name=" + name + ", count=" + count + "]";
	}
	
}
