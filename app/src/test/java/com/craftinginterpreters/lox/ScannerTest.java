package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.Scanner;
import com.craftinginterpreters.lox.Token;
import com.craftinginterpreters.lox.TokenType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static com.craftinginterpreters.lox.TokenType.*;

class ScannerTest {

    @Test
    void testSingleCharacterTokens() {
        Scanner scanner = new Scanner("(){},.-+;*");
        List<Token> tokens = scanner.scanTokens();
        TokenType[] expectedTypes = {LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, COMMA, DOT, MINUS, PLUS, SEMICOLON, STAR, EOF};
        assertEquals(expectedTypes.length, tokens.size());
        for (int i = 0; i < expectedTypes.length; i++) {
            assertEquals(expectedTypes[i], tokens.get(i).type);
        }
    }

    @Test
    void testTwoCharacterTokens() {
        Scanner scanner = new Scanner("!= == <= >=");
        List<Token> tokens = scanner.scanTokens();
        TokenType[] expectedTypes = {BANG_EQUAL, EQUAL_EQUAL, LESS_EQUAL, GREATER_EQUAL, EOF};
        assertEquals(expectedTypes.length, tokens.size());
        for (int i = 0; i < expectedTypes.length; i++) {
            assertEquals(expectedTypes[i], tokens.get(i).type);
        }
    }

    @Test
    void testLiterals() {
        Scanner scanner = new Scanner("\"string\" 1234 identifier");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(STRING, tokens.get(0).type);
        assertEquals("string", tokens.get(0).literal);
        assertEquals(NUMBER, tokens.get(1).type);
        assertEquals(1234.0, tokens.get(1).literal);
        assertEquals(IDENTIFIER, tokens.get(2).type);
        assertEquals("identifier", tokens.get(2).lexeme);
    }

    @Test
    void testKeywords() {
        Scanner scanner = new Scanner("class var fun");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(CLASS, tokens.get(0).type);
        assertEquals(VAR, tokens.get(1).type);
        assertEquals(FUN, tokens.get(2).type);
    }

    @Test
    void testComments() {
        Scanner scanner = new Scanner("// This is a comment\n123");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(NUMBER, tokens.get(0).type);
        assertEquals(123.0, tokens.get(0).literal);
    }

    @Test
    void testWhitespace() {
        Scanner scanner = new Scanner(" \r\t\n123");
        List<Token> tokens = scanner.scanTokens();
        assertEquals(2, tokens.size()); // NUMBER and EOF
        assertEquals(NUMBER, tokens.get(0).type);
    }
}