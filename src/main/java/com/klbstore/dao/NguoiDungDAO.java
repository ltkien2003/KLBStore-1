package com.klbstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.klbstore.model.NguoiDung;

public interface NguoiDungDAO extends JpaRepository<NguoiDung, Integer> {
    NguoiDung findByTenDangNhap(String tenDangNhap);

    NguoiDung findByEmail(String email);

    NguoiDung findBySdt(String sdt);

    @Procedure(name = "TaoNguoiDung")
    void TaoNguoiDung(
        @Param("tenDangNhap") String tenDangNhap,
        @Param("matKhau") String matKhau,
        @Param("hoTen") String hoTen,
        @Param("email") String email,
        @Param("sdt") String sdt
    );
}
