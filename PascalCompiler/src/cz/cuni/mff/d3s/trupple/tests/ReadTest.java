package cz.cuni.mff.d3s.trupple.tests;

import org.junit.Test;

public class ReadTest extends JUnitTest{

	@Test
	public void simpleParameterless() {
		String code = "program main; begin readln; write('asfd'); end.";
		String input = "ads\r\n";
		String output = "asfd";
		this.testWithInput(code, input, output);
	}
	
	@Test
	public void simpleParameterless2() {
		String code = "program main; begin readln(); write('asfd'); end.";
		String input = "ads\r\n";
		String output = "asfd";
		this.testWithInput(code, input, output);
	}
	
	@Test
	public void simpleReadInteger() {
		String code="program main; var i:integer;\n"+
				"\n"+
				"begin\n"+
				" readln(i);\n"+
				" write(i);\n"+
				"end.";
		String input = "15324";
		String output = "15324";
		this.testWithInput(code, input, output);
	}
	
	@Test
	public void simpleMultipleRead() {
		String code="program main; var i:integer;\n"+
				" b:boolean;\n"+
				"\n"+
				"begin\n"+
				" readln(i,b);\n"+
				" write(i,b);\n"+
				"end.";
		String input = "126\r\ntrue\r\n";
		String output = "126true";
		this.testWithInput(code, input, output);
	}
	
	@Test
	public void simpleCalculationBasedOnInput() {
		String code="program main; Var \n"+
				" Num1, Num2, Sum : Integer;\n"+
				"\n"+
				"Begin {no semicolon}\n"+
				"\tReadln(Num1);\n"+
				"\tReadln(Num2);\n"+
				"\tSum := Num1 + Num2; {addition} \n"+
				"\tWrite(Sum);\n"+
				"\tReadln;\n"+
				"End. ";
		String input="36\r\n18\r\n\r\n";
		String output="54";
		this.testWithInput(code, input, output);
	}
}
