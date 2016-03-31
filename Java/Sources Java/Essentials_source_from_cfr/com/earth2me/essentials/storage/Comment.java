/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.storage;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.FIELD})
@Documented
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Comment {
    public String[] value() default {""};
}

