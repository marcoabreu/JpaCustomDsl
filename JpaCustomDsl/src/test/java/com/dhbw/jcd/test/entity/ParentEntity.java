package com.dhbw.jcd.test.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity(name = "ParentEntity")
public class ParentEntity {
	
	@Column(name = "intColumn")
	private int intColumn;
	
	@OneToMany()
	private List<ChildEntity> childEntityRelation = new ArrayList<>();

	public ParentEntity() {
		// TODO Auto-generated constructor stub
	}

}
