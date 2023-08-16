package com.klbstore.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class PhieuXuat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer phieuXuatId;

    @Column
    @Temporal(TemporalType.DATE)
    @NotNull (message = "{NotNull.Model.nullDate}")
    private Date ngayXuat;

    @ManyToOne
    @JoinColumn(name = "nguoiDungId")
    private NguoiDung nguoiDung;

    @OneToMany(mappedBy = "phieuXuat")
    private Set<ChiTietPhieuXuat> phieuXuatChiTietPhieuXuats;
    
}
