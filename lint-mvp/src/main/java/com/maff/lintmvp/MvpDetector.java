package com.maff.lintmvp;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.maff.lintmvp.validators.MethodNameNotEqValidator;
import com.maff.lintmvp.validators.MethodNotPrefixValidator;
import com.maff.lintmvp.validators.MethodValidator;

import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;

import java.util.Collections;
import java.util.List;

/**
 * Lint detector that checks View and Presenter interfaces for MVP concept violations via method
 * name validation. Errors in contracts method naming usually shows lack of MVP concepts understanding.
 *
 * (c) Michael Shtutman, 2018, MIT Licence
 */
public class MvpDetector extends Detector implements Detector.UastScanner
{
    // Validators

    private static final MethodValidator VIEW_EVENTS_VALIDATOR = new MethodNotPrefixValidator(
        "View should implement strict commands, not reacting for events. Avoid onXXX methods. Try use showXXX notation.",
        "on");

    private static final MethodValidator PRESENTER_BUSINESS_LOGIC_VALIDATOR = new MethodNotPrefixValidator(
            "Presenter should not leak business logic. Getters are not recommended.",
            "get", "is", "should", "was");

    private static final MethodValidator PRESENTER_LIFECYCLE_VALIDATOR = new MethodNameNotEqValidator(
            "Presenter should not implement activity lifecycle methods. Consider only base onAttach and onDetach methods.",
            "onStart", "onStop", "onResume", "onPause", "onCreate", "onViewCreated", "onCreateView", "onDestroy");



    private final static Implementation JAVA_IMPLEMENTATION = new Implementation(
            MvpDetector.class,
            Scope.JAVA_FILE_SCOPE
    );

    public static final Issue MVP_ISSUE = Issue.create(
            "mvp.violation",
            "Possible MVP design pattern violation.",
            "Possible MVP design pattern violation.",
            Category.USABILITY,
            8,
            Severity.WARNING,
            JAVA_IMPLEMENTATION
    );

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes()
    {
        return Collections.<Class<? extends UElement>>singletonList(UClass.class);
    }

    @Override
    public UElementHandler createUastHandler(JavaContext context)
    {
        return new MvpContractVisitor(context);
    }

    /**
     * Main detector logic. Visits Presenters and Views and validates them.
     */
    private static class MvpContractVisitor extends UElementHandler
    {
        private JavaContext javaContext;

        private MvpContractVisitor(JavaContext javaContext)
        {
            this.javaContext = javaContext;
        }

        @Override
        public void visitClass(UClass clazz)
        {
            switch (getClassType(clazz))
            {
                case view:
                    validateClass(clazz, VIEW_EVENTS_VALIDATOR);
                    break;

                case presenter:
                    validateClass(clazz, PRESENTER_LIFECYCLE_VALIDATOR);
                    validateClass(clazz, PRESENTER_BUSINESS_LOGIC_VALIDATOR);
                    break;

                case other:
                    // Ignore
            }
        }

        /**
         * Checks if a class is an MVP Presenter or View: interface with name ending with such words
         * or a class implementing such interface, e.g. {@code class DetailsPres implements MyBasePresenter}
         *
         * @param clazz Checked class.
         * @return Class type.
         */
        private ClassType getClassType(UClass clazz)
        {
            if(clazz.isInterface())
            {
                return ClassType.getByName(clazz.getName());
            }
            else
            {
                for (PsiClass psiClass : clazz.getInterfaces())
                {
                    ClassType superType = ClassType.getByName(psiClass.getName());

                    if(superType != ClassType.other) {
                        return superType;
                    }
                }
            }

            return ClassType.other;
        }

        /**
         * Validates class methods with desired validator.
         * @param clazz Target class.
         * @param validator Validator the class methods will be checked with.
         */
        private void validateClass(PsiClass clazz, MethodValidator validator)
        {
            for (PsiMethod method : clazz.getAllMethods())
            {
                String problem = validator.validate(method);

                if(problem != null) {
                    javaContext.report(MVP_ISSUE, method, javaContext.getLocation(method), problem);
                }
            }
        }
    }

    /**
     * Support class for detecting MVP Presenters and Views.
     */
    private enum ClassType
    {
        view, presenter, other;

        public static ClassType getByName(String name)
        {
            if(name == null) {
                return ClassType.other;
            }

            if(name.endsWith("Presenter"))
            {
                return ClassType.presenter;
            }
            else if(name.endsWith("View"))
            {
                return ClassType.view;
            }

            return ClassType.other;
        }
    }
}