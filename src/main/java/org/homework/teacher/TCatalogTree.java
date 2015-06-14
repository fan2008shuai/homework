package org.homework.teacher;

import lombok.Getter;
import org.homework.db.DBConnecter;
import org.homework.db.model.StudentAnswer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.homework.utils.Utils.add4Index;
import static org.homework.utils.Utils.getChineseNum;

/**
 * Created by hasee on 2015/5/5.
 */
public class TCatalogTree {

    @Getter
    static JTree tree;
    //                            科目           章节            班级          学生学号姓名       试题类型        学生答案
    public final static TreeMap<String,TreeMap<Integer,TreeMap<String,TreeMap<String, TreeMap<Integer,List<StudentAnswer>>>>>> allStudentAnswer = new TreeMap();
    //public final static TreeMap<String,TreeMap<Integer,Integer>> allScore = new TreeMap();;
    public static Object firstLeafObect;
    static {
        List<StudentAnswer> studentAnswers = DBConnecter.getAllStudentAnswers();
        add4Index(allStudentAnswer, studentAnswers);

        System.out.println(allStudentAnswer);

    }

    private static DefaultTreeModel defaultTreeModel;
    private static DefaultMutableTreeNode top;
    private static DefaultMutableTreeNode firstLeaf;

    public TCatalogTree() {
        top = new DefaultMutableTreeNode("catalog");

        defaultTreeModel =new DefaultTreeModel(top);
        tree = new JTree(defaultTreeModel);
        // 添加选择事件

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();

                if (node == null)
                    return;

                Object object = node.getUserObject();
                if (node.isLeaf()) {
                    click(object,false);
                }
            }
        });

        tree.setRootVisible(false);
        initTop();
//        tree.setShowsRootHandles(true);

        //firstLeafObect = firstLeaf.getUserObject();
        //clickFirst();
    }

    public static void initTop() {
        top.removeAllChildren();
        for (Map.Entry<String,TreeMap<Integer,TreeMap<String,TreeMap<String, TreeMap<Integer,List<StudentAnswer>>>>>>
                entry1 : allStudentAnswer.entrySet()) {
            //1. course name
            DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(entry1.getKey());
            top.add(node1);
            for (Map.Entry<Integer,TreeMap<String,TreeMap<String, TreeMap<Integer,List<StudentAnswer>>>>>
                    entry2 : entry1.getValue().entrySet()) {
                //2.chapter name
                DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(new ChapterNode(entry2.getKey()));
                node1.add(node2);
                for (Map.Entry<String,TreeMap<String, TreeMap<Integer,List<StudentAnswer>>>>
                        entry3 : entry2.getValue().entrySet()) {
                    //3. studentClass name
                    DefaultMutableTreeNode node3 = new DefaultMutableTreeNode(entry3.getKey());
                    node2.add(node3);

                    for (Map.Entry<String,TreeMap<Integer,List<StudentAnswer>>> entry4 : entry3.getValue().entrySet()) {
                        //4. studentNumberName
                        DefaultMutableTreeNode node4 = new DefaultMutableTreeNode(new AnswerNode(entry3.getKey(), entry4.getKey(), entry1.getKey(), entry2.getKey(), entry4.getValue()));
                        node3.add(node4);
                        if(firstLeaf == null)
                            firstLeaf = node4;
                    }

                }
            }
        }

        ecTreeTest(tree);
        clickFirst();
    }


    public static void clickFirst(){
        defaultTreeModel.reload();
        ecTreeTest(tree);
    }


    private static void click(Object object,boolean isReload) {
        AnswerNode ansNode = (AnswerNode) object;
        System.out.println("你选择了：" + ansNode.map);
        TContentPanel.getTContentPanel().fullContent(ansNode.studentClass,ansNode.studentNumName, ansNode.course, ansNode.chapter, ansNode.map);
        if(isReload) {
            defaultTreeModel.reload();
            ecTreeTest(tree);
        }
    }


    public static void ecTreeTest(JTree tree) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandTree(tree, new TreePath(root));
    }

    private static void expandTree(JTree tree, TreePath parent) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandTree(tree, path);
            }
        }
        tree.expandPath(parent);
    }


    public static class AnswerNode {
        String studentClass;
        String studentNumName;
        String course;
        Integer chapter;
        TreeMap<Integer,List<StudentAnswer>> map;

        public AnswerNode(String studentClass, String studentNumName, String course, Integer chapter, TreeMap<Integer,List<StudentAnswer>> map) {
            this.studentClass = studentClass;
            this.studentNumName = studentNumName;
            this.course = course;
            this.chapter = chapter;
            this.map = map;
        }

        @Override
        public String toString(){
            return studentNumName;
        }
    }

    public static class ChapterNode {
        int chapter;
        //String course;
        //TreeMap<Integer,List<TableQuestion>> map;

        public ChapterNode(int chapter/*,String course, TreeMap<Integer,List<TableQuestion>> map, Integer score*/){
            this.chapter = chapter;
            //this.course = course;
            //this.map = map;
        }

        @Override
        public String toString() {
            String name = "第" + getChineseNum(chapter) + "章";
            return name;
        }
    }

    public static void main(String[] args) {
        TCatalogTree cata = new TCatalogTree();
        final JTree tree = cata.getTree();
        JFrame f = new JFrame("JTreeDemo");
        f.add(tree);
        f.setSize(300, 300);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
