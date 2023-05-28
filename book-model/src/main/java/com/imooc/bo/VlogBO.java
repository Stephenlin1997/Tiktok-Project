package com.imooc.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VlogBO {
    @NotBlank(message = "必须不为空")
    private String id;

    @NotBlank(message = "必须不为空")
    private String vlogerId;

    @NotBlank(message = "必须不为空")
    @Pattern(regexp = "^(https?|ftp|file)://.*$",message = "必须为正确的url开头如http-ftp-file")
    private String url;

    @NotBlank(message = "必须不为空")
    @Pattern(regexp = "^(https?|ftp|file)://.*$",message = "必须为正确的cover开头如http-ftp-file")
    private String cover;

    @NotBlank(message = "必须不为空")
    private String title;

    @Range(min = 0, max = 1080, message = "分辨率最高为1920 x 1080")
    private Integer width;

    @Range(min = 0, max = 1920, message = "分辨率最高为1920 x 1080")
    private Integer height;

    private Integer likeCounts;

    private Integer commentsCounts;
}