package org.apache.openjpa.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class CacheMapPutTest {

    private CacheMap cacheMap;

    private Object key;
    private Object value;
    private boolean alreadyExists;
    private int mapSize;
    private int numElementsAlreadyPresent;
    private final String alreadyPresentValue = "it's not possible to test everything";
    private boolean expected;



    public CacheMapPutTest(Object key,Object value, boolean alreadyExists, int mapSize, int numEl, boolean expected) {
        configure(key, value, alreadyExists, mapSize, numEl, expected);
    }

    public void configure(Object key, Object value, boolean alreadyExist, int mapSize, int numEl, boolean expected){
        this.key = key;
        this.value = value;
        this.alreadyExists = alreadyExist;
        this.mapSize = mapSize;
        this.numElementsAlreadyPresent = numEl;
        this.expected = expected;

    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][]{
                // key, value, alreadyExists, mapSize, numEl, expected output
                {null, new Object(), false, -1, 1, false},
                {new Object(), null, false, 1, 1, false},
                {new Object(), new Object(), false, 1, 2, false},
                {new Object(), new Object(), false, 1, 1, true},
                {new Object(), new Object(), true, 1, 1, false},
        });
    }


    @Before
    public void setUp(){
        this.cacheMap = new CacheMap(true, this.mapSize, this.mapSize, .75F, 16);

        // counting the added elements
        int count = 0;
        /* adding the same elements if alreadyExists. It differs from the base object to check if the
           returned value is the same. */
        if (this.alreadyExists) {
            this.cacheMap.put(this.key, this.alreadyPresentValue);
            this.value = "it's not possible to test everything";
            count++;
        }
        // adding elements to fill size
        for (; count < this.numElementsAlreadyPresent; count++) {
            this.cacheMap.put(count, count);
        }
    }

    @Test
    public void putTest() {

        Object oldValue = this.cacheMap.put(this.key, this.value);

        if (this.alreadyExists && this.mapSize != 0) {
            //the two values should be equals
            Assert.assertEquals(oldValue, this.alreadyPresentValue);
        } else {
            Assert.assertNull(oldValue);
        }

        // check if i can read the same value
        Object redValue = this.cacheMap.get(this.key);
        Assert.assertEquals(this.value, redValue);

    }

}
