package me.alexisevelyn.annotationprocessor.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PARAMETER})
public @interface TranslatableText {
	String test();
}
