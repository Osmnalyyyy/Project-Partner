package com.pp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_attachment_data")
@Entity
public class AttachmentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] data;

    public AttachmentData(byte[] data) {
        this.data = data;
    }

    public AttachmentData(Long id) {
        this.id = id;
    }
}