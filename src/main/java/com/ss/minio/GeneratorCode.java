package com.ss.minio;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

// 代码自动生成器
public class GeneratorCode {
    public static void main(String[] args) {
        // 需要构建一个 代码自动生成器 对象
        AutoGenerator mpg = new AutoGenerator();
        // 配置策略
        // 1、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath+"/src/main/java"); //完整路径
//        gc.setAuthor("ss");
        gc.setOpen(false);
        gc.setFileOverride(false); // 是否覆盖
        gc.setServiceName("%sService"); // 去Service的I前缀
        gc.setIdType(IdType.AUTO); // id生成策略
        gc.setDateType(DateType.ONLY_DATE);  // 时间策略
        gc.setSwagger2(false);  // 是否开启swagger
        mpg.setGlobalConfig(gc);
        //2、设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/ss_minio?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        dsc.setDbType(DbType.MYSQL); // 数据库类型
        mpg.setDataSource(dsc);
        //3、包的配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName("blog");  // 模块名 默认 ""
        pc.setParent("com.ss.minio");  // 对应的包名
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");
        mpg.setPackageInfo(pc);
        //4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("minio_config"); // 设置要映射的表名 支持多个表名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true); // 自动lombok；
        strategy.setLogicDeleteFieldName("delete_flag");  // 逻辑删除
        // 自动填充配置  添加
//        TableFill createBy = new TableFill("create_by", FieldFill.INSERT);
        TableFill createDate = new TableFill("create_time", FieldFill.INSERT);

        // 修改
//        TableFill lastModifiedBy = new TableFill("last_modified_by",FieldFill.UPDATE);
        TableFill lastModifiedDate = new TableFill("update_time",FieldFill.UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
//        tableFills.add(createBy);
        tableFills.add(createDate);

//        tableFills.add(lastModifiedBy);
        tableFills.add(lastModifiedDate);
        strategy.setTableFillList(tableFills);
        // 乐观锁
//        strategy.setVersionFieldName("version");
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true); //
        mpg.setStrategy(strategy);
        mpg.execute(); //执行
    }
}