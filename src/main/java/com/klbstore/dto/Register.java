package com.klbstore.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Register {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    String tenDangNhap;

    @NotBlank(message = "Họ tên không được để trống")
    String hoTen;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không không đúng định dạng")
    String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    String sdt;
    
    @NotBlank(message = "Mật khẩu không được để trống")
    String matKhau;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    String nhapLaiMatKhau;
}
