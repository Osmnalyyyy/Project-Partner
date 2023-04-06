package com.pp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_image_data")
public class ImageData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Lob
	private byte[]  data;// image datası byte array şeklinde
	
	public ImageData(byte[]  data) {
		this.data=data;
	}

	public ImageData(Long   id) {
		this.id=id;
	}
	
	
	
}
