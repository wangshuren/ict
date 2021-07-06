package com.ict.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ：wangsr
 * @description： emqtt服务器配置
 * @date ：Created in 2021/7/6 0006 14:37
 */
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitmqProperties {
    private Server server;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public static class Server {
        //TODO 不支持多服务器??
        private String host;
        private int port;
        private String username;
        private String password;
        private String virtualHost;

        public String getVirtualHost() {
            return virtualHost;
        }

        public void setVirtualHost(String virtualHost) {
            this.virtualHost = virtualHost;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
