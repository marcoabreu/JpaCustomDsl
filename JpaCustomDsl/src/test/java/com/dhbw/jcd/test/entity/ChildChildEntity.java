package com.dhbw.jcd.test.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "ChildChildEntity")
public class ChildChildEntity {
	@Id
	private long id;
	
	public ChildChildEntity() {
	}

}
