package os.memorandum.core;

//import java.util.List;
//import java.util.Optional;

@FunctionalInterface
public interface Converter {

    Object convert(Class<?> destinationClass, Object[] args);



//    default Optional<Integer> getMaxCountOfArguments() {
//        return Optional.empty();
//    }
//
//    default Optional<List<Class<?>>> getAllowedTypes() {
//        return Optional.empty();
//    }


}
