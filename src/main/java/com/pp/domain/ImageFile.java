package com.pp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_image_file")
public class ImageFile {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name="uuid",strategy = "uuid2")
	private String id;
	@Column
	private String name;
	@Column
	private String type;
	@Column
	private long length;
	
	@OneToOne(cascade =CascadeType.ALL )
	private ImageData imageData;
	
	public ImageFile(String name,String type,ImageData imageData) {
		this.type=type;
		this.name=name;
		this.imageData=imageData;
	}
}
