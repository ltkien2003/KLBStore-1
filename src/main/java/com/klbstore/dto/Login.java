package com.klbstore.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login implements Serializable{
    @NotBlank(message = "Hãy nhập số điện thoại")
    private String sdt;
    
    @NotBlank(message = "Hãy nhập mật khẩu")
    private String matKhau;
}
