package com.infobelt.aadhaar.service;

/**
 * Support for a security advisor in the service to decide if you have the rights as the current
 * user to perform this action
 */
public interface SecurityAdvisor {

    /**
     * Check if we are permitted to perform the given action on the resource
     *
     * @param resource
     * @param action
     * @return True, if permitted
     */
    boolean isPermitted(String resource, String action);

    /**
     * Check if we are permitted to perform the given action on the resource with id
     *
     * @param resource
     * @param action
     * @param id
     * @return True, if permitted
     */
    boolean isPermitted(String resource, String action, Long id);

}
