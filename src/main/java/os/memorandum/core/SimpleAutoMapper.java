package os.memorandum.core;

import os.memorandum.reflection.ReflectionUtils;
import os.memorandum.converters.SimpleConverter;
import os.memorandum.exceptions.MapperException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleAutoMapper implements AutoMapper  {

    private Converter defaultConverter = new SimpleConverter();

//    private Comparator<String> comparator = (o1, o2) -> o1.equals(o2) ? 0 : -1;

    public void setDefaultConverter(Converter defaultConverter) {
        this.defaultConverter = defaultConverter;
    }
//    public void setComparator(Comparator<String> comparator) {
//        this.comparator = comparator;
//    }

    @Override
    public <T> T map(Object source, Class<T> destinationClass) {
        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(destinationClass, "DestinationClass cannot be null");
        return map(source, instanceOf(destinationClass));
    }

    @Override
    public <T> T map(Object source, T destination) {
        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(source, "Destination cannot be null");
        return transfer(source, destination,
                findComparisons(source.getClass().getDeclaredFields(), destination.getClass().getDeclaredFields()));
    }
    
    
    private List<O2MComparison> findComparisons(Field[] sourceFields, Field[] destinationFields) {
        List<O2MComparison> comparisons = new ArrayList<>();
        Map<String, Field> sourceFieldsMap = toMap(sourceFields);

        for (Field destinationField : destinationFields) {
            MapConverter mapConverter = destinationField.getAnnotation(MapConverter.class);

//            if (mapConverter == null) {
//
//            } todo
            String[] sourceFieldsNamesToFind = mapConverter == null || mapConverter.from().length < 1 ? new String[]{destinationField.getName()} : mapConverter.from() ;
            Converter converter = mapConverter == null ? defaultConverter : instanceOf(mapConverter.converter());

            List<Field> sourceFieldsToFind = Arrays.stream(sourceFieldsNamesToFind).map(sourceFieldsMap::get).filter(Objects::nonNull).toList();

            if (sourceFieldsToFind.isEmpty()) {
                continue;
            }

            comparisons.add(new O2MComparison(sourceFieldsToFind, destinationField, converter));
        }
        return comparisons;
    }

    private <T> T transfer(Object source, T destination, List<O2MComparison> comparisons) {
        for (O2MComparison comparison : comparisons) {
            Object[] sourceValues = comparison.sourceFields.stream()
                    .map(sourceField -> ReflectionUtils.getValue(source, sourceField)).toArray();

            Object destinationValue = comparison.converter.convert(comparison.destinationField.getDeclaringClass(), sourceValues);

            ReflectionUtils.setValue(destination, comparison.destinationField, destinationValue);
        }

        return destination;
    }

    private Map<String, Field> toMap(Field[] fields) {
        return Arrays.stream(fields).collect(Collectors.toMap(Field::getName, f -> f));
    }

    private <T> T instanceOf(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new MapperException("An empty constructor is required.");
        }
    }


    private record O2MComparison(List<Field> sourceFields, Field destinationField, Converter converter) {}
}
