package com.klbstore.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.klbstore.config.SecurityRestTemplate;
import com.klbstore.config.VnpayConfig;
import com.klbstore.dao.ChiTietDonHangDAO;
import com.klbstore.dao.DonHangDAO;
import com.klbstore.dao.GiamGiaTrucTiepDAO;
import com.klbstore.dao.GioHangDAO;
import com.klbstore.dao.HinhThucThanhToanDAO;
import com.klbstore.dao.MaXacNhanDAO;
import com.klbstore.dao.MauSacDAO;
import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.dao.SanPhamDAO;
import com.klbstore.dto.AllChiTietGioHangDTO;
import com.klbstore.dto.ChangePassword;
import com.klbstore.dto.ChiTietGioHangDTO;
import com.klbstore.dto.EmailForm;
import com.klbstore.dto.Login;
import com.klbstore.dto.NguoiDungDTO;
import com.klbstore.dto.Register;
import com.klbstore.dto.ResetPassword;
import com.klbstore.extensions.ContactService;
import com.klbstore.extensions.HashedPasswordArgon2;
import com.klbstore.extensions.OrderService;
import com.klbstore.extensions.OtpGenerator;
import com.klbstore.extensions.OtpService;
import com.klbstore.jwt.JwtTokenProvider;
import com.klbstore.model.ChiTietDonHang;
import com.klbstore.model.ChiTietSanPham;
import com.klbstore.model.CustomUserDetails;
import com.klbstore.model.DonHang;
import com.klbstore.model.HinhThucThanhToan;
import com.klbstore.model.LienHe;
import com.klbstore.model.MaXacNhan;
import com.klbstore.model.MauSac;
import com.klbstore.model.NguoiDung;
import com.klbstore.model.SanPham;
import com.klbstore.service.CookieService;
import com.klbstore.service.MailerServiceImpl;
import com.klbstore.service.ParamService;
import com.klbstore.service.SessionService;
import com.klbstore.service.ShoppingCartService;

