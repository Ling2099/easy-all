package com.test;

import com.easy.annotations.StartAnnotations;
import org.springframework.boot.SpringApplication;

/**
 * TODO
 *
 * @author LZH
 * @version 5.0.0
 * @since 2023-05-06
 */
@StartAnnotations
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
        System.out.println(".------..------..------..------..------..------..------.\n" +
                "|S.--. ||U.--. ||C.--. ||C.--. ||E.--. ||S.--. ||S.--. |\n" +
                "| :/\\: || (\\/) || :/\\: || :/\\: || (\\/) || :/\\: || :/\\: |\n" +
                "| :\\/: || :\\/: || :\\/: || :\\/: || :\\/: || :\\/: || :\\/: |\n" +
                "| '--'S|| '--'U|| '--'C|| '--'C|| '--'E|| '--'S|| '--'S|\n" +
                "`------'`------'`------'`------'`------'`------'`------'");
        System.out.println("(♥◠‿◠)ﾉﾞ  测试服务-启动成功   ლ(´ڡ`ლ)ﾞ ");
    }

}
