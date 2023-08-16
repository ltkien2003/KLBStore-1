package com.klbstore.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DonHang implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer donHangId;

    @Column
    private Double giamGiaTrucTiep;

    @Column
    private LocalDateTime ngayDatHang;

    @Column(length = 200)
    private String diaChiGiaoHang;

    @Column
    private LocalDateTime ngayGiaoHang;

    @Column
    private Double phiVanChuyen;

    @Column
    private Double tongTienSanPham;

    @Column
    private LocalDateTime ngayGiaoHangDuKien;

    @Column(columnDefinition = "varchar(max)")
    private String ghiChu;

    @Column
    private Boolean tinhTrangThanhToan;

    @Column
    private Boolean tinhTrangGiaoHang;

    @ManyToOne
    @JoinColumn(name = "nguoiDungId")
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "hinhThucThanhToan")
    private HinhThucThanhToan hinhThucThanhToan;

    @OneToMany(mappedBy = "donHang")
    private List<ChiTietDonHang> donHangChiTietDonHangs;

    @OneToMany(mappedBy = "donHang")
    private List<ChiTietPhieuXuat> donHangChiTietPhieuXuats;

}