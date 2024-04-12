package com.gyf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author 郭云飞
 * @since 2021-12-21
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
public class UserInfo implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 用户的账号
     */
        @TableId("USER_ACCOUNT")
      private String userAccount;

      /**
     * 用户的密码
     */
      @TableField("USER_PASSWORD")
    private String userPassword;

      /**
     * 用户的昵称
     */
      @TableField("USER_NIKENAME")
    private String userNikename;

      /**
     * 用户账号的注册时间
     */
      @TableField("REGISTERED_DATE")
    private LocalDate registeredDate;

}
