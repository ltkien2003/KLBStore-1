package com.klbstore.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;



@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class EmailForm implements Serializable{
    @NotBlank(message = "Hãy nhập email của bạn")
    @Email(message = "Email không đúng định dạng")
    private String email;

    // @NotBlank(message = "Vui lòng nhập mã xác nhận")
    private String mxn;
}
