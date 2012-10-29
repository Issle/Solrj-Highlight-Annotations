package org.apache.solr.client.solrj.beans;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

public class ResponseWrapper {

	public static <T> QueryResponse getBeans(Class<T> type, SolrQuery query, SolrServer server) {
		
		List<T> beans = null;
		try {
			java.lang.reflect.Field[] fields = type.getDeclaredFields();

			String result = buildHighlightQueryField(fields);

			if (result.length() > 0) {
				query.setParam("hl", true);
				query.setParam("hl.fl", result);
			}

			return server.query(query);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private static String buildHighlightQueryField(
			java.lang.reflect.Field[] fields) {
		StringBuilder queryParam = new StringBuilder();

		for (java.lang.reflect.Field field : fields) {
			if (field.getAnnotation(Highlight.class) == null)
				continue;

			Field annotation = field.getAnnotation(Field.class);

			if (annotation == null)
				continue;

			if (queryParam.length() == 0)
				queryParam.append(field.getAnnotation(Field.class).value());
			else {
				queryParam.append(",");
				queryParam.append(field.getAnnotation(Field.class).value());
			}
		}

		return queryParam.toString();
	}

	public static <T> void injectBeans(List<T> beans, Class<T> type,
			QueryResponse response) {
		try {
			java.lang.reflect.Field idField = null;
			java.lang.reflect.Field[] fields = type.getDeclaredFields();

			for (java.lang.reflect.Field candidateField : type
					.getDeclaredFields()) {
				if (candidateField.getAnnotation(Identifier.class) != null) {
					idField = candidateField;
					break;
				}
			}

			for (T bean : beans) {
				for (java.lang.reflect.Field field : type.getDeclaredFields()) {
					Field fieldAnnotation = field.getAnnotation(Field.class);

					Highlight highlightAnnotation = field
							.getAnnotation(Highlight.class);

					if (highlightAnnotation == null)
						continue;

					idField.setAccessible(true);
					String id = (String) idField.get(bean); 
					idField.setAccessible(false);
					if (response.getHighlighting().get(id) != null) {
						List<String> highlightSnippets = response.getHighlighting().get(id).get(fieldAnnotation.value());
						if(highlightSnippets == null)
							continue;
						field.setAccessible(true);
						String snippet = highlightSnippets.get(0);
						field.set(bean, snippet);
						field.setAccessible(false);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
