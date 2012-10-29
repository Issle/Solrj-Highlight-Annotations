package org.apache.solr.client.solrj.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotating a bean's field with this
 * annotation will cause it to be used as
 * a Solr primary key.
 * @author ipapaste
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Identifier {

}
