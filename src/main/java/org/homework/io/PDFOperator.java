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
        // 1.�½�document����
        // ��һ��������ҳ���С���������Ĳ����ֱ������ҡ��Ϻ���ҳ�߾ࡣ
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        // 2.����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽�����С�
        // ���� PdfWriter ���� ��һ�������Ƕ��ĵ���������ã��ڶ����������ļ���ʵ�����ƣ��ڸ������л�����������·����
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

        // 3.���ĵ�
        document.open();

        // 4.���ĵ����������
        // ͨ�� com.lowagie.text.Paragraph ������ı����������ı�����Ĭ�ϵ����塢��ɫ����С�ȵ�����������һ��Ĭ�϶���
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

        // 5.�ر��ĵ�
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
