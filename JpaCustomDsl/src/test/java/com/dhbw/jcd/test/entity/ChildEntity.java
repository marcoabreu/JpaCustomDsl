package com.dhbw.jcd.test.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "ChildEntity")
public class ChildEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ALBUM_ID")
	private long id;
	
	@Column(name= "albumName")
	private String albumName;
	
	@Column(name = "amountOfTracks")
	private int amountOfTracks;
	
	@Column(name = "shortColumn")
	private short shortColumn;
	
	@Column(name = "longColumn")
	private long longColumn;
	
	@OneToMany(targetEntity= ChildChildEntity.class)
	private List<ChildChildEntity> childChildEntityRelation = new ArrayList<>();
	

	@ManyToOne()
	@JoinColumn(name = "ARTIST_ID", referencedColumnName = "ARTIST_ID")
	private ParentEntity artist;
	
	public ChildEntity(String albumName, int amountOfTracks) {
		this.albumName = albumName;
		this.amountOfTracks = amountOfTracks;
	}
	

	public ParentEntity getArtist() {
		return artist;
	}

	public void setArtist(ParentEntity artist) {
		this.artist = artist;
	}
	
	public void addTrack (ChildChildEntity track) {
		this.childChildEntityRelation.add(track);
		track.setAlbum(this);
	}
	
	public void removeTrack (ChildChildEntity track) {
		track.setAlbum(null);
		this.childChildEntityRelation.remove(track);
	}


	public String getAlbumName() {
		return albumName;
	}


	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}


	public int getAmountOfTracks() {
		return amountOfTracks;
	}


	public void setAmountOfTracks(int amountOfTracks) {
		this.amountOfTracks = amountOfTracks;
	}
	
	
}
