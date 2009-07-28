package zz.utils.ui.propertyeditors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be associated to a property so as to indicate
 * which editor to use to edit its value.
 * The specified class must have a constructor that takes the property
 * as its only argument.
 * @author gpothier
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PropertyEditor {
	Class<? extends SimplePropertyEditor<?>> value();
}
