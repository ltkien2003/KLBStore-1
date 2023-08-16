package com.klbstore.dto;

import javax.validation.constraints.NotBlank;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ChangePassword {
    @NotBlank(message = "Mật khẩu hiện tại không được để trống")
    String currentPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu mới không được để trống")
    String confirmPassword;
}
