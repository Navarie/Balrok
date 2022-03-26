package stub;

import framework.FieldType;

public class FieldTypeStub implements FieldType {

    private String type;

    public FieldTypeStub(String type) {
        this.type = type;
    }

    @Override
    public String getFieldType() {
        return type;
    }
}
