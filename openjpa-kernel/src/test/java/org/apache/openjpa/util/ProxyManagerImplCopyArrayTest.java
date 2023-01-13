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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class ProxyManagerImplCopyArrayTest {

    private ProxyManagerImpl proxyManager;
    private boolean expectedOutput;
    private Object orig;


    public ProxyManagerImplCopyArrayTest(Object orig, boolean expectedOutput) {
        configure(orig, expectedOutput);
    }


    public void configure(Object orig, boolean expectedOutput){
        this.proxyManager = new ProxyManagerImpl();
        this.orig = orig;
        this.expectedOutput = expectedOutput;
    }



    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){

        return Arrays.asList(new Object[][]{

                //orig, expected
                {"it's not possible to test everything".toCharArray(), true},
                {3, false},
                {null, false},
        });

    }


    @Test
    public void copyCustomTest() {

        String output, expected;
        try{
            output = new String((char[]) this.proxyManager.copyArray(this.orig));
            expected = new String((char[]) this.orig);

            Assert.assertEquals(expected,output);
        }catch (Exception e){
            Assert.assertFalse(this.expectedOutput);
        }
    }
}
