package com;
import com.chat.interceptor.ChatHandler;
import com.lzw.face.FaceHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ResourceUtils;


@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        FaceHelper.clear();
        SpringApplication springApplication=new SpringApplication(DemoApplication.class);
        ConfigurableApplicationContext context=springApplication.run(args);
        ChatHandler.setApplicationContext(context);

    }

}

