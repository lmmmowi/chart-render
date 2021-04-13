package com.lmmmowi.chartrender;

import org.junit.Test;

import java.io.File;

/**
 * @Author: lmmmowi
 * @Date: 2021/4/13
 * @Description:
 */
public class ChartRenderTest {

    @Test
    public void test() {
        File baseDir = new File(new File("").getAbsolutePath());
        File libDir = new File(baseDir, "lib");
        File demoDir = new File(baseDir, "demo");
        File dataFile = new File(demoDir, "demo.json");
        File outputFile = new File(demoDir, "out.jpg");

        ChartRender chartRender = ChartRender.getInstance(libDir);
        chartRender.render(dataFile, outputFile);
    }
}
