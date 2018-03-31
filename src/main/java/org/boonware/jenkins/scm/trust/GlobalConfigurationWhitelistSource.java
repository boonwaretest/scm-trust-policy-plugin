package org.boonware.jenkins.scm.trust;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

/**
 * A configuration entry in the central Jenkins configuration page for defining a global trusted
 * pull request author whitelist.
 */
@Extension
public class GlobalConfigurationWhitelistSource extends GlobalConfiguration implements WhitelistSource {

    public static GlobalConfigurationWhitelistSource get() {
        return GlobalConfiguration.all().get(GlobalConfigurationWhitelistSource.class);
    }

    private String pullRequestWhitelist;

    private Set<String> userIds;

    public GlobalConfigurationWhitelistSource() {
        load();
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) {
        req.bindJSON(this, json);
        userIds = extractUserIds(getPullRequestWhitelist());
        return true;
    }

    public synchronized String getPullRequestWhitelist() {
        return pullRequestWhitelist;
    }

    public synchronized void setPullRequestWhitelist(@CheckForNull String whitelist) {
        this.pullRequestWhitelist = whitelist;
        save();
    }

    @Override
    public synchronized Set<String> getUserIds() {
        return extractUserIds(pullRequestWhitelist);
    }

    @Override
    public synchronized boolean contains(String userId) {
        // GitHub user IDs are case-insensitive
        return getUserIds().contains(userId.toLowerCase().trim());
    }

    private Set<String> extractUserIds(String whitelist) {
        Set<String> userIds = new HashSet<>(whitelist == null ? Collections.<>emptySet() : Arrays.asList(whitelist.split("\\s+")));
        Iterator<String> iterator = userIds.iterator();
        Set<String> sanitizedSet = new HashSet<>();
        while (iterator.hasNext()) {
            String sanitizedString = iterator.next().toLowerCase().trim();
            if (!sanitizedString.isEmpty()) {
                sanitizedSet.add(sanitizedString);
            }
        }
        return sanitizedSet;
    }

}
