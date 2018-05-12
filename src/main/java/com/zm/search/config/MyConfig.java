package com.zm.search.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class MyConfig {

    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private int port;
    @Value("${elasticsearch.cluster.name}")
    private String clusterName;
    public static final String CLUSTER_NAME = "cluster.name";

    @Bean
    public TransportClient client() throws UnknownHostException {
        InetSocketTransportAddress node = new InetSocketTransportAddress(
                InetAddress.getByName(host), port
        );
        Settings settings = Settings.builder()
                .put(CLUSTER_NAME, clusterName)
                .build();
        TransportClient client = new PreBuiltTransportClient(settings);
        //分布式部署的时候需要添加多个Address
        client.addTransportAddress(node);
        return client;
    }

}
