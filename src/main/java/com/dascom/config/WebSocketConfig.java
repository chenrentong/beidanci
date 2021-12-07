package com.dascom.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author hy
 */
@Configuration
public class WebSocketConfig {

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 文件最大
		factory.setMaxFileSize("200MB"); // KB,MB
		/// 设置总上传数据总大小
		factory.setMaxRequestSize("500MB");
		return factory.createMultipartConfig();
	}

	@Bean
	public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
		// 修改内置的 tomcat 容器配置
		TomcatServletWebServerFactory tomcatServlet = new TomcatServletWebServerFactory();
		tomcatServlet.addConnectorCustomizers(
				(TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
		return tomcatServlet;
	}

}
