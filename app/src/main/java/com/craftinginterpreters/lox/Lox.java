package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

// Main class for the Lox interpreter
public class Lox {
  private static final Interpreter interpreter = new Interpreter();
  // Flag to indicate if an error has occurred
  static boolean hadError = false;
  static boolean hadRuntimeError = false;

  // Entry point of the program
  public static void main(String[] args) throws IOException {
    // Check command line arguments to determine mode of operation
    if (args.length > 1) {
      // If more than one argument, print usage and exit
      System.out.println("Usage: jlox [script]");
      System.exit(64);
    } else if (args.length == 1) {
      // If exactly one argument, treat it as a file path and run the file
      runFile(args[0]);
    } else {
      // If no arguments, enter interactive prompt mode
      runPrompt();
    }
  }

  // Runs the Lox interpreter on a file
  private static void runFile(String path) throws IOException {
    // Read the entire file into a String
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    // Execute the source code
    run(new String(bytes, Charset.defaultCharset()));
    // If an error occurred, exit with error code
    if (hadError) System.exit(65);
    if (hadRuntimeError) System.exit(70);
  }

  // Runs the Lox interpreter in interactive prompt mode
  private static void runPrompt() throws IOException {
    // Setup for reading lines from the console
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    // Infinite loop for the interactive prompt
    for (;;) {
      System.out.print("> ");
      String line = reader.readLine();
      // Exit loop if end-of-input is detected
      if (line == null) break;
      // Execute the entered line
      run(line);
      // Reset error flag after each command
      hadError = false;
    }
  }

  // Core method for interpreting Lox source code
  private static void run(String source) {
    // Initialize scanner with the source code
    Scanner scanner = new Scanner(source);
    // Scan the source code into tokens
    List<Token> tokens = scanner.scanTokens();
    Parser parser = new Parser(tokens);
    Expr expression = parser.parse();

    // Stop if there was a syntax error.
    if (hadError) return;

    interpreter.interpret(expression);
  }

  // Utility method for reporting errors
  static void error(int line, String message) {
    report(line, "", message);
  }

  // Formats and prints an error message
  private static void report(int line, String where, String message) {
    System.err.println("[line " + line + "] Error" + where + ": " + message);
    // Set the error flag
    hadError = true;
  }

  static void error(Token token, String message) {
    if (token.type == TokenType.EOF) {
      report(token.line, " at end", message);
    } else {
      report(token.line, " at '" + token.lexeme + "'", message);
    }
  }

  static void runtimeError(RuntimeError error) {
    System.err.println(error.getMessage() +
        "\n[line " + error.token.line + "]");
    hadRuntimeError = true;
  }
}
