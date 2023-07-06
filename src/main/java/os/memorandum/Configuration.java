package os.memorandum;

import os.memorandum.converters.SimpleConverter;


public class Configuration {

    public final static Class<Converter> NULL = Converter.class;

    public final static Class<? extends Converter> DEFAULT_CONVERTER_CLASS = SimpleConverter.class;

    public final ComparisonMatcher DEFAULT_COMPARISON_MATCHER_CLASS = new ComparisonMatcherImpl(this);


    private Class<? extends Converter> defaultConverterClass = DEFAULT_CONVERTER_CLASS;

    private ComparisonMatcher comparisonMatcher = DEFAULT_COMPARISON_MATCHER_CLASS;


    public Class<? extends Converter> getDefaultConverterClass() {
        return defaultConverterClass;
    }

    public void setDefaultConverterClass(Class<? extends Converter> defaultConverterClass) {
        this.defaultConverterClass = defaultConverterClass;
    }

    public ComparisonMatcher getComparisonMatcher() {
        return comparisonMatcher;
    }

    public void setComparisonMatcher(ComparisonMatcher comparisonMatcher) {
        this.comparisonMatcher = comparisonMatcher;
    }
}
