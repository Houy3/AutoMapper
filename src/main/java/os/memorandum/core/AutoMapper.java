package os.memorandum.core;

public interface AutoMapper {

    <T> T map(Object source, Class<T> destination);
    <T> T map(Object source, T destination);
    
}
