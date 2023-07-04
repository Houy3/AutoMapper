package os.memorandum.core;

import os.memorandum.converters.SimpleConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapConverter {

    String[] from() default {};

    Class<? extends Converter> converter() default SimpleConverter.class;
}
