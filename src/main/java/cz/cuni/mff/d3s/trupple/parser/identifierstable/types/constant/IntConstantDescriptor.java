package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.IntDescriptor;

/**
 * Type descriptor for a integer-type constant. It also contains the constant's value.
 */
public class IntConstantDescriptor implements OrdinalConstantDescriptor {

    private final int value;

    /**
     * The default descriptor containing value of the constant.
     */
    public IntConstantDescriptor(int value) {
        this.value = value;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Int;
    }

    @Override
    public Object getDefaultValue() {
        return value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public boolean isSigned() {
        return true;
    }

    @Override
    public ConstantDescriptor negatedCopy() throws LexicalException {
        return new IntConstantDescriptor(-this.value);
    }

    @Override
    public int getOrdinalValue() {
        return this.value;
    }

    @Override
    public TypeDescriptor getInnerType() {
        return this.getType();
    }

    @Override
    public TypeDescriptor getType() {
        return IntDescriptor.getInstance();
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return type instanceof IntDescriptor;
    }

}
