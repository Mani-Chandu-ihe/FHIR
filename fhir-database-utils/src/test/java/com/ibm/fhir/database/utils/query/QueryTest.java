/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import static com.ibm.fhir.database.utils.query.Select.alias;
import static com.ibm.fhir.database.utils.query.Select.select;
import static com.ibm.fhir.database.utils.query.SqlConstants.IS_NOT_NULL;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Query Tests
 */
public class QueryTest {
    // Some constants we use when testing the query build
    public static final String FOO_TAB = "FOO_TAB";
    public static final String FOO_ID = "FOO_ID";
    public static final String FOO_NAME = "FOO_NAME";
    public static final String FOO_AGE = "FOO_AGE";
    public static final String FOO_TOWN = "FOO_TOWN";

    @Test
    public void plainQuery() {
        Select query = Select.select(FOO_ID, FOO_NAME, FOO_AGE)
                .from(FOO_TAB)
                .where(FOO_AGE + " " + IS_NOT_NULL)
                .build();

        // What our statement should look like
        final String SQL = "SELECT FOO_ID, FOO_NAME, FOO_AGE FROM FOO_TAB WHERE FOO_AGE IS NOT NULL";
        assertEquals(query.toString(), SQL);
    }

    @Test
    public void groupByQuery() {
        // Find the age of the oldest person by towns with a population of 10 or more
        Select query = Select.select(FOO_TOWN, "MAX(" + FOO_AGE + ")")
            .from(FOO_TAB)
            .where(FOO_AGE + " " + IS_NOT_NULL)
            .groupBy(FOO_TOWN)
            .having("COUNT(*) > 10")
            .build();
        
        // Here's what our SQL should be
        final String SQL = "SELECT FOO_TOWN, MAX(FOO_AGE) FROM FOO_TAB WHERE FOO_AGE IS NOT NULL GROUP BY FOO_TOWN HAVING COUNT(*) > 10";
        assertEquals(query.toString(), SQL);
    }

    @Test
    public void orderByQuery() {
        Select query = Select.select(FOO_ID, FOO_NAME, FOO_AGE)
                .from(FOO_TAB)
                .orderBy(FOO_ID, FOO_NAME, FOO_AGE)
                .build();

        // What our statement should look like
        final String SQL = "SELECT FOO_ID, FOO_NAME, FOO_AGE FROM FOO_TAB ORDER BY FOO_ID, FOO_NAME, FOO_AGE";
        assertEquals(query.toString(), SQL);
    }

    @Test
    public void fromSubQuery() {
        Select query = select("*")
                .from(select(FOO_ID).from(FOO_TAB).build(), alias("sub"))
                .build();

        // What our statement should look like
        final String SQL = "SELECT * FROM (SELECT FOO_ID FROM FOO_TAB) AS sub";
        assertEquals(query.toString(), SQL);
    }

}
