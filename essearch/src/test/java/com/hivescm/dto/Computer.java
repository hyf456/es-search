package com.hivescm.dto;

import java.io.Serializable;

public class Computer implements Serializable {

	private String logal;
	private int size;
	private Memory memory;

	public Computer() {
	}

	public Computer(String logal, int size, Memory memory) {
		this.logal = logal;
		this.size = size;
		this.memory = memory;
	}

	public String getLogal() {
		return logal;
	}
	public void setLogal(String logal) {
		this.logal = logal;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Memory getMemory() {
		return memory;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Computer{");
		sb.append("logal='").append(logal).append('\'');
		sb.append(", size=").append(size);
		sb.append(", memory=").append(memory);
		sb.append('}');
		return sb.toString();
	}
}
