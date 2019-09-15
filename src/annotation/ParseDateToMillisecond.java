package annotation;
import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParseDateToMillisecond{
	String dateFormat() default "dd/MM/yyyy";
}
