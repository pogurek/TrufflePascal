// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.FileValue;

@GeneratedBy(ResetBuiltinNode.class)
public final class ResetBuiltinNodeGen extends ResetBuiltinNode {

    @Child private ExpressionNode file_;
    @CompilationFinal private boolean seenUnsupported0;

    private ResetBuiltinNodeGen(ExpressionNode file) {
        this.file_ = file;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        FileValue fileValue_;
        try {
            fileValue_ = PascalTypesGen.expectFileValue(file_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            throw unsupported(ex.getResult());
        }
        this.reset(fileValue_);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object fileValue) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {file_}, fileValue);
    }

    public static ResetBuiltinNode create(ExpressionNode file) {
        return new ResetBuiltinNodeGen(file);
    }

}