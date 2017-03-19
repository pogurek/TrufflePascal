package cz.cuni.mff.d3s.trupple.language.nodes.variables.accessroute;

import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import cz.cuni.mff.d3s.trupple.exceptions.runtime.PascalRuntimeException;
import cz.cuni.mff.d3s.trupple.language.customvalues.PascalArray;
import cz.cuni.mff.d3s.trupple.language.customvalues.Reference;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNode;

import java.util.List;

public class ArrayAccessNode extends AccessNode {

    @Children private final ExpressionNode[] indexExpressions;
    private Object[] indexes;

    public ArrayAccessNode(AccessNode applyToNode, List<ExpressionNode> indexExpressions) {
        super(applyToNode);
        this.indexExpressions = indexExpressions.toArray(new ExpressionNode[indexExpressions.size()]);
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        indexes = new Object[this.indexExpressions.length];

        for (int i = 0; i < this.indexExpressions.length; ++i) {
            indexes[i] = this.indexExpressions[i].executeGeneric(frame);
        }

        applyToNode.executeVoid(frame);
    }

    @Override
    protected void assignValue(VirtualFrame frame, AssignmentNode.SlotAssignment slotAssignment, Object value) throws FrameSlotTypeException {
        PascalArray array = (PascalArray) this.applyToNode.getValue(frame);
        array.setValueAt(this.indexes, value);
    }

    @Override
    protected void assignReference(Reference reference, AssignmentNode.SlotAssignment slotAssignment, Object value) {
        throw new PascalRuntimeException("Array element cannot be a reference.");
    }

    @Override
    protected Object applyTo(VirtualFrame frame, Object value) {
        if (value instanceof PascalArray) {
            PascalArray array = (PascalArray) value;
            return array.getValueAt(this.indexes);
        } else {
            throw new PascalRuntimeException("Cannot index a variable of this type.");
        }
    }

}