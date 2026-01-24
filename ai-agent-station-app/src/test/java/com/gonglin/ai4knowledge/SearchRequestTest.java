package com.gonglin.ai4knowledge;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class SearchRequestTest {
    @Test
    void test() {
        SearchRequest request = SearchRequest.builder().build();
        Filter.Expression filterExpression = request.getFilterExpression();

        log.info("{}", filterExpression);
    }
}
