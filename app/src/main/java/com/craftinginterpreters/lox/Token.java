package com.craftinginterpreters.lox;

// Represents a single token in the Lox language
class Token {
  // The type of the token (e.g., NUMBER, STRING, IDENTIFIER)
  final TokenType type;
  // The actual text of the token as it appeared in the source code
  final String lexeme;
  // The literal value of the token, if applicable (e.g., the actual number for a NUMBER token)
  final Object literal;
  // The line number in the source code where the token was found
  final int line;

  // Constructor for creating a new Token instance
  Token(TokenType type, String lexeme, Object literal, int line) {
    this.type = type; // Assign the type of the token
    this.lexeme = lexeme; // Assign the text of the token
    this.literal = literal; // Assign the literal value of the token
    this.line = line; // Assign the line number of the token
  }

  // Overrides the default toString method to provide a string representation of the token
  public String toString() {
    return type + " " + lexeme + " " + literal; // Returns a string combining the token's type, text, and literal value
  }
}
