package com.lms.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    private String type;

    @Column(name = "available_count", nullable = false)
    private Integer available_count;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getAvailable_count() {
		return available_count;
	}

	public void setAvailable_count(Integer available_count) {
		this.available_count = available_count;
	}

	
    
}
