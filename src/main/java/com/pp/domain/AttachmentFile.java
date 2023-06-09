package com.pp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_attachment_file")
@Entity
public class AttachmentFile {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column
    private String name;
    @Column
    private String type;
    @Column
    private int size;


    @OneToOne(cascade = CascadeType.ALL)
    private AttachmentData attachmentData;

    public AttachmentFile(String name, String type, AttachmentData attachmentData) {
        this.name = name;
        this.type = type;
        this.attachmentData = attachmentData;
    }
}