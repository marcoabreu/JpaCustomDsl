package com.dhbw.jcd.test.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "ParentEntity")
public class ParentEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ARTIST_ID")
	private long id;
	
	@Column(name= "artist")
	private String artist;
	
	@Column(name= "label")
	private String label;
	
	@Column(name= "amountOfAlbums")
	private int amountOfAlbums;
	
	@Column(name = "intColumn")
	private int intColumn;
	
	@Column(name = "stringColumn")
	private String stringColumn;
	
	@OneToMany(targetEntity= ChildEntity.class)
	private List<ChildEntity> childEntityRelation = new ArrayList<>();


	public ParentEntity(String artist, String label, int amountOfAlbums) {
		this.artist = artist;
		this.label = label;
		this.amountOfAlbums = amountOfAlbums;
	}
	
	public void addAlbum (ChildEntity album) {
		this.childEntityRelation.add(album);
		album.setArtist(this);
	}
	
	public void removeAlbum (ChildEntity album) {
		album.setArtist(null);
		this.childEntityRelation.remove(album);
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
	

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getAmountOfAlbums() {
		return amountOfAlbums;
	}

	public void setAmountOfAlbums(int amountOfAlbums) {
		this.amountOfAlbums = amountOfAlbums;
	}
	
	

}
