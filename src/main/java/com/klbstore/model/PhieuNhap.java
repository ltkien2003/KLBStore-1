package com.klbstore.model;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class PhieuNhap implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int phieuNhapId;

    @Column
    @NotNull
    private Double tongTien;

    @Column
    @NotNull (message = "{NotNull.Model.nullDate}")
    private LocalDate ngayNhap;

    @ManyToOne
    @JoinColumn(name = "nhaCungCapId")
    private NhaCungCap nhaCungCap;

    @ManyToOne
    @JoinColumn(name = "nguoiDungId")
    private NguoiDung nguoiDung;

    @OneToMany(mappedBy = "phieuNhap")
    private List<ChiTietPhieuNhap> phieuNhapChiTietPhieuNhaps;

}
