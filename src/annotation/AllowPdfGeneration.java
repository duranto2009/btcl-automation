package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import util.BillTemplateLocator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AllowPdfGeneration {
	Class<? extends BillTemplateLocator> billLocator();
}
