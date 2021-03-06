package cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive;

import com.oracle.truffle.api.frame.FrameSlotKind;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.ArrayDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.LongConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.constant.StringConstantDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.extension.PCharDesriptor;

/**
 * Type descriptor representing the string type.
 */
public class StringDescriptor extends ArrayDescriptor implements PrimitiveDescriptor {

    private static StringDescriptor instance = new StringDescriptor();

    public static StringDescriptor getInstance() {
        return instance;
    }

    private StringDescriptor() {
        super(new OrdinalDescriptor.RangeDescriptor(new LongConstantDescriptor(0), new LongConstantDescriptor(Integer.MAX_VALUE)), CharDescriptor.getInstance());

    }

    @Override
    public FrameSlotKind getSlotKind() {
        return FrameSlotKind.Object;
    }

    @Override
    public Object getDefaultValue() {
        return new PascalString();
    }

    @Override
    public boolean convertibleTo(TypeDescriptor type) {
        return type instanceof PCharDesriptor || type instanceof StringConstantDescriptor;
    }

}