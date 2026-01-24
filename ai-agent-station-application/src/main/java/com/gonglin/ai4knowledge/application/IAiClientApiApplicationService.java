package com.gonglin.ai4knowledge.application;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiApi;

import java.util.List;

public interface IAiClientApiApplicationService {
    AiApi createAiClientApi(AiApi aiApi);

    AiApi updateAiClientApiById(AiApi aiApi);

    AiApi updateAiClientApiByApiId(AiApi aiApi);

    void deleteAiClientApiById(Long id);

    void deleteAiClientApiByApiId(String apiId);

    AiApi queryAiClientApiById(Long id);

    AiApi queryAiClientApiByApiId(String apiId);

    List<AiApi> queryEnabledAiClientApis();

    List<AiApi> queryAiClientApiList(AiApi aiApi, Integer pageNum, Integer pageSize);

    List<AiApi> queryAllAiClientApis();
}
