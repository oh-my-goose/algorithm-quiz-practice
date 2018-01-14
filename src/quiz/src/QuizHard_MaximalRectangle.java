// Copyright (c) 2016-present boyw165
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
//    The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
//    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import org.junit.Assert;

import java.util.Locale;
import java.util.Stack;

/**
 * Given a 2D binary matrix filled with 0's and 1's, find the largest rectangle
 * containing only 1's and return its area.
 * <p/>
 * Example:
 * <pre>
 * m=  1 0 1 0 0
 *     1 0 1 1 1
 *     1 1 1 1 1
 *     1 0 0 1 0
 *
 * The maximal rectangle is:
 *
 *     1 0 1 0 0
 *        .-----.
 *     1 0|1 1 1|
 *     1 1|1 1 1|
 *        '-----'
 *     1 0 0 1 0
 * </pre>
 * Reference:
 * <ul>
 * <li>https://leetcode.com/problems/maximal-rectangle/description/</li>
 * </ul>
 */
public class QuizHard_MaximalRectangle {

    public static void main(String[] args) {
        long startTs;

        QuizHard_MaximalRectangle test = new QuizHard_MaximalRectangle();

        // Solution 1
        startTs = System.currentTimeMillis();
        for (Verification v : sVerification) {
            Assert.assertEquals(
                v.expected,
                test.maximalRectangle(v.matrix));
        }
        System.out.println(String.format(Locale.ENGLISH, "Solution 1 takes %d ms",
                                         System.currentTimeMillis() - startTs));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected / Private Methods ////////////////////////////////////////////

    // Solution #1 ////////////////////////////////////////////////////////////

    /**
     * <p>
     * Naive approach.
     * Beats 41% of other submission.
     * </p>
     */
    private int maximalRectangle(char[][] matrix) {
        if (matrix == null) return 0;

        final int maxRow = matrix.length;
        if (maxRow == 0) return 0;
        final int maxCol = matrix[0].length;
        if (maxCol == 0) return 0;
        final int[] lookupTable = new int[maxCol];

        int maxArea = 0;

        for (int row = 0; row < maxRow; ++row) {
            for (int col = 0; col < maxCol; ++col) {
                int value = matrix[row][col] - '0';
                if (value > 0) {
                    lookupTable[col] += 1;
                } else {
                    lookupTable[col] = 0;
                }
            }
            maxArea = Math.max(
                maxArea,
                largestRectangleArea(lookupTable));
        }

        return maxArea;
    }

    private int largestRectangleArea(int[] hist) {
        // Create an empty stack. The stack holds indexes of hist[] array
        // The bars stored in stack are always in increasing order of their
        // heights.
        final Stack<Integer> s = new Stack<>();

        // Initialize max area
        int maxArea = 0;
        // To store top of stack
        int tp;
        // To store area with top bar as the smallest bar
        int areaWithTop;

        // Run through all bars of given histogram
        int i = 0;
        while (i < hist.length) {
            // If this bar is higher than the bar on top stack, push it to stack
            if (s.empty() || hist[s.peek()] <= hist[i]) {
                s.push(i++);

                // If this bar is lower than top of stack, then calculate area of
                // rectangle with stack top as the smallest (or minimum height)
                // bar. 'i' is 'right index' for the top and element before top in
                // stack is 'left index'
            } else {
                tp = s.pop();  // store the top index

                int w = s.empty() ? i : i - s.peek() - 1;
                // Calculate the area with hist[tp] stack as smallest bar
                areaWithTop = hist[tp] * w;

                // update max area, if needed
                if (maxArea < areaWithTop)
                    maxArea = areaWithTop;
            }
        }

        // Now pop the remaining bars from stack and calculate area with every
        // popped bar as the smallest bar
        while (!s.empty()) {
            tp = s.pop();

            int w = s.empty() ? i : i - s.peek() - 1;
            areaWithTop = hist[tp] * w;

            if (maxArea < areaWithTop)
                maxArea = areaWithTop;
        }

        return maxArea;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Challenges /////////////////////////////////////////////////////////////

    private static final Verification[] sVerification = new Verification[]{
//        new Verification(6, new char[][]{
//            new char[]{'1', '0', '1', '0', '0'},
//            new char[]{'1', '0', '1', '1', '1'},
//            new char[]{'1', '1', '1', '1', '1'},
//            new char[]{'1', '0', '0', '1', '0'}
//        }),
        new Verification(8, new char[][]{
            new char[]{'1', '0', '1', '1', '0', '1'},
            new char[]{'1', '1', '1', '1', '1', '1'},
            new char[]{'0', '1', '1', '0', '1', '1'},
            new char[]{'1', '1', '1', '0', '1', '0'},
            new char[]{'1', '1', '0', '1', '1', '1'}
        })
    };

    ///////////////////////////////////////////////////////////////////////////
    // Clazz //////////////////////////////////////////////////////////////////

    private static class RectF {

        float top;
        float left;
        float right;
        float bottom;

        public RectF(float top, float left, float right, float bottom) {
            this.top = top;
            this.left = left;
            this.right = right;
            this.bottom = bottom;
        }

        float getWidth() {
            return this.right - this.left;
        }

        float getHeight() {
            return this.top - this.bottom;
        }
    }

    private static class Verification {
        final int expected;
        final char[][] matrix;

        public Verification(int expected,
                            char[][] histograms) {
            this.expected = expected;
            this.matrix = histograms;
        }
    }
}
