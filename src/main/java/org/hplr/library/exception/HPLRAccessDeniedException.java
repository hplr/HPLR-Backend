package org.hplr.library.exception;

import org.springframework.security.access.AccessDeniedException;

public class HPLRAccessDeniedException extends AccessDeniedException {
    public HPLRAccessDeniedException(String msg) {
        super(msg);
    }
}
