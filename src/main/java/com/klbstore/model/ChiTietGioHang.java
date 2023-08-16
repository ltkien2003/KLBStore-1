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
public class ChiTietGioHang implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chiTietGioHangId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "gioHangId")
    private GioHang gioHang;
    @ManyToOne
    @JoinColumn(name = "mauSacId")
    private MauSac mauSac;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;
    @Column
    private Integer soLuong;

}
