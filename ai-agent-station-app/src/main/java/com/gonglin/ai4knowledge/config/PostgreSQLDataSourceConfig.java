package com.gonglin.ai4knowledge.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "spring.datasource.postgresql", name = "url")
@MapperScan(basePackages = "com.gonglin.ai4knowledge.infrastructure.postgresql.mapper",
        sqlSessionFactoryRef = "postgresqlSqlSessionFactory")
public class PostgreSQLDataSourceConfig {

    @Bean(name = "postgresqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.postgresql")
    public DataSource postgresqlDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setName("PostgreSQLDruidPool");
        dataSource.setValidationQuery("SELECT 1");
        return dataSource;
    }

    @Bean(name = "postgresqlSqlSessionFactory")
    public SqlSessionFactory postgresqlSqlSessionFactory(
            @Qualifier("postgresqlDataSource") DataSource dataSource,
            @Value("${mybatis.config-location:classpath:mybatis-config.xml}") String mybatisConfigPath
    ) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath:mapper/postgresql/*.xml"));
            bean.setConfigLocation(resolver.getResource(mybatisConfigPath));
        } catch (Exception e) {
            bean.setMapperLocations();
        }
        return bean.getObject();
    }

    @Bean(name = "postgresqlSqlSessionTemplate")
    public SqlSessionTemplate postgresqlSqlSessionTemplate(
            @Qualifier("postgresqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "postgresqlJdbcTemplate")
    public JdbcTemplate postgresqlJdbcTemplate(@Qualifier("postgresqlDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource, true);
    }
}
