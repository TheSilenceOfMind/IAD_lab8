var host = "ws://localhost:58905/lab8/ping";
var Socket = new WebSocket(host);

Socket.onopen = function () {
    console.log("Connected");
    open = 1;
};

Socket.onmessage = function (event) {
    console.log("HERE " + event.data);
    var data = JSON.parse(event.data);
    switch (data.type) {
        case "Change radius and draw points":
            first = parseInt(data.first);
            xList = data.xList;
            yList = data.yList;
            console.log("CHANGE RADIUS AND DRAW POINTS HITS ARRAY: " + data.hitList);
            for (ind in xList) {
                i = parseInt(ind) + first;
                drawPoint(canvas.getContext("2d"), xList[i], yList[i], data.hitList[i]);
            }
            break;

        case "Add point to DB":
            console.log(data);
            table = document.getElementById("Table:resTable").getElementsByTagName("tbody")[0];
            index = parseInt(data.ind);

            var row = table.insertRow(0);
            var style_class;
            if (index % 2 == 0) {
                style_class = "res-table-odd-row";
            } else {
                style_class = "res-table-even-row";
            }
            row.setAttribute("class", style_class);

            var x_td = row.insertCell(0);
            var y_td = row.insertCell(1);
            var r_td = row.insertCell(2);
            var check_td = row.insertCell(3);

            x_td.innerHTML = Math.round(xList[index] * 100) / 100;
            y_td.innerHTML = Math.round(yList[index] * 100) / 100;
            r_td.innerHTML = data.R;
            var inString;
            if (data.isInside) {
                inString = "IN";
            } else {
                inString = "OUT";
            }
            check_td.innerHTML = inString;
            drawPoint(canvas.getContext("2d"), xList[index], yList[index], data.isInside);
            break;
    }
};

Socket.onclose = function () {
    console.log('Connection Lost');
    open = 0;
};