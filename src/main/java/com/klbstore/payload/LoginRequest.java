package com.klbstore.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Số điện thoại không được bỏ trống")
    String sdt;

    @NotBlank(message = "Mật khẩu không được bỏ trống")
    String matKhau;
}
