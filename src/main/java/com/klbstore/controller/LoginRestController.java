package com.klbstore.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.klbstore.dao.ChiTietGioHangDAO;
import com.klbstore.dao.HoatDongSaiMatKhauDAO;
import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.dto.AllChiTietGioHangDTO;
import com.klbstore.dto.ChiTietGioHangDTO;
import com.klbstore.jwt.JwtTokenProvider;
import com.klbstore.model.CustomUserDetails;
import com.klbstore.model.HoatDongSaiMatKhau;
import com.klbstore.model.NguoiDung;
import com.klbstore.payload.LoginRequest;
import com.klbstore.payload.LoginResponse;
import com.klbstore.payload.RandomStuff;
import com.klbstore.service.SessionService;
import com.klbstore.service.ShoppingCartService;

@Controller
public class LoginRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SessionService sessionService;

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    ChiTietGioHangDAO chiTietGioHangDAO;

    @Autowired
    NguoiDungDAO nguoiDungDAO;

    @Autowired
    HttpServletRequest request;

    @Autowired
    HoatDongSaiMatKhauDAO hoatDongSaiMatKhauDAO;

    public NguoiDung getNguoiDung() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        NguoiDung nguoiDung = nguoiDungDAO.findByTenDangNhap(authentication.getName());
        return nguoiDung;
    }

    @PostMapping("/user/login")
    public String authenticateUser(@Valid @ModelAttribute("login") LoginRequest login,
            BindingResult result, Model model, HttpServletRequest request) {
        if (!result.hasErrors()) {
            String regex = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(login.getSdt());
            if (!matcher.matches()) {
                model.addAttribute("message", "Số điện thoại không hợp lệ!");
                return "/user/login";
            }
            NguoiDung nd = nguoiDungDAO.findBySdt(login.getSdt());

            if (nd != null && nd.isTrangThaiKhoa()) {
                HoatDongSaiMatKhau hoatDongSaiMatKhau = hoatDongSaiMatKhauDAO
                        .findLatestHoatDongSaiMatKhauByNguoiDung(nd);

                if (hoatDongSaiMatKhau != null) {
                    Date thoiGianKhoaTaiKhoan = hoatDongSaiMatKhau.getThoiGianKhoaTaiKhoan();
                    System.out.println("Thời gian khóa tài khoản: " + thoiGianKhoaTaiKhoan);
                    Date thoiGianHienTai = new Date();

                    if (thoiGianKhoaTaiKhoan != null
                            && thoiGianHienTai.getTime() - thoiGianKhoaTaiKhoan.getTime() >= 3600000) {
                        nd.setTrangThaiKhoa(false);
                        hoatDongSaiMatKhau.setSoLanSaiMatKhau(0);
                        hoatDongSaiMatKhau.setThoiGianKhoaTaiKhoan(null);
                        nguoiDungDAO.save(nd);
                        hoatDongSaiMatKhauDAO.save(hoatDongSaiMatKhau);
                    } else if (thoiGianKhoaTaiKhoan != null
                            && thoiGianHienTai.getTime() - thoiGianKhoaTaiKhoan.getTime() < 3600000) {
                        model.addAttribute("message",
                                "Tài khoản của bạn đã bị khóa do nhập sai mật khẩu quá nhiều lần. Vui lòng thử lại sau 1 giờ.");
                        return "/user/login";
                    }
                }
            }
            if(nd == null) {
                model.addAttribute("message", "Tài khoản hoặc mật khẩu không hợp lệ!");
                return "/user/login";
            }

            Authentication customAuthentication = new UsernamePasswordAuthenticationToken(
                    login.getSdt(),
                    login.getMatKhau());

            try {
                Authentication authentication = authenticationManager.authenticate(customAuthentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
                sessionService.set("jwt", jwt);
                AllChiTietGioHangDTO cart = shoppingCartService.getCart();

                if (cart != null) {
                    List<ChiTietGioHangDTO> cartItems = cart.getDanhSachSanPhamTrongGioHang();

                    if (cartItems != null && !cartItems.isEmpty()) {
                        for (ChiTietGioHangDTO chiTietGioHangDTO : cartItems) {
                            Integer mauSacId = chiTietGioHangDTO.getChiTietGioHang().getMauSac().getMauSacId();
                            Integer soLuong = chiTietGioHangDTO.getChiTietGioHang().getSoLuong();

                            chiTietGioHangDAO.themSanPhamVaoGioHang(mauSacId, soLuong, getNguoiDung().getNguoiDungId());
                        }
                        shoppingCartService.clearCart();
                    }
                }
                return "redirect:/user/index";
            } catch (BadCredentialsException e) {
                if (nd != null) {
                    HoatDongSaiMatKhau hoatDongSaiMatKhau = hoatDongSaiMatKhauDAO
                            .findLatestHoatDongSaiMatKhauByNguoiDung(nd);

                    if (hoatDongSaiMatKhau != null) {
                        // Hoạt động sai mật khẩu đã tồn tại, cập nhật số lần sai mật khẩu
                        int soLanSaiMatKhau = hoatDongSaiMatKhau.getSoLanSaiMatKhau();
                        hoatDongSaiMatKhau.setSoLanSaiMatKhau(soLanSaiMatKhau + 1);
                        hoatDongSaiMatKhau.setThoiGianSaiMatKhauCuoi(new Date());
                    } else {
                        // Hoạt động sai mật khẩu chưa tồn tại, tạo mới
                        hoatDongSaiMatKhau = new HoatDongSaiMatKhau();
                        hoatDongSaiMatKhau.setNguoiDung(nd);
                        hoatDongSaiMatKhau.setSoLanSaiMatKhau(1);
                        hoatDongSaiMatKhau.setThoiGianSaiMatKhauCuoi(new Date());
                        hoatDongSaiMatKhau.setDiaChiIp(request.getRemoteAddr());
                    }

                    // Lưu hoạt động sai mật khẩu
                    hoatDongSaiMatKhauDAO.save(hoatDongSaiMatKhau);

                    if (hoatDongSaiMatKhau.getSoLanSaiMatKhau() >= 5) {
                        // Tài khoản sai quá 5 lần, khóa tài khoản và set thời gian khóa là 1 giờ
                        nd.setTrangThaiKhoa(true);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.HOUR_OF_DAY, 1); // Thêm 1 giờ
                        Date thoiGianMoKhoa = calendar.getTime();

                        hoatDongSaiMatKhau.setThoiGianKhoaTaiKhoan(thoiGianMoKhoa);

                        nguoiDungDAO.save(nd); // Lưu thay đổi trạng thái khóa
                        hoatDongSaiMatKhauDAO.save(hoatDongSaiMatKhau); // Lưu thay đổi thời gian khóa

                        model.addAttribute("message",
                                "Tài khoản đã bị khóa do sai mật khẩu quá 5 lần. Vui lòng thử lại sau 1 giờ.");
                        return "/user/login";
                    }

                    // Tiếp tục xử lý khi sai mật khẩu ít hơn 5 lần

                }
                model.addAttribute("message", "Tài khoản hoặc mật khẩu không hợp lệ!");
                return "/user/login";
            }
        }
        return "/user/login";
    }

    // Api /api/random yêu cầu phải xác thực mới có thể request
    @GetMapping("/random")
    public RandomStuff randomStuff() {
        return new RandomStuff("JWT Hợp lệ mới có thể thấy được message này");
    }

    @GetMapping("/checkLogin")
    public ResponseEntity<?> checkLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }
        String jwt = sessionService.get("jwt");
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }
        LoginResponse loginResponse = new LoginResponse(jwt);
        return ResponseEntity.ok(loginResponse);
    }

}
