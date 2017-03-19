package cz.cuni.mff.d3s.trupple.language.customvalues;

import cz.cuni.mff.d3s.trupple.language.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.language.runtime.heap.HeapSlot;
import cz.cuni.mff.d3s.trupple.language.runtime.heap.PascalHeap;

public class PointerValue implements ICustomValue {

    private HeapSlot heapSlot;

    private final TypeDescriptor innerType;

    public PointerValue(TypeDescriptor innerType) {
        this.innerType = innerType;
        this.heapSlot = PascalHeap.NIL;
    }

    @Override
    public Object getValue() {
        return this;
    }

    @Override
    public boolean equals(Object pointerValue) {
        PointerValue comprareTo = (PointerValue) pointerValue;
        return comprareTo.getHeapSlot().equals(this.getHeapSlot());
    }

    public Object getDereferenceValue() {
        return PascalHeap.getInstance().getValueAt(this.heapSlot);
    }

    public HeapSlot getHeapSlot() {
        return heapSlot;
    }

    public TypeDescriptor getType() {
        return innerType;
    }

    public void setHeapSlot(HeapSlot heapSlot) {
        this.heapSlot = heapSlot;
    }

    public void setDereferenceValue(Object value) {
        PascalHeap.getInstance().setValueAt(this.heapSlot, value);
    }
}