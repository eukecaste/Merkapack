package com.merkapack.erp.core.model;


public class MkpkCoreException extends RuntimeException {

	private static final long serialVersionUID = -3730190195705367708L;

    public MkpkCoreException() {
        super();
    }
    public MkpkCoreException(String message) {
        super(message);
    }
    public MkpkCoreException(Throwable cause) {
        super(cause);
    }
    public MkpkCoreException(String message, Throwable cause) {
        super(message, cause);
    }
  
}