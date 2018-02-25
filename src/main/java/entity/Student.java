package entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Student {

	private String id;
	private String name;
	private Date birthday;
	private String description;
	private Integer avgscore;
	public Student() {
		super();
	}
	public Student(String name, Date birthday, String description, Integer avgscore) {
		super();
		this.name = name;
		this.birthday = birthday;
		this.description = description;
		this.avgscore = avgscore;
	}
	public Student(String id, String name, Date birthday, String description, Integer avgscore) {
		super();
		this.id = id;
		this.name = name;
		this.birthday = birthday;
		this.description = description;
		this.avgscore = avgscore;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirthday() {
		return birthday;
	}
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getAvgscore() {
		return avgscore;
	}
	public void setAvgscore(Integer avgscore) {
		this.avgscore = avgscore;
	}
	
}
