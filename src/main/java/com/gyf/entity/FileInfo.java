package com.gyf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 郭云飞
 * @since 2022-02-23
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 文件id
     */
        @TableId(value = "file_id", type = IdType.AUTO)
      private Integer fileId;

      /**
     * 文件名
     */
      private String fileName;

      /**
     * 文件类型
     */
      private String fileType;

      /**
     * 上传时间
     */
      private LocalDateTime uploadDate;

      /**
     * 文件大小
     */
      private Long fileSize;

      /**
     * 上传人
     */
      private String uploader;

}
