var system = require('system');
var fs = require('fs');
var page = require('webpage').create();

var url = system.args[1];
var dataUrl = system.args[2];
var output = system.args[3];
var width = system.args[4] || 0;
var height = system.args[5] || 0;
var echartsData = fs.read(dataUrl);

page.open(url, function (status) {
    if (status === "success") {
        // set chart width and heigth
        var script = "function(){ window.chartWidth = " + width + "; window.chartHeight = " + height + "; }";
        page.evaluateJavaScript(script);

        // set echarts data
        script = "function(){ window.option = " + echartsData + "; }";
        page.evaluateJavaScript(script);

        window.setTimeout(function () {
            var rect = page.evaluate(function () {
                return document.getElementById('chart').getBoundingClientRect();
            });
            console.debug(JSON.stringify(rect));
            page.clipRect = {
                top: rect.top,
                left: rect.left,
                width: rect.width,
                height: rect.height,
            }

            page.render(output);
            console.log("render success");
            phantom.exit();
        }, 1000);
    }
    else {
        console.log("fail to open url");
        phantom.exit();
    }
});

