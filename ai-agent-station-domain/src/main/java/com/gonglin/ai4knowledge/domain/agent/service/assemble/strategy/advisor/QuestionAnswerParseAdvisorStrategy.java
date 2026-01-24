package com.gonglin.ai4knowledge.domain.agent.service.assemble.strategy.advisor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.domain.agent.model.entity.AiAdvisorEntity;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.QuestionAnswerConfig;
import com.gonglin.ai4knowledge.types.common.AdvisorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service("questionAnswer")
public class QuestionAnswerParseAdvisorStrategy implements IParseAdvisorStrategy {


    private final ObjectMapper jsonMapper;
    private final PgVectorStore pgVectorStore;

    @Autowired
    public QuestionAnswerParseAdvisorStrategy(@Qualifier("jsonMapper") ObjectMapper jsonMapper,
                                              @Qualifier("pgVectorStore") PgVectorStore pgVectorStore) {
        this.jsonMapper = jsonMapper;
        this.pgVectorStore = pgVectorStore;
    }

    @Override
    public Advisor parse(AiAdvisorEntity aiAdvisorEntity) {
        if (aiAdvisorEntity == null) {
            log.warn("解析 Advisor 失败：传入 aiAdvisorEntity 为空，无法加载数据");
            return null;
        } else if (!AdvisorType.QUESTION_ANSWER.equalsIgnoreCase(aiAdvisorEntity.getAdvisorType())) {
            log.warn("解析 Advisor 失败：传入 aiAdvisorEntity 类型不匹配，无法加载数据，期待类型：{}，实际类型：{}",
                     AdvisorType.QUESTION_ANSWER, aiAdvisorEntity.getAdvisorType());
            return null;
        }

        String extParam = aiAdvisorEntity.getExtParam();
        if (extParam == null || extParam.isBlank()) {
            extParam = "{}";
        }
        QuestionAnswerConfig config;
        try {
            config = jsonMapper.readValue(extParam, QuestionAnswerConfig.class);
        } catch (Exception e) {
            log.error("解析 Advisor 失败：传入 aiAdvisorEntity 扩展参数无法转换为 SearchRequest 对象，extParam：{}",
                      extParam, e);
            return null;
        }
        SearchRequest request = SearchRequest.builder()
                .topK(config.getTopK())
                .query(config.getQuery())
                .filterExpression(config.getFilterExpression())
                .similarityThreshold(config.getSimilarityThreshold())
                .build();
        return QuestionAnswerAdvisor.builder(this.pgVectorStore)
                .searchRequest(request)
                .build();
    }
}
