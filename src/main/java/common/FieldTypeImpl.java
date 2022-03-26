package common;

import framework.FieldType;

public class FieldTypeImpl implements FieldType {

    private final String type;

    public FieldTypeImpl(String type) {
        this.type = type;
    }

    @Override
    public String getFieldType() {
        return type;
    }
}
