package com.example.searchengine;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/*@Getter
@Component
@ConfigurationProperties(prefix = "sites")*/
public class SiteProps {

    private List<Map<String, Object>> siteList;

}
