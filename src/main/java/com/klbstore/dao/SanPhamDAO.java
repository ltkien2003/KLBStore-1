package com.klbstore.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.klbstore.dto.SanPhamDTO;
import com.klbstore.model.SanPham;

public interface SanPhamDAO extends JpaRepository<SanPham, Integer> {
       @Query("SELECT s FROM SanPham s "
                     + "JOIN s.sanPhamChiTietSanPhams c "
                     + "WHERE s.hienThi = true "
                     + "AND s.tenSanPham LIKE %:keywords% "
                     + "AND c.soLuongTrongKho > 0 "
                     + "GROUP BY s ")
       Page<SanPham> findAllByTenSanPhamLikeAndHienThi(String keywords, Pageable pageable);

       @Query("SELECT sp FROM SanPham sp WHERE sp.sanPhamId = :sanPhamId")
       SanPham findBySanPhamId(@Param("sanPhamId") Long sanPhamId);
       
       @Query("SELECT s FROM SanPham s "
                     + "JOIN s.sanPhamChiTietSanPhams c "
                     + "LEFT JOIN s.sanPhamGiamGiaTrucTieps ggt "
                     + "WHERE s.hienThi = true "
                     + "AND s.tenSanPham LIKE %:keywords% "
                     + "AND c.soLuongTrongKho > 0 "
                     + "GROUP BY s, (s.giaBan - (s.giaBan * COALESCE(ggt.giamGiaTrucTiep, 0))) "
                     + "ORDER BY (s.giaBan - (s.giaBan * COALESCE(ggt.giamGiaTrucTiep, 0))) ASC")
       Page<SanPham> findAllByTenSanPhamLikeAndHienThiOrderByGiaTongAsc(String keywords, Pageable pageable);

       @Query("SELECT s FROM SanPham s "
                     + "JOIN s.sanPhamChiTietSanPhams c "
                     + "LEFT JOIN s.sanPhamGiamGiaTrucTieps ggt "
                     + "WHERE s.hienThi = true "
                     + "AND s.tenSanPham LIKE %:keywords% "
                     + "AND c.soLuongTrongKho > 0 "
                     + "GROUP BY s, (s.giaBan - (s.giaBan * COALESCE(ggt.giamGiaTrucTiep, 0))) "
                     + "ORDER BY (s.giaBan - (s.giaBan * COALESCE(ggt.giamGiaTrucTiep, 0))) DESC")
       Page<SanPham> findAllByTenSanPhamLikeAndHienThiOrderByGiaTongDesc(String keywords, Pageable pageable);

       @Query("SELECT s FROM SanPham s "
                     + "JOIN s.sanPhamChiTietSanPhams c "
                     + "LEFT JOIN s.sanPhamGiamGiaTrucTieps ggt "
                     + "WHERE s.danhMucSanPham.nhomSanPham.nhomSanPhamId = :nhomSanPhamId "
                     + "AND s.hienThi = true "
                     + "AND c.soLuongTrongKho > 0 "
                     + "GROUP BY s, (s.giaBan - (s.giaBan * COALESCE(ggt.giamGiaTrucTiep, 0))) "
                     + "ORDER BY (s.giaBan - (s.giaBan * COALESCE(ggt.giamGiaTrucTiep, 0))) ASC")
       Page<SanPham> findAllByNhomSanPhamIdAndHienThiOrderByGiaTongAsc(
                     int nhomSanPhamId,
                     Pageable pageable);

       @Query("SELECT s FROM SanPham s "
                     + "JOIN s.sanPhamChiTietSanPhams c "
                     + "LEFT JOIN s.sanPhamGiamGiaTrucTieps ggt "
                     + "WHERE s.danhMucSanPham.nhomSanPham.nhomSanPhamId = :nhomSanPhamId "
                     + "AND s.hienThi = true "
                     + "AND c.soLuongTrongKho > 0 "
                     + "GROUP BY s, (s.giaBan - (s.giaBan * COALESCE(ggt.giamGiaTrucTiep, 0))) "
                     + "ORDER BY (s.giaBan - (s.giaBan * COALESCE(ggt.giamGiaTrucTiep, 0))) DESC")
       Page<SanPham> findAllByNhomSanPhamIdAndHienThiOrderByGiaTongDesc(
                     int nhomSanPhamId,
                     Pageable pageable);

