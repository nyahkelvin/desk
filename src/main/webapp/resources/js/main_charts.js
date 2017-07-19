// make console.log safe to use
window.console||(console={log:function(){}});

with ((console && console._commandLineAPI) || {}) {
  "some values goes here";
}

$(document).ready(function () {
    var options = {
        chart: {
            renderTo: 'incidentBar',
            type: 'bar'
        },
        title: {
            text: 'Monthly Payment'
        },
        tooltip: {
            valueSuffix: ''
        },
        xAxis: {
            categories: []
        },
        yAxis: {
            title: {
                text: 'Amount in Naira'
            }
        },
        credits: {
            enabled: false
        },
        series: [{}]
    };

    ids = [];
    cats = [];


    $.ajax({
        dataType: "json",
        url: "https://newheavenmm.com:9000/dashservice/paymentReport",
    }).done(function (data) {

        for (var i = 0; i < data.length; i++) {
            ids.push(data[i].amountTotal);
            cats.push(data[i].monthName);

        }

        options.series[0].data = ids;
        options.xAxis.categories = cats;
        options.series[0].color = "#0099cc";
        options.series[0].name = "Total Payments Received";

        var chart = new Highcharts.Chart(options);
        console.log(ids);
        console.log(cats);
        console.log(options);
    });

});