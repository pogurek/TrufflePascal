package cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.parser.FormalParameter;

import java.util.List;

public class FunctionReturnDescriptor extends SubroutineDescriptor {

    private final TypeDescriptor returnTypeDescriptor;

    public FunctionReturnDescriptor(List<FormalParameter> formalParameters, TypeDescriptor returnTypeDescriptor) {
        super(formalParameters);
        this.returnTypeDescriptor = returnTypeDescriptor;
    }

    @Override
    public FrameSlotKind getSlotKind() {
        return returnTypeDescriptor.getSlotKind();
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }
}
