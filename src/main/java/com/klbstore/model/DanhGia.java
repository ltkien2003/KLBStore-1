package com.klbstore.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DanhGia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer danhGiaSanPhamId;

    @Column(length = 200)
    private String noiDung;

    @Column
    @Temporal(TemporalType.DATE)
    private Date ngayDanhGia;

    @Column
    private Boolean hienThi;

    @Column
    private Integer sao;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sanPhamId", insertable = false, updatable = false)
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "nguoiDungId")
    private NguoiDung nguoiDung;
}
