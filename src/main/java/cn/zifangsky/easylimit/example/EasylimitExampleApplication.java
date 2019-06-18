package cn.zifangsky.easylimit.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ServletComponentScan
@EnableAsync
@MapperScan("cn.zifangsky.easylimit.example.mapper")
public class EasylimitExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasylimitExampleApplication.class, args);
	}

}
