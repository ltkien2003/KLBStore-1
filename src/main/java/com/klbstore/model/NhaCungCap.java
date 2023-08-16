package com.klbstore.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NhaCungCap implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nhaCungCapId;

    @Column(length = 100)
    @NotBlank (message = "{NotBlank.Model.nullText}")
    private String tenNhaCungCap;

    @Column(length = 100)
    @NotBlank (message = "{NotBlank.Model.nullText}")
    private String tenGiaoDich;

    @Column(length = 20)
    @NotBlank (message = "{NotBlank.Model.nullText}")
    @Size(min = 10, max = 10, message = "{Size.Model.sdt}")
    private String sdt;

    @Column(length = 100)
    @NotBlank (message = "{NotBlank.Model.nullText}")
    @Email (message = "{Email.Model.email}")
    private String email;

    @Column
    private Boolean hienThi;

    @OneToMany(mappedBy = "nhaCungCap")
    private Set<PhieuNhap> phieuNhaps;

}
