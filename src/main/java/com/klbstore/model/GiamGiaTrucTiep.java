package com.klbstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class GiamGiaTrucTiep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer giamGiaTrucTiepId;

    @Column(precision = 7, scale = 2)
    private BigDecimal giamGiaTrucTiep;

    @Column
    private LocalDateTime ngayBatDau;

    @Column
    private LocalDateTime ngayKetThuc;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;
}
