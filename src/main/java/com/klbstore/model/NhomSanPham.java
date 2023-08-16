package com.klbstore.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NhomSanPham implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nhomSanPhamId;

    @Column(length = 100)
    private String tenNhomSanPham;

    @JsonIgnore
    @OneToMany(mappedBy = "nhomSanPham")
    private Set<DanhMucSanPham> danhMucSanPhams;

}