@Controller
public class UserController {
    @Autowired
    NguoiDungDAO nguoiDungDao;
    @Autowired
    CookieService cookieService;
    @Autowired
    ParamService paramService;
    @Autowired
    SessionService sessionService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private SanPhamDAO sanPhamDAO;
    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    MauSacDAO mauSacDAO;
    @Autowired
    MaXacNhanDAO maXacNhanDAO;
    @Autowired
    MailerServiceImpl mailer;
    @Autowired
    OtpService otpService;
    @Autowired
    HashedPasswordArgon2 hashedPassword;
    @Autowired
    com.klbstore.dao.ChiTietSanPhamDAO chiTietSanPhamDAO;
    @Autowired
    HinhThucThanhToanDAO htttDAO;
    @Autowired
    ChiTietDonHangDAO ctdhDAO;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    SecurityRestTemplate restTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    public NguoiDung getNguoiDung() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            NguoiDung nguoiDung = nguoiDungDAO.findByTenDangNhap(auth.getName());
            return nguoiDung;
        }
        return null;
    }

    @GetMapping("/user/log-out")
    public String logOut() {
        sessionService.invalidateSession();
        return "redirect:/user/login";
    }

    @GetMapping("/user/404")
    public String error404() {
        return "user/404";
    }

    @Autowired
    GiamGiaTrucTiepDAO giamGiaTrucTiepDAO;

    // @Autowired
    // private JwtTokenProvider tokenProvider;

    String getToken() {
        String token = sessionService.get("jwt");
        if (token == null) {
            return "";
        }
        return token;
    }

    @GetMapping(value = { "/", "/user/index" })
    public String index(Model model) {
        model.addAttribute("noiBat",
                restTemplate.get("http://localhost:8080/rest/xinchao?hienThi=true&noiBat=true&sortBy=giamGiaGiamDan"));
        model.addAttribute("dienThoai",
                restTemplate.get("http://localhost:8080/rest/xinchao?hienThi=true&sortBy=giaGiamDan"));
        model.addAttribute("useHeader", getNguoiDung());
        return "user/index";
    }

    // GET đăng nhập - đăng kí
    @GetMapping("/user/login")
    public String login(Model model) {
        NguoiDung nguoiDung = getNguoiDung();
        if (nguoiDung != null) {
            return "redirect:/user/index";
        } else {
            Login nd = new Login();
            model.addAttribute("login", nd);
            return "user/login";
        }
    }

    @GetMapping("/user/register")
    public String register(Model model) {
        NguoiDung nguoiDung = getNguoiDung();
        if (nguoiDung != null) {
            return "redirect:/user/index";
        } else {
            Register register = new Register();
            model.addAttribute("register", register);
            return "user/register";
        }
    }

    @PostMapping("/user/contact")
    public String contact(Model model, @Valid @ModelAttribute("lienhe") LienHe lh, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/contact";
        }
        contactService.receiveContactFormSubmission(lh.getName(), lh.getEmail(), lh.getMessage());
        model.addAttribute("message", "Gửi liên hệ thành công");
        LienHe lienHe = new LienHe();
        lienHe.setName(lh.getName());
        lienHe.setEmail(lh.getEmail());
        model.addAttribute("lienhe", lienHe);
        return "user/contact";
    }

    @PostMapping("/user/register")
    public String getRegister(Model model, @Valid @ModelAttribute("register") Register register,
            BindingResult result) {
        try {
            Pattern p = Pattern.compile(
                    "^(?=.*[`!@#\\$%\\^&*()-=_+\\\\[\\\\]{}|;':\\\",./<>?])(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])\\S{8,}$");
            Matcher m = p.matcher(register.getMatKhau());
            Boolean e = m.matches();
            if (!result.hasErrors()) {
                Boolean check = true;
                if (nguoiDungDao.findByTenDangNhap(register.getTenDangNhap()) != null) {
                    check = false;
                    model.addAttribute("errorMessage", "Tên đăng nhập đã tồn tại!");
                    return "user/register";
                }
                if (nguoiDungDao.findByEmail(register.getEmail()) != null) {
                    check = false;
                    model.addAttribute("errorMessage", "Email đã tồn tại!");
                    return "user/register";
                }
                if (nguoiDungDao.findBySdt(register.getSdt()) != null) {
                    check = false;
                    model.addAttribute("errorMessage", "Số điện thoại đã tồn tại!");
                    return "user/register";
                }
                if (e == false) {
                    check = false;
                    model.addAttribute("errorMessage",
                            "Mật khẩu bao gồm ít nhất 8 ký tự và phải chứa ít nhất một ký tự in hoa, một ký tự in thường, một chữ số !");
                    return "user/register";
                }
                if (!register.getMatKhau().equals(register.getNhapLaiMatKhau())) {
                    check = false;
                    model.addAttribute("errorMessage", "Sai mật khẩu xác nhận!");
                    return "user/register";
                }
                if (check == true) {
                    String tenDangNhap = register.getTenDangNhap();
                    String password = register.getMatKhau();
                    String hoTen = register.getHoTen();
                    String email = register.getEmail();
                    String sdt = register.getSdt();
                    nguoiDungDAO.TaoNguoiDung(tenDangNhap, password, hoTen, email, sdt);
                    model.addAttribute("errorMessage", "Đăng ký tài khoản thành công");
                    String otp = "";
                    String message = "";
                    try {
                        NguoiDung user = nguoiDungDao.findByEmail(register.getEmail());

                        Date curDate = new Date();
                        MaXacNhan maXacNhan = maXacNhanDAO.findByNguoiDungId(user.getNguoiDungId());
                        if (maXacNhan == null || maXacNhan.getDaXacNhan()
                                || (curDate.compareTo(maXacNhan.getHanHieuLucOtp()) > 0)) {
                            otp = OtpGenerator.generateOtp("otp_key");
                            MaXacNhan mxacnhan = new MaXacNhan();
                            mxacnhan.setNguoiDungId(user.getNguoiDungId());
                            mxacnhan.setMaOtp(otp);
                            mxacnhan.setNgayTaoOtp(new Date());
                            maXacNhanDAO.save(mxacnhan);

                            // Gửi email
                            otpService.generateAndSendOtp(register.getEmail(), "MÃ XÁC NHẬN KÍCH HOẠT TÀI KHOẢN");
                            message = "Mã xác nhận sẽ được gửi đến email của bạn trong vài giây\r\n" +
                                    "Mã có hiệu lực trong 5 phút";
                        } else {// chưa hết hạn lấy mã cũ
                            message = "Mã xác nhận đã được gửi vào email của bạn";
                            otp = maXacNhan.getMaOtp();
                        }
                        model.addAttribute("message", message);
                        model.addAttribute("email", register.getEmail());
                        return "user/active";
                    } catch (Exception err) {
                        model.addAttribute("message", "Email không tồn tại hoặc chưa được đăng ký!");
                        err.printStackTrace();
                    }

                    return "user/active";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Lỗi");
        }
        return "user/register";
    }

    @GetMapping("/user/active")
    public String activeAcc(Model model) {
        NguoiDung nguoiDung = getNguoiDung();
        if (nguoiDung != null) {
            return "redirect:/user/index";
        } else {
            model.addAttribute("message", "Mã xác nhận đã được gửi qua email của bạn.");
            return "user/active";
        }
    }

    @PostMapping("/user/active/{email}")
    public String activeAcc(Model model, @PathVariable("email") String email, @RequestParam("otp") String usetotp) {
        try {
            var user = nguoiDungDao.findByEmail(email);
            var otp = maXacNhanDAO.findByNguoiDungId(user.getNguoiDungId());
            String textPassword = user.getMatKhau();
            user.setMatKhau(passwordEncoder.encode(textPassword));
            nguoiDungDAO.save(user);
            if (usetotp.equals(otp.getMaOtp())) {
                user.setTrangThaiKhoa(false);
                nguoiDungDao.save(user);
                Authentication customAuthentication = new UsernamePasswordAuthenticationToken(
                        user.getSdt(),
                        textPassword);

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

                            chiTietGioHangDAO.themSanPhamVaoGioHang(mauSacId, soLuong, user.getNguoiDungId());
                        }
                        shoppingCartService.clearCart();
                        String checkOut = sessionService.get("checkout");
                        if (checkOut != null) {
                            return "redirect:/user/checkout";
                        }
                    }
                }
                model.addAttribute("message", "Tài khoản được kích hoạt thành công. Quay về trang ");
                model.addAttribute("error", "đăng nhập");
                return "redirect:/user/index";
            }
        } catch (Exception e) {
            model.addAttribute("message", "Có lỗi xảy ra trong quá trình xử lý. Quay về trang ");
            model.addAttribute("error", "đăng nhập");
        }
        return "user/active";
    }

    // Gửi email - quên mật khẩu
    @RequestMapping("/user/forgot-password/sendmail")
    public String sendCode(Model model, @Valid @ModelAttribute("sendForm") EmailForm sendForm, BindingResult result) {
        NguoiDung nguoiDung = getNguoiDung();
        if (nguoiDung != null) {
            return "redirect:/user/index";
        } else {
            String otp = "";
            String message = "";
            try {
                NguoiDung user = nguoiDungDao.findByEmail(sendForm.getEmail());

                Date curDate = new Date();
                MaXacNhan maXacNhan = maXacNhanDAO.findByNguoiDungId(user.getNguoiDungId());
                // MaXacNhan hết hạn tạo mã mới
                if (maXacNhan == null || maXacNhan.getDaXacNhan()
                        || (curDate.compareTo(maXacNhan.getHanHieuLucOtp()) > 0)) {
                    otp = OtpGenerator.generateOtp("otp_key");
                    MaXacNhan mxacnhan = new MaXacNhan();
                    mxacnhan.setNguoiDungId(user.getNguoiDungId());
                    mxacnhan.setMaOtp(otp);
                    mxacnhan.setNgayTaoOtp(new Date());
                    maXacNhanDAO.save(mxacnhan);

                    // Gửi email
                    otpService.generateAndSendOtp(sendForm.getEmail(), "MÃ XÁC NHẬN KHÔI PHỤC MẬT KHẨU");
                    message = "Mã xác nhận sẽ được gửi đến email của bạn trong vài giây\r\n" +
                            "Mã có hiệu lực trong 5 phút";
                } else {// chưa hết hạn lấy mã cũ
                    message = "Mã xác nhận đã được gửi vào email của bạn";
                    otp = maXacNhan.getMaOtp();
                }

                model.addAttribute("message", message);
                return "user/send-code";
            } catch (Exception e) {
                model.addAttribute("message", "Email không tồn tại hoặc chưa được đăng ký!");
                e.printStackTrace();
            }
            return "user/send-code";

        }
    }

    @PostMapping("/user/rate")
    public String rate(@RequestParam("productId") Integer sanPhamId, @RequestParam("productName") String tenSanPham,
            @RequestParam("rating") Integer rating, @RequestParam("comment") String comment) {
        if (getNguoiDung() != null) {
            danhGiaDAO.createDanhGia(sanPhamId, comment, getNguoiDung().getNguoiDungId(), true, rating);
        }
        return "redirect:/user/product-details?productId=" + sanPhamId + "&productName=" + tenSanPham + "#reviews";

    }

    // GET Quên mật khẩu
    @GetMapping("/user/forgot-password")
    public String getForgotPassword(Model model) {
        EmailForm emailForm = new EmailForm();
        model.addAttribute("sendForm", emailForm);
        model.addAttribute("useHeader", getNguoiDung());
        return "user/send-code";
    }

    // Sang trang quên mk
    @PostMapping("/user/forgot-password")
    public String postForgotPassword(Model model, @Valid @ModelAttribute("sendForm") EmailForm sendForm,
            BindingResult result) {
        String message = "Email của bạn chưa được đăng ký";

        if (result.hasErrors()) {
            // in lỗi
        } else {
            try {
                NguoiDung user = nguoiDungDao.findByEmail(sendForm.getEmail());
                MaXacNhan otp = maXacNhanDAO.findByNguoiDungId(user.getNguoiDungId());
                Date curDate = new Date();
                if (otp != null && !otp.getDaXacNhan() && !(curDate.compareTo(otp.getHanHieuLucOtp()) > 0)) {
                    if (sendForm.getEmail().equals(user.getEmail()) && sendForm.getMxn().equals(otp.getMaOtp())) {
                        sessionService.set("user-email", user.getEmail());
                        return "redirect:/user/change-password";
                    } else if (!sendForm.getEmail().equals(user.getEmail())) {
                        message = "Địa chỉ email không hợp lệ";
                    } else {
                        message = "Mã xác nhận không chính xác";
                    }
                } else {
                    message = "Mã xác nhận không chính xác";
                }
            } catch (Exception e) {
                model.addAttribute("message", "Mã xác nhận không đúng!");
            }
            model.addAttribute("message", message);
        }
        return "user/send-code";
    }

    // GET Đổi mật khẩu
    @GetMapping("/user/change-password")
    public String changePassword(Model model, @ModelAttribute("changePassword") ChangePassword change) {
        model.addAttribute("nguoidung", getNguoiDung());
        model.addAttribute("usercheck", getNguoiDung());
        return "user/change-password";
    }

    // Đổi mật khẩu xử lý
    @PostMapping("/user/change-password")
    public String changePassword(Model model, @Valid @ModelAttribute("changePassword") ResetPassword change,
            BindingResult result) {
        if (!result.hasErrors()) {
            NguoiDung nguoiDung = nguoiDungDao.findByEmail(sessionService.get("user-email"));
            Pattern p = Pattern.compile(
                    "^(?=.*[`!@#\\$%\\^&*()-=_+\\\\[\\\\]{}|;':\\\",./<>?])(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])\\S{8,}$");
            Matcher m = p.matcher(change.getNewPassword());
            Boolean b = m.matches();

            if (b == false) {
                model.addAttribute("message",
                        "Mật khẩu bao gồm ít nhất 8 ký tự và phải chứa ít nhất một ký tự in hoa, một ký tự in thường, một chữ số !");
                return "user/change-password";
            }
            if (change.getNewPassword().equals(change.getConfirmPassword())) {
                try {
                    nguoiDung.setMatKhau(passwordEncoder.encode(change.getNewPassword()));
                    nguoiDungDao.save(nguoiDung);
                    model.addAttribute("message", "Đổi mật khẩu thành công. Vui lòng đăng nhập lại");
                    sessionService.remove("user-email");
                    return "redirect:/user/login";
                } catch (Exception e) {
                    model.addAttribute("message", "Có lỗi xảy ra. Quay về ");
                    model.addAttribute("error", "trang đăng nhập");
                    return "user/change-password";
                }
            } else {
                model.addAttribute("message", "Mật khẩu xác nhận không chính xác");
            }
        }
        return "user/change-password";
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    @GetMapping("/user/profile")
    public String profile(Model model, @ModelAttribute("userProfile") NguoiDungDTO user) {
        NguoiDung userForm = getNguoiDung();
        NguoiDungDTO nddto = new NguoiDungDTO();
        nddto.setTenDangNhap(userForm.getTenDangNhap());
        nddto.setHoTen(userForm.getHoTen());
        nddto.setSdt(userForm.getSdt());
        nddto.setEmail(userForm.getEmail());
        nddto.setDiaChi(userForm.getDiaChi());
        nddto.setGioiTinh(userForm.getGioiTinh());
        if (userForm.getNgaySinh() != null) {
            nddto.setNgaySinh(dateToString(userForm.getNgaySinh()));
        } else {
            nddto.setNgaySinh(null);
        }
        String diaChi = nddto.getDiaChi();
        String[] diaChiComponents = null;
        String diaChiCuThe = null;
        String xaPhuong = "";
        String quanHuyen = "";
        String tinhThanh = "";
        if (diaChi == null) {
            diaChi = "";
        } else {
            diaChiComponents = diaChi.split(","); // Tách theo dấu phẩy
            diaChiCuThe = diaChiComponents[0].trim(); // Địa chỉ cụ thể
            xaPhuong = diaChiComponents[diaChiComponents.length - 3].trim(); // Phường/Xã
            quanHuyen = diaChiComponents[diaChiComponents.length - 2].trim(); // Quận/Huyện
            tinhThanh = diaChiComponents[diaChiComponents.length - 1].trim(); // Tỉnh/Thành phố
        }
        nddto.setDiaChi(diaChiCuThe);
        nddto.setXaPhuong(xaPhuong);
        nddto.setQuanHuyen(quanHuyen);
        nddto.setTinhThanh(tinhThanh);
        System.out.print(xaPhuong + " " + quanHuyen + " " + tinhThanh);
        model.addAttribute("userProfile", nddto);
        return "user/profile";
    }

    public int calculateAge(String ngaySinhString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date ngaySinh = dateFormat.parse(ngaySinhString);
            Date ngayHienTai = new Date();

            Calendar calNgaySinh = Calendar.getInstance();
            calNgaySinh.setTime(ngaySinh);
            Calendar calNgayHienTai = Calendar.getInstance();
            calNgayHienTai.setTime(ngayHienTai);

            int tuoi = calNgayHienTai.get(Calendar.YEAR) - calNgaySinh.get(Calendar.YEAR);

            // Kiểm tra nếu chưa qua ngày sinh trong năm hiện tại
            if (calNgayHienTai.get(Calendar.MONTH) < calNgaySinh.get(Calendar.MONTH) ||
                    (calNgayHienTai.get(Calendar.MONTH) == calNgaySinh.get(Calendar.MONTH) &&
                            calNgayHienTai.get(Calendar.DAY_OF_MONTH) < calNgaySinh.get(Calendar.DAY_OF_MONTH))) {
                tuoi--;
            }

            return tuoi;
        } catch (ParseException e) {
            System.out.println("Lỗi xử lý ngày tháng");
            return -1; // Hoặc giá trị đặc biệt nào bạn chọn để biểu thị lỗi
        }
    }

    @PostMapping("/user/profile")
    public String profileUpdate(Model model, @Valid @ModelAttribute("userProfile") NguoiDungDTO userForm,
            BindingResult result) {
        if (result.hasErrors()) {
            if (userForm.getDiaChi().equals("")) {
                model.addAttribute("message", "Địa chỉ không được để trống!");
                return "user/profile";
            }
            return "user/profile";
        }

        NguoiDung user = getNguoiDung();

        NguoiDung userName = nguoiDungDao.findByTenDangNhap(userForm.getTenDangNhap());
        NguoiDung userPhone = nguoiDungDao.findBySdt(userForm.getSdt());
        NguoiDung userEmail = nguoiDungDao.findByEmail(userForm.getEmail());

        String diaChi = userForm.getDiaChi().trim();
        diaChi = diaChi.replace(",", "");
        diaChi = diaChi + ", " + userForm.getXaPhuong().trim() + ", " + userForm.getQuanHuyen().trim() + ", "
                + userForm.getTinhThanh().trim();

        if (userName != null && !userName.getNguoiDungId().equals(user.getNguoiDungId())) {
            model.addAttribute("message", "Tên đăng nhập đã tồn tại!");
            return "user/profile";
        }
        if (userPhone != null && !userPhone.getNguoiDungId().equals(user.getNguoiDungId())) {
            model.addAttribute("message", "Số điện thoại đã tồn tại!");
            return "user/profile";
        }
        if (userEmail != null && !userEmail.getNguoiDungId().equals(user.getNguoiDungId())) {
            model.addAttribute("message", "Email đã tồn tại!");
            return "user/profile";
        }
        System.out.println(calculateAge(userForm.getNgaySinh()));
        if (calculateAge(userForm.getNgaySinh()) < 16) {
            model.addAttribute("message", "Bạn phải đủ 16 tuổi để cập nhật tài khoản!");
            return "user/profile";
        }
        user.setDiaChi(diaChi);
        user.setHoTen(userForm.getHoTen());
        user.setSdt(userForm.getSdt());
        user.setEmail(userForm.getEmail());
        user.setNgaySinh(cstd(userForm.getNgaySinh()));
        user.setGioiTinh(userForm.getGioiTinh());
        nguoiDungDao.save(user);
        model.addAttribute("message", "Cập nhật thành công");
        String checkout = sessionService.get("checkout");
        if (checkout != null) {
            sessionService.remove("checkout");
            return "redirect:/user/checkout";
        }

        return "user/profile";
    }

    // GET Đổi mật khẩu từ profile
    @GetMapping("/user/profile/change-password")
    public String changePasswordProfile(Model model) {
        ChangePassword change = new ChangePassword();
        model.addAttribute("changePassword", change);
        return "user/change-password-profile";
    }

    // Đổi mật khẩu xử lý từ profile
    @PostMapping("/user/profile/change-password")
    public String changePasswordProfile(Model model, @Valid @ModelAttribute("changePassword") ChangePassword change,
            BindingResult result) {
        if (!result.hasErrors()) {
            NguoiDung nguoiDung = getNguoiDung();
            Pattern p = Pattern.compile(
                    "^(?=.*[`!@#\\$%\\^&*()-=_+\\\\[\\\\]{}|;':\\\",./<>?])(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])\\S{8,}$");
            Matcher m = p.matcher(change.getNewPassword());
            Boolean b = m.matches();
            if (getNguoiDung() != null) {
                nguoiDung = getNguoiDung();

                if (change.getCurrentPassword() == "") {
                    model.addAttribute("message", "Hãy nhập mật khẩu hiện tại!");
                    return "user/change-password-profile";
                }
                if (passwordEncoder.matches(change.getCurrentPassword(), nguoiDung.getMatKhau()) == false) {
                    model.addAttribute("message", "Mật khẩu hiện tại chưa chính xác!");
                    return "user/change-password-profile";
                }
                if (b == false) {
                    model.addAttribute("message",
                            "Mật khẩu bao gồm ít nhất 8 ký tự và phải chứa ít nhất một ký tự in hoa, một ký tự in thường, một chữ số !");
                    return "user/change-password-profile";
                }
                if (change.getNewPassword().equals(change.getConfirmPassword())) {
                    try {
                        nguoiDung.setMatKhau(passwordEncoder.encode(change.getNewPassword()));
                        nguoiDungDao.save(nguoiDung);
                        model.addAttribute("message", "Đổi mật khẩu thành công");
                        sessionService.remove("user-email");
                        return "user/change-password-profile";
                    } catch (Exception e) {
                        model.addAttribute("message", "Có lỗi xảy ra. Quay về ");
                        model.addAttribute("error", "trang đăng nhập");
                        return "user/change-password-profile";
                    }
                } else {
                    model.addAttribute("message", "Mật khẩu xác nhận không chính xác");
                }
            } else {
                model.addAttribute("message", "Có lỗi xảy ra trong quá trình xử lý. Quay về ");
                model.addAttribute("error", "trang chủ.");
            }
        }
        return "user/change-password-profile";
    }

    @GetMapping("/user/orders-list")
    public String getOrderList(Model model) {
        NguoiDung nguoiDung = getNguoiDung();
        List<DonHang> donHangs = donHangDAO.findByNguoiDungOrderByNgayDatHangDesc(nguoiDung);
        model.addAttribute("donHangs", donHangs);
        return "user/orders-list";
    }

    @Autowired
    ChiTietGioHangRestController chiTietGioHangRestController;

    @GetMapping("/user/checkout")
    public String checkout(Model model) {
        NguoiDung nguoiDung = getNguoiDung();
        if (nguoiDung == null) {
            sessionService.set("checkout", "true");
            return "redirect:/user/login";
        } else {
            if (nguoiDung.getDiaChi() != null) {
                List<HinhThucThanhToan> paymentMethods = htttDAO.findAll();
                model.addAttribute("nguoiDung", nguoiDung);
                model.addAttribute("cart", restTemplate.get("http://localhost:8080/checkoutCart?address="
                        + nguoiDung.getDiaChi() + "&userId=" + nguoiDung.getNguoiDungId()));
                model.addAttribute("paymentMethods", paymentMethods);
            } else {
                NguoiDung userForm = getNguoiDung();
                NguoiDungDTO nddto = new NguoiDungDTO();
                nddto.setTenDangNhap(userForm.getTenDangNhap());
                nddto.setHoTen(userForm.getHoTen());
                nddto.setSdt(userForm.getSdt());
                nddto.setEmail(userForm.getEmail());
                nddto.setDiaChi(userForm.getDiaChi());
                nddto.setGioiTinh(userForm.getGioiTinh());
                if (userForm.getNgaySinh() != null) {
                    nddto.setNgaySinh(dateToString(userForm.getNgaySinh()));
                } else {
                    nddto.setNgaySinh(null);
                }
                String diaChi = nddto.getDiaChi();
                String[] diaChiComponents = null;
                String diaChiCuThe = null;
                String xaPhuong = "";
                String quanHuyen = "";
                String tinhThanh = "";
                if (diaChi == null) {
                    diaChi = "";
                } else {
                    diaChiComponents = diaChi.split(","); // Tách theo dấu phẩy
                    diaChiCuThe = diaChiComponents[0].trim(); // Địa chỉ cụ thể
                    xaPhuong = diaChiComponents[diaChiComponents.length - 3].trim(); // Phường/Xã
                    quanHuyen = diaChiComponents[diaChiComponents.length - 2].trim(); // Quận/Huyện
                    tinhThanh = diaChiComponents[diaChiComponents.length - 1].trim(); // Tỉnh/Thành phố
                }
                nddto.setDiaChi(diaChiCuThe);
                nddto.setXaPhuong(xaPhuong);
                nddto.setQuanHuyen(quanHuyen);
                nddto.setTinhThanh(tinhThanh);
                System.out.print(xaPhuong + " " + quanHuyen + " " + tinhThanh);
                model.addAttribute("userProfile", nddto);
                model.addAttribute("message",
                        "Bạn chưa có địa chỉ giao hàng. Vui lòng cập nhật địa chỉ giao hàng trước khi thanh toán.");
                return "user/profile";
            }
            return "user/checkout";
        }
    }

    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null; // Trả về null nếu không thể chuyển đổi
        }
    }

    public static Date cstd(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null; // Trả về null nếu không thể chuyển đổi
        }
    }

    @PostMapping("/user/checkout")
    public String paymentVnpay(
            @RequestParam(value = "phuongThucThanhToanId", defaultValue = "1") int phuongThucThanhToanId,
            @RequestParam(value = "ghiChu", required = false) String ghiChu,
            Model model) throws InvalidKeyException, UnsupportedEncodingException, UnknownHostException,
            SignatureException, NoSuchAlgorithmException {
        NguoiDung nguoiDung = getNguoiDung();
        JsonNode map = restTemplate
                .get("http://localhost:8080/checkoutSpecifiedCart?address=" + nguoiDung.getDiaChi()
                        + "&userId=" + nguoiDung.getNguoiDungId());
        {
            String tongPhiVanChuyen = map.get("tongPhiVanChuyen").asText();
            String ngayGiaoHangDuKien = map.get("ngayGiaoHangDuKien").asText();
            if (phuongThucThanhToanId == 1) {

                if (tongPhiVanChuyen != "null" && ngayGiaoHangDuKien != "null") {
                    String orderId = donHangDAO.taoDonHang(nguoiDung.getNguoiDungId(), 1,
                            convertStringToDate(ngayGiaoHangDuKien), null, Double.parseDouble(tongPhiVanChuyen));
                    orderService.sendEmailOrderSuccess(Integer.parseInt(orderId));
                    return "redirect:/user/find-order?donHangId=" + orderId;
                } else {
                    String orderId = donHangDAO.taoDonHang(nguoiDung.getNguoiDungId(), 1, null, null, 0.0);
                    orderService.sendEmailOrderSuccess(Integer.parseInt(orderId));
                    return "redirect:/user/find-order?donHangId=" + orderId;
                }
            } else {
                if (tongPhiVanChuyen != "null" && ngayGiaoHangDuKien != "null") {
                    String orderId = donHangDAO.taoDonHang(nguoiDung.getNguoiDungId(), 1,
                            convertStringToDate(ngayGiaoHangDuKien), null, Double.parseDouble(tongPhiVanChuyen));
                    DonHang dh = donHangDAO.findById(Integer.parseInt(orderId)).orElse(null);
                    double value = dh.getTongTienSanPham() + dh.getPhiVanChuyen();
                    long longValue = (long) value;
                    orderService.sendEmailOrderSuccess(Integer.parseInt(orderId));
                    return "redirect:" + paymentvnpay("Thanh toán đơn hàng " + orderId, longValue);
                } else {
                    String orderId = donHangDAO.taoDonHang(nguoiDung.getNguoiDungId(), 1, null, null, 0.0);
                    DonHang dh = donHangDAO.findById(Integer.parseInt(orderId)).orElse(null);
                    double value = dh.getTongTienSanPham() + dh.getPhiVanChuyen();
                    long longValue = (long) value;
                    orderService.sendEmailOrderSuccess(Integer.parseInt(orderId));
                    return "redirect:" + paymentvnpay("Thanh toán đơn hàng " + orderId, longValue);
                }
            }
        }
    }

    @GetMapping("/user/recheckout")
    public String recheckout(@RequestParam("orderId") Integer orderId) throws InvalidKeyException,
            UnsupportedEncodingException, UnknownHostException, SignatureException, NoSuchAlgorithmException {
        DonHang dh = donHangDAO.findById(orderId).orElse(null);
        double value = dh.getTongTienSanPham() + dh.getPhiVanChuyen();
        long longValue = (long) value;
        return "redirect:" + paymentvnpay("Thanh toán đơn hàng " + dh.getDonHangId(), longValue);
    }

    @GetMapping("/product/quick-view/{id}")
    public String quickView(@PathVariable(value = "id") int sanPhamId, Model model) {
        SanPham sanPham = sanPhamDAO.findById(sanPhamId).get();

        List<MauSac> mauSacList = new ArrayList<>();
        List<MauSac> spMauSacList = sanPham.getSanPhamMauSacs();
        for (MauSac mauSac : spMauSacList) {
            List<ChiTietSanPham> chiTietSanPhamList = mauSac.getMauSacChiTietSanPhams();
            boolean coSanPhamConHang = false;
            for (ChiTietSanPham chiTietSanPham : chiTietSanPhamList) {
                int soLuongTrongKho = chiTietSanPham.getSoLuongTrongKho();
                if (soLuongTrongKho > 0) {
                    coSanPhamConHang = true;
                    break;
                }
            }
            if (coSanPhamConHang) {
                mauSacList.add(mauSac);
            }
        }
        sanPham.setSanPhamMauSacs(mauSacList);

        if (!mauSacList.isEmpty()) {
            model.addAttribute("quickView", sanPham);
        }

        return "user/index";
    }

    @Autowired
    GioHangDAO cartDAO;

    @GetMapping("/user/contact")
    public String contact(Model model) {
        NguoiDung nguoiDung = getNguoiDung();
        if (nguoiDung == null) {
            model.addAttribute("lienhe", new LienHe());
        } else {
            model.addAttribute("lienhe", new LienHe(nguoiDung.getHoTen(), nguoiDung.getEmail()));
        }
        return "user/contact";
    }

    @Autowired
    DonHangDAO donHangDAO;
    @Autowired
    com.klbstore.dao.DanhGiaDAO danhGiaDAO;

    public String paymentvnpay(String tenGiaoDich, long soTienThanhToan)
            throws UnsupportedEncodingException, UnknownHostException, InvalidKeyException,
            SignatureException, NoSuchAlgorithmException {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        String createDate = sdf.format(currentDate);

        calendar.add(Calendar.MINUTE, 5);
        Date futureDate = calendar.getTime();

        // Gán ngày hết hạn xa hơn với định dạng chuẩn
        String expireDate = sdf.format(futureDate);

        // Sử dụng createDate và expireDate trong chuỗi input data
        String input = "vnp_Amount=" + (soTienThanhToan * 100) +
                "&vnp_Command=pay" +
                "&vnp_CreateDate=" + createDate +
                "&vnp_CurrCode=VND" +
                "&vnp_ExpireDate=" + expireDate +
                "&vnp_IpAddr=127.0.0.1" +
                "&vnp_Locale=vn" +
                "&vnp_OrderInfo=" + URLEncoder.encode(tenGiaoDich, StandardCharsets.UTF_8) +
                "&vnp_OrderType=other" +
                "&vnp_ReturnUrl=" + URLEncoder.encode(VnpayConfig.vnp_Returnurl, StandardCharsets.UTF_8) +
                "&vnp_TmnCode=ZRW18TX8" +
                "&vnp_TxnRef=" + VnpayConfig.getRandomNumber(8) +
                "&vnp_Version=2.1.0";
        String vnp_SecureHash = VnpayConfig.hmacSHA512("SNLNODLJHCHDAKWQUZEPFCIECKIRPTIE", input);
        input += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + input;
        return paymentUrl;
    }

    public Integer getDonHangId(String chuoi) {
        int viTriSo = -1;
        for (int i = 0; i < chuoi.length(); i++) {
            if (Character.isDigit(chuoi.charAt(i))) {
                viTriSo = i;
                break;
            }
        }
        if (viTriSo != -1) {
            StringBuilder so = new StringBuilder();
            while (viTriSo < chuoi.length() && Character.isDigit(chuoi.charAt(viTriSo))) {
                so.append(chuoi.charAt(viTriSo));
                viTriSo++;
            }
            return Integer.parseInt(so.toString());
        }

        return null;
    }

    @Autowired
    OrderService orderService;

    @GetMapping("/user/payment")
    public String payment(@RequestParam("vnp_ResponseCode") String vnp_ResponseCode,
            @RequestParam("vnp_OrderInfo") String vnp_OrderInfo, Model model) {
        if (vnp_ResponseCode.equals("00")) {
            Integer donHangId = getDonHangId(vnp_OrderInfo);
            DonHang donHang = donHangDAO.findById(donHangId).orElse(null);
            if (donHang != null) {
                donHangDAO.updatePaymentMethod(donHangId, 2);
                donHangDAO.capNhatTrangThaiThanhToanDonHang(donHang.getDonHangId());
                orderService.sendEmailCheckoutSuccess(donHang);
            }
            return "redirect:/user/find-order?donHangId=" + donHangId;
        } else {
            Integer donHangId = getDonHangId(vnp_OrderInfo);
            return "redirect:/user/find-order?donHangId=" + donHangId;
        }
    }

    @Autowired
    com.klbstore.dao.NguoiDungDAO nguoiDungDAO;

    @Autowired
    com.klbstore.dao.GioHangDAO gioHangDAO;

    @Autowired
    com.klbstore.dao.ChiTietGioHangDAO chiTietGioHangDAO;

    @GetMapping("/user/product-details")
    public String getProductDetails(@RequestParam("productId") Integer productId,
            @RequestParam("productName") String productName,
            Model model) {
        if (productId == null || productName == null) {
            return "redirect:/user/404";
        }
        model.addAttribute("chiTiet", restTemplate.get("http://localhost:8080/rest/xinchao?hienThi=true&sanPhamId="
                + productId + "&tenSanPham=" + productName));
        model.addAttribute("sanPhamLienQuan",
                restTemplate.get("http://localhost:8080/rest/hello?hienThi=true&related=" + productId));
        model.addAttribute("danhGias",
                restTemplate.get("http://localhost:8080/rest/danhGia?sanPhamId=" + productId));
        NguoiDung nguoiDung = nguoiDungDAO
                .findByTenDangNhap(SecurityContextHolder.getContext().getAuthentication().getName());
        if (nguoiDung != null) {
            model.addAttribute("canUserRateProduct",
                    restTemplate.get("http://localhost:8080/rest/canUserRateProduct?sanPhamId=" + productId
                            + "&nguoiDungId=" + nguoiDung.getNguoiDungId()));
        }

        return "user/product-details";

    }

    @RequestMapping("/user/shop-list")
    public String searchAndPage(Model model,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "nhomSanPhamId", defaultValue = "") int nhomSanPhamId,
            @RequestParam(value = "sortBy", defaultValue = "") String sortBy) {
        model.addAttribute("pg", restTemplate.get("http://localhost:8080/rest/sanpham?hienThi=true" + "&page=" + page
                + "&nhomSanPhamId=" + nhomSanPhamId + "&sortBy=" + sortBy));
        model.addAttribute("sortBy", sortBy);
        return "user/shop-list";
    }

    @RequestMapping("/user/search")
    public String search(Model model,
            @RequestParam(value = "keywords", defaultValue = "") String keywords,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "sortBy", defaultValue = "") String sortBy) {
        model.addAttribute("pg", restTemplate.get("http://localhost:8080/rest/sanpham?hienThi=true" + "&page=" + page
                + "&tenSanPham=" + keywords + "&sortBy=" + sortBy));
        model.addAttribute("sortBy", sortBy);
        return "user/shop-list";
    }

    @Autowired
    ContactService contactService;

    @GetMapping("/user/shopping-cart")
    public String shoppingCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        NguoiDung nguoiDung = nguoiDungDAO.findByTenDangNhap(authentication.getName());
        if (nguoiDung != null) {
            chiTietGioHangDAO.kiemTraVaXoaSanPhamKhongHopLeTrongGioHang(nguoiDung.getNguoiDungId());
            AllChiTietGioHangDTO allChiTietGioHangDTO = new AllChiTietGioHangDTO(
                    chiTietGioHangDAO.layDanhSachSanPhamTrongGioHangTheoNguoiDung(nguoiDung.getNguoiDungId()),
                    chiTietGioHangDAO.tongSoSanPhamTrongGioHang(nguoiDung.getNguoiDungId()),
                    chiTietGioHangDAO.tinhTongTienTrongGioHang(nguoiDung.getNguoiDungId()), null,
                    null);
            model.addAttribute("allCart", allChiTietGioHangDTO);
        } else {
            model.addAttribute("allCart", shoppingCartService.getShoppingCartDTO());
        }
        return "user/shopping-cart";
    }

    @GetMapping("/user/wishlist")
    public String wishlist() {
        return "user/wishlist";
    }

    public Double getTotalAmountOrder(List<ChiTietDonHang> list) {
        Double total = 0.0;
        for (ChiTietDonHang ctdh : list) {
            total += ctdh.getGiaBan() * ctdh.getSoLuong();
        }
        return total;
    }

    // @GetMapping("/user/order")
    // public String getOrder(Model model) {
    // NguoiDung nguoiDung = getNguoiDung();
    // if (nguoiDung != null) {
    // List<DonHang> donHangs =
    // donHangDAO.findByNguoiDung_NguoiDungId(nguoiDung.getNguoiDungId());
    // for (DonHang donHang : donHangs) {
    // donHang.setTongDonHang(getTotalAmountOrder(donHang.getDonHangChiTietDonHangs()));
    // }
    // model.addAttribute("donHangs", donHangs);
    // }
    // return "user/order";
    // }
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value); // Cố gắng chuyển đổi giá trị thành số nguyên
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @GetMapping("/user/find-order")
    public String findOrder(@RequestParam(name = "donHangId", required = false) Integer donHangId, Model model) {
        NguoiDung nguoiDung = getNguoiDung();
        if (nguoiDung != null) {
            DonHang dh = donHangDAO.findById(donHangId).orElse(null);
            if (dh != null) {
                model.addAttribute("donHangs", dh);
            }
        }
        return "user/order";
    }

    @Autowired
    ChiTietDonHangDAO chiTietDonHangDAO;

    @GetMapping("/user/order-details")
    public String getOrder(@RequestParam(name = "donHangId", required = false) Integer donHangId,
            Model model) {
        NguoiDung nguoiDung = getNguoiDung();
        if (nguoiDung != null) {
            List<ChiTietDonHang> ctdh = chiTietDonHangDAO.findByDonHangId(donHangId);
            model.addAttribute("chiTietDonHangLists", ctdh);
        }
        return "user/order-detail";
    }
}