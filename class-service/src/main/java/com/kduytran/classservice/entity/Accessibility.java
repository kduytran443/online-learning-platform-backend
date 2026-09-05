package com.kduytran.classservice.entity;

/**
 * Defines the accessibility levels for a class or content.
 */
public enum Accessibility {

    /**
     * Fully public, anyone can access without restrictions.
     */
    PUBLIC,

    /**
     * Private content, only visible to the creator or explicitly assigned users.
     */
    PRIVATE,

    /**
     * Accessible only via a direct link, not discoverable through search or listings.
     */
    LINK_ONLY,

    /**
     * Paid or premium access, requires payment or subscription.
     */
    PAID
}
