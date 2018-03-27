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
        logger.info("test library");
        try {
            logger.info("dir get 0:"+new String(new File("").getCanonicalPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("dir get 1:"+Hello.class.getResource("").getPath());
        if(true){
            //打包成Jar运行时当前运行的jar包所在的路径
            throw new XiaoException(System.getProperty("user.dir"));
        }
    }
}
