package com.um.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextUtil {
	
	private static final String VAR_PATTERN = "\\{([^}]+)\\}";
	
	public static String substituteVariables(String input, Map<String, String> variables) {
	        // Pattern to match variables in the format ${variableName}
	        Pattern pattern = Pattern.compile(VAR_PATTERN);
	        Matcher matcher = pattern.matcher(input);

	        StringBuffer result = new StringBuffer();

	        while (matcher.find()) {
	            String variableName = matcher.group(1); // Extract the variable name
	            String replacement = variables.get(variableName); // Get the value from the map
	            if (replacement == null) {
	                replacement = ""; // If the variable is not found, replace with an empty string or handle as needed
	            }
	            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
	        }
	        matcher.appendTail(result);

	        return result.toString();
	}

}
