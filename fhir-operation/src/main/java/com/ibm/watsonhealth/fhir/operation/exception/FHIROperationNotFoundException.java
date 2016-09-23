/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.exception;

public class FHIROperationNotFoundException extends FHIROperationException {
    private static final long serialVersionUID = 1L;

    public FHIROperationNotFoundException(String message) {
        super(message);
    }

    public FHIROperationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
