package com.klbstore.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.klbstore.model.DanhGia;

public interface DanhGiaDAO extends JpaRepository<DanhGia, Integer> {

    @Query(value = "SELECT ND.HoTen, DG.Sao, DG.NoiDung, " +
            "CASE " +
            "   WHEN DATEDIFF(DAY, DH.NgayGiaoHang, GETDATE()) >= 365 THEN DATEDIFF(YEAR, DH.NgayGiaoHang, GETDATE()) "
            +
            "   WHEN DATEDIFF(DAY, DH.NgayGiaoHang, GETDATE()) >= 30 THEN DATEDIFF(MONTH, DH.NgayGiaoHang, GETDATE()) "
            +
            "   WHEN DATEDIFF(DAY, DH.NgayGiaoHang, GETDATE()) >= 7 THEN DATEDIFF(WEEK, DH.NgayGiaoHang, GETDATE()) " +
            "   ELSE DATEDIFF(DAY, DH.NgayGiaoHang, GETDATE()) " +
            "END AS ThoiGianSuDung, " +
            "CASE " +
            "   WHEN DATEDIFF(DAY, DH.NgayGiaoHang, GETDATE()) >= 365 THEN N'năm' " +
            "   WHEN DATEDIFF(DAY, DH.NgayGiaoHang, GETDATE()) >= 30 THEN N'tháng' " +
            "   WHEN DATEDIFF(DAY, DH.NgayGiaoHang, GETDATE()) >= 7 THEN N'tuần' " +
            "   ELSE 'ngày' " +
            "END AS DonViThoiGian " +
            "FROM DanhGia DG " +
            "INNER JOIN SanPham SP ON DG.SanPhamID = SP.SanPhamID " +
            "INNER JOIN ChiTietDonHang CTDH ON SP.SanPhamID = CTDH.SanPhamID " +
            "INNER JOIN DonHang DH ON CTDH.DonHangID = DH.DonHangID " +
            "INNER JOIN NguoiDung ND ON DG.NguoiDungID = ND.NguoiDungID " +
            "WHERE DH.TinhTrangThanhToan = 1 " +
            "   AND DH.TinhTrangGiaoHang = 1 " +
            "   AND CTDH.SanPhamID IS NOT NULL " +
            "   AND DG.NguoiDungID = DH.NguoiDungID " +
            "   AND SP.SanPhamID = :sanPhamId " +
            "ORDER BY ThoiGianSuDung DESC", nativeQuery = true)
    List<Object[]> findReviewsForProduct(@Param("sanPhamId") Long sanPhamId);
    
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO DanhGia (SanPhamID, NoiDung, NguoiDungID, NgayDanhGia, HienThi, Sao) " +
                   "VALUES (:sanPhamId, :noiDung, :nguoiDungId, GETDATE(), :hienThi, :sao)", nativeQuery = true)
    void createDanhGia(@Param("sanPhamId") Integer sanPhamId,
                       @Param("noiDung") String noiDung,
                       @Param("nguoiDungId") Integer nguoiDungId,
                       @Param("hienThi") Boolean hienThi,
                       @Param("sao") Integer sao);

    @Query(value = "DECLARE @CoTheDanhGia BIT; " +
            "IF EXISTS ( " +
            "    SELECT 1 " +
            "    FROM DonHang DH " +
            "    INNER JOIN ChiTietDonHang CTDH ON DH.DonHangID = CTDH.DonHangID " +
            "    WHERE DH.NguoiDungID = :nguoiDungId " +
            "        AND CTDH.SanPhamID = :sanPhamId " +
            "        AND DH.TinhTrangThanhToan = 1 " +
            "        AND DH.TinhTrangGiaoHang = 1 " +
            ") " +
            "BEGIN " +
            "    IF NOT EXISTS ( " +
            "        SELECT 1 " +
            "        FROM DanhGia " +
            "        WHERE NguoiDungID = :nguoiDungId AND SanPhamID = :sanPhamId " +
            "    ) " +
            "        SET @CoTheDanhGia = 1; " +
            "    ELSE " +
            "        SET @CoTheDanhGia = 0; " +
            "END " +
            "ELSE " +
            "    SET @CoTheDanhGia = 0; " +
            "SELECT CASE WHEN @CoTheDanhGia = 1 THEN 'true' ELSE 'false' END", nativeQuery = true)
    boolean canUserRateProduct(@Param("nguoiDungId") Long nguoiDungId, @Param("sanPhamId") Long sanPhamId);
}
