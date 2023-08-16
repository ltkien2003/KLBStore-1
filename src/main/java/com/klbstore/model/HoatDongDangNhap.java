package com.klbstore.model;

import java.time.OffsetDateTime;

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
public class HoatDongDangNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hoatDongDangNhapId;

    @Column(columnDefinition = "datetime2")
    private OffsetDateTime thoiGianDangNhap;

    @Column(length = 50)
    private String diaChiIp;
    @Column
    private Boolean thanhCong;

    @ManyToOne
    @JoinColumn(name = "nguoiDungId")
    private NguoiDung nguoiDung;
}
