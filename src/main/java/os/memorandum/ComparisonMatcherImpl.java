package os.memorandum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static os.memorandum.Configuration.NULL;

public class ComparisonMatcherImpl implements ComparisonMatcher {

    private final Configuration configuration;

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public ComparisonMatcherImpl(Configuration configuration) {
        this.configuration = configuration;
    }


    public List<Comparison> match(Class<?> sourceClass, Class<?> destinationClass) {
        Field[] sourceFields = sourceClass.getDeclaredFields();

        return Arrays.stream(destinationClass.getDeclaredFields())
                .map(destinationField -> new RowComparison(destinationField)
                        .toComparison(getMatcher(sourceClass, destinationClass, sourceFields)))
                .filter(Objects::nonNull).toList();
    }


    protected Function<RowComparison, Comparison> getMatcher(Class<?> sourceClass, Class<?> destinationClass, Field[] sourceFields) {
        return (rowComparison) -> {
            Comparison comparison = new Comparison(
                    getFieldMatcher(sourceClass, sourceFields).apply(rowComparison.sourceFieldsCodes),
                    rowComparison.destinationField,
                    rowComparison.converter);
            if (comparison.sourceFields().isEmpty()) {
                log.debug("For " + destinationClass.getName() + "." + rowComparison.destinationField + " comparison not found.");
                return null;
            }
            return comparison;
        };
    }

    protected Function<String[], List<Field>> getFieldMatcher(Class<?> sourceClass, Field[] sourceFields) {
        return (sourceFieldsCodes) -> Arrays.stream(sourceFieldsCodes).map(fieldCode -> {
            Optional<Field> field = findField(fieldCode, sourceFields);
            if (field.isEmpty()) {
                log.debug("In class " + sourceClass.getName() + " field with code " + fieldCode + " not found.");
                return null;
            }
            return field.get();
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected Optional<Field> findField(String fieldCode, Field[] fields) {
        return Arrays.stream(fields).filter((field) -> isTheRightField(fieldCode,field)).findFirst();
    }

    protected boolean isTheRightField(String fieldCode, Field field) {
        return fieldCode.equals(field.getName());
    }



    protected class RowComparison {

        private String[] sourceFieldsCodes;

        private boolean isFieldName = false;

        private Field destinationField;

        private Class<? extends Converter> converter;

        protected RowComparison(Field destinationField) {
            this(destinationField.getAnnotation(MapConverter.class));
            this.destinationField = destinationField;
            if (ArrayUtils.isEmpty(sourceFieldsCodes)) {
                sourceFieldsCodes = new String[]{destinationField.getName()};
                isFieldName = true;
            }
            if (converter == null || converter.equals(NULL)) {
                converter = configuration.getDefaultConverterClass();
            }
        }

        protected RowComparison(MapConverter mapConverter) {
            if (mapConverter != null) {
                sourceFieldsCodes = mapConverter.from();
                converter = mapConverter.converter();
            }
        }

        private Comparison toComparison(Function<RowComparison, Comparison> matcher) {
            return matcher.apply(this);
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RowComparison that = (RowComparison) o;
            return Arrays.equals(sourceFieldsCodes, that.sourceFieldsCodes) && Objects.equals(destinationField, that.destinationField);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(destinationField);
            result = 31 * result + Arrays.hashCode(sourceFieldsCodes);
            return result;
        }

        @Override
        public String toString() {
            return "RowComparison{" +
                    "sourceFieldsCodes=" + Arrays.toString(sourceFieldsCodes) +
                    ", isFieldName=" + isFieldName +
                    ", destinationField=" + destinationField +
                    ", converter=" + converter +
                    '}';
        }
    }
}
