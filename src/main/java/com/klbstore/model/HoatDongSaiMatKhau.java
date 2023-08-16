package com.klbstore.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoatDongSaiMatKhau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hoatDongSaiMatKhauId;
    @Column
    private Integer soLanSaiMatKhau;

    @Column()
    private Date thoiGianSaiMatKhauCuoi;

    @Column()
    private Date thoiGianKhoaTaiKhoan;

    @Column(length = 50)
    private String diaChiIp;

    @ManyToOne
    @JoinColumn(name = "nguoiDungId")
    private NguoiDung nguoiDung;
}
