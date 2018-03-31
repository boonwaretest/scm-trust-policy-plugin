package org.boonware.jenkins.scm.trust;

import edu.umd.cs.findbugs.annotations.NonNull;

import hudson.Extension;

import jenkins.scm.api.SCMHeadOrigin;

import jenkins.scm.api.trait.SCMHeadAuthority;
import jenkins.scm.api.trait.SCMHeadAuthorityDescriptor;

import org.jenkinsci.plugins.github_branch_source.GitHubSCMSourceRequest;
import org.jenkinsci.plugins.github_branch_source.PullRequestSCMHead;
import org.jenkinsci.plugins.github_branch_source.PullRequestSCMRevision;

import org.kohsuke.stapler.DataBoundConstructor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WhitelistTrustPolicy extends SCMHeadAuthority<GitHubSCMSourceRequest, PullRequestSCMHead, PullRequestSCMRevision> {

    private static final Logger LOGGER = Logger.getLogger(WhitelistTrustPolicy.class.getName());

    private WhitelistSource whitelist;

    @DataBoundConstructor
    public WhitelistTrustPolicy(WhitelistSource ) {
    }

    @Override
    protected boolean checkTrusted(@NonNull GitHubSCMSourceRequest request, @NonNull PullRequestSCMHead head) {
        boolean isWhitelisted = whitelist.contains(head.getSourceOwner());
        LOGGER.log(Level.INFO, "Pull request author {0} whitelisted? {1}", new Object[]{head.getSourceOwner(), String.valueOf(isWhitelisted)});
        return !head.getOrigin().equals(SCMHeadOrigin.DEFAULT) && isWhitelisted;
    }

    @Extension
    public static class DescriptorImpl extends SCMHeadAuthorityDescriptor {

        @Override
        public String getDisplayName() {
            return Messages.WhitelistTrustPolicy_displayName();
        }

        @Override
        public boolean isApplicableToOrigin(@NonNull Class<? extends SCMHeadOrigin> originClass) {
            return SCMHeadOrigin.Fork.class.isAssignableFrom(originClass);
        }
    }


}
