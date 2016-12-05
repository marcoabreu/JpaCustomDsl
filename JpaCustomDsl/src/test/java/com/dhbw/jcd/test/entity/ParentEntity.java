package com.dhbw.jcd.test.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "ParentEntity")
public class ParentEntity {
	
	@Id
	private long id;
	
	@Column(name = "intColumn")
	private int intColumn;
	
	@Column(name = "stringColumn")
	private String stringColumn;
	
	@OneToMany()
	private List<ChildEntity> childEntityRelation = new ArrayList<>();

	public ParentEntity() {
	}

}
