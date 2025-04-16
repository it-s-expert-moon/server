package gain.moon.expert_moon.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ResourceConfig(@Value("\${image.folder}") val location: String): WebMvcConfigurer{
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:/" + location)
    }
}