package org.homework.student;

import lombok.Getter;
import org.homework.db.DBConnecter;
import org.homework.db.model.Score;
import org.homework.db.model.TableQuestion;
import org.homework.io.IoOperator;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static org.homework.utils.Utils.*;

/**
 * Created by hasee on 2015/5/5.
 */
public class CatalogTree{

    @Getter
    static JTree tree;
    //                           科目           章节              类型
    public final static TreeMap<String,TreeMap<Integer,TreeMap<Integer,List<TableQuestion>>>> allData = new TreeMap();
    public final static TreeMap<String,TreeMap<Integer,Float>> allScore = new TreeMap();;
    public static DefaultMutableTreeNode firstLeaf;
    static {
        List<TableQuestion> questions = DBConnecter.getAllQuestion();
        add3Index(allData, questions);
        updateAllScore();
    }

    private static DefaultTreeModel defaultTreeModel;
    private static DefaultMutableTreeNode top;

    public static void updateAllScore(){
        List<Score> scores = DBConnecter.getAllScore();
        add2Index(allScore, scores);
    }


    public CatalogTree() {
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
                    clickTreeLeaf( node);
                } else if (object instanceof ChapterNode && e.getButton() == MouseEvent.BUTTON3) {
                    final ChapterNode chapterNode = (ChapterNode) object;
                    JPopupMenu jPopupMenu = new JPopupMenu();
                    JMenuItem jmenuItem1 = new JMenuItem("提交");
                    jmenuItem1.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            IoOperator.submitWork(chapterNode.course, chapterNode.chapter, chapterNode.map);
                        }
                    });
                    JMenuItem jmenuItem2 = new JMenuItem("导出");
                    jmenuItem2.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            IoOperator.generatePDF(chapterNode.chapter, chapterNode.map);
                        }
                    });
                    jPopupMenu.add(jmenuItem1);
                    jPopupMenu.add(jmenuItem2);
                    jPopupMenu.show(tree, e.getX(), e.getY());
                }
            }
        });
        tree.setRootVisible(false);
        initTop();
    }


    public static void initTop(){
        top.removeAllChildren();
        for (Map.Entry<String,TreeMap<Integer,TreeMap<Integer,List<TableQuestion>>>> entry1 : allData.entrySet()){
            DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(entry1.getKey());
            top.add(node1);
            for (Map.Entry<Integer,TreeMap<Integer,List<TableQuestion>>> entry2 : entry1.getValue().entrySet()){
                //1.get score
                Float score = null;
                if(allScore.containsKey(entry1.getKey()) &&
                        allScore.get(entry1.getKey()).containsKey(entry2.getKey()))
                    score = allScore.get(entry1.getKey()).get(entry2.getKey());
                //2.chapter name
                DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(
                        new ChapterNode(entry2.getKey(),entry1.getKey(),entry2.getValue(),score));
                node1.add(node2);
                for (Map.Entry<Integer,List<TableQuestion>> entry3 : entry2.getValue().entrySet()){
                    DefaultMutableTreeNode node3 = new DefaultMutableTreeNode(new TypeNode(entry3.getKey(),entry3.getValue()));
                    node2.add(node3);
                    if(firstLeaf == null)
                        firstLeaf = node3;
                }
            }
        }
//        tree.setShowsRootHandles(true);
        ecTreeTest(tree);
        clickFirst();
    }

    public static void clickFirst(){
        clickTreeLeaf(firstLeaf);
        defaultTreeModel.reload();
        ecTreeTest(tree);
    }

    private static void clickTreeLeaf(DefaultMutableTreeNode node){
//        ChapterNode father = (ChapterNode) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
//        boolean mark = father.score==null ? false : true;
        TypeNode typeNode = (TypeNode) node.getUserObject();
//        System.out.println("你选择了：" + typeNode.list);
        TreeMap<Integer, List<TableQuestion>> listMap = new TreeMap<Integer, List<TableQuestion>>();
        listMap.put(typeNode.type, typeNode.list);
        ContentPanel.reloadContentPanel().fullContent(getTypeWord(typeNode.type), listMap);

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

    public static class TypeNode{
        int type;
        List<TableQuestion> list;

        public TypeNode(int type, List<TableQuestion> list){
            this.type = type;
            this.list = list;
        }

        @Override
        public String toString(){
            return getTypeWord(type);
        }
    }

    public static class ChapterNode{
        int chapter;
        String course;
        TreeMap<Integer,List<TableQuestion>> map;
        Float score;

        public ChapterNode(int chapter,String course, TreeMap<Integer,List<TableQuestion>> map, Float score){
            this.chapter = chapter;
            this.course = course;
            this.map = map;
            this.score = score;
        }

        @Override
        public String toString(){
            String name = "第" + getChineseNum(chapter) + "章";
            if(score != null)
                name += "(" + score + ")";
            return name;
        }
    }

    public static void main(String[] args) {
        CatalogTree cata = new CatalogTree();
        final JTree tree = cata.getTree();
        JFrame f = new JFrame("JTreeDemo");
        f.add(tree);
        f.setSize(300, 300);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
