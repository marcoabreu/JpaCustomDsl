package com.dhbw.jcd.test.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity(name = "ChildEntity")
public class ChildEntity {

	@OneToMany()
	private List<ChildChildEntity> childChildEntityRelation = new ArrayList<>();
	
	public ChildEntity() {
		// TODO Auto-generated constructor stub
	}

}
