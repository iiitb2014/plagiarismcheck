import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Tuple{
  public int a;
  public int b;
  public Tuple(int x,int y){
    a = x;
    b = y;
  }

  public String toString(){
    return "< " + a + " : " + b + " >\n";
  }
}

public class Similarity{
  private static Map<String, Integer> frequencies = new LinkedHashMap<String, Integer>();
  private static int size = 0;
  private static ArrayList<Tuple> indexes;
  private static int k = 10;
  public static ArrayList<Integer> mapFreq(String text){
    for (Map.Entry<String,Integer> entry : frequencies.entrySet())
    {
      entry.setValue(0);
    }
    String nonWordDelimiter="\\s+";
    text = text.replaceAll("\\p{P}","").toLowerCase();
    String[] words = text.split(" ");
    ArrayList<Integer> i = new ArrayList<Integer>();
    for (String word : words) {
      if (!word.isEmpty()) {
        Integer frequency = frequencies.get(word);

        if (frequency == null) {
            frequency = 0;
        }

        ++frequency;
        frequencies.put(word, frequency);
      }
    }
    for (Map.Entry<String,Integer> entry : frequencies.entrySet()){
      i.add(entry.getValue());
    }
    size = i.size();
    return i;
  }

  private static double cosine(ArrayList<Integer> a, ArrayList<Integer> b){
   double dotProduct = DotProduct(a, b);
   double magnitudeOfA = Magnitude(a);
   double magnitudeOfB = Magnitude(b);
   return dotProduct/(magnitudeOfA*magnitudeOfB);
  }

  private static double DotProduct(ArrayList<Integer> a, ArrayList<Integer> b){
    double dotProduct = 0;
    int s = size;
    for (int i = 0; i < s; i++)
    {
      int aValue = i < a.size() ? a.get(i) : 0;
      int bValue = i < b.size() ? b.get(i) : 0;
      dotProduct += (aValue * bValue);
    }
    return dotProduct;
  }

  // Magnitude of the vector is the square root of the dot product of the vector with itself.
  private static double Magnitude(ArrayList<Integer> vector){
    return Math.sqrt(DotProduct(vector, vector));
  }

  private static Map<Integer, Integer> shingles(String line){
    Map<Integer, Integer> map= new LinkedHashMap<Integer,Integer>();
    ArrayList<Integer> s = new ArrayList<Integer>();
    for(int i=0; i< line.length() - k +1; ++i){
      String x = line.substring(i,i+k);
      map.put(x.hashCode(),i);
    }
    return map;
  }

  public static double cosineSimilarity(String a, String b){
    ArrayList<Integer> m = mapFreq(a);
    ArrayList<Integer> n = mapFreq(b);
    return cosine(m,n);
  }

  public static double jaccardSimilarity(String a, String b){
    Map<Integer, Integer> shinglesA= shingles(a);
    Map<Integer, Integer> shinglesB= shingles(b);
    Set<Integer> set1= new HashSet<Integer>(shinglesA.keySet());
    Set<Integer> set2= new HashSet<Integer>(shinglesA.keySet());
    set1.retainAll(shinglesB.keySet());
    set2.addAll(shinglesB.keySet());
    Iterator itr = set1.iterator();
    Object first = itr.next();
    indexes = new ArrayList<Tuple>();
    while(itr.hasNext()){
      indexes.add(new Tuple(shinglesA.get(first),shinglesB.get(first)));
      first = itr.next();
    }
    return (double)set1.size() / (double)set2.size();
  }

  public static ArrayList<Tuple> getCommonIndex(){
    return indexes;
  }

  public static void main(String[] a) throws FileNotFoundException{
    Scanner scanner = new Scanner(new File(a[0])).useDelimiter("\\Z");
    String l = scanner.next();
    scanner = new Scanner(new File(a[1])).useDelimiter("\\Z");
    String t = scanner.next();
    System.out.println(cosineSimilarity(l,t));
    System.out.println(jaccardSimilarity(l,t));
    System.out.println(getCommonIndex());
    System.out.println(size);
  }
}
