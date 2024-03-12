package com.example.demo.controller.intercetor;

import com.example.demo.controller.util.RequestKeys;
import com.example.demo.exception.FormatException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class InterceptorHelper {

    public static long parseIdAndThrow(String serialnoStr) throws FormatException {
        try {
            return Long.parseLong(serialnoStr);
        } catch (NumberFormatException e) {
            throw new FormatException("failed to parse "+serialnoStr+" to serialno.");
        }
    }

    public static String extractVariableByUrl(HttpServletRequest request, String key) {
        Object uriTemplateVariablesObj = request.getAttribute("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables");
        Map<String, String> uriTemplateVariablesMap = (Map<String, String>) uriTemplateVariablesObj;
        return uriTemplateVariablesMap.get(key);
    }
}
