// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalSubroutine;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PointerValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.SetTypeValue;

@GeneratedBy(PascalTypes.class)
public final class PascalTypesGen extends PascalTypes {

    @Deprecated public static final PascalTypesGen PASCALTYPES = new PascalTypesGen();

    protected PascalTypesGen() {
    }

    public static boolean isLong(Object value) {
        return value instanceof Long;
    }

    public static long asLong(Object value) {
        assert value instanceof Long : "PascalTypesGen.asLong: long expected";
        return (long) value;
    }

    public static long expectLong(Object value) throws UnexpectedResultException {
        if (value instanceof Long) {
            return (long) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isBoolean(Object value) {
        return value instanceof Boolean;
    }

    public static boolean asBoolean(Object value) {
        assert value instanceof Boolean : "PascalTypesGen.asBoolean: boolean expected";
        return (boolean) value;
    }

    public static boolean expectBoolean(Object value) throws UnexpectedResultException {
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isCharacter(Object value) {
        return value instanceof Character;
    }

    public static char asCharacter(Object value) {
        assert value instanceof Character : "PascalTypesGen.asCharacter: char expected";
        return (char) value;
    }

    public static char expectCharacter(Object value) throws UnexpectedResultException {
        if (value instanceof Character) {
            return (char) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isDouble(Object value) {
        return value instanceof Double;
    }

    public static double asDouble(Object value) {
        assert value instanceof Double : "PascalTypesGen.asDouble: double expected";
        return (double) value;
    }

    public static double expectDouble(Object value) throws UnexpectedResultException {
        if (value instanceof Double) {
            return (double) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isPascalSubroutine(Object value) {
        return value instanceof PascalSubroutine;
    }

    public static PascalSubroutine asPascalSubroutine(Object value) {
        assert value instanceof PascalSubroutine : "PascalTypesGen.asPascalSubroutine: PascalSubroutine expected";
        return (PascalSubroutine) value;
    }

    public static PascalSubroutine expectPascalSubroutine(Object value) throws UnexpectedResultException {
        if (value instanceof PascalSubroutine) {
            return (PascalSubroutine) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isPascalString(Object value) {
        return value instanceof PascalString;
    }

    public static PascalString asPascalString(Object value) {
        assert value instanceof PascalString : "PascalTypesGen.asPascalString: PascalString expected";
        return (PascalString) value;
    }

    public static PascalString expectPascalString(Object value) throws UnexpectedResultException {
        if (value instanceof PascalString) {
            return (PascalString) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isEnumValue(Object value) {
        return value instanceof EnumValue;
    }

    public static EnumValue asEnumValue(Object value) {
        assert value instanceof EnumValue : "PascalTypesGen.asEnumValue: EnumValue expected";
        return (EnumValue) value;
    }

    public static EnumValue expectEnumValue(Object value) throws UnexpectedResultException {
        if (value instanceof EnumValue) {
            return (EnumValue) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isPascalArray(Object value) {
        return value instanceof PascalArray;
    }

    public static PascalArray asPascalArray(Object value) {
        assert value instanceof PascalArray : "PascalTypesGen.asPascalArray: PascalArray expected";
        return (PascalArray) value;
    }

    public static PascalArray expectPascalArray(Object value) throws UnexpectedResultException {
        if (value instanceof PascalArray) {
            return (PascalArray) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isReference(Object value) {
        return value instanceof Reference;
    }

    public static Reference asReference(Object value) {
        assert value instanceof Reference : "PascalTypesGen.asReference: Reference expected";
        return (Reference) value;
    }

    public static Reference expectReference(Object value) throws UnexpectedResultException {
        if (value instanceof Reference) {
            return (Reference) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isPointerValue(Object value) {
        return value instanceof PointerValue;
    }

    public static PointerValue asPointerValue(Object value) {
        assert value instanceof PointerValue : "PascalTypesGen.asPointerValue: PointerValue expected";
        return (PointerValue) value;
    }

    public static PointerValue expectPointerValue(Object value) throws UnexpectedResultException {
        if (value instanceof PointerValue) {
            return (PointerValue) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isSetTypeValue(Object value) {
        return value instanceof SetTypeValue;
    }

    public static SetTypeValue asSetTypeValue(Object value) {
        assert value instanceof SetTypeValue : "PascalTypesGen.asSetTypeValue: SetTypeValue expected";
        return (SetTypeValue) value;
    }

    public static SetTypeValue expectSetTypeValue(Object value) throws UnexpectedResultException {
        if (value instanceof SetTypeValue) {
            return (SetTypeValue) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static boolean isFileValue(Object value) {
        return value instanceof FileValue;
    }

    public static FileValue asFileValue(Object value) {
        assert value instanceof FileValue : "PascalTypesGen.asFileValue: FileValue expected";
        return (FileValue) value;
    }

    public static FileValue expectFileValue(Object value) throws UnexpectedResultException {
        if (value instanceof FileValue) {
            return (FileValue) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static double asImplicitDouble(Object value) {
        if (value instanceof Long) {
            return castLongToDouble((long) value);
        } else if (value instanceof Double) {
            return (double) value;
        } else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new IllegalArgumentException("Illegal type ");
        }
    }

    public static boolean isImplicitDouble(Object value) {
        return value instanceof Long
             || value instanceof Double;
    }

    public static double asImplicitDouble(Object value, Class<?> typeHint) {
        if (typeHint == long.class) {
            return castLongToDouble((long) value);
        } else if (typeHint == double.class) {
            return (double) value;
        } else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new IllegalArgumentException("Illegal type ");
        }
    }

    public static double expectImplicitDouble(Object value, Class<?> typeHint) throws UnexpectedResultException {
        if (typeHint == long.class && value instanceof Long) {
            return castLongToDouble((long) value);
        } else if (typeHint == double.class && value instanceof Double) {
            return (double) value;
        } else {
            throw new UnexpectedResultException(value);
        }
    }

    public static boolean isImplicitDouble(Object value, Class<?> typeHint) {
        return (typeHint == long.class && value instanceof Long)
             || (typeHint == double.class && value instanceof Double);
    }

    public static Class<?> getImplicitDoubleClass(Object value) {
        if (value instanceof Long) {
            return long.class;
        } else if (value instanceof Double) {
            return double.class;
        } else if (value == null) {
            return Object.class;
        } else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new IllegalArgumentException("Illegal type ");
        }
    }

    public static PascalString asImplicitPascalString(Object value) {
        if (value instanceof Character) {
            return castCharToString((char) value);
        } else if (value instanceof PascalString) {
            return (PascalString) value;
        } else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new IllegalArgumentException("Illegal type ");
        }
    }

    public static boolean isImplicitPascalString(Object value) {
        return value instanceof Character
             || value instanceof PascalString;
    }

    public static PascalString asImplicitPascalString(Object value, Class<?> typeHint) {
        if (typeHint == char.class) {
            return castCharToString((char) value);
        } else if (typeHint == PascalString.class) {
            return (PascalString) value;
        } else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new IllegalArgumentException("Illegal type ");
        }
    }

    public static PascalString expectImplicitPascalString(Object value, Class<?> typeHint) throws UnexpectedResultException {
        if (typeHint == char.class && value instanceof Character) {
            return castCharToString((char) value);
        } else if (typeHint == PascalString.class && value instanceof PascalString) {
            return (PascalString) value;
        } else {
            throw new UnexpectedResultException(value);
        }
    }

    public static boolean isImplicitPascalString(Object value, Class<?> typeHint) {
        return (typeHint == char.class && value instanceof Character)
             || (typeHint == PascalString.class && value instanceof PascalString);
    }

    public static Class<?> getImplicitPascalStringClass(Object value) {
        if (value instanceof Character) {
            return char.class;
        } else if (value instanceof PascalString) {
            return PascalString.class;
        } else if (value == null) {
            return Object.class;
        } else {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new IllegalArgumentException("Illegal type ");
        }
    }

}
