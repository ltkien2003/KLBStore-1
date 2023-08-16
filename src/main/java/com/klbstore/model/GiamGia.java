package com.klbstore.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GiamGia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer giamGiaId;

    @Column(length = 100)
    @NotBlank (message = "{NotBlank.Model.nullText}")
    private String tenGiamGia;

    @Column(columnDefinition = "varchar(max)")
    private String moTa;

    @Temporal(TemporalType.DATE)
    @Column
    @NotNull (message = "{NotNull.Model.nullDate}")
    private Date ngayBatDau;

    @Temporal(TemporalType.DATE)
    @Column
    @Future (message = "{FutureOrPresent.dateFuture}")
    @NotNull (message = "{NotNull.Model.nullDate}")
    private Date ngayKetThuc;

    @Column(length = 100)
    @Min(0)
    private String mucGiamGia;

    @Column
    private Boolean hienThi;

    @OneToMany(mappedBy = "giamGia")
    private List<GiamGiaSanPham> giamGiaGiamGiaSanPhams;

    @OneToMany(mappedBy = "giamGia")
    private List<GiamGiaDanhMuc> giamGiaGiamGiaDanhMucs;

    @OneToMany(mappedBy = "giamGia")
    private List<GiamGiaDanhMucCon> giamGiaGiamGiaDanhMucCons;

}
