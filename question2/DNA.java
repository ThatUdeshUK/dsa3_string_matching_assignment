import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * DNA
 */
public class DNA {

  public static void main(String[] args) {
    // Check for input parameters
    if (args.length < 2) {
      System.out.println("Need input file arguments");
      return;
    }

    // Parse file path arguments
    String databaseFilePath = args[0];
    String queryFilePath = args[1];
    String outputFilePath = "output.txt";
    if (args.length == 3)
      outputFilePath = args[2];

    // Read dna database input
    List<Sequence> dnaSeqList = new ArrayList<>();
    try {
      dnaSeqList = readFile(databaseFilePath);
    } catch (FileNotFoundException e) {
      System.out.println("Input file '" + databaseFilePath + "' not found");
      return;
    }

    // Read query input
    List<Sequence> querySeqList = new ArrayList<>();
    try {
      querySeqList = readFile(queryFilePath);
    } catch (FileNotFoundException e) {
      System.out.println("Input file '" + queryFilePath + "' not found");
      return;
    }

    // Write matches to output file
    try {
      FileWriter outputWriter = new FileWriter(outputFilePath);
      PrintWriter writer = new PrintWriter(outputWriter);

      // Get the matches
      for (Sequence query : querySeqList) {
        ArrayList<Match> matches = new ArrayList<>();
        for (Sequence dna : dnaSeqList) {
          matches.addAll(getMatchesKarpRabin(dna, query));
        }
        System.out.println(query.name);
        writer.println(query.name);
        if (matches.size() > 0) {
          for (Match match : matches) {
            System.out.println("[" + match.dna + "] at offset " + match.position);
            writer.println("[" + match.dna + "] at offset " + match.position);
          }
          System.out.println("");
          writer.println("");
        } else {
          System.out.println("NOT FOUND");
          writer.println("NOT FOUND");
        }
      }
      writer.close();
    } catch (IOException e) {
      System.out.println("Couldn't write to file '" + outputFilePath + "'");
      return;
    }
  }

  /**
   * DEPRECATED
   * Get the matches from a given text and a pattern using bruteforce algorithm
   * 
   * @param text
   * @param pattern
   * @return List of matches
   */
  @Deprecated
   private static List<Match> getMatchesNaive(Sequence dna, Sequence query) {
    ArrayList<Match> matches = new ArrayList<>();

    for (int i = 0; i < dna.data.length() - query.data.length(); i++) {
      boolean matched = true;
      for (int j = 0; j < query.data.length(); j++) {
        if (dna.data.charAt(i + j) != query.data.charAt(j) && query.data.charAt(j) != '_') {
          matched = false;
          break;
        }
      }
      if (matched)
        matches.add(new Match(query.name, dna.name, i));
    }
    return matches;
  }

  /**
   * Get the matches from a given text and a pattern using karp/rabin algorithm
   * 
   * @param text
   * @param pattern
   * @return List of matches
   */
  private static List<Match> getMatchesKarpRabin(Sequence dna, Sequence query) {
    ArrayList<Match> matches = new ArrayList<>();

    RollingHash queryHash = new RollingHash(query.data);
    RollingHash dnaHash = new RollingHash(dna.data.substring(0, query.data.length()));

    if (dnaHash.hashEqual(queryHash))
      matches.add(new Match(query.name, dna.name, 0));

    for (int i = query.data.length(); i < dna.data.length(); i++) {
      dnaHash.roll(dna.data.charAt(i));
      if (dnaHash.hashEqual(queryHash))
        matches.add(new Match(query.name, dna.name, i - query.data.length() + 1));
    }

    return matches;
  }

  /**
   * Read a file and return an list of sequences
   * 
   * @param filePath - File path of the given file
   * @return List of sequences
   * @throws FileNotFoundException
   */
  public static List<Sequence> readFile(String filePath) throws FileNotFoundException {
    ArrayList<Sequence> seqList = new ArrayList<>();
    Scanner textScanner = new Scanner(new File(filePath));
    while (textScanner.hasNextLine()) {
      String line = textScanner.nextLine();
      if (line.charAt(0) == '>') {
        String restofString = line.substring(1);
        if (restofString.equals("EOF"))
          break;
        Sequence newSequence = new Sequence();
        newSequence.name = restofString;
        seqList.add(newSequence);
      } else {
        Sequence lastSequence = seqList.get(seqList.size() - 1);
        lastSequence.appendData(line);
      }
    }
    textScanner.close();
    return seqList;
  }

  /**
   * Sequence
   * 
   * Model to keep input DNA/Query sequences
   */
  public static class Sequence {
    String name;
    String data;

    Sequence() {
      this.name = "";
      this.data = "";
    }

    Sequence(String name, String data) {
      this.name = name;
      this.data = data;
    }

    void appendData(String data) {
      this.data += data;
    }

    @Override
    public String toString() {
      return "Name -> " + this.name + ", Data -> " + this.data;
    }
  }

  /**
   * Match
   * 
   * Model to keep the found matches.
   */
  public static class Match {
    String query;
    String dna;
    int position;

    Match(String query, String dna, int position) {
      this.query = query;
      this.dna = dna;
      this.position = position;
    }
  }

  /**
   * RollingHash
   */
  public static class RollingHash {
  
    double value;
    private ArrayList<Character> sequence;

    RollingHash() {
      this.value = 0;
      this.sequence = new ArrayList<>(); 
    }

    RollingHash(String initial) {
      this.value = 0;
      this.sequence = new ArrayList<>();
      for (int i = 0; i < initial.length(); i++) {
        this.value *= 10;
        this.value += initial.charAt(i);
        sequence.add(initial.charAt(i));
      }
    }
    
    double append(char letter) {
      this.value = this.value * 10 + letter;
      sequence.add(letter);
      return this.value;
    }

    double roll(char letter) {
      this.value = (this.value - sequence.get(0) * Math.pow(10, sequence.size() - 1)) * 10 + letter;
      sequence.remove(0);
      sequence.add(letter);
      return this.value;
    }

    public boolean hashEqual(RollingHash obj) {
      return this.value == obj.value;
    }
  }
}
