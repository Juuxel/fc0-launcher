/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package juuxel.fc0.launcher.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH.mm.ss");

    private Logger() {
    }

    public static void info(String message) {
        System.out.printf("%s [INFO] %s%n", LocalTime.now().format(TIME_FORMATTER), message);
    }

    public static void error(String message, Throwable error) {
        System.err.printf("%s [ERROR] %s%n", LocalTime.now().format(TIME_FORMATTER), message);
        error.printStackTrace();
    }

    public static void warn(String message) {
        System.err.printf("%s [WARN] %s%n", LocalTime.now().format(TIME_FORMATTER), message);
    }
}
