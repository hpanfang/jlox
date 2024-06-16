package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Import static members from TokenType to avoid prefixing constants with TokenType.
import static com.craftinginterpreters.lox.TokenType.*;

// Scanner class is responsible for converting a source string into a list of tokens.
class Scanner {
  private final String source; // Source code as a string.
  private final List<Token> tokens = new ArrayList<>(); // List to hold generated tokens.
  private int start = 0; // Index of the first character in the current lexeme.
  private int current = 0; // Current character position in the source code.
  private int line = 1; // Current line number in the source code.
  private static final Map<String, TokenType> keywords; // Map of reserved keywords to their corresponding token types.

  // Static initializer block to populate the keywords map.
  static {
    keywords = new HashMap<>();
    // Populate the keywords map with Lox language keywords.
    keywords.put("and", AND);
    keywords.put("class", CLASS);
    keywords.put("else", ELSE);
    keywords.put("false", FALSE);
    keywords.put("for", FOR);
    keywords.put("fun", FUN);
    keywords.put("if", IF);
    keywords.put("nil", NIL);
    keywords.put("or", OR);
    keywords.put("print", PRINT);
    keywords.put("return", RETURN);
    keywords.put("super", SUPER);
    keywords.put("this", THIS);
    keywords.put("true", TRUE);
    keywords.put("var", VAR);
    keywords.put("while", WHILE);
  }

  // Constructor that takes the source code as input.
  Scanner(String source) {
    this.source = source;
  }

  // Checks if the current position is at the end of the source code.
  private boolean isAtEnd() {
    return current >= source.length();
  }

  // Advances the current position and returns the character at the old current position.
  private char advance() {
    return source.charAt(current++);
  }

  // Adds a token to the list of tokens without a literal value.
  private void addToken(TokenType type) {
    addToken(type, null);
  }

  // Adds a token to the list of tokens, possibly with a literal value.
  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }

  // The main method that scans the source code and converts it into tokens.
  List<Token> scanTokens() {
    while (!isAtEnd()) {
      start = current; // Start of a new lexeme.
      scanToken(); // Scan for a single token.
    }

    // Add an EOF token at the end of the token list.
    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  // Scans and adds a single token based on the current character in the source code.
  private void scanToken() {
    char c = advance(); // Get the current character and advance.
    switch (c) {
      // Single-character tokens.
      case '(': addToken(LEFT_PAREN); break;
      case ')': addToken(RIGHT_PAREN); break;
      case '{': addToken(LEFT_BRACE); break;
      case '}': addToken(RIGHT_BRACE); break;
      case ',': addToken(COMMA); break;
      case '.': addToken(DOT); break;
      case '-': addToken(MINUS); break;
      case '+': addToken(PLUS); break;
      case ';': addToken(SEMICOLON); break;
      case '*': addToken(STAR); break;
      // One or two character tokens.
      case '!':
        addToken(match('=') ? BANG_EQUAL : BANG);
        break;
      case '=':
        addToken(match('=') ? EQUAL_EQUAL : EQUAL);
        break;
      case '<':
        addToken(match('=') ? LESS_EQUAL : LESS);
        break;
      case '>':
        addToken(match('=') ? GREATER_EQUAL : GREATER);
        break;
      case '/':
        if (match('/')) {
          // A comment goes until the end of the line.
          while (peek() != '\n' && !isAtEnd()) advance();
        } else {
          addToken(SLASH);
        }
        break;
      // Ignore whitespace.
      case ' ':
      case '\r':
      case '\t':
        break;
      // Increment line number on newline.
      case '\n':
        line++;
        break;
      // String literals.
      case '"': string(); break;
      default:
        // Digits.
        if (isDigit(c)) {
          number();
        } else if (isAlpha(c)) {
          // Identifiers or keywords.
          identifier();
        } else {
          // Error on unexpected character.
          Lox.error(line, "Unexpected character.");
        }
        break;
    }
  }

  // Handles identifiers and keywords.
  private void identifier() {
    while (isAlphaNumeric(peek())) advance();
    String text = source.substring(start, current);
    TokenType type = keywords.getOrDefault(text, IDENTIFIER);
    addToken(type);
  }

  // Handles number literals, including integers and floating-point numbers.
  private void number() {
    while (isDigit(peek())) advance();

    // Look for a fractional part.
    if (peek() == '.' && isDigit(peekNext())) {
      advance(); // Consume the '.'

      while (isDigit(peek())) advance();
    }

    addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
  }

  // Handles string literals.
  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n') line++;
      advance();
    }

    if (isAtEnd()) {
      Lox.error(line, "Unterminated string.");
      return;
    }

    advance(); // The closing quote.

    // Trim the surrounding quotes and add the string token.
    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
  }

  // Checks if the current character matches the expected character.
  private boolean match(char expected) {
    if (isAtEnd() || source.charAt(current) != expected) return false;
    current++;
    return true;
  }

  // Returns the current character without advancing.
  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }

  // Returns the next character without advancing.
  private char peekNext() {
    if (current + 1 >= source.length()) return '\0';
    return source.charAt(current + 1);
  }

  // Checks if a character is an alphabet letter or underscore.
  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
  }

  // Checks if a character is alphanumeric (letter, digit, or underscore).
  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  // Checks if a character is a digit.
  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }
}