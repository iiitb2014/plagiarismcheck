import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

import java.awt.Color;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.lang.NullPointerException;
import java.util.ArrayList;
import java.util.Random;

public class PlagiarismChecker{
  public static void main(String[] args) throws FileNotFoundException {
    Runnable r = new Runnable() {

      @Override
      public void run() {
        new PlagiarismChecker().createUI();
      }
    };
    EventQueue.invokeLater(r);
  }
  private static File f;
  private static File f1;
  private void createUI() {
    final JFrame frame = new JFrame();
    final JFrame frame1 = new JFrame();
    frame.setLayout(new BorderLayout());
    frame1.setLayout(new BorderLayout());
    JButton openBtn1 = new JButton("Open File 1");
    JButton openBtn2= new JButton("Open File 2");
    JButton check = new JButton("Check Plagiarism");
    final JLabel input = new JLabel("Similarity by term frequency: " + "\t \t" + "Semantic Similarity: ",JLabel.CENTER);
    final JTextArea textArea=new JTextArea(60,60);
    textArea.setWrapStyleWord(true);
    textArea.setLineWrap(true);
    textArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textArea);
    final JTextArea textArea1=new JTextArea(60,60);
    textArea1.setWrapStyleWord(true);
    textArea1.setLineWrap(true);
    textArea1.setEditable(false);
    JScrollPane scrollPane1 = new JScrollPane(textArea1);

    openBtn2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser openFile = new JFileChooser();
        openFile.showOpenDialog(null);
        f =openFile.getSelectedFile();
        System.out.println(f.getPath());
        try {
          Scanner sc = new Scanner(f);
          sc.useDelimiter("\\Z");
          String s = sc.next();
          textArea1.setText(s);
          frame.setVisible(false);
          frame1.setVisible(true);
        }catch(FileNotFoundException e){
          e.printStackTrace();
        }
      }
    });
    openBtn1.addActionListener(new ActionListener() {
      @Override
        public void actionPerformed(ActionEvent arg0) {
        JFileChooser openFile1 = new JFileChooser();
        openFile1.showOpenDialog(null);
        f1 = openFile1.getSelectedFile();
        try {
          Scanner sc = new Scanner(f1);
          sc.useDelimiter("\\Z");
          String s = sc.next();
          System.out.println(f1.getPath());
          textArea.setText(s);
        }catch(FileNotFoundException e) {
          e.printStackTrace();
        }
      }
    });
    check.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0){
        Scanner scanner;
        String l = "",t = "";
        try {
          scanner = new Scanner(f).useDelimiter("\\Z");
          l = scanner.next();
        }catch(FileNotFoundException e) {}
        try {
          scanner = new Scanner(f1).useDelimiter("\\Z");
          t = scanner.next();
        }catch(FileNotFoundException e) {}
        double n = Similarity.cosineSimilarity(l,t); //.toString()
        String s = String.valueOf(n*100);
        input.setText("Similarity by term frequency: " + s + "    " + "Semantic Similarity: " + String.valueOf(100* Similarity.jaccardSimilarity(l,t)));
        ArrayList<Tuple> indexes = Similarity.getCommonIndex();
        ArrayList<Tuple>[] highList = new ArrayList[2];
        try {
          highList= Search.Searching(indexes);
        }catch(IndexOutOfBoundsException o){
        }catch(NullPointerException p) {}
        Highlighter highlighter = textArea.getHighlighter();
        Random random = new Random();
        HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter( new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        Highlighter highlighter1 = textArea1.getHighlighter();
        int isize = highList[0].size();
        System.out.println(isize);
        try {
          for(int i = 0; i < isize; ++i) {
            highlighter.addHighlight(highList[1].get(i).a,highList[1].get(i).a+highList[1].get(i).b, painter );
            highlighter1.addHighlight(highList[0].get(i).a,highList[0].get(i).a+highList[0].get(i).b, painter );
            painter = new DefaultHighlighter.DefaultHighlightPainter( new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
          }
        }catch (BadLocationException e) {}
      }
    });
    frame.add(new JLabel("File Chooser"), BorderLayout.NORTH);
    frame.add(openBtn1, BorderLayout.CENTER);
    frame.add(openBtn2, BorderLayout.SOUTH);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame1.add(input, BorderLayout.NORTH);
    frame1.add(check, BorderLayout.SOUTH);
    frame1.add(scrollPane, BorderLayout.WEST);
    frame1.add(scrollPane1, BorderLayout.EAST);
    frame.setTitle("File Chooser");
    frame.pack();
    frame.setTitle("Similarity");
    frame1.pack();
    frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame1.setVisible(false);
  }
}
