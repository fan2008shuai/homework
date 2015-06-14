package org.homework.io;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by hasee on 2015/5/8.
 */
public class PDFOperator {

    public static void writePdf(List<PDFLiner> list,String path) throws IOException, DocumentException {
        // 1.新建document对象
        // 第一个参数是页面大小。接下来的参数分别是左、右、上和下页边距。
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

        // 3.打开文档
        document.open();

        // 4.向文档中添加内容
        // 通过 com.lowagie.text.Paragraph 来添加文本。可以用文本及其默认的字体、颜色、大小等等设置来创建一个默认段落
        BaseFont bfChinese = BaseFont.createFont("c://windows//fonts//simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        for (PDFLiner p : list){
            if (p.o instanceof String) {
                Font chineseFont = new Font(bfChinese, p.size, p.style);
                document.add(new Paragraph((String) p.o, chineseFont));
            }else if(p.o instanceof Image){
                Image image = (Image)p.o;
                image.scalePercent(55);
                document.add(image);
            }
        }

        // 5.关闭文档
        document.close();
    }

    public static class PDFLiner{
        Object o;
        int size;
        int style;

        public PDFLiner(Object o, int size, int style) {
            this.o = o;
            this.size = size;
            this.style = style;
        }
    }

    public static void main(String[] args) throws IOException, DocumentException {

    }
}
