package easy

import org.junit.Assert

/**
 * Given start and end date, calculate the interval. Date has year, month, and
 * day.
 */
class DateInterval {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val solver = DateInterval()

            // 0000.01.01 - 0000.01.01
            Assert.assertEquals(Date(0, 0, 0),
                                solver.intervalBetween(Date(0, 1, 1),
                                                       Date(0, 1, 1)))

            // start:0000.01.01 - end:0000.01.15
            Assert.assertEquals(Date(0, 0, 14),
                                solver.intervalBetween(Date(0, 1, 1),
                                                       Date(0, 1, 15)))

            // start:2000.10.01 - end:2010.03.21
            Assert.assertEquals(Date(9, 5, 20),
                                solver.intervalBetween(Date(2000, 10, 1),
                                                       Date(2010, 3, 21)))

            // start:2000.10.01 - end:2010.03.21
            Assert.assertEquals(Date(9, 5, 20),
                                solver.intervalBetween(Date(2000, 10, 1),
                                                       Date(2010, 3, 21)))
            // start:2000.10.18 - end:2010.03.15
            Assert.assertEquals(Date(9, 4, 28),
                                solver.intervalBetween(Date(2000, 10, 18),
                                                       Date(2010, 3, 15)))
            // Proof: 2000.10.18 - 2000.10.31 = 0000.00.13
            //     +  2000.10.31 - 2010.03.15 = 0009.04.14
            //       -------------------------------------
            //                                  0009.04.28

            // start:2008.11.10 - end:2007.12.21
            Assert.assertEquals(Date(0, 10, 20),
                                solver.intervalBetween(Date(2008, 11, 10),
                                                       Date(2007, 12, 21)))
            // "start" is smaller than the "end", exchange them so that...
            // start:2007.12.21 - end:2008.11.10
            //
            // Proof: 2007.12.21 - 2007.12.31 = 0000.00.10
            //     +  2007.12.31 - 2008.11.10 = 0000.10.10
            //       -------------------------------------
            //                                  0000.10.20

            // start:2008.02.28 - end: 2009.02.28
            Assert.assertEquals(Date(1, 0, 0),
                                solver.intervalBetween(Date(2008, 2, 28),
                                                       Date(2009, 2, 28)))
        }
    }

    fun intervalBetween(givenStart: Date,
                        givenEnd: Date): Date {
        var start = givenStart
        var end = givenEnd
        val interval = Date()

        // Figure out which is bigger.
        if (start > end) {
            val tmp = start

            start = end
            end = tmp
        }

        // Day.
        interval.day = end.day - start.day
        var carryOfMonth = 0
        if (interval.day < 0) {
            interval.day += daysOfMonth(start.month, start.year)
            carryOfMonth = 1
        }

        // Month.
        interval.month = end.month - start.month - carryOfMonth
        var carryOfYear = 0
        if (interval.month < 0) {
            interval.month += 12
            carryOfYear = 1
        }

        // Year.
        interval.year = end.year - start.year - carryOfYear

        return interval
    }

    private fun daysOfMonth(month: Int, year: Int): Int {
        return if (month == 2) {
            // Detect leap year.
            if (year % 4 == 0) {
                29
            } else {
                28
            }
        } else if (month == 1 ||
            month == 3 ||
            month == 5 ||
            month == 7 ||
            month == 8 ||
            month == 10 ||
            month == 12) {
            31
        } else if (month == 4 ||
            month == 6 ||
            month == 9 ||
            month == 11) {
            30
        } else {
            throw IllegalArgumentException()
        }
    }

    data class Date constructor(var year: Int = 0,
                                var month: Int = 0,
                                var day: Int = 0) {

        init {
            if (month < 0 || month > 12 ||
                day < 0 || day > 31) {
                throw IllegalArgumentException()
            } else if (month == 2) {
                if ((year % 4 == 0 && day > 29) ||
                    (Math.abs(year) % 4 > 0 && day > 28)) {
                    throw IllegalArgumentException()
                }
            }
        }

        val timestamp: Int
            get() = 10000 * this.year +
                100 * this.month +
                this.day

        /**
         * @return Zero: It is equal than other;
         * A positive value: It is greater than other;
         * A negative value: It is smaller than other.
         */
        operator fun compareTo(other: Date): Int {
            return timestamp - other.timestamp
        }
    }
}
