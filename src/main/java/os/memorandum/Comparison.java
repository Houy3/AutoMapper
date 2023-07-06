package os.memorandum;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;


public record Comparison(List<Field> sourceFields, Field destinationField, Class<? extends Converter> converter) {
    public Comparison {
        Assert.notNull(sourceFields, "sourceFields");
        Assert.notNull(destinationField, "destinationField");
        Assert.notNull(converter, "converter");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comparison that = (Comparison) o;
        return Objects.equals(sourceFields, that.sourceFields) && Objects.equals(destinationField, that.destinationField);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(destinationField);
        result = 31 * result + Objects.hash(sourceFields);
        return result;
    }

    @Override
    public String toString() {
        return "Comparison{" +
                "sourceFields=" + sourceFields +
                ", destinationField=" + destinationField +
                ", converter=" + converter +
                '}';
    }
}

