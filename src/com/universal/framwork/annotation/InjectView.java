package com.universal.framwork.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ��������View��ViewId����findViewById����
 * com.universal.framwork.inject.InjectView
 * @author yuanzeyao <br/>
 * create at 2014��5��23�� ����11:04:19
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView
{
   public int id() default -1;
   public String click() default "";
}
