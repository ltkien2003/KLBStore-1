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
public class MauSac implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mauSacId;

    @Column(length = 100)
    private String tenMauSac;

    @Column(columnDefinition = "varchar(max)")
    private String duongDanAnh;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;

    @JsonIgnore
    @OneToMany(mappedBy = "mauSac")
    private List<ChiTietSanPham> mauSacChiTietSanPhams;

    @JsonIgnore
    @OneToMany(mappedBy = "mauSac")
    private List<ChiTietGioHang> mauSacChiTietGioHangs;
    
    @JsonIgnore
    @OneToMany(mappedBy = "mauSac")
    private List<ChiTietDonHang> mauSacChiTietDonHangs;

}