/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.openjpa.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CacheMapRemoveTest {

    private CacheMap cacheMap;

    private Object key;
    private boolean alreadyExists;
    private boolean isPinned;
    private final String alreadyPresentValue = "it's not possible to test everything";


    public CacheMapRemoveTest(Object key, boolean isPinned, boolean alreadyExists) {
        configure(key, isPinned, alreadyExists);
    }

    public void configure(Object key, boolean isPinned, boolean alreadyExists){
        this.key = key;
        this.isPinned = isPinned;
        this.alreadyExists = alreadyExists;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][]{
                // key, isPinned, alreadyExists
                {null, false, false},
                {null, false, true},
                {null, true, false},
                {null, true, true},

                {new Object(), false, false},
                {new Object(), false, true},
                {new Object(), true, false},
                {new Object(), true, true},

        });
    }


    @Before
    public void setUp(){
        this.cacheMap = new CacheMap(true);

        if (this.alreadyExists)
            this.cacheMap.put(this.key, this.alreadyPresentValue);

        if (this.isPinned)
            this.cacheMap.pin(this.key);
    }

    @Test
    public void removeTest() {

        Object oldValue = this.cacheMap.remove(this.key);
        if (this.alreadyExists){
            Assert.assertEquals(oldValue, this.alreadyPresentValue);
        }
        else{
            Assert.assertNull(oldValue);
        }

        // check if i cannot read the same value again
        oldValue = this.cacheMap.remove(this.key);
        Assert.assertNull(oldValue);
    }

}
