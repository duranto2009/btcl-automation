package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import util.ConnectionType;
import util.TransactionType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional {
	ConnectionType connectionType() default ConnectionType.OLD_CONNECTION;
	TransactionType transactionType() default TransactionType.PART_OF_PREVIOUS_TRANSACTION;
}
