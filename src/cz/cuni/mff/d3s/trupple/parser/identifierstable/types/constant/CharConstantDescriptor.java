package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.parser.exceptions.CantBeNegatedException;
import cz.cuni.mff.d3s.trupple.parser.exceptions.LexicalException;

public class CharConstantDescriptor implements OrdinalConstantDescriptor {

    private final char value;

    public CharConstantDescriptor(char value) {
        this.value = value;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Byte;
    }

    @Override
    public Object getDefaultValue() {
        return this.value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public boolean isSigned() {
        return false;
    }

    @Override
    public ConstantDescriptor negatedCopy() throws LexicalException {
        throw new CantBeNegatedException();
    }

    @Override
    public int getOrdinalValue() {
        return (int)this.value;
    }

}