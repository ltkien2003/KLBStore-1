package com.klbstore.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.klbstore.dao.DanhGiaDAO;
import com.klbstore.dto.DanhGiaDTO;

@org.springframework.web.bind.annotation.RestController
public class DanhGiaRestController {
    @org.springframework.beans.factory.annotation.Autowired
    DanhGiaDAO danhGiaDAO;

    @GetMapping("/rest/danhGia")
    public ResponseEntity<List<DanhGiaDTO>> getDanhGia(@RequestParam("sanPhamId") Long sanPhamId) {
        List<Object[]> danhGia = danhGiaDAO.findReviewsForProduct(sanPhamId);
        List<DanhGiaDTO> danhGiaDTOs = danhGia.stream().map(dg -> convertToDanhGiaDTO(dg)).collect(Collectors.toList());
        return new ResponseEntity<List<DanhGiaDTO>>(danhGiaDTOs, HttpStatus.OK);
    }

    private DanhGiaDTO convertToDanhGiaDTO(Object[] dg) {
        return new DanhGiaDTO(
                (String) dg[0],        // hoTen
                (int) dg[1],           // sao
                (String) dg[2],        // noiDung
                (int) dg[3],           // thoiGianSuDung
                (String) dg[4]         // donViThoiGian
        );
    }
    @GetMapping("/rest/canUserRateProduct")
    ResponseEntity<Map<String, Boolean>> getRoleDanhGia(@RequestParam("sanPhamId") Long sanPhamId, @RequestParam("nguoiDungId") Long nguoiDungId) {
        Boolean roleDanhGia = danhGiaDAO.canUserRateProduct(nguoiDungId, sanPhamId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("roleDanhGia", roleDanhGia);
        return new ResponseEntity<Map<String, Boolean>>(map, HttpStatus.OK);
    }
    
}
