package com.edu.ulab.app.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.edu.ulab.app.storage.Idable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "person", schema = "ulab_edu")
public class Person implements Idable<Long> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
	@SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
	private Long id;
	
	@Column(name = "full_name", nullable = false)
	private String fullName;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private int age;
	
	@ElementCollection
	@Column(name = "books_id")
	private List<Long> booksId;
	
	@Column
	private String resume; // добавленное поле - коротко о пользователе. Соответственно модифицированы DTO, запросы, ответы, сервисы
}
