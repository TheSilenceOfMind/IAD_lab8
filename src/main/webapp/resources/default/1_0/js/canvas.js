var canvas;
var R, height, width;
var xList, yList;
var open = 0;
var map = 0.1;

function reloadPage() {
    xList = [];
    yList = [];

    canvas = document.getElementById("graph");
    width = parseInt(canvas.width);
    height = parseInt(canvas.height);
    R = parseFloat(document.getElementById("inputForm:valueR").value);
    if (R < 0)
        R = 0;
    var valX = parseFloat(document.getElementById("inputForm:valueX").innerHTML);
    if (isNaN(valX)) {
        document.getElementById("inputForm:valueXHidden").value = 100;
    }

    drawBackground();
    reloadPoints();
}

function drawBackground() {
    var ctx = canvas.getContext("2d");
    //clear old info
    ctx.clearRect(0, 0, width, height);

    ctx.fillStyle="#FFFFFF";
    ctx.fillRect(0, 0, width, height);

    if( R > 0) {
        drawFigures();
    }
    drawAxis();
}

function drawAxis() {
    var ctx = canvas.getContext("2d");

    ctx.strokeStyle="black";
    ctx.beginPath();
    //draw axis lines
    ctx.moveTo(0,height/2);
    ctx.lineTo(width,height/2);
    ctx.moveTo(width/2,0);
    ctx.lineTo(width/2,height);
    //draw one arrow (y-axis)
    ctx.moveTo(width/2 - width*0.02 ,height*0.02);
    ctx.lineTo(width/2,0);
    ctx.lineTo(width/2 + width*0.02,height*0.02);
    //draw one arrow (x-axis)
    ctx.moveTo(width - width*0.02 ,height/2 - height*0.02);
    ctx.lineTo(width,height/2);
    ctx.lineTo(width - width*0.02,height/2 + height*0.02);
    //marks on axis (y)
    for( var i = 1; i < height/(height*map); i++ ){
        ctx.moveTo(width/2 - width*0.01 ,height*map * i);
        ctx.lineTo(width/2 + width*0.01 ,height*map * i);
    }
    //marks on axis (x)
    for( var i = 1; i < width/(width*map); i++ ){
        ctx.moveTo(width*map * i,height/2-height*0.01);
        ctx.lineTo(width*map * i,height/2+height*0.01);
    }
    ctx.closePath();
    ctx.stroke();
}

function drawFigures() {
    var ctx = canvas.getContext("2d");

    ctx.strokeStyle="#b3b3ff";
    ctx.fillStyle="#b3b3ff";
    ctx.beginPath();

    ctx.moveTo(width/2, height/2);
    ctx.lineTo(width/2, height/2 - R/2 * height*map);
    ctx.lineTo(width/2-R*width*map, height/2);
    ctx.fill();

    ctx.fillRect(width/2, height/2, R/2*width*map, R*height*map);

    ctx.arc(width/2, height/2, R/2*height*map, 0, -0.5 * Math.PI, true);
    ctx.fill();

    ctx.closePath();
    ctx.stroke();
}

function clickPoint(event) {

    var rect = canvas.getBoundingClientRect();  // get element's abs. position
    var offset = (rect.width - canvas.width) / 2 + 1;

    var x = ((event.clientX - rect.left - offset) - canvas.width / 2) / (canvas.height * map);
    var y = (canvas.height / 2 - (event.clientY - rect.top - offset) ) / (canvas.height * map);
    if (R <= 0) {
        alert('Некорректное значение радиуса!');
    } else {
        xList.push(x);
        yList.push(y);
        addPoint(x, y, xList.length-1);
    }
}

function addPoint(x, y, index) {
    data = JSON.stringify({
        type: "Add point to DB",
        x: x,
        y: y,
        ind: index,
        R: parseFloat(R)
    });
    console.log(data);
    if (open == 1) {
        Socket.send(data);
    }
}

function drawPoint(context, real_x, real_y, isInside) {
    x = real_x;
    y = real_y;
    context.beginPath();
    if (isInside) {
        context.fillStyle = "green";
    } else {
        context.fillStyle = "red";
    }
    context.arc(x * width * map + width/2, -y * height * map + height/2, 3, 0 * Math.PI, 2 * Math.PI, false);
    context.fill();
}

function reloadPoints() {
    var gFunc = function () {
        if (open == 1) {
            data = JSON.stringify({
                type: "Change radius and draw points",
                beginInd: 0,
                R: parseFloat(R)
            });
            console.log("RELOAD POINTS" + data);
            Socket.send(data);
            clearInterval(myVar);
        }
    }
    myVar = setTimeout(gFunc, 200);

}

function saveRAndRepaint(value) {
    document.getElementById("inputForm:valueR").value = value;
    R = parseFloat(value);
    drawBackground();
    reloadPoints();
}