package com.klbstore.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.klbstore.extensions.VNT;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MaXacNhan implements Serializable {
    @Id
    @Column(name = "nguoiDungId")
    private Integer nguoiDungId;
    
    @Column
    private String maOtp;

    @Column
    private Boolean daXacNhan = false;

    @Column
    private Date ngayTaoOtp = VNT.getThoiGianVietNam();

    @Column
    private Date hanHieuLucOtp = new Date(ngayTaoOtp.getTime() + 300000); // 5 phút

}
