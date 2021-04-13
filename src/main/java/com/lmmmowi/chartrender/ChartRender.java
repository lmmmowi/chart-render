package com.lmmmowi.chartrender;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: lmmmowi
 * @Date: 2021/4/13
 * @Description:
 */
public class ChartRender {

    private static final String BLANK = " ";
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 400;

    private final File phantomJsFile;
    private final File renderJsFile;
    private final File templateHtmlFile;

    public ChartRender(File phantomJsFile, File renderJsFile, File templateHtmlFile) {
        this.phantomJsFile = phantomJsFile;
        this.renderJsFile = renderJsFile;
        this.templateHtmlFile = templateHtmlFile;
    }

    public static ChartRender getInstance(File binPath) {
        File phantomJsFile = new File(binPath, "phantomjs");
        File renderJsFile = new File(binPath, "render.js");
        File templateHtmlFile = new File(binPath, "template.html");
        return new ChartRender(phantomJsFile, renderJsFile, templateHtmlFile);
    }

    public void render(File dataFile, File outputFile) {
        this.render(dataFile, outputFile, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void render(File dataFile, File outputFile, int width, int height) {
        String command = this.buildCommand(dataFile, outputFile, width, height);
        System.out.println(command);
        try {
            this.execute(command);
        } catch (IOException e) {
            throw new RuntimeException("render fail", e);
        }
    }

    private void execute(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
             BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            this.checkError(errorReader);
            this.checkError(inputReader);
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    private void checkError(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        String message = sb.toString();
        if (message.length() > 0 && !message.contains("success")) {
            throw new IOException(message);
        }
    }

    private String buildCommand(File dataFile, File outputFile, int width, int height) {
        String phantomJs = phantomJsFile.getAbsolutePath();
        String renderJs = renderJsFile.getAbsolutePath();
        String templateHtml = templateHtmlFile.getAbsolutePath();
        String data = dataFile.getAbsolutePath();
        String output = outputFile.getAbsolutePath();

        if (OsUtils.isWindows()) {
            templateHtml = fixPath(templateHtml);
            data = fixPath(data);
            output = fixPath(output);
        }

        return phantomJs + BLANK
                + renderJs + BLANK
                + templateHtml + BLANK
                + data + BLANK
                + output + BLANK
                + width + BLANK
                + height + BLANK;
    }

    private String fixPath(String path) {
        int index = path.indexOf(":");
        if (index >= 0) {
            path = path.substring(index + 1);
        }
        path = path.replace("\\", "/");
        return path;
    }
}
