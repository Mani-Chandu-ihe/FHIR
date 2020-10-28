/*
 * (C) Copyright IBM Corp. 2018, 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.search.test;

import java.util.Properties;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.cache.CommonTokenValuesCacheImpl;
import com.ibm.fhir.persistence.jdbc.cache.FHIRPersistenceJDBCCacheImpl;
import com.ibm.fhir.persistence.jdbc.cache.NameIdCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.fhir.persistence.search.test.AbstractSearchStringTest;


public class JDBCSearchStringTest extends AbstractSearchStringTest {

    private Properties testProps;

    private PoolConnectionProvider connectionPool;
    
    private FHIRPersistenceJDBCCache cache;

    public JDBCSearchStringTest() throws Exception {
        this.testProps = TestUtil.readTestProperties("test.jdbc.properties");
    }

    @Override
    public void bootstrapDatabase() throws Exception {
        DerbyInitializer derbyInit;
        String dbDriverName = this.testProps.getProperty("dbDriverName");
        if (dbDriverName != null && dbDriverName.contains("derby")) {
            derbyInit = new DerbyInitializer(this.testProps);
            IConnectionProvider cp = derbyInit.getConnectionProvider(false);
            this.connectionPool = new PoolConnectionProvider(cp, 1);
            ICommonTokenValuesCache rrc = new CommonTokenValuesCacheImpl(100, 100);
            cache = new FHIRPersistenceJDBCCacheImpl(new NameIdCache<Integer>(), new NameIdCache<Integer>(), rrc);
        }
    }

    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
        if (this.connectionPool == null) {
            throw new IllegalStateException("Database not bootstrapped");
        }
        return new FHIRPersistenceJDBCImpl(this.testProps, this.connectionPool, cache);
    }

    @Override
    protected void shutdownPools() throws Exception {
        // Mark the pool as no longer in use. This allows the pool to check for
        // lingering open connections/transactions.
        if (this.connectionPool != null) {
            this.connectionPool.close();
        }
    }


    /*
     * Currently, documented in our conformance statement. We do not support
     * modifiers on chained parameters.
     * https://ibm.github.io/FHIR/Conformance#search-modifiers
     * Refer to https://github.com/IBM/FHIR/issues/473 to track the issue.
     */
    @Override
    @Test(expectedExceptions = FHIRPersistenceNotSupportedException.class)
    public void testSearchString_string_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.string:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.string:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-string:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-string:missing", "false");
    }
}
