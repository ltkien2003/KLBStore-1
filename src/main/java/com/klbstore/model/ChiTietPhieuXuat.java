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
public class ChiTietPhieuXuat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chiTietPhieuXuatId;

    @Column
    private Integer soLuongXuat;

    @Column
    private Double giaBan;

    @ManyToOne
    @JoinColumn(name = "donHangId")
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "phieuXuatId")
    private PhieuXuat phieuXuat;

    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;

}
