package com.klbstore.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class HinhThucThanhToan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hinhThucThanhToanId;

    @Column(length = 100)
    private String tenHinhThucThanhToan;

    @Column(columnDefinition = "varchar(max)")
    private String moTa;

    @Column
    private Boolean hinhThuc;

    @OneToMany(mappedBy = "hinhThucThanhToan")
    private Set<DonHang> hinhThucThanhToanDonHangs;

}
