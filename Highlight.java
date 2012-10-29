package org.apache.solr.client.solrj.beans;

import java.lang.annotation.*;


/**
 * Annotate a field with this annotation to produce
 * highlight results in the given column.
 * @author ipapaste
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Highlight {

}
