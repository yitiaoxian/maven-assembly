package poi;

import org.apache.poi.hslf.model.OLEShape;
import org.apache.poi.hslf.usermodel.HSLFObjectData;
import org.apache.poi.hslf.usermodel.HSLFShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class POITest {
    public static void main(String[] arg){
        FileInputStream is = null;
        try {
            is = new FileInputStream("E:\\tika-test-dir\\test\\ppt嵌套xlsx\\qxk\\2016内嵌odp单独一行.ppt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HSLFSlideShow ppt = null;
        try {
            ppt = new HSLFSlideShow(is);
            is.close();
            for (HSLFSlide slide : ppt.getSlides()) {
                for (HSLFShape shape : slide.getShapes()) {
                    if (shape instanceof OLEShape) {
                        OLEShape ole = (OLEShape) shape;
                        HSLFObjectData data = ole.getObjectData();
                        String name = ole.getInstanceName();
                        System.out.println(name);
                        if ("Worksheet".equals(name)) {
                            try {
                                HSSFWorkbook wb = new HSSFWorkbook(data.getData());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if ("Document".equals(name)) {
                            try {
                                HWPFDocument doc = new HWPFDocument(data.getData());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else if("Presentation".equals(name)){
                            //无赖啊
                            InputStream inputStream = data.getData();
                            Tika tika = new Tika();
                            System.out.println(tika.detect(inputStream));
                        }
                    }
                }
            }
        }catch (IOException e) {
                e.printStackTrace();
            }
    }
}