       @Query("SELECT s FROM SanPham s "
                     + "LEFT JOIN s.sanPhamDanhGias d "
                     + "JOIN s.sanPhamChiTietSanPhams c "
                     + "WHERE s.danhMucSanPham.nhomSanPham.nhomSanPhamId = :nhomSanPhamId "
                     + "AND s.hienThi = true "
                     + "AND c.soLuongTrongKho > 0 "
                     + "GROUP BY s ")
       Page<SanPham> findAllByHienThiAndDanhMucSanPham_NhomSanPham_NhomSanPhamId(int nhomSanPhamId,
                     Pageable pageable);

       @Query("SELECT s FROM SanPham s "
                     + "LEFT JOIN s.sanPhamDanhGias d "
                     + "JOIN s.sanPhamChiTietSanPhams c "
                     + "WHERE s.danhMucSanPham.nhomSanPham.nhomSanPhamId = :nhomSanPhamId "
                     + "AND s.hienThi = true "
                     + "AND c.soLuongTrongKho > 0 "
                     + "GROUP BY s "
                     + "ORDER BY COALESCE(AVG(d.sao), 0) ASC")
       Page<SanPham> findAllByNhomSanPhamIdOrderByAverageRatingAsc(
                     int nhomSanPhamId,
                     Pageable pageable);

       @Query("SELECT s FROM SanPham s "
                     + "LEFT JOIN s.sanPhamDanhGias d "
                     + "JOIN s.sanPhamChiTietSanPhams c "
                     + "WHERE s.danhMucSanPham.nhomSanPham.nhomSanPhamId = :nhomSanPhamId "
                     + "AND s.hienThi = true "
                     + "AND c.soLuongTrongKho > 0 "
                     + "GROUP BY s "
                     + "ORDER BY COALESCE(AVG(d.sao), 0) DESC")
       Page<SanPham> findAllByNhomSanPhamIdOrderByAverageRatingDesc(
                     int nhomSanPhamId,
                     Pageable pageable);

       @Query("SELECT s FROM SanPham s "
                     + "LEFT JOIN s.sanPhamDanhGias d "
                     + "JOIN s.sanPhamChiTietSanPhams c "
                     + "WHERE s.hienThi = true "
                     + "AND s.tenSanPham LIKE %:keywords% "
                     + "AND c.soLuongTrongKho > 0 "
                     + "GROUP BY s "
                     + "ORDER BY COALESCE(AVG(d.sao), 0) ASC")
       Page<SanPham> findAllByTenSanPhamOrderByAverageRatingAsc(
                     String keywords,
                     Pageable pageable);

       @Query("SELECT s FROM SanPham s "
                     + "LEFT JOIN s.sanPhamDanhGias d "
                     + "JOIN s.sanPhamChiTietSanPhams c "
                     + "WHERE s.hienThi = true "
                     + "AND s.tenSanPham LIKE %:keywords% "
                     + "AND c.soLuongTrongKho > 0 "
                     + "GROUP BY s "
                     + "ORDER BY COALESCE(AVG(d.sao), 0) DESC")
       Page<SanPham> findAllByTenSanPhamOrderByAverageRatingDesc(
                     String keywords,
                     Pageable pageable);

