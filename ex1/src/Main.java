
import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;
public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Symbol s;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
        {
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);

			/***********************/
			/* [4] Read next token */
			/***********************/
			s = l.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			while (s.sym != TokenNames.EOF)
			{
				/************************/
				/* [6] Print to console */
				/************************/
                String tokenName = tokenIdToTokenName(s.sym);
                if ((tokenName.equals("INT") && (int)s.value > (Math.pow(2, 15) - 1)) || tokenName.equals("ERROR")) {
                    file_writer = new PrintWriter(outputFilename);
                    file_writer.print("");
                    file_writer.print("ERROR");
                    file_writer.close();
                    System.out.println("ERROR");
                    return;
                }
                System.out.print(tokenName);
                if (s.value != null) {
                    System.out.print("(");
                    System.out.print(s.value);
                    System.out.print(")");
                }
                System.out.print("[");
                System.out.print(l.getLine());
                System.out.print(",");
                System.out.print(l.getTokenStartPosition());
                System.out.print("]");
                System.out.print("\n");

				/*********************/
				/* [7] Print to file */
				/*********************/

                file_writer.print(tokenName);
                if (s.value != null) {
                    file_writer.print("(");
                    file_writer.print(s.value);
                    file_writer.print(")");
                }
                file_writer.print("[");
                file_writer.print(l.getLine());
                file_writer.print(",");
                file_writer.print(l.getTokenStartPosition());
                file_writer.print("]");
                file_writer.print("\n");
				
				/***********************/
				/* [8] Read next token */
				/***********************/
				s = l.next_token();
			}
			
			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [10] Close output file */
			/**************************/
			file_writer.close();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
    private static String tokenIdToTokenName(int tokenId){
        switch (tokenId){
            case 0:
                return "EOF";
            case 1:
                return "PLUS";
            case 2:
                return "MINUS";
            case 3:
                return "TIMES";
            case 4:
                return "DIVIDE";
            case 5:
                return "LPAREN";
            case 6:
                return "RPAREN";
            case 7:
                return "INT";
            case 8:
                return "ID";
            case 9:
                return "STRING";
            case 10:
                return "LBRACK";
            case 11:
                return "RBRACK";
            case 12:
                return "LBRACE";
            case 13:
                return "RBRACE";
            case 14:
                return "NIL";
            case 15:
                return "COMMA";
            case 16:
                return "DOT";
            case 17:
                return "SEMICOLON";
            case 18:
                return "ASSIGN";
            case 19:
                return "EQ";
            case 20:
                return "LT";
            case 21:
                return "GT";
            case 22:
                return "ARRAY";
            case 23:
                return "CLASS";
            case 24:
                return "EXTENDS";
            case 25:
                return "RETURN";
            case 26:
                return "WHILE";
            case 27:
                return "IF";
            case 28:
                return "NEW";
            case 29:
                return "TYPE_INT";
            case 30:
                return "TYPE_STRING";
            case 31:
                return "TYPE_VOID";
            default:
                return "ERROR";
        }
    }
}


