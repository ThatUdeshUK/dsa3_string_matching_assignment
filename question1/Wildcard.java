import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Wildcard
 */
public class Wildcard {

  public static void main(String[] args) {
    // Check for input parameters
    if (args.length < 2) {
      System.out.println("Need input file arguments");
      return;
    }

    // Parse file path arguments
    String textFilePath = args[0];
    String patternFilePath = args[1];
    String outputFilePath = "output.txt";
    if (args.length == 3)
      outputFilePath = args[2];

    // Read text input
    String text = "";
    try {
      Scanner textScanner = new Scanner(new File(textFilePath));
      while (textScanner.hasNextLine()) {
        text += textScanner.nextLine();
      }
      textScanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("Input file '" + textFilePath + "' not found");
      return;
    }

    // Read pattern input
    String pattern = "";
    try {
      Scanner patternScanner = new Scanner(new File(patternFilePath));
      while (patternScanner.hasNextLine()) {
        pattern += patternScanner.nextLine();
      }
      patternScanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("Input file '" + patternFilePath + "' not found");
      return;
    }

    // Get the matches
    List<Match> matches = getMatches(text, pattern);

    // Write matches to output file
    try {
      FileWriter outputWriter = new FileWriter(outputFilePath);
      PrintWriter writer = new PrintWriter(outputWriter);
      for (Match match : matches) {
        System.out.println(match.position + " -> " + match.match);
        writer.println(match.position + " -> " + match.match);
      }
      writer.close();
    } catch (IOException e) {
      System.out.println("Couldn't write to file '" + outputFilePath + "'");
      return;
    }
  }

  /**
   * Get the matches from a given text and a pattern
   * 
   * @param text
   * @param pattern
   * @return List of matches
   */
  private static List<Match> getMatches(String text, String pattern) {
    ArrayList<Match> matches = new ArrayList<>();

    for (int i = 0; i < text.length() - pattern.length(); i++) {
      boolean matched = true;
      for (int j = 0; j < pattern.length(); j++) {
        if (text.charAt(i + j) != pattern.charAt(j) && pattern.charAt(j) != '_') {
          matched = false;
          break;
        }
      }
      if (matched)
        matches.add(new Match(i, text.substring(i, i + pattern.length())));
    }
    return matches;
  }

  /**
   * Match
   * 
   * Model to keep the found matches.
   */
  public static class Match {
    int position;
    String match;

    Match(int position, String match) {
      this.position = position;
      this.match = match;
    }
  }
}
