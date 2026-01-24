package com.gonglin.ai4knowledge.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientRagOrderUploadRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 知识库名称（非空字符串，最大长度50）
     */
    private String name;

    /**
     * 知识标签（非空字符串，最大长度50）
     */
    private String tag;

    /**
     * 上传文件列表（非空，至少包含一个文件）
     */
    private List<MultipartFile> files;
}
