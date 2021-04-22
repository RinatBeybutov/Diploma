
package main.Config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class Config implements WebMvcConfigurer {

  @Value("${upload.folder}")
  private String pathUploadFolder;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/" + pathUploadFolder + "**", "/**")
        .addResourceLocations("file:" + pathUploadFolder, "classpath:static/");  // /src/main/java/resources
  }

}

