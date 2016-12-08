package cz.cuni.mff.d3s.trupple.language.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.frame.FrameSlot;

import cz.cuni.mff.d3s.trupple.language.customtypes.ICustomType;
import cz.cuni.mff.d3s.trupple.language.nodes.BlockNode;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.InitializationNodeFactory;
import cz.cuni.mff.d3s.trupple.language.nodes.NopNode;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.AddNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.DivideIntegerNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.DivideNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.ModuloNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.MultiplyNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.NegationNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.SubstractNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.RandomBuiltinNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.RandomizeBuiltinNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.ReadlnBuiltinNode;
import cz.cuni.mff.d3s.trupple.language.nodes.call.InvokeNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.control.BreakNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.CaseNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.ForNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.IfNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.RepeatNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.WhileNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.FunctionBodyNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.FunctionBodyNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.function.ProcedureBodyNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.CharLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.DoubleLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.FunctionLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.LogicLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.LongLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.StringLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.AndNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.EqualsNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.LessThanNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.LessThanOrEqualNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.NotNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.OrNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.ArrayIndexAssignmentNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.ReadArrayIndexNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.ReadVariableNodeGen;
import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalFunctionRegistry;

public class NodeFactory {

	private Parser parser;
	private LexicalScope lexicalScope;
    private int loopDepth = 0;

	/* State while parsing case statement */
	// --> TODO: this causes to be unable to create nested cases....
	private List<ExpressionNode> caseExpressions;
	private List<StatementNode> caseStatements;
	private StatementNode caseElse;

	private Map<String, Unit> units = new HashMap<>();
	private Unit currentUnit = null;

	public NodeFactory(Parser parser) {
		this.parser = parser;
	}

	// -------------------------------------------------------
	void startPascal() {
		assert this.lexicalScope == null;
		this.lexicalScope = new LexicalScope(null, "main");
	}

	void registerVariables(List<String> identifiers, Token variableType) {
		String typeName = variableType.val.toLowerCase();

		for (String identifier : identifiers) {
			try {
				lexicalScope.registerLocalVariable(identifier, typeName);
			} catch (LexicalException e) {
				parser.SemErr(e.getMessage());
			}
		}
	}

    OrdinalDescriptor createSimpleOrdinalDescriptor(final int lowerBound, final int upperBound) {
        try {
            return lexicalScope.createRangeDescriptor(lowerBound, upperBound);
        } catch (LexicalException e){
            parser.SemErr("Greater lower bound then upper bound.");
            return lexicalScope.createImplicitRangeDescriptor();
        }
    }

    OrdinalDescriptor createSimpleOrdinalDescriptorFromTypename(Token typeNameToken) {
        String identifier = this.getIdentifierFromToken(typeNameToken);
        try {
            return lexicalScope.createRangeDescriptorFromTypename(identifier);
        } catch (LexicalException e){
            parser.SemErr("Greater lower bound then upper bound.");
            return lexicalScope.createImplicitRangeDescriptor();
        }
    }

	void registerArrayVariable(List<String> identifiers, List<OrdinalDescriptor> ordinalDimensions, Token returnTypeToken) {
		for(String identifier : identifiers) {
            try {
                lexicalScope.registerLocalArrayVariable(identifier, ordinalDimensions, returnTypeToken.val.toLowerCase());
            } catch (LexicalException e) {
                parser.SemErr(e.getMessage());
            }
		}
	}

