package org.jetbrains.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Minimal NotNull annotation used for compile-time compatibility when
 * external JetBrains annotations are not available in the module classpath.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({
        ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.FIELD,
        ElementType.LOCAL_VARIABLE,
        ElementType.TYPE_USE
})
public @interface NotNull {
}

