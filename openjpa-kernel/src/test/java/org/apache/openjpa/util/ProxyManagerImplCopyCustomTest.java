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

import java.nio.charset.Charset;
import java.util.*;

@RunWith(Parameterized.class)
public class ProxyManagerImplCopyCustomTest {
    private ProxyManagerImpl proxyManager;

    private Object orig;
    private boolean expectedOutput;



    public ProxyManagerImplCopyCustomTest(Object orig, boolean expected) {
        configure(orig, expected);
    }

    public void configure(Object orig, boolean expected){
        this.proxyManager = new ProxyManagerImpl();
        this.orig = orig;
        this.expectedOutput = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters(){
        Random randomGenerator = new Random();

        WithGetterSetter withGetterSetter = new WithGetterSetter(randomGenerator.nextInt(),
                generateRandomString(10));

        WithDefaultConstructorAndGetterSetter withDefaultConstructorAndGetterSetter = new WithDefaultConstructorAndGetterSetter();
        withDefaultConstructorAndGetterSetter.setIntValue(randomGenerator.nextInt());
        withDefaultConstructorAndGetterSetter.setStringValue(generateRandomString(10));

        WithCopyConstructor withCopyConstructor = new WithCopyConstructor(randomGenerator.nextInt(),
                generateRandomString(10));


        WithoutGetterSetterDefaultCopyCostructor withoutGetterSetterDefaultCopyCostructor =
                new WithoutGetterSetterDefaultCopyCostructor(randomGenerator.nextInt(),
                        generateRandomString(10));

        return Arrays.asList(new Object[][]{
                // orig, expected
                {withDefaultConstructorAndGetterSetter, true},
                //{withCopyConstructor, true},
                {withCopyConstructor, !true},
                {"it’s not possible to test everything".toCharArray(), false},
                {withGetterSetter, false},
                {withoutGetterSetterDefaultCopyCostructor, false},
                {null, false},
                // increasing copyCustom coverage:
                {new ProxyManagerImpl().newDateProxy(Date.class), true},
                {generateRandomCollection(5), true},
                {generateRandomIntHashMap(5), true},
                {new Date(randomGenerator.nextLong()), true},
                {new GregorianCalendar(TimeZone.getDefault()), true},
        });
    }



    @Test
    public void copyCustomTest() {
        Object output = this.proxyManager.copyCustom(this.orig);

        if (this.expectedOutput) {
            Assert.assertEquals(this.orig, output);
        } else {
            Assert.assertNull(output);
        }

    }

    public static class WithGetterSetter {
        int intValue;
        String stringValue;

        public WithGetterSetter(int intValue, String stringValue) {
            this.intValue = intValue;
            this.stringValue = stringValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }
    }

    public static class WithDefaultConstructorAndGetterSetter {
        int intValue;
        String stringValue;

        public WithDefaultConstructorAndGetterSetter() {
            Random randomGenerator = new Random();
            this.intValue = randomGenerator.nextInt();
            this.stringValue = generateRandomString(10);
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        @Override
        public boolean equals(Object other){
            return this.getIntValue() == ((WithDefaultConstructorAndGetterSetter) other).getIntValue() &&
                    this.getStringValue().equals(((WithDefaultConstructorAndGetterSetter) other).getStringValue());
        }
    }

    public static class WithCopyConstructor {
        int intValue;
        String stringValue;

        public WithCopyConstructor(int intValue, String stringValue){
            this.intValue = intValue;
            this.stringValue = stringValue;
        }

        public WithCopyConstructor(WithCopyConstructor toCopy) {
            this.intValue = toCopy.intValue;
            this.stringValue = toCopy.stringValue;
        }
    }

    public static class WithoutGetterSetterDefaultCopyCostructor {
        int intValue;
        String stringValue;

        public WithoutGetterSetterDefaultCopyCostructor(int intValue, String stringValue) {
            this.intValue = intValue;
            this.stringValue = stringValue;
        }

    }

    public static String generateRandomString(int size){
        byte[] array = new byte[size];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

    public static Map generateRandomIntHashMap(int size){
        Map<Integer, Integer> map = new HashMap<>();
        Random randomGenerator = new Random();
        for (int i = 0; i < size; i++){
            map.put(i, randomGenerator.nextInt());
        }
        return map;
    }

    public static Collection generateRandomCollection(int size){
        Collection collection = new ArrayList();
        Random randomGenerator = new Random();
        for (int i = 0; i < size; i++){
            collection.add(randomGenerator.nextInt());
        }
        return collection;
    }
}
