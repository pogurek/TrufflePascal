package cz.cuni.mff.d3s.trupple.language.nodes.literals;

import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.complex.OrdinalDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.EnumLiteralDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.SetDescriptor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Node representing set constructor.
 */
public class SetConstructorNode extends ExpressionNode {

    @Children private final ExpressionNode[] valueNodes;

    public SetConstructorNode(List<ExpressionNode> valueNodes) {
        this.valueNodes = valueNodes.toArray(new ExpressionNode[valueNodes.size()]);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Set<Object> data = new HashSet<>();

        for (ExpressionNode valueNode : this.valueNodes) {
            Object actualValue = valueNode.executeGeneric(frame);
            data.add(actualValue);
        }

        return new SetTypeValue(data);
    }

    @Override
    public TypeDescriptor getType() {
        TypeDescriptor innerType = this.valueNodes[0].getType();
        if (innerType instanceof EnumLiteralDescriptor) {
            return new SetDescriptor(((EnumLiteralDescriptor) innerType).getEnumType());
        } else {
            return new SetDescriptor((OrdinalDescriptor) innerType);
        }
    }
}
