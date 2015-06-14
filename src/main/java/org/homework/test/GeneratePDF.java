package org.homework.test;

 import java.awt.Color;
import java.io.FileOutputStream; 
 import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfWriter; 
 /**
 * ������TODO ��JAVA����PDF��
 *  
 * 
 * @title GeneratePDF
 * @author SYJ
 * @email songyanjun_stars@126.com
 * @date 2013-4-6
 * @version V1.0
 */
public class GeneratePDF {

 public static void main(String[] args) {

  //���õ�һ����������C������һ������ΪITextTest.pdf ���ļ�
  try {
   writeSimplePdf();
  } 
  catch (Exception e) { e.printStackTrace(); }

  
  //���õڶ�����������C������ΪITextTest.pdf���ļ�������½ڡ�
  try {
   writeCharpter();
  } 
  catch (Exception e) { e.printStackTrace(); }

  
 }
  
  public static void writeSimplePdf() throws Exception {

  // 1.�½�document����
  // ��һ��������ҳ���С���������Ĳ����ֱ������ҡ��Ϻ���ҳ�߾ࡣ
  Document document = new Document(PageSize.A4, 50, 50, 50, 50);

  // 2.����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽�����С�
  // ���� PdfWriter ���� ��һ�������Ƕ��ĵ���������ã��ڶ����������ļ���ʵ�����ƣ��ڸ������л�����������·����
  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\ITextTest.pdf"));

  // 3.���ĵ�
  document.open();

  // 4.���ĵ����������
  // ͨ�� com.lowagie.text.Paragraph ������ı����������ı�����Ĭ�ϵ����塢��ɫ����С�ȵ�����������һ��Ĭ�϶���
  document.add(new Paragraph("First page of the document."));
  document.add(new Paragraph("Some more text on the  first page with different color and font type.", FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, new Color(255, 150, 200))));

  // 5.�ر��ĵ�
  document.close();
 } 
 
 /**
  * ��Ӻ����½ڵ�pdf�ļ�
  * 
  * @throws Exception
  */
 public static void writeCharpter() throws Exception {

  // �½�document���� ��һ��������ҳ���С���������Ĳ����ֱ������ҡ��Ϻ���ҳ�߾ࡣ
  Document document = new Document(PageSize.A4, 20, 20, 20, 20);

  // ����һ����д��(Writer)��document���������ͨ����д��(Writer)���Խ��ĵ�д�뵽�����С�
  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\ITextTest.pdf"));

  // ���ļ�
  document.open();

  // ����
  document.addTitle("Hello mingri example");

  // ����
  document.addAuthor("wolf");

  // ����
  document.addSubject("This example explains how to add metadata.");
  document.addKeywords("iText, Hello mingri");
  document.addCreator("My program using iText");

  // document.newPage();
  // ���ĵ����������
  document.add(new Paragraph("\n"));
  document.add(new Paragraph("\n"));
  document.add(new Paragraph("\n"));
  document.add(new Paragraph("\n"));
  document.add(new Paragraph("\n"));
  document.add(new Paragraph("First page of the document."));
  document.add(new Paragraph("First page of the document."));
  document.add(new Paragraph("First page of the document."));
  document.add(new Paragraph("First page of the document."));
  document.add(new Paragraph("Some more text on the first page with different color and font type.", FontFactory.getFont(FontFactory.defaultEncoding, 10, Font.BOLD, new Color(0, 0, 0))));
  Paragraph title1 = new Paragraph("Chapter 1", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC, new Color(0, 0, 255)));

  // �½��½�
  Chapter chapter1 = new Chapter(title1, 1);
  chapter1.setNumberDepth(0);
  Paragraph title11 = new Paragraph("This is Section 1 in Chapter 1", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(255, 0, 0)));
  Section section1 = chapter1.addSection(title11);
  Paragraph someSectionText = new Paragraph("This text comes as part of section 1 of chapter 1.");
  section1.add(someSectionText);
  someSectionText = new Paragraph("Following is a 3 X 2 table.");
  section1.add(someSectionText);
  document.add(chapter1);

  // �ر��ĵ�
  document.close();
 }
  
 } 
 