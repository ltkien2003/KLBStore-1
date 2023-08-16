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
    @Pattern(message = "Sai định dạng số điện thoại", regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$")
    String sdt;
    
    @NotBlank(message = "Mật khẩu không được để trống")
    String matKhau;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    String nhapLaiMatKhau;
}
