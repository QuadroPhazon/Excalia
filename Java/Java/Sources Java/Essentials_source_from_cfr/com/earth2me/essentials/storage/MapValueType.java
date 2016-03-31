/*
 * Decompiled with CFR 0_110.
 */
package com.earth2me.essentials.storage;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface MapValueType {
    public Class value() default String.class;
}

