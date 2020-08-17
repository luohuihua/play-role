package com.luohh.playrole.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.luohh.playrole.config.properties.DruidProperties;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * MybatisPlus与数据源相关配置
 */
//@EnableTransactionManagement
@Configuration
//@EnableConfigurationProperties(value = {MybatisPlusProperties.class})
public class MybatisPlusConfig {
//    private MybatisPlusProperties mybatisPlusProperties;
//
//    public MybatisPlusConfig(MybatisPlusProperties mybatisPlusProperties) {
//        this.mybatisPlusProperties = mybatisPlusProperties;
//    }


    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 属性自动拼装
     *
     * @return
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MyMetaObjectHandler();
    }

    /**
     * 主库注入
     *
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid.master")
    public DataSource masterDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }
//    @Bean
//    @ConfigurationProperties("spring.datasource.druid.master")
//    public DataSource masterDataSource(DruidProperties druidProperties) {
//        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
//        return druidProperties.dataSource(dataSource);
//    }

    /**
     * 分库注入
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.slave")
    public DataSource slaveDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Bean
    public DataSource myRoutingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                          @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.MASTER, masterDataSource);
        targetDataSources.put(DBTypeEnum.SLAVE, slaveDataSource);
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);// 主库数据源
        myRoutingDataSource.setTargetDataSources(targetDataSources);// 路由数据源
        return myRoutingDataSource;
    }


    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(MetaObjectHandler metaObjectHandler, PaginationInterceptor paginationInterceptor, DataSource myRoutingDataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        // 配置多数据源
        sqlSessionFactory.setDataSource(myRoutingDataSource);
        //扫描mybatis  xml路径
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/**/*Mapper.xml"));
        // 实体扫描，多个package用逗号或者分号分隔
        sqlSessionFactory.setTypeAliasesPackage("com.luohh.playrole.*.model");

        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        // 主键类型  AUTO:"数据库ID自增", INPUT:"用户输入ID",ID_WORKER:"全局唯一ID (数字类型唯一ID)", UUID:"全局唯一ID UUID";
        dbConfig.setIdType(IdType.AUTO);
        // 字段策略 IGNORED:"忽略判断",NOT_NULL:"非 NULL 判断"),NOT_EMPTY:"非空判断"
        dbConfig.setInsertStrategy(FieldStrategy.NOT_NULL);
        dbConfig.setUpdateStrategy(FieldStrategy.NOT_NULL);
        dbConfig.setSelectStrategy(FieldStrategy.NOT_NULL);

        // 数据库大写下划线转换
        dbConfig.setCapitalMode(true);
        // 逻辑删除配置
        dbConfig.setLogicDeleteValue("1");
        dbConfig.setLogicNotDeleteValue("0");
        // 表前缀
        dbConfig.setTablePrefix("");

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        //#刷新mapper 调试神器
        globalConfig.setDbConfig(dbConfig);
        globalConfig.setMetaObjectHandler(metaObjectHandler);
        sqlSessionFactory.setGlobalConfig(globalConfig);
        // mybatis
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        configuration.setCallSettersOnNulls(true);
        //这个配置会将执行的sql打印出来，在开发或测试的时候可以用
        configuration.setLogImpl(StdOutImpl.class);
        sqlSessionFactory.setConfiguration(configuration);
        //  添加分页功能
        sqlSessionFactory.setPlugins(paginationInterceptor);
        return sqlSessionFactory.getObject();
    }
}