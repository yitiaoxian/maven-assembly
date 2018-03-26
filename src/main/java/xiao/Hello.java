package xiao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hello {
    public static void main(String[] args){
        System.out.println("hello world1");
        Logger logger = LoggerFactory.getLogger("Main");
        System.out.println("hello world2");
        logger.info("test library");
    }
}
