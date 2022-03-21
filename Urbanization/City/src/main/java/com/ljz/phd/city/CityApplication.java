package com.ljz.phd.city;

import cn.hutool.core.io.FileUtil;
import com.ljz.phd.city.service.Inputer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
@MapperScan("com.ljz.phd.city")
@Slf4j
public class CityApplication implements CommandLineRunner {

    private final Inputer inputer;

    public CityApplication(Inputer inputer) {
        this.inputer = inputer;
    }


    public static void main(String[] args) {
        SpringApplication.run(CityApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {


        File[] listFoler = FileUtil.ls(FileUtil.getAbsolutePath("") + "static/city/");
        for (File f : listFoler) {
            String ff = FileUtil.getName(f.getAbsolutePath());
            log.info("starting [{}]..", ff);

            Integer year = Integer.parseInt(ff);
            File[] list = FileUtil.ls(FileUtil.getAbsolutePath("") + "static/city/" + year);
            for (File file : list) {
                String fileName = file.getAbsolutePath();
                this.inputer.noModelRead(year, fileName);
            }
        }
    }
}
