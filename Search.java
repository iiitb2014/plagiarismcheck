import java.util.StringTokenizer;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.IndexOutOfBoundsException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.NullPointerException;

public class Search {
  public static ArrayList<Integer> dataset(File f) throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(f));
    ArrayList<Character> array= new ArrayList();
    ArrayList<Integer> array1= new ArrayList();
    int a;
    while((a=br.read())!=-1)
    {
  //      System.out.print((char)a);
      array.add((char)a);
      array1.add(a);
    }
    br.close();
    return array1;

  }

  public static String getStringRepresentation(ArrayList<Character> list)
  {
   StringBuilder builder = new StringBuilder(list.size());
    for(Character ch : list)
    {
      builder.append(ch);
    }
    return builder.toString();
  }
  public static ArrayList<Tuple>[] Searching(ArrayList<Tuple> array) throws IndexOutOfBoundsException
  {
    Comparator<Tuple> comparator = new Comparator<Tuple>()
    {

      public int compare(Tuple tupleA,Tuple tupleB)
      {
        return Integer.compare(tupleA.a,tupleB.a);
      }
    };
    Collections.sort(array, comparator);
    ArrayList<Tuple> outputarray= new ArrayList();
    ArrayList<Tuple> outputarray1= new ArrayList();
    int j=0, block = 0;
    int beg = array.get(0).a == 0 ? 1 : array.get(0).a;
    int beg1 = array.get(0).b == 0 ? 1 : array.get(0).b;
    int flag = 0;
    for(int i=0; i<array.size()-1; i++)
    {
      int d1 = array.get(i+1).a - array.get(i).a;
      int d2 = array.get(i+1).b - array.get(i).b;
      if(d1 == d2 || d1 < 9)
      {
        j+=d1;
      }
      else{
        outputarray.add(new Tuple(beg,j+9));
        outputarray1.add(new Tuple(beg1,j+9));
        j=0;
        flag = 1;
        beg = array.get(i+1).a + 1;
        beg1 = array.get(i+1).b + 1;
      }
      // System.out.println(String.valueOf(d1) + "," + String.valueOf(d2));
    }
    if(flag == 0){
      outputarray.add(new Tuple(beg,j+9));
      outputarray1.add(new Tuple(beg1,j+9));
    }
    ArrayList<Tuple>[] ar= new ArrayList[2];
    ar[0] = outputarray;
    ar[1]= outputarray1;
    System.out.println(outputarray);
    return ar;
  }

  public static void main(String[] args) throws FileNotFoundException, IOException,IndexOutOfBoundsException,NullPointerException {
    Scanner scanner;   String l = "",t = "";
    scanner = new Scanner(new File("l")).useDelimiter("\\Z");
    l = scanner.next();

    scanner = new Scanner(new File("t")).useDelimiter("\\Z");
    t = scanner.next();

    double n = Similarity.cosineSimilarity(l,t);
    String s = String.valueOf(n*100);
    ArrayList<Integer> arr=new ArrayList();
    arr=dataset(new File("l"));

    ArrayList<Integer> arr1=new ArrayList();
    arr1=dataset(new File("t"));
    Similarity.jaccardSimilarity(l,t);
    ArrayList<Tuple> indexes = Similarity.getCommonIndex();
    Comparator<Tuple> comparator = new Comparator<Tuple>()
    {

      public int compare(Tuple tupleA,Tuple tupleB)
      {
        return Integer.compare(tupleA.a,tupleB.a);
      }
    };
    Collections.sort(indexes, comparator);
    ArrayList<Tuple>[] ay=new ArrayList[2];

    ay=Searching(indexes);
    System.out.println(ay[0]);
  }
}
