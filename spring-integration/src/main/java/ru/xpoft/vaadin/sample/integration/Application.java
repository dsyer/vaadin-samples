/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.xpoft.vaadin.sample.integration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.web.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.DispatcherServlet;

import ru.xpoft.vaadin.SpringVaadinServlet;
import ru.xpoft.vaadin.VaadinMessageSource;

/**
 * @author Dave Syer
 *
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	
	public static void main(String[] args) {
		new Application().configure(new SpringApplicationBuilder()).run(args);
	}
	
	@Bean
	public VaadinMessageSource vaadinMessageSource() {
		return new VaadinMessageSource();
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setBasename("classpath:/locales/messages");
		source.setFallbackToSystemLocale(false);
		return source;
	}
	
	@Bean
	public ServletRegistrationBean vaadinServlet() {
		SpringVaadinServlet servlet = new SpringVaadinServlet();
		ServletRegistrationBean registration = new ServletRegistrationBean(servlet, "/*", "/VAADIN/*");
		registration.addInitParameter("beanName", "myUI");
		registration.addInitParameter("systemMessagesBeanName", "DEFAULT");
		return registration;
	}

	@Bean
	public ServletRegistrationBean dispatcherRemoved(DispatcherServlet servlet) {
		return new ServletRegistrationBean(servlet, "/not-needed/*");
	}
	
	@Bean
	public EmbeddedServletContainerCustomizer customizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainerFactory factory) {
				factory.addInitializers(new ServletContextInitializer() {
					@Override
					public void onStartup(ServletContext servletContext) throws ServletException {
						servletContext.setInitParameter("productionMode", "false");
					}
				});
			}
		};
	}

}
