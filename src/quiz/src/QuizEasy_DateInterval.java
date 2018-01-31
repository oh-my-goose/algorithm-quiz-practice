import org.junit.Assert;

import java.util.Objects;

/**
 * Given start and end date, calculate the interval. Date has year, month, and
 * day.
 */
public class QuizEasy_DateInterval {

    public static void main(String[] args) {
        final QuizEasy_DateInterval solver = new QuizEasy_DateInterval();

        // 0000.01.01 - 0000.01.01
        Assert.assertEquals(new Date(0, 0, 0),
                            solver.intervalBetween(new Date(0, 1, 1),
                                                   new Date(0, 1, 1)));


        // start:0000.01.01 - end:0000.01.15
        Assert.assertEquals(new Date(0, 0, 14),
                            solver.intervalBetween(new Date(0, 1, 1),
                                                   new Date(0, 1, 15)));

        // start:2000.10.01 - end:2010.03.21
        Assert.assertEquals(new Date(9, 5, 20),
                            solver.intervalBetween(new Date(2000, 10, 1),
                                                   new Date(2010, 3, 21)));

        // start:2000.10.01 - end:2010.03.21
        Assert.assertEquals(new Date(9, 5, 20),
                            solver.intervalBetween(new Date(2000, 10, 1),
                                                   new Date(2010, 3, 21)));
        // start:2000.10.18 - end:2010.03.15
        Assert.assertEquals(new Date(9, 4, 28),
                            solver.intervalBetween(new Date(2000, 10, 18),
                                                   new Date(2010, 3, 15)));
        // Proof: 2000.10.18 - 2000.10.31 = 0000.00.13
        //     +  2000.10.31 - 2010.03.15 = 0009.04.14
        //       -------------------------------------
        //                                  0009.04.28

        // start:2008.11.10 - end:2007.12.21
        Assert.assertEquals(new Date(0, 10, 20),
                            solver.intervalBetween(new Date(2008, 11, 10),
                                                   new Date(2007, 12, 21)));
        // "start" is smaller than the "end", exchange them so that...
        // start:2007.12.21 - end:2008.11.10
        //
        // Proof: 2007.12.21 - 2007.12.31 = 0000.00.10
        //     +  2007.12.31 - 2008.11.10 = 0000.10.10
        //       -------------------------------------
        //                                  0000.10.20

        // start:2008.02.28 - end: 2009.02.28
        Assert.assertEquals(new Date(1, 0, 0),
                            solver.intervalBetween(new Date(2008, 2, 28),
                                                   new Date(2009, 2, 28)));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected / Private Methods ////////////////////////////////////////////

    private Date intervalBetween(Date start, Date end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException();
        }

        final Date interval = new Date();

        // Figure out which is bigger.
        if (start.compareTo(end) > 0) {
            Date tmp = start;

            start = end;
            end = tmp;
        }

        // Day.
        interval.day = end.day - start.day;
        int carryOfMonth = 0;
        if (interval.day < 0) {
            interval.day += daysOfMonth(start.month, start.year);
            carryOfMonth = 1;
        }

        // Month.
        interval.month = end.month - start.month - carryOfMonth;
        int carryOfYear = 0;
        if (interval.month < 0) {
            interval.month += 12;
            carryOfYear = 1;
        }

        // Year.
        interval.year = end.year - start.year - carryOfYear;

        return interval;
    }

    private int daysOfMonth(int month, int year) {
        if (month == 2) {
            // Detect leap year.
            if (year % 4 == 0) {
                return 29;
            } else {
                return 28;
            }
        } else if (month == 1 ||
            month == 3 ||
            month == 5 ||
            month == 7 ||
            month == 8 ||
            month == 10 ||
            month == 12) {
            return 31;
        } else if (month == 4 ||
                   month == 6 ||
                   month == 9 ||
                   month == 11) {
            return 30;
        } else {
            throw new IllegalArgumentException();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Clazz //////////////////////////////////////////////////////////////////

    static class Date {

        int year;
        int month;
        int day;

        Date(int year, int month, int day) {
//            if (month < 0 || month > 12 ||
//                day < 0 || day > 31) {
//                throw new IllegalArgumentException();
//            } else if (month == 2) {
//                if ((year % 4 == 0 && day > 29) ||
//                    (Math.abs(year) % 4 > 0 && day > 28)) {
//                    throw new IllegalArgumentException();
//                }
//            }

            this.year = year;
            this.month = month;
            this.day = day;
        }

        Date() {
            this(0, 0, 0);
        }

        /**
         * @return Zero: It is equal than other;
         * A positive value: It is greater than other;
         * A negative value: It is smaller than other.
         */
        int compareTo(Date other) {
            return getTimestamp() - other.getTimestamp();
        }

        int getTimestamp() {
            return 10000 * this.year +
                   100 * this.month +
                   this.day;
        }

        @Override
        public String toString() {
            return "Date{" +
                   "year=" + year +
                   ", month=" + month +
                   ", day=" + day +
                   '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Date date = (Date) o;
            return year == date.year &&
                   month == date.month &&
                   day == date.day;
        }

        @Override
        public int hashCode() {
            return Objects.hash(year, month, day);
        }
    }
}