       @Query("SELECT sp FROM SanPham sp " +
                     "LEFT JOIN FETCH sp.sanPhamChiTietSanPhams chiTiet " +
                     "WHERE chiTiet.soLuongTrongKho > 0" +
                     "AND (:soLuongTrongKho IS NULL OR chiTiet.soLuongTrongKho = :soLuongTrongKho)" +
                     "AND (:minSoLuongTrongKho IS NULL OR chiTiet.soLuongTrongKho >= :minSoLuongTrongKho)" +
                     "AND (:maxSoLuongTrongKho IS NULL OR chiTiet.soLuongTrongKho <= :maxSoLuongTrongKho)" +
                     "AND (:mauSacId IS NULL OR chiTiet.mauSac.mauSacId = : mauSacId) " +
                     "AND (:tenMauSac IS NULL OR chiTiet.mauSac.tenMauSac LIKE :tenMauSac) " +
                     "AND (:duongDanAnh IS NULL OR chiTiet.mauSac.duongDanAnh LIKE :duongDanAnh) " +
                     "AND (:viTriLuuTru IS NULL OR chiTiet.viTriLuuTru LIKE :viTriLuuTru)")
       List<SanPham> findSanPhamConHang(@Param("soLuongTrongKho") Integer soLuongTrongKho,
                     @Param("minSoLuongTrongKho") Integer minSoLuongTrongKho,
                     @Param("maxSoLuongTrongKho") Integer maxSoLuongTrongKho, @Param("mauSacId") Integer mauSacId,
                     @Param("tenMauSac") String tenMauSac, @Param("duongDanAnh") String duongDanAnh,
                     @Param("viTriLuuTru") String viTriLuuTru);

       @Query("SELECT sp FROM SanPham sp " +
                     "LEFT JOIN FETCH sp.sanPhamChiTietSanPhams chiTiet " +
                     "WHERE chiTiet.soLuongTrongKho <= 0" +
                     "AND (:soLuongTrongKho IS NULL OR chiTiet.soLuongTrongKho = :soLuongTrongKho)" +
                     "AND (:minSoLuongTrongKho IS NULL OR chiTiet.soLuongTrongKho >= :minSoLuongTrongKho)" +
                     "AND (:maxSoLuongTrongKho IS NULL OR chiTiet.soLuongTrongKho <= :maxSoLuongTrongKho)" +
                     "AND (:mauSacId IS NULL OR chiTiet.mauSac.mauSacId = : mauSacId) " +
                     "AND (:tenMauSac IS NULL OR chiTiet.mauSac.tenMauSac LIKE :tenMauSac) " +
                     "AND (:duongDanAnh IS NULL OR chiTiet.mauSac.duongDanAnh LIKE :duongDanAnh) " +
                     "AND (:viTriLuuTru IS NULL OR chiTiet.viTriLuuTru LIKE :viTriLuuTru)")
       List<SanPham> findSanPhamHetHang(@Param("soLuongTrongKho") Integer soLuongTrongKho,
                     @Param("minSoLuongTrongKho") Integer minSoLuongTrongKho,
                     @Param("maxSoLuongTrongKho") Integer maxSoLuongTrongKho, @Param("mauSacId") Integer mauSacId,
                     @Param("tenMauSac") String tenMauSac, @Param("duongDanAnh") String duongDanAnh,
                     @Param("viTriLuuTru") String viTriLuuTru);


