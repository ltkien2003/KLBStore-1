package com.klbstore.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThongKeDoanhThu implements Serializable{
    @Id
    Serializable ngayDat;
    Integer soLuong;

}
