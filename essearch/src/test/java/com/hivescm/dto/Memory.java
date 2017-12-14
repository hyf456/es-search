package com.hivescm.dto;

import java.io.Serializable;

public class Memory implements Serializable {
	private String name;
	private int size;

	public Memory(String name, int size) {
		this.name = name;
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Memory{");
		sb.append("name='").append(name).append('\'');
		sb.append(", size=").append(size);
		sb.append('}');
		return sb.toString();
	}
}
