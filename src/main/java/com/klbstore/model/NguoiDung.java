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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NguoiDung implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nguoiDungId;

    @Column(length = 50)
    private String tenDangNhap;

    @Column(length = 100)
    private String matKhau;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String hoTen;

    @Temporal(TemporalType.DATE)
    @Column
    private Date ngaySinh;

    @Column
    private Boolean gioiTinh;

    @Column(length = 200)
    private String diaChi;

    @Column(length = 20)
    private String sdt;

    @Column
    private Boolean quyenDangNhap = false;

    @Temporal(TemporalType.DATE)
    @Column
    private Date ngayDangKy = new Date();

    @Column
    private boolean trangThaiKhoa = false;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<DonHang> nguoiDungDonHangs;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<SanPham> nguoiDungSanPhams;
    
    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<PhieuXuat> nguoiDungPhieuXuats;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<DanhGia> nguoiDungDanhGias;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<PhieuNhap> nguoiDungPhieuNhaps;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<GioHang> nguoiDungGioHangs;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<HoatDongDangNhap> nguoiDungHoatDongDangNhaps;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<HoatDongSaiMatKhau> nguoiDungHoatDongSaiMatKhaus;

}
