package com.yolt.providers.common.exception;

/**
 * Thrown when polling at yodlee and the mfa form isn't usable anymore.
 * Result will be UserSiteStatusCode.MFA_TIMED_OUT;
 */
public class MfaTimedOutException extends HandledProviderCheckedException {
}
