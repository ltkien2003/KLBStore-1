package com.klbstore.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChiTietSanPham implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chiTietSanPhamId;

    @Column
    private Integer soLuongTrongKho;

    @Column(length = 200)
    private String viTriLuuTru;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;
    @ManyToOne
    @JoinColumn(name = "mauSacId")
    private MauSac mauSac;
}
