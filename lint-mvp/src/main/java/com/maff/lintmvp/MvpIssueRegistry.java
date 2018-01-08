package com.maff.lintmvp;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Issue registry
 */
public class MvpIssueRegistry extends IssueRegistry
{
    @Override
    public List<Issue> getIssues()
    {
        return Arrays.asList(MvpDetector.MVP_ISSUE);
    }
}
