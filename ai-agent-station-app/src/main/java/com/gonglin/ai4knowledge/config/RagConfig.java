package com.gonglin.ai4knowledge.config;

import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(prefix = "spring.datasource.postgresql", name = "url")
public class RagConfig {
    @Bean
    public TokenTextSplitter tokenTextSplitter() {
        return new TokenTextSplitter();
    }

    @Bean("pgVectorStore")
    public PgVectorStore pgVectorStore(@Qualifier("postgresqlJdbcTemplate") JdbcTemplate jdbcTemplate) {
        // 嵌入模型配置
        OpenAiApi deepseekEmbeddingApi = OpenAiApi.builder()
                .baseUrl("https://api.siliconflow.cn/")
                .apiKey("sk-fqxfpupthbtjotojseclmfjwplhtzswufmhoyffjfezlssdh")
                .completionsPath("v1/chat/completions")
                .embeddingsPath("v1/embeddings")
                .build();

        OpenAiEmbeddingOptions deepseekEmbeddingOptions = OpenAiEmbeddingOptions.builder()
                .model("BAAI/bge-m3")
                .dimensions(1024)
                .build();
        OpenAiEmbeddingModel embeddingModel = new OpenAiEmbeddingModel(
                deepseekEmbeddingApi,
                MetadataMode.EMBED,
                deepseekEmbeddingOptions);


        PgVectorStore deepSeekPgVectorStore = PgVectorStore.builder(
                        jdbcTemplate, embeddingModel)
                .vectorTableName("vector_store_1024")
                .build();
        return deepSeekPgVectorStore;
    }
}
