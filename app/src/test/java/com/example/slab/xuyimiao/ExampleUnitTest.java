package com.example.slab.xuyimiao;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        int[] values = new int[10];
        for (int i = 0; i < values.length; i++) {
            values[i] = 10 - i;
        }
        int target  = 10;
        int sum = 0;
        int start;
        int end;
        for (start = 0, end =0; start < values.length && end < values.length;) {
            if (sum == target) {
                System.out.println(start + " ," + end);
                return;
            }
            if (sum + values[end] <= target) {
                sum += values[end++];
            } else {
                sum -= values[start++];
            }
        }

    }
}