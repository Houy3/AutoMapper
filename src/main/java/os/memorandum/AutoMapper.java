package os.memorandum;

import os.memorandum.reflection.ReflectionUtils;

import java.util.List;
import java.util.Objects;

import static os.memorandum.reflection.ReflectionUtils.instanceOf;

public class AutoMapper {

    private final Configuration config;
    private final AutoMapperEngine engine;

    public Configuration getConfiguration() {
        return config;
    }


    public AutoMapper() {
        this.config = new Configuration();
        this.engine = new AutoMapperEngine(config);
    }



    public <T> T map(Object source, Class<T> destinationClass) {
        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(destinationClass, "DestinationClass cannot be null");
        return map(source, instanceOf(destinationClass));
    }

    public <T> T map(Object source, T destination) {
        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(source, "Destination cannot be null");
        return transfer(source, destination,
                config.getComparisonMatcher().match(source.getClass(), destination.getClass()));
    }
    


    private <T> T transfer(Object source, T destination, List<Comparison> comparisons) {
        for (Comparison comparison : comparisons) {
            Object[] sourceValues = comparison.sourceFields().stream()
                    .map(sourceField -> ReflectionUtils.getValue(source, sourceField)).toArray();

            //todo
            Converter converter = instanceOf(comparison.converter());
            Object destinationValue = converter.convert(comparison.destinationField().getDeclaringClass(), sourceValues);

            ReflectionUtils.setValue(destination, comparison.destinationField(), destinationValue);
        }

        return destination;
    }

}
