package com.klbstore.model;

import java.io.Serializable;

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
public class GiamGiaSanPham implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer giamGiaSanPhamId;

    @ManyToOne
    @JoinColumn(name = "giamGiaId")
    private GiamGia giamGia;

    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;

}
