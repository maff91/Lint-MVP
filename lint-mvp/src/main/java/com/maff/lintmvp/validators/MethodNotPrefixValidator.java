package com.maff.lintmvp.validators;

import com.intellij.psi.PsiMethod;

import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * Method validator. Valid method should not contain certain prefixes.
 */

public class MethodNotPrefixValidator implements MethodValidator
{
    private String errorMessage;
    private Pattern regex;

    /**
     * Constructor
     * @param errorMessage Message for failed validations.
     * @param prefixes Prefixes method should not contain.
     */
    public MethodNotPrefixValidator(String errorMessage, String... prefixes)
    {
        this.errorMessage = errorMessage;

        // Build regex
        StringJoiner joiner = new StringJoiner("|", "(", ")[A-Z](.+)");

        for (String prefix : prefixes) {
            joiner.add(prefix);
        }

        regex = Pattern.compile(joiner.toString());
    }

    @Override
    public String validate(PsiMethod method)
    {
        String name = method.getName();

        if(regex.matcher(name).matches()) {
            return errorMessage;
        }

        return null;
    }
}
