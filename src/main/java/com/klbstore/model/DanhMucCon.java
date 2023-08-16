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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DanhMucCon implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer danhMucConId;

    @Column(length = 100)
    private String tenDanhMucCon;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "danhMucSanPhamId")
    private DanhMucSanPham danhMucSanPham;

    @JsonIgnore
    @OneToMany(mappedBy = "danhMucCon")
    private List<SanPham> danhMucConSanPhams;

    @JsonIgnore
    @OneToMany(mappedBy = "danhMucCon")
    private List<GiamGiaDanhMucCon> danhMucConGiamGiaDanhMucCons;

}