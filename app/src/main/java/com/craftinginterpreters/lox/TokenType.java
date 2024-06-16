package com.craftinginterpreters.lox;

// Defines the various types of tokens that can be encountered in the Lox language.
enum TokenType {
  // Single-character tokens.
  LEFT_PAREN, // Represents '('
  RIGHT_PAREN, // Represents ')'
  LEFT_BRACE, // Represents '{'
  RIGHT_BRACE, // Represents '}'
  COMMA, // Represents ','
  DOT, // Represents '.'
  MINUS, // Represents '-'
  PLUS, // Represents '+'
  SEMICOLON, // Represents ';'
  SLASH, // Represents '/'
  STAR, // Represents '*'

  // One or two character tokens.
  BANG, // Represents '!'
  BANG_EQUAL, // Represents '!='
  EQUAL, // Represents '='
  EQUAL_EQUAL, // Represents '=='
  GREATER, // Represents '>'
  GREATER_EQUAL, // Represents '>='
  LESS, // Represents '<'
  LESS_EQUAL, // Represents '<='

  // Literals.
  IDENTIFIER, // Represents variable names and other identifiers
  STRING, // Represents string literals
  NUMBER, // Represents numeric literals

  // Keywords.
  AND, // Represents the logical 'and'
  CLASS, // Used to define classes
  ELSE, // Used in conditional statements
  FALSE, // Represents the boolean value 'false'
  FUN, // Used to define functions
  FOR, // Used to define for loops
  IF, // Used in conditional statements
  NIL, // Represents the 'null' value
  OR, // Represents the logical 'or'
  PRINT, // Used to print values
  RETURN, // Used in functions to return a value
  SUPER, // Used in classes to refer to the superclass
  THIS, // Used in classes to refer to the current instance
  TRUE, // Represents the boolean value 'true'
  VAR, // Used to declare variables
  WHILE, // Used to define while loops

  EOF // Represents the end of the file
}