/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.chronos.util;

import java.util.concurrent.TimeUnit;

/**
 * Utility methods for time manipulation.
 *
 * @author Aleksandar Seovic  2013.04.16
 */
public class TimeUtil {
    /**
     * Convert time duration to milliseconds.
     *
     * @param duration  time duration
     * @param unit      time units
     *
     * @return duration in milliseconds
     */
    public static long toMillis(int duration, TimeUnit unit) {
        switch (unit) {
            case DAYS:
                return duration * 86400 * 1000;
            case HOURS:
                return duration * 3600 * 1000;
            case MINUTES:
                return duration * 60 * 1000;
            case SECONDS:
                return duration * 1000;
            default:
                throw new IllegalArgumentException("Invalid time unit: " + unit + ". Supported units are SECONDS, MINUTES, HOURS or DAYS");
        }
    }
}