    void registerEnumType(String identifier, List<String> identifiers){
        try {
            lexicalScope.registerEnumType(identifier, identifiers);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

	void startProcedure(Token identifierToken, List<FormalParameter> formalParameters) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        try {
            lexicalScope.registerProcedureInterface(identifier, formalParameters);
            lexicalScope = new LexicalScope(lexicalScope, identifier);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
	}

	void startFunction(Token identifierToken, List<FormalParameter> formalParameters, Token returnTypeToken) {
        String identifier = this.getIdentifierFromToken(identifierToken);
        String returnType = this.getIdentifierFromToken(returnTypeToken);
        try {
            lexicalScope.registerFunctionInterface(identifier, formalParameters, returnType);
            lexicalScope = new LexicalScope(lexicalScope, identifier);
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    void appendFormalParameter(List<FormalParameter> parameter, List<FormalParameter> params) {
        params.addAll(parameter);
    }

    List<FormalParameter> createFormalParametersList(List<String> identifiers, String typeName, boolean isOutput) {
        List<FormalParameter> paramList = new ArrayList<>();
        for (String identifier : identifiers) {
            paramList.add(new FormalParameter(identifier, typeName, isOutput));
        }

        return paramList;
    }

    void finishProcedure() {
        finishSubroutine();
    }

    void finishFunction() {
        finishSubroutine();
    }

    void finishProcedure(StatementNode bodyNode) {
        StatementNode subroutineNode = createSubroutineNode(bodyNode);
        final ProcedureBodyNode procedureBodyNode = new ProcedureBodyNode(subroutineNode);
        finishSubroutine(procedureBodyNode);
    }

    void finishFunction(StatementNode bodyNode) {
        StatementNode subroutineNode = createSubroutineNode(bodyNode);
        final FunctionBodyNode functionBodyNode = FunctionBodyNodeGen.create(subroutineNode, lexicalScope.getReturnSlot());
        finishSubroutine(functionBodyNode);
    }

    void startLoop() {
        lexicalScope.increaseLoopDepth();
    }

    StatementNode createForLoop(boolean ascending, Token variableToken, ExpressionNode startValue, ExpressionNode finalValue, StatementNode loopBody) {
        String iteratingIdentifier = this.getIdentifierFromToken(variableToken);
        FrameSlot iteratingSlot = lexicalScope.getLocalSlot(iteratingIdentifier);
        if (iteratingSlot == null) {
            parser.SemErr("Unknown identifier: " + iteratingIdentifier);
        }
        return new ForNode(ascending, iteratingSlot, startValue, finalValue, loopBody);
    }

    StatementNode createRepeatLoop(ExpressionNode condition, StatementNode loopBody) {
        return new RepeatNode(condition, loopBody);
    }

    StatementNode createWhileLoop(ExpressionNode condition, StatementNode loopBody) {
        return new WhileNode(condition, loopBody);
    }

    StatementNode createBreak() {
        // TODO: check if TurboPascal standard is set
        if (lexicalScope.isInLoop()) {
            parser.SemErr("Break outside a loop: ");
        }
        return new BreakNode();
    }

	void finishLoop() {
        try {
            lexicalScope.decreaseLoopDepth();
        } catch (LexicalException e) {
            parser.SemErr(e.getMessage());
        }
    }

    StatementNode createIfStatement(ExpressionNode condition, StatementNode thenNode, StatementNode elseNode) {
        return new IfNode(condition, thenNode, elseNode);
    }

    StatementNode createNopStatement() {
        return new NopNode();
    }

    ExpressionNode createBinaryExpression(Token operator, ExpressionNode leftNode, ExpressionNode rightNode) {
        switch (operator.val.toLowerCase()) {

            // arithmetic
            case "+":
                return AddNodeGen.create(leftNode, rightNode);
            case "-":
                return SubstractNodeGen.create(leftNode, rightNode);
            case "*":
                return MultiplyNodeGen.create(leftNode, rightNode);
            case "/":
                return DivideNodeGen.create(leftNode, rightNode);
            case "div":
                return DivideIntegerNodeGen.create(leftNode, rightNode);
            case "mod":
                return ModuloNodeGen.create(leftNode, rightNode);

            // logic
            case "and":
                return AndNodeGen.create(leftNode, rightNode);
            case "or":
                return OrNodeGen.create(leftNode, rightNode);

            case "<":
                return LessThanNodeGen.create(leftNode, rightNode);
            case "<=":
                return LessThanOrEqualNodeGen.create(leftNode, rightNode);
            case ">":
                return NotNodeGen.create(LessThanOrEqualNodeGen.create(leftNode, rightNode));
            case ">=":
                return NotNodeGen.create(LessThanNodeGen.create(leftNode, rightNode));
            case "=":
                return EqualsNodeGen.create(leftNode, rightNode);
            case "<>":
                return NotNodeGen.create(EqualsNodeGen.create(leftNode, rightNode));

            default:
                parser.SemErr("Unknown binary operator: " + operator.val);
                return null;
        }
    }

    ExpressionNode createUnaryExpression(Token operator, ExpressionNode son) {
        switch (operator.val) {
            case "+":
                return son;
            case "-":
                return NegationNodeGen.create(son);
            case "not":
                return NotNodeGen.create(son);
            default:
                parser.SemErr("Unexpected unary operator: " + operator.val);
                return null;
        }
    }

    ExpressionNode createLogicLiteral(boolean value) {
        return new LogicLiteralNode(value);
    }

    ExpressionNode createNumericLiteral(Token literalToken) {
        try {
            return new LongLiteralNode(Long.parseLong(literalToken.val));
        } catch (NumberFormatException e) {
            parser.SemErr("Integer literal out of range");
            return new LongLiteralNode(0);
        }
    }

    ExpressionNode createFloatLiteral(Token token) {
        double value = Float.parseFloat(token.val.toString());
        return new DoubleLiteralNode(value);
    }

    ExpressionNode createCharOrStringLiteral(String literal) {
        return (literal.length() == 1) ? new CharLiteralNode(literal.charAt(0)) : new StringLiteralNode(literal);
    }

    StatementNode createBlockNode(List<StatementNode> bodyNodes) {
        return new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
    }

    // TODO: this main node can be in lexical scope instead of a parser
    PascalRootNode finishMainFunction(StatementNode blockNode) {
        StatementNode bodyNode = this.createSubroutineNode(blockNode);
        return new PascalRootNode(lexicalScope.getFrameDescriptor(), new ProcedureBodyNode(bodyNode));
    }

	private String getIdentifierFromToken(Token identifier) {
        return identifier.val.toLowerCase();
    }

    private StatementNode createSubroutineNode(StatementNode bodyNode) {
        List<StatementNode> subroutineNodes = lexicalScope.createInitializationNodes();
        subroutineNodes.add(bodyNode);

        return new BlockNode(subroutineNodes.toArray(new StatementNode[lexicalScope.scopeNodes.size()]));
    }

    private void finishSubroutine() {
        lexicalScope = lexicalScope.getOuterScope();
    }

    private void finishSubroutine(ExpressionNode subroutineBodyNode) {
        final PascalRootNode rootNode = new PascalRootNode(lexicalScope.getFrameDescriptor(), subroutineBodyNode);

        String subroutineIdentifier = lexicalScope.getName();
        lexicalScope = lexicalScope.getOuterScope();
        lexicalScope.getContext().getGlobalFunctionRegistry().setFunctionRootNode(subroutineIdentifier, rootNode);
    }

	// ------------------------------------------------------

	public void startCaseList() {
		this.caseExpressions = new ArrayList<>();
		this.caseStatements = new ArrayList<>();
	}

	public void addCaseOption(ExpressionNode expression, StatementNode statement) {
		this.caseExpressions.add(expression);
		this.caseStatements.add(statement);
	}

	public void setCaseElse(StatementNode statement){
		this.caseElse = statement;
	}

	public CaseNode finishCaseStatement(ExpressionNode caseIndex) {
		CaseNode node = new CaseNode(caseIndex, caseExpressions.toArray(new ExpressionNode[caseExpressions.size()]),
				caseStatements.toArray(new StatementNode[caseStatements.size()]), caseElse);

		caseExpressions = null;
		caseStatements = null;
		caseElse = null;

		return node;
	}

	public ExpressionNode createFunctionNode(Token tokenName) {
		String functionName = tokenName.val.toLowerCase();

		PascalContext context = lexicalScope.getContext();
		while(context != null){
			if(!context.containsFunction(functionName)){
				context = context.getOuterContext();
			} else {
				return new FunctionLiteralNode(context, functionName);
			}
		}
		parser.SemErr("Undefined function: " + functionName);
		return null;
	}

	public ExpressionNode createReadArrayValue(Token identifier, List<ExpressionNode> indexingNodes) {
		return ReadArrayIndexNodeGen.create(indexingNodes.toArray(new ExpressionNode[indexingNodes.size()]),
				lexicalScope.getLocalSlot(identifier.val.toLowerCase()));
	}

	public ExpressionNode createIndexingNode(Token identifier) {
		return new StringLiteralNode(identifier.val.toLowerCase());
	}

	public ExpressionNode createArrayIndexAssignment(Token name, List<ExpressionNode> indexingNodes, ExpressionNode valueNode) {
		return new ArrayIndexAssignmentNode(lexicalScope.getLocalSlot(name.val.toLowerCase()),
				indexingNodes.toArray(new ExpressionNode[indexingNodes.size()]), valueNode);
	}

	public StatementNode createRandomizeNode() {
		return new RandomizeBuiltinNode(lexicalScope.getContext());
	}

	public ExpressionNode createRandomNode() {
		return new RandomBuiltinNode(lexicalScope.getContext());
	}

	public ExpressionNode createRandomNode(Token numericLiteral) {
		return new RandomBuiltinNode(lexicalScope.getContext(), Long.parseLong(numericLiteral.val));
	}

	public ExpressionNode readSingleIdentifier(Token nameToken) {
		String identifier = nameToken.val.toLowerCase();
		FrameSlot frameSlot = lexicalScope.getVisibleSlot(identifier);

		// firstly check if it is a variable
		if (frameSlot != null){
			return ReadVariableNodeGen.create(frameSlot);

		// secondly, try to create a procedure or function literal (with no arguments)
		} else {
            LexicalScope iteratingScope = this.lexicalScope;
			while(iteratingScope != null) {
				if(iteratingScope.getContext().containsParameterlessSubroutine(identifier)) {
					ExpressionNode literal = this.createFunctionNode(nameToken);
					return this.createCall(literal, new ArrayList<>());
				} else {
                    iteratingScope = iteratingScope.getOuterScope();
				}
			}

			return null;
		}
	}

	public ExpressionNode createCall(ExpressionNode functionLiteral, List<ExpressionNode> params) {
		return InvokeNodeGen.create(params.toArray(new ExpressionNode[params.size()]), functionLiteral);
	}

	public StatementNode createReadLine() {
		return new ReadlnBuiltinNode(lexicalScope.getContext());
	}

	public StatementNode createReadLine(List<String> identifiers){
		FrameSlot[] slots = new FrameSlot[identifiers.size()];
		for(int i = 0; i < slots.length; i++) {
			String currentIdentifier = identifiers.get(i);
			slots[i] = lexicalScope.getLocalSlot(currentIdentifier);
			if(slots[i] == null) {
				parser.SemErr("UnknownDescriptor identifier: " + currentIdentifier + ".");
			}
		}

		ReadlnBuiltinNode readln = new ReadlnBuiltinNode(lexicalScope.getContext(), slots);
		return readln;
	}


	public String createStringFromLiteral(Token t) {
		return (String)t.val.subSequence(1, t.val.length() - 1);
	}

	public void createNumericConstant(Token nameToken, NumericConstant value) {
		if (value.isDoubleType) {
			this.createDoubleConstant(nameToken, value.getDouble());
		} else {
			this.createLongConstant(nameToken, value.getLong());
		}
	}

	// Tip of the day: Fuck Java

	public void createLongConstant(Token nameToken, long value) {
		FrameSlot newSlot = registerConstant(nameToken, value);

		if (newSlot == null) {
			return;
		}
        lexicalScope.addInitializationNode(InitializationNodeFactory.create(newSlot, value));
	}

	public void createDoubleConstant(Token nameToken, double value) {
		FrameSlot newSlot = registerConstant(nameToken, value);

		if (newSlot == null) {
			return;
		}
        lexicalScope.addInitializationNode(InitializationNodeFactory.create(newSlot, value));
	}

	public void createStringOrCharConstant(Token nameToken, String value) {
		if(value.length() == 1) {
			createCharConstant(nameToken, value.charAt(0));
		} else {
			createStringConstant(nameToken, value);
		}
	}

	public void createCharConstant(Token nameToken, char value) {
		FrameSlot newSlot = registerConstant(nameToken, value);

		if (newSlot == null) {
			return;
		}
        lexicalScope.addInitializationNode(InitializationNodeFactory.create(newSlot, value));
	}

	public void createStringConstant(Token nameToken, String value) {
		FrameSlot newSlot = registerConstant(nameToken, value);

		if (newSlot == null) {
			return;
		}
        lexicalScope.addInitializationNode(InitializationNodeFactory.create(newSlot, value));
	}

	public void createBooleanConstant(Token nameToken, boolean value) {
		FrameSlot newSlot = registerConstant(nameToken, value);

		if (newSlot == null) {
			return;
		}
        lexicalScope.addInitializationNode(InitializationNodeFactory.create(newSlot, value));
	}

	public void createObjectConstant(Token nameToken, Token objectNameToken) {
		/*
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		String identifier = nameToken.val.toLowerCase();
		FrameSlot objectValueSlot = getVisibleSlot(objectNameToken.val.toLowerCase());
		*/
	}

	private FrameSlot registerConstant(Token nameToken, Object value) {
		String identifier = nameToken.val.toLowerCase();
        try {
            return lexicalScope.registerLocalConstant(identifier, value);
        } catch (IllegalArgumentException e) {
            parser.SemErr("Duplicate identifier: " + identifier + ".");
            return null;
        }
	}

	public NumericConstant createUnsignedConstant(NumericConstant value, Token signToken) {
		switch(signToken.val) {
		case "-":
			return (value.isDoubleType)?
					new NumericConstant(-value.getDouble(), true) :
					new NumericConstant(-value.getLong(), false);
		case "+":
			return value;
		default:
			parser.SemErr("Unkown operator " + signToken.val + ".");
			return null;
		}
	}

	public NumericConstant createNumericConstantFromBinary(NumericConstant value, NumericConstant rvalue, Token opToken) {
		boolean isDoubleType = value.isDoubleType || rvalue.isDoubleType;

		switch(opToken.val) {
		case "-":
			return (isDoubleType)?
				new NumericConstant(value.getDouble() - rvalue.getDouble()) :
				new NumericConstant(value.getLong() - rvalue.getLong(), false);
		case "+":
			return (isDoubleType)?
					new NumericConstant(value.getDouble() + rvalue.getDouble()) :
					new NumericConstant(value.getLong() + rvalue.getLong(), false);
		case "*":
			return (isDoubleType)?
					new NumericConstant(value.getDouble() * rvalue.getDouble()) :
					new NumericConstant(value.getLong() * rvalue.getLong(), false);
		case "/":
			return new NumericConstant(value.getDouble() / rvalue.getDouble());
		case "div":
			if (isDoubleType) {
				parser.SemErr("Operand types do not match operator.");
				return null;
			}
			return new NumericConstant(value.getLong() / rvalue.getLong(), false);
		case "mod":
			if (isDoubleType) {
				parser.SemErr("Operand types do not match operator.");
				return null;
			}
			return new NumericConstant(value.getLong() % rvalue.getLong(), false);
		default:
			parser.SemErr("Unkown operator " + opToken.val + ".");
			return null;
		}
	}

	public NumericConstant getNumericConstant(Token nameToken) {
		String identifier = nameToken.val.toLowerCase();
		Object c = getConstant(identifier);
		if (c == null) {
			return null;
		}

		if (c instanceof Long) {
			return new NumericConstant(c, false);
		} else if (c instanceof Double) {
			return new NumericConstant(c, true);
		} else {
			parser.SemErr("Wrong constant type in expression " + identifier +".");
			return null;
		}
	}

	public String getStringConstant(Token nameToken) {
		String identifier = nameToken.val.toLowerCase();
		Object str = getConstant(identifier);
		if (str == null) {
			return null;
		} else if ( str instanceof String) {
			return (String)str;
		} else {
			parser.SemErr("Wrong constant type in expression " + identifier +".");
			return null;
		}
	}

	public boolean getBooleanConstant(Token nameToken) {
		String identifier = nameToken.val.toLowerCase();
		Object b = getConstant(identifier);
		if (b == null) {
			return false;
		} else if (b instanceof Boolean){
			return (boolean)b;
		} else {
			parser.SemErr("Wrong constant type in expression " + identifier +".");
			return false;
		}
	}

	private Object getConstant(String identifier) {
		if (!lexicalScope.containsLocalConstant(identifier)) {
			parser.SemErr("UnknownDescriptor constant " + identifier +".");
			return null;
		}

		return lexicalScope.getLocalConstant(identifier);
	}

	public String createStringFromToken(Token t) {
		String literal = t.val;
		return literal.substring(1, literal.length() - 1);
	}

	public ExpressionNode createAssignment(Token nameToken, ExpressionNode valueNode) {
		FrameSlot slot = lexicalScope.getVisibleSlot(nameToken.val.toLowerCase());
		if (slot == null)
			return null;

		return AssignmentNodeGen.create(valueNode, slot);
	}

	public void importUnit(Token unitToken) {
		String importingUnit = unitToken.val.toLowerCase();

		if (!units.containsKey(importingUnit)) {
			parser.SemErr("UnknownDescriptor unit. Did you forget to import it to compiler? - " + importingUnit);
			return;
		}

		Unit unit = units.get(importingUnit);

		// functions
		PascalFunctionRegistry fRegistry = unit.getContext().getGlobalFunctionRegistry();
		lexicalScope.getContext().getGlobalFunctionRegistry().addAll(fRegistry);

		// custom types
		for(String typeIdentifier : unit.getLexicalScope().getAllCustomTypes().keySet()){
			ICustomType custom = unit.getLexicalScope().getAllCustomTypes().get(typeIdentifier);
			if(custom.isGlobal()){
				lexicalScope.registerCustomType(typeIdentifier, custom);
			}
		}
	}

    boolean containsIdentifier(String identifier) {
        return this.lexicalScope.containsLocalIdentifier(identifier);
    }

	/*****************************************************************************
	 * UNIT SECTION
	 *****************************************************************************/

	public void startUnit(Token t) {
		String unitName = t.val.toLowerCase();

		if (units.containsValue(t.val.toLowerCase())) {
			parser.SemErr("Unit with name " + unitName + " is already defined.");
			return;
		}

		currentUnit = new Unit(unitName);
		this.units.put(unitName, currentUnit);
        this.lexicalScope = currentUnit.getLexicalScope();
	}

	public void endUnit() {
		currentUnit = null;
        this.lexicalScope = null;
	}

	public void addProcedureInterface(Token name, List<FormalParameter> formalParameters) {
		if(currentUnit == null) {
			lexicalScope.getContext().getGlobalFunctionRegistry().registerFunctionName(name.val.toLowerCase());
            this.lexicalScope = this.lexicalScope.getOuterScope();
		} else if (!currentUnit.addProcedureInterface(name.val.toLowerCase(), formalParameters)) {
			parser.SemErr("Subroutine with this name is already defined: " + name);
		}
	}

	public void addFunctionInterface(Token name, List<FormalParameter> formalParameters, String returnType) {
		if(currentUnit == null) {
			lexicalScope.getContext().getGlobalFunctionRegistry().registerFunctionName(name.val.toLowerCase());
		} else if (!currentUnit.addFunctionInterface(name.val.toLowerCase(), formalParameters, returnType)) {
			parser.SemErr("Subroutine with this name is already defined: " + name);
		}
	}

	public void finishFormalParameterListProcedure(Token name, List<FormalParameter> parameters) {
		String identifier = name.val.toLowerCase();

		// the subroutine is in outer context because now the parser is in the subroutine's own context
		lexicalScope.getOuterScope().getContext().setMySubroutineParametersCount(identifier, parameters.size());

		if (currentUnit == null)
			return;

		if (!currentUnit.checkProcedureMatchInterface(identifier, parameters)) {
			parser.SemErr("Procedure heading for " + identifier + " does not match any procedure from the interface.");
		}
	}

	public void finishFormalParameterListFunction(Token name, List<FormalParameter> parameters, String returnType) {
		String identifier = name.val.toLowerCase();

		// the subroutine is in outer context because now the parser is in the subroutine's own context
		lexicalScope.getOuterScope().getContext().setMySubroutineParametersCount(identifier, parameters.size());

		if(currentUnit == null)
			return;

		if (!currentUnit.checkFunctionMatchInterface(identifier, parameters, returnType)) {
			parser.SemErr("Function heading for " + identifier + " does not match any function from the interface.");
		}
	}

	public void leaveUnitInterfaceSection(){
		assert currentUnit != null;
		currentUnit.leaveInterfaceSection();
	}
}
