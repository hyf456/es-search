package com.hivescm.dto;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {

	private int id;
	private String name;
	private String city;
	private Date birth;
	private int age;

	private Computer computer;

	public Student() {
	}

	public Student(int id,String name, String city, Date birth, int age, Computer computer) {
		this.id =id;
		this.name = name;
		this.city = city;
		this.birth = birth;
		this.age = age;
		this.computer = computer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Computer getComputer() {
		return computer;
	}

	public void setComputer(Computer computer) {
		this.computer = computer;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Student{");
		sb.append("id=").append(id);
		sb.append(", name='").append(name).append('\'');
		sb.append(", city='").append(city).append('\'');
		sb.append(", birth=").append(birth);
		sb.append(", age=").append(age);
		sb.append(", computer=").append(computer);
		sb.append('}');
		return sb.toString();
	}
}
