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
import org.junit.Test;

/**
 * There are two sorted arrays nums1 and nums2 of size m and n respectively.
 * Find the median of the two sorted arrays.
 * <br/>
 * The overall run time complexity should be O(log (m+n)).
 * <p/>
 * Example:
 * <pre>
 *     case 1:
 *     nums1 = [1, 3]
 *     nums2 = [2]
 *
 *     The median is 2.0
 *
 *     case 2:
 *     nums1 = [1, 2]
 *     nums2 = [3, 4]
 *
 *     The median is (2 + 3)/2 = 2.5
 * </pre>
 * Reference:
 * <ul>
 * <li>https://leetcode.com/problems/median-of-two-sorted-arrays/description/</li>
 * </ul>
 */
public class QuizHard_MedianOfTwoSortedArrays {

    public static void main(String[] args) {
        QuizHard_MedianOfTwoSortedArrays test = new QuizHard_MedianOfTwoSortedArrays();

        Assert.assertEquals(2f,
                            test.findMedianSortedArrays(
                                    new int[]{1, 3},
                                    new int[]{2}),
                            0f);
        Assert.assertEquals(2.5f,
                            test.findMedianSortedArrays(
                                    new int[]{1, 2},
                                    new int[]{3, 4}),
                            0f);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected / Private Methods ////////////////////////////////////////////

    // Solution #1 ////////////////////////////////////////////////////////////

    public double findMedianSortedArrays(int[] nums1,
                                         int[] nums2) {
        // A median is the number in the middle of an array, where the length
        // of left part is equal to the length of the right part.
        //
        // So we need to find a pair of pivots in the two arrays that the total
        // length of the two left parts is equal to the length of the two right
        // parts.
        //
        // For example:
        // nums1: a1, a2, a3, a4
        //                  ^   <-------------.
        // nums2: b1, b2, b3, b4, b5, b6      +-- pair of the pivots.
        //              ^   <-----------------'
        //
        //     left     |      right
        // a1, a2, a3   |  a4
        // b1, b2       |  b3, b4, b5, b6
        //
        // Where all the element of the left part is less than all the elements
        // of the right part!
        // But how to find the pivots???
        // Because the arrays are sorted, a3 < a4 and b2 < b3 are guaranteed.
        // In addition to that, a3 < b3 and b2 < a4 must be also guaranteed!
        //
        // So we got three criteria to fulfill, summarized as:
        // 1. The length of two merged left parts is equal to the length of two
        //    merged right part.
        // 2. The max value of left sub-array #1 is less than the min value of
        //    right sub-array #2 (a3 < b3).
        // 3. The max value of left sub-array #2 is less than the min value of
        //    right sub-array #1 (b2 < a4).
        //
        // With criterion #1, we only need to traverse the shorter array!
        //
        // Once we get the pivots, we could get the median by this formula:
        //
        // For even length:
        // median = avg(max(left1, left2),
        //              min(right1, right2))
        //
        // For odd length:
        // median = max(left1, left2)

        final int[] shorter;
        final int[] longer;
        if (nums1.length < nums2.length) {
            shorter = nums1;
            longer = nums2;
        } else {
            shorter = nums2;
            longer = nums1;
        }

        boolean isEven = (shorter.length + longer.length) % 2 == 0;

        // nums1:    a1   a2   a3   a4
        // pivot:  0    1    2    3    4    5    6
        // nums2:    b1   b2   b3   b4   b5   b6

        int begin = 0;
        int end = shorter.length;
        int half = (shorter.length + longer.length + 1) / 2;
        int pivotInShorter = getPivot(begin, end);
        int pivotInLonger = half - pivotInShorter;

        while (end > begin) {
            if (numOf(shorter, pivotInShorter - 1) < numOf(longer, pivotInLonger + 1) &&
                numOf(longer, pivotInLonger - 1) < numOf(shorter, pivotInShorter + 1)) {
                // The pivots are founded!!!
                break;
            } else {
                if (numOf(shorter, pivotInShorter - 1) > numOf(longer, pivotInLonger + 1)) {
                    // Move pivot to left.
                    if (end - begin == 1) {
                        end = begin;
                    } else {
                        end = pivotInShorter;
                    }
                } else {
                    // Move pivot to right.
                    begin = pivotInShorter;
                }

                pivotInShorter = getPivot(begin, end);
                pivotInLonger = half - pivotInShorter;
            }
        }

        if (isEven) {
            int leftMax = Math.max(numOf(shorter, pivotInShorter - 1),
                                   numOf(longer, pivotInLonger - 1));
            int rightMin = Math.min(numOf(shorter, pivotInShorter + 1),
                                    numOf(longer, pivotInLonger + 1));

            return (double) (leftMax + rightMin) / 2f;
        } else {
            return (double) Math.max(numOf(shorter, pivotInShorter - 1),
                                     numOf(longer, pivotInLonger - 1));
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected / Private Methods ////////////////////////////////////////////

    private int getPivot(int begin, int end) {
        // Plus one would make the half lean to right, which means the half would
        // never approach the left margin and we need to handle that corner cases.
        return (begin + end + 1) / 2;
    }

    private int numOf(int[] nums, int index) {
        if (index < 0) {
            return Integer.MIN_VALUE;
        } else if (index >= nums.length) {
            return Integer.MAX_VALUE;
        } else {
            return nums[index];
        }
    }
}
