package os.memorandum.converters;

import os.memorandum.Converter;

import java.util.Arrays;

public class ConcatConverter implements Converter {

    @Override
    public Object convert(Class<?> destinationClass, Object[] args) {
        StringBuilder concat = new StringBuilder();
        Arrays.stream(args).forEach(concat::append);
        return concat.toString();
    }
}
