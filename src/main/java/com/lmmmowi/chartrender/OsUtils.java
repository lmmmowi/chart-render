package com.lmmmowi.chartrender;

/**
 * @Author: lmmmowi
 * @Date: 2021/4/13
 * @Description:
 */
class OsUtils {

    static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("win");
    }
}
