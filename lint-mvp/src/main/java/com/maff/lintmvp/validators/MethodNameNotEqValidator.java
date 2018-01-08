package com.maff.lintmvp.validators;

import com.intellij.psi.PsiMethod;

/**
 * Method validator. Valid method name should not be equal to certain names.
 *
 *
 */

public class MethodNameNotEqValidator implements MethodValidator
{
    private String errorMessage;
    private String[] prohibitedNames;

    /**
     * Constructor
     * @param errorMessage Message for failed validations.
     * @param names Name the method name should not be equal to.
     */
    public MethodNameNotEqValidator(String errorMessage, String... names)
    {
        this.errorMessage = errorMessage;
        this.prohibitedNames = names;
    }

    @Override
    public String validate(PsiMethod method)
    {
        String methodName = method.getName();

        for (String prohibited : prohibitedNames)
        {
            if(methodName.equals(prohibited)) {
                return errorMessage;
            }
        }

        return null;
    }
}
