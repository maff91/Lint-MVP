package com.maff.lintmvp.validators;

import com.intellij.psi.PsiMethod;

/**
 * Interface for method validation classes.
 */
public interface MethodValidator
{
    /**
     * Validates a method.
     * @param name A method to validate.
     * @return {@code null} if the method is valid or error message otherwise.
     */
    String validate(PsiMethod name);
}
