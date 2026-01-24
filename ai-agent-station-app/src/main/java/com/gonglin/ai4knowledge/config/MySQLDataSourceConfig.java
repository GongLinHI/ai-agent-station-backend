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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * 通过MyBatis
 * <p>
 * 业务代码调用 Mapper 接口
 * │
 * ▼
 * mysqlSqlSessionTemplate (代理 Mapper 方法)
 * │
 * ▼
 * mysqlSqlSessionFactory (创建 SqlSession)
 * │
 * ▼
 * mysqlDataSource (获取数据库连接)
 * │
 * ▼
 * MySQL 数据库执行 SQL
 * <p>
 * <p>
 * 不通过 MyBatis
 * <p>
 * 业务代码调用 JdbcTemplate
 * │
 * ▼
 * mysqlJdbcTemplate
 * │
 * ▼
 * mysqlDataSource (获取数据库连接)
 * │
 * ▼
 * MySQL 数据库执行 SQL
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.datasource.mysql", name = "url")
@MapperScan(basePackages = "com.gonglin.ai4knowledge.infrastructure.mysql.mapper",
        sqlSessionFactoryRef = "mysqlSqlSessionFactory")
public class MySQLDataSourceConfig {
    @Primary
    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource mysqlDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setName("PostgreSQLDruidPool");
        dataSource.setValidationQuery("SELECT 1");
        return dataSource;
    }

    @Primary
    @Bean(name = "mysqlSqlSessionFactory")
    public SqlSessionFactory mysqlSqlSessionFactory(
            @Qualifier("mysqlDataSource") DataSource dataSource,
            @Value("${mybatis.config-location:classpath:mybatis-config.xml}") String mybatisConfigPath
    ) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath:mapper/mysql/*.xml"));
            bean.setConfigLocation(resolver.getResource(mybatisConfigPath));
        } catch (Exception e) {
            bean.setMapperLocations();
        }
        return bean.getObject();
    }

    @Primary
    @Bean(name = "mysqlSqlSessionTemplate")
    public SqlSessionTemplate mysqlSqlSessionTemplate(
            @Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(Objects.requireNonNull(sqlSessionFactory));
    }

    @Primary
    @Bean(name = "mysqlJdbcTemplate")
    public JdbcTemplate mysqlJdbcTemplate(@Qualifier("mysqlDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource, true);
    }
}
