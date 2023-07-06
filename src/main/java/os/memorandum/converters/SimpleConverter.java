package os.memorandum.converters;

import os.memorandum.Converter;

public class SimpleConverter implements Converter {


    @Override
    public Object convert(Class<?> destinationClass, Object[] args) {
        return args[0];
    }
}
