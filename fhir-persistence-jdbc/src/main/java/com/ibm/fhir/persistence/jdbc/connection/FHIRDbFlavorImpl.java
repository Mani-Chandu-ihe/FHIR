/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import com.ibm.fhir.database.utils.api.DatabaseType;

/**
 * Describes the capability of the underlying database and the schema it has
 * been configured with.
 */
public class FHIRDbFlavorImpl implements FHIRDbFlavor {
    
    // does the database schema support multi-tenancy
    private final boolean multitenant;

    // basic type of the database (DB2, Derby etc)
    private final DatabaseType type;
    
    public FHIRDbFlavorImpl(DatabaseType type, boolean multitenant) {
        this.type = type;
        this.multitenant = multitenant;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor#isMultitenant()
     */
    @Override
    public boolean isMultitenant() {
        return this.multitenant;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor#getType()
     */
    @Override
    public DatabaseType getType() {
        return this.type;
    }

}
