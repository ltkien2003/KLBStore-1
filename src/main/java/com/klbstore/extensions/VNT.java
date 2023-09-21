package com.klbstore.extensions;

import com.klbstore.dao.GiamGiaTrucTiepDAO;
import com.klbstore.model.GiamGiaTrucTiep;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
public class VNT {

    public static Date getThoiGianVietNam() {
        // Lấy thời gian hiện tại ở múi giờ GMT
        Date thoiGianHienTai = new Date();

        // Chọn múi giờ của Việt Nam
        TimeZone muiGioVietNam = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");

        // Chuyển múi giờ của thời gian hiện tại về múi giờ của Việt Nam
        thoiGianHienTai.setTime(thoiGianHienTai.getTime() + muiGioVietNam.getRawOffset());

        return thoiGianHienTai;
    }


    public static LocalDateTime getLocalDateTime() {
        // Lấy thời gian hiện tại ở múi giờ GMT
        LocalDateTime thoiGianHienTai = LocalDateTime.now();

        // Chọn múi giờ của Việt Nam
        ZoneId muiGioVietNam = ZoneId.of("Asia/Ho_Chi_Minh");

        // Chuyển múi giờ của thời gian hiện tại về múi giờ của Việt Nam
        thoiGianHienTai = thoiGianHienTai.atZone(ZoneId.of("GMT")).withZoneSameInstant(muiGioVietNam).toLocalDateTime();

        return thoiGianHienTai;
    }

}

