
import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;
import AST.*;

public class Main
{
	static public void main(String argv[])
	{
		// 23/11
		Lexer l;
		Parser p;
		Symbol s;
		AST_PROGRAM AST;
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
			
			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			AST = (AST_PROGRAM) p.parse().value;

			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			AST.PrintMe();
			file_writer.write("OK\n");

			/*************************/
			/* [7] Close output file */
			/*************************/
			file_writer.close();
			
			/*************************************/
			/* [8] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile();

    	}

		catch (IllegalArgumentException e){
			writeTOfile(e.getMessage(), outputFilename);
			e.printStackTrace();
		}
		catch (IllegalStateException e) {
			writeTOfile(e.getMessage(), outputFilename);
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeTOfile(String message, String outputFile){
		try {
			PrintWriter file_writer = new PrintWriter(outputFile);
			file_writer.write(message);
			file_writer.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}


