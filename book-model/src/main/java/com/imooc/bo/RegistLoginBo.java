package com.imooc.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegistLoginBo {
        @NotBlank(message = "手机号不能为空")
        @Length(min = 10, max = 10, message = "手机长度不正确")
        private String mobile;
        @NotBlank(message = "手机号不能为空")
        private String smsCode;

}
