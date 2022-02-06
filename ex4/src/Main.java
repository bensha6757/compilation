   
import java.io.*;
import java.io.PrintWriter;

import AST.AST_DEC_LIST;
import AST.AST_GRAPHVIZ;
import AST.AST_PROGRAM;
import java_cup.runtime.Symbol;
import IR.*;
import MIPS.*;

public class Main
{
	static public void main(String argv[])
	{
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

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			AST.SemantMe();

			/**********************/
			/* [8] IR the AST ... */
			/**********************/
			AST.IRme();

            Register_Allocation.register_allocation_algorithm();

            /***********************/
			/* [9] MIPS the IR ... */
			/***********************/
			IR.getInstance().MIPSme();

			/**************************************/
			/* [10] Finalize AST GRAPHIZ DOT file */
			/**************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile();

			/***************************/
			/* [11] Finalize MIPS file */
			/***************************/
			MIPSGenerator.getInstance().finalizeFile();			

			/**************************/
			/* [12] Close output file */
			/**************************/
			file_writer.close();
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