       @Query("SELECT new com.klbstore.dto.SanPhamDTO(sp, " +
       "CASE WHEN (ggTrucTiep IS NOT NULL " +
       "AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP " +
       "AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP) THEN TRUE ELSE FALSE END, " +
       "CASE WHEN (ggTrucTiep IS NOT NULL " +
       "AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP " +
       "AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP) THEN (sp.giaBan * (1 - COALESCE(ggTrucTiep.giamGiaTrucTiep, 0))) ELSE sp.giaBan END, " +
       "CASE WHEN (ggTrucTiep IS NOT NULL " +
       "AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP " +
       "AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP) THEN CAST(ggTrucTiep.giamGiaTrucTiep * 100 AS integer) ELSE 0 END, " +
       "(SELECT CAST(AVG(dg.sao) AS integer) FROM sp.sanPhamDanhGias dg INNER JOIN sp.sanPhamChiTietDonHangs spdh WHERE dg.sanPham = sp AND dg.hienThi = 1 AND spdh.donHang.tinhTrangThanhToan = 1 AND spdh.donHang.tinhTrangGiaoHang = 1 " +
       "AND dg.ngayDanhGia <= CURRENT_TIMESTAMP)) " +
       "FROM SanPham sp " +
       "LEFT JOIN sp.sanPhamGiamGiaTrucTieps ggTrucTiep " +
       "WHERE sp IN :sanPhams " + 
       "AND (:nhomSanPhamId IS NULL OR sp.danhMucSanPham.nhomSanPham.nhomSanPhamId = :nhomSanPhamId) " +
       "AND (:tenNhomSanPham IS NULL OR sp.danhMucSanPham.nhomSanPham.tenNhomSanPham LIKE :tenNhomSanPham) " +
       "AND (:danhMucSanPhamId IS NULL OR sp.danhMucSanPham.danhMucSanPhamId = :danhMucSanPhamId) " +
       "AND (:tenDanhMucSanPham IS NULL OR sp.danhMucSanPham.tenDanhMucSanPham LIKE :tenDanhMucSanPham) " +
       "AND (:danhMucConId IS NULL OR sp.danhMucCon.danhMucConId = :danhMucConId) " +
       "AND (:sanPhamLienQuan IS NULL OR sp.sanPhamId <> :sanPhamLienQuan) " +
       "AND (:sanPhamId IS NULL OR sp.sanPhamId = :sanPhamId) " +
       "AND (:tenSanPham IS NULL OR sp.tenSanPham LIKE :tenSanPham) " + 
       "AND (:moTa IS NULL OR sp.moTa LIKE %:moTa%) " +
       "AND (:xuatSu IS NULL OR sp.xuatSu LIKE :xuatSu) " +
       "AND (:giaBan IS NULL OR sp.giaBan = :giaBan) " +
       "AND (:minGiaBan IS NULL OR sp.giaBan >= :minGiaBan) " +
       "AND (:maxGiaBan IS NULL OR sp.giaBan <= :maxGiaBan) " +
       "AND (:soLuotXem IS NULL OR sp.soLuotXem = :soLuotXem) " +
       "AND (:minSoLuotXem IS NULL OR sp.soLuotXem >= :minSoLuotXem) " +
       "AND (:maxSoLuotXem IS NULL OR sp.soLuotXem <= :maxSoLuotXem) " +
       "AND (:ngayTao IS NULL OR CAST(sp.ngayTao AS date) = :ngayTao) " +
       "AND (:minNgayTao IS NULL OR CAST(sp.ngayTao AS date) >= :minNgayTao) " +
       "AND (:maxNgayTao IS NULL OR CAST(sp.ngayTao AS date) <= :maxNgayTao) " + 
       "AND (:noiBat IS NULL OR sp.noiBat = :noiBat) " +
       "AND (:hienThi IS NULL OR sp.hienThi = :hienThi) " + 
       "AND (:giamGia IS NULL " +
       "OR (" +
       ":giamGia = TRUE " +
       "AND ggTrucTiep IS NOT NULL " +
       "AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP " +
       "AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP" +
       ") " +                        
       "OR (" +
       ":giamGia = FALSE " +
       "OR ggTrucTiep IS NULL " +
       "OR (ggTrucTiep IS NOT NULL AND ggTrucTiep.ngayBatDau > CURRENT_TIMESTAMP " +
       "AND ggTrucTiep.ngayKetThuc < CURRENT_TIMESTAMP)" +
       ")) " +              
       "ORDER BY " +
       "CASE WHEN :sortBy = 'giaTangDan' THEN (CASE WHEN (ggTrucTiep IS NOT NULL AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP) THEN (sp.giaBan * (1 - COALESCE(ggTrucTiep.giamGiaTrucTiep, 0))) ELSE sp.giaBan END) END ASC, " +
       "CASE WHEN :sortBy = 'giaGiamDan' THEN (CASE WHEN (ggTrucTiep IS NOT NULL AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP) THEN (sp.giaBan * (1 - COALESCE(ggTrucTiep.giamGiaTrucTiep, 0))) ELSE sp.giaBan END) END DESC, " +                        
       "CASE WHEN :sortBy = 'ngayTaoTangDan' THEN sp.ngayTao END ASC, " +
       "CASE WHEN :sortBy = 'ngayTaoGiamDan' THEN sp.ngayTao END DESC, " +
       "CASE WHEN :sortBy = 'soLuotXemTangDan' THEN sp.soLuotXem END ASC, " +
       "CASE WHEN :sortBy = 'soLuotXemGiamDan' THEN sp.soLuotXem END DESC, " +
       "CASE WHEN :sortBy = 'soSaoTangDan' THEN (SELECT CAST(AVG(dg.sao) AS integer) FROM sp.sanPhamDanhGias dg WHERE dg.sanPham = sp AND dg.ngayDanhGia <= CURRENT_TIMESTAMP) END ASC, " +
       "CASE WHEN :sortBy = 'soSaoGiamDan' THEN (SELECT CAST(AVG(dg.sao) AS integer) FROM sp.sanPhamDanhGias dg WHERE dg.sanPham = sp AND dg.ngayDanhGia <= CURRENT_TIMESTAMP) END DESC, " +
       "CASE WHEN :sortBy = 'tenSanPhamTangDan' THEN sp.tenSanPham END ASC, " +
       "CASE WHEN :sortBy = 'tenSanPhamGiamDan' THEN sp.tenSanPham END DESC, " +
       "CASE WHEN :sortBy = 'giamGiaTangDan' AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP THEN CAST(ggTrucTiep.giamGiaTrucTiep * 100 AS integer) END ASC, " +
       "CASE WHEN :sortBy = 'giamGiaGiamDan' AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP THEN CAST(ggTrucTiep.giamGiaTrucTiep * 100 AS integer) END DESC")
       List<SanPhamDTO> getDTO(@Param("sanPhams") List<SanPham> sanPhams, @Param("nhomSanPhamId") Integer nhomSanPhamId, @Param("tenNhomSanPham") String tenNhomSanPham, @Param("danhMucSanPhamId") Integer danhMucSanPhamId, @Param("tenDanhMucSanPham") String tenDanhMucSanPham, @Param("danhMucConId") Integer danhMucConId, @Param("sanPhamId") Long sanPhamId, @Param("tenSanPham") String tenSanPham, @Param("moTa") String moTa, @Param("xuatSu") String xuatSu, @Param("giaBan") Double giaBan, @Param("minGiaBan") Double minGiaBan, @Param("maxGiaBan") Double maxGiaBan, @Param("soLuotXem") Integer soLuotXem, @Param("minSoLuotXem") Integer minSoLuotXem, @Param("maxSoLuotXem") Integer maxSoLuotXem,@Param("ngayTao") LocalDate ngayTao, @Param("minNgayTao") LocalDate minNgayTao, @Param("maxNgayTao") LocalDate maxNgayTao, @Param("noiBat") Boolean noiBat, @Param("hienThi") Boolean hienThi, @Param("giamGia") Boolean giamGia, @Param("sanPhamLienQuan") Integer sanPhamLienQuan, @Param("sortBy") String sortBy);
       
