package com.watermelon.sso.service.impl;


import com.watermelon.sso.common.VersionBuilder;
import com.watermelon.sso.service.VersionService;
import org.springframework.stereotype.Service;

@Service
public class VersionServiceImpl implements VersionService {
    @Override
    public String readVersion() {
        return new VersionBuilder<>(this).readVersion();
    }
}