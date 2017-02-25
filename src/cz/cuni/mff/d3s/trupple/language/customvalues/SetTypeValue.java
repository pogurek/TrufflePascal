package cz.cuni.mff.d3s.trupple.language.customvalues;

import java.util.HashSet;
import java.util.Set;

public class SetTypeValue implements ICustomValue {

    private final Set<Object> data;

    public SetTypeValue() {
        this(new HashSet<>());
    }

    public SetTypeValue(Set<Object> data) {
        this.data = data;
    }

    @Override
    public Object getValue() {
        return this;
    }

    public Set<Object> getData() {
        return this.data;
    }

    public SetTypeValue createDeepCopy() {
        Set<Object> dataCopy = new HashSet<>();
        for (Object item : data) {
            dataCopy.add(item);
        }

        return new SetTypeValue(dataCopy);
    }

    public boolean equals(SetTypeValue comp) {
        Set<Object> compData = comp.getData();
        return this.data.containsAll(compData) && compData.containsAll(this.data);
    }

    public int getSize() {
        return this.data.size();
    }

    public boolean contains(Object o) {
        return this.data.contains(o);
    }
}
