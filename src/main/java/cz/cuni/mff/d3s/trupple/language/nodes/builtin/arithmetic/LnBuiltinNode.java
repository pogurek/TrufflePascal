package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.LogarithmInvalidArgumentException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

@NodeInfo(shortName = "ln")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class LnBuiltinNode extends ExpressionNode {

    @Specialization
    double ln(double value) {
        if (value > 0) {
            return Math.log(value);
        } else {
            throw new LogarithmInvalidArgumentException(value);
        }
    }

    @Override
    public TypeDescriptor getType() {
        return RealDescriptor.getInstance();
    }

}