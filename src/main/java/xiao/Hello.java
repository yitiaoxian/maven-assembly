package xiao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Hello {
    public static void main(String[] args){
        System.out.println("hello world1");
        Logger logger = LoggerFactory.getLogger("Main");
        System.out.println("hello world2");
        System.out.println(System.getProperty("user.dir"));

        logger.info(" dir get :"+Hello.class.getResource("").getPath());
        logger.info(System.getProperty("user.dir"));
        System.out.println();
        System.out.println(System.getProperty("user.dir"));

    }
}
