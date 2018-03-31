package org.boonware.jenkins.scm.trust;

import java.util.Set;

/**
 * Whitelists may be used for checking user permissions outside of the context of SCM user
 * permissions. Different whitelist sources may be defined, for example, a list of user IDs
 * defined in a text field in the Jenkins UI.
 */
interface WhitelistSource {

    /**
     * @return The list of user IDs from the whitelist.
     */
    public Set<String> getUserIds();

    /**
     * @return True if and only if the user ID is on the whitelist.
     */
    public boolean contains(String userId);

}
