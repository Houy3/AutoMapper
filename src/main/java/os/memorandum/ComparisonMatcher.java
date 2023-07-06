package os.memorandum;

import java.util.List;

public interface ComparisonMatcher {

    List<Comparison> match(Class<?> sourceClass, Class<?> destinationClass);
}
