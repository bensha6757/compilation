/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/

/*****************************************************/
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */
/*****************************************************************************/
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; }

	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; }
	public int cup(){return yycolumn; }
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator	  = \r|\n|\r\n
Space             = [ \t\f]
WhiteSpace		  = {LineTerminator}|{Space}
INT 			  = 0|[1-9][0-9]*
LeadingZero       = 0+{INT}*
DIGIT             = [0-9]
ID				  = [a-z]+
LETTER            = [a-zA-Z]
Identifier        = {LETTER}[a-zA-Z0-9]*
STRING            = \"{LETTER}*\"
ANY               = .
Comment           = ({DIGIT}|{LETTER}|{Space}|\(|\)|\[|\]|\{|\}|\!|\?|\+|\-|\*|\/|\.|\;)*
CommentNoStarNoSLash   = ({DIGIT}|{LETTER}|{WhiteSpace}|\(|\)|\[|\]|\{|\}|\!|\?|\+|\-|\.|\;)
CommentNoStar    = ({DIGIT}|{LETTER}|{WhiteSpace}|\(|\)|\[|\]|\{|\}|\!|\?|\+|\-|\/|\.|\;)*
ShortComment      = \/\/{Comment}{LineTerminator}
LongComment 	  = \/\*((\*+{CommentNoStarNoSLash}+)|{CommentNoStar})*\**\*\/
IllegalComment    = \/\*
NIL               = nil
TYPE_INT          = int
ARRAY             = array
CLASS             = class
EXTENDS           = extends
RETURN            = return
WHILE             = while
IF                = if
NEW               = new
TYPE_STRING       = string
TYPE_VOID         = void


/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {

{NIL}			    { return symbol(TokenNames.NIL);}
{ARRAY}             { return symbol(TokenNames.ARRAY);}
{CLASS}             { return symbol(TokenNames.CLASS);}
{EXTENDS}           { return symbol(TokenNames.EXTENDS);}
{RETURN}            { return symbol(TokenNames.RETURN);}
{WHILE}             { return symbol(TokenNames.WHILE);}
{IF}                { return symbol(TokenNames.IF);}
{NEW}               { return symbol(TokenNames.NEW);}
int                 { return symbol(TokenNames.TYPE_INT);}
{TYPE_STRING}       { return symbol(TokenNames.TYPE_STRING);}
{TYPE_VOID}         { return symbol(TokenNames.TYPE_VOID);}
"+"					{ return symbol(TokenNames.PLUS);}
"-"					{ return symbol(TokenNames.MINUS);}
"*"				    { return symbol(TokenNames.TIMES);}
"/"					{ return symbol(TokenNames.DIVIDE);}
"("					{ return symbol(TokenNames.LPAREN);}
")"					{ return symbol(TokenNames.RPAREN);}
{INT}			    { if(Integer.parseInt(yytext()) > 0xffff) { return symbol(TokenNames.ERROR);} else {return symbol(TokenNames.INT, new Integer(yytext()));}}
{LeadingZero}       { return symbol(TokenNames.ERROR); }
{Identifier}		{ return symbol(TokenNames.ID, new String( yytext()));}
{WhiteSpace}		{ /* just skip what was found, do nothing */ }
{STRING}            { return symbol(TokenNames.STRING, new String( yytext()));}
"["					{ return symbol(TokenNames.LBRACK);}
"]"					{ return symbol(TokenNames.RBRACK);}
"{"					{ return symbol(TokenNames.LBRACE);}
"}"					{ return symbol(TokenNames.RBRACE);}
","                 { return symbol(TokenNames.COMMA);}
"."                 { return symbol(TokenNames.DOT);}
";"                 { return symbol(TokenNames.SEMICOLON);}
":="                { return symbol(TokenNames.ASSIGN);}
"="                 { return symbol(TokenNames.EQ);}
"<"                 { return symbol(TokenNames.LT);}
">"                 { return symbol(TokenNames.GT);}
{ShortComment}		{ /* just skip what was found, do nothing */ }
{LongComment}		{ /* just skip what was found, do nothing */ }
{IllegalComment}    { return symbol(TokenNames.ERROR); }
<<EOF>>				{ return symbol(TokenNames.EOF);}
{ANY}                 { return symbol(TokenNames.ERROR); }
}
