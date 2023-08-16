package com.klbstore.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChiTietPhieuNhap implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chiTietPhieuNhapId;

    @Column
    private Integer soLuong;

    @Column
    private Double donGia;

    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "phieuNhapId")
    private PhieuNhap phieuNhap;

}