package com.klbstore.model;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DanhMucSanPham implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer danhMucSanPhamId;

    @Column(length = 100)
    @NotBlank (message = "{NotBlank.Model.nullText}")
    private String tenDanhMucSanPham;

    @ManyToOne
    @JoinColumn(name = "nhomSanPhamId")
    private NhomSanPham nhomSanPham;

    @JsonIgnore
    @OneToMany(mappedBy = "danhMucSanPham")
    private List<DanhMucCon> danhMucSanPhamDanhMucCons;

    @JsonIgnore
    @OneToMany(mappedBy = "danhMucSanPham")
    private List<SanPham> danhMucSanPhamSanPhams;

    @JsonIgnore
    @OneToMany(mappedBy = "danhMucSanPham")
    private List<GiamGiaDanhMuc> danhMucSanPhamGiamGiaDanhMucs;

}
