package pascal.language.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class NopNode extends StatementNode{

	public void executeVoid(VirtualFrame frame){}
}