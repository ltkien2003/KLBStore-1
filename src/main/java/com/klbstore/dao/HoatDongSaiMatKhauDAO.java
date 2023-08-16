package com.klbstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.klbstore.model.HoatDongSaiMatKhau;
import com.klbstore.model.NguoiDung;

public interface HoatDongSaiMatKhauDAO extends JpaRepository<HoatDongSaiMatKhau, Integer> {
    @Query("SELECT h FROM HoatDongSaiMatKhau h WHERE h.nguoiDung = :nguoiDung ORDER BY h.thoiGianKhoaTaiKhoan DESC")
    HoatDongSaiMatKhau findLatestHoatDongSaiMatKhauByNguoiDung(@Param("nguoiDung") NguoiDung nguoiDung);
}
