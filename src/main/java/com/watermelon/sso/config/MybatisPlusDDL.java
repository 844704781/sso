package com.watermelon.sso.config;

import com.baomidou.mybatisplus.extension.ddl.SimpleDdl;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component

public class MybatisPlusDDL extends SimpleDdl {
    private final ResourceLoader resourceLoader;

    public MybatisPlusDDL(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private final static String ddlPath = "db/migration/";

    @Override

    public List<String> getSqlFiles() {
        List<String> sqlFileNames = new ArrayList<>();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        try {
            Resource[] resources = resolver.getResources(String.format("classpath*:%s*.sql", ddlPath));
            for (Resource resource : resources) {
                sqlFileNames.add(resource.getFilename());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ddl files", e);
        }
        return sqlFileNames.stream().map(name -> String.format("%s%s", ddlPath, name)).sorted().
                toList();
    }
}