       @Query("SELECT new com.klbstore.dto.SanPhamDTO(sp, " +
       "CASE WHEN (ggTrucTiep IS NOT NULL " +
       "AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP " +
       "AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP) THEN TRUE ELSE FALSE END, " +
       "CASE WHEN (ggTrucTiep IS NOT NULL " +
       "AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP " +
       "AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP) THEN (sp.giaBan * (1 - COALESCE(ggTrucTiep.giamGiaTrucTiep, 0))) ELSE sp.giaBan END, " +
       "CASE WHEN (ggTrucTiep IS NOT NULL " +
       "AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP " +
       "AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP) THEN CAST(ggTrucTiep.giamGiaTrucTiep * 100 AS integer) ELSE 0 END, " +
       "(SELECT CAST(AVG(dg.sao) AS integer) FROM sp.sanPhamDanhGias dg WHERE dg.sanPham = sp " +
       "AND dg.ngayDanhGia <= CURRENT_TIMESTAMP)) " +
       "FROM SanPham sp " +
       "LEFT JOIN sp.sanPhamGiamGiaTrucTieps ggTrucTiep " +
       "WHERE sp IN :sanPhams " + 
       "AND (sp.sanPhamId <> :related) ")
       List<SanPhamDTO> findDTO(@Param("sanPhams") List<SanPham> sanPhams, @Param("related") Long related);
}
