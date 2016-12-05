package com.dhbw.jcd.test.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "ChildEntity")
public class ChildEntity {
	@Id
	private long id;
	
	@OneToMany()
	private List<ChildChildEntity> childChildEntityRelation = new ArrayList<>();
	
	public ChildEntity() {
		// TODO Auto-generated constructor stub
	}

}
