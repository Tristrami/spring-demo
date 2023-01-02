package com.seamew.config;

import com.seamew.mapper.IUserMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

import javax.sql.DataSource;

@Configuration
// 使用 @MapperScan 注解后就无需逐个注入 mapper 实现类了，mybatis 会扫描 mapper 接口并创建代理对象
// 我们只需要向容器中注入 SqlSessionFactory 即可
@MapperScan("com.seamew.mapper")
public class MybatisConfig
{
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Autowired DataSource ds) throws Exception
    {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(ds);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.seamew.entity");
        return sqlSessionFactoryBean.getObject();
    }

//    @Bean
//    public IUserMapper userMapper(@Autowired SqlSessionFactory factory)
//    {
//        SqlSessionTemplate template = new SqlSessionTemplate(factory);
//
//        // 使用 xml 配置文件时是从 MapperFactoryBean 中获取 mapper, MapperFactoryBean 继承自 DaoSupport,
//        // 在初始化时 (afterPropertiesSet) 会调用 checkDaoConfig() -> getConfiguration().addMapper()
//        // 将接口注册(为接口生成代理对象) 到 knownMappers 中, 我们调用 getMapper() 方法时 mybatis 会从
//        // knownMappers 获取对用的代理对象. 但是这里我们通过注解来注册 bean 时, 使用的是 SqlSessionTemplate,
//        // 并不会帮我们自动注册 mapper 所以不手动注册 mapper 会抛找不到 mapper 的异常
//        template.getConfiguration().addMapper(IUserMapper.class);
//        return template.getMapper(IUserMapper.class);
//    }
}
