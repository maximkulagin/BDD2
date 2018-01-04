let ordered_tree;
let short_ordered_tree;

window.addEventListener("load", function () {
    ordered_tree = document.getElementById("ordered_tree").value;
    ordered_tree = ordered_tree.split(";");
    short_ordered_tree = document.getElementById("short_ordered_tree").value;
    short_ordered_tree = short_ordered_tree.split(";")

    let FirstCanvas = document.getElementById("areaCanvas");
    let ctx = FirstCanvas.getContext("2d");
    ctx.strokeStyle = "black";
    let st_tree = document.getElementById("st_tree").value;
    drawGraph(FirstCanvas, st_tree);
    drawSigns(document.getElementById('orderedTree').getContext('2d'), "УБДР, сокращенная по правилу слияния");
    drawSigns(document.getElementById('shortOrderedTree').getContext('2d'), "Итоговая СУБДР");

})

function drawSigns(ctx, text) {

    ctx.beginPath();
    ctx.strokeStyle = "#bb5a2b";
    ctx.moveTo(10, 10);
    ctx.lineTo(60, 10);
    ctx.stroke();
    ctx.closePath();

    ctx.beginPath();
    ctx.strokeStyle = "#000000";
    ctx.strokeText("0", 70, 10);
    ctx.strokeText(text, 10, 70);
    ctx.closePath();

    ctx.beginPath();
    ctx.strokeStyle = "#699e59";
    ctx.moveTo(10, 40);
    ctx.lineTo(60, 40);
    ctx.stroke();
    ctx.closePath();

    ctx.beginPath();
    ctx.strokeStyle = "#000000"
    ctx.strokeText("1", 70, 40);
    ctx.closePath();
}

function clearCanvas(canvas) {
    canvas.getContext("2d").clearRect(0, 0, canvas.width, canvas.height);
}

function drawLineToChild(xP, yP, x, y, ctx) {
    ctx.beginPath();
    ctx.moveTo(xP, yP);
    ctx.lineTo(x, y);
    ctx.stroke();

}

function drawNode(x, y, name, ctx) {
    ctx.strokeText(name, x - 7, y);
}

function drawFuncRes(x, y, res, ctx) {
    ctx.strokeText(res, x + 2, y);
}

function drawGraph(FirstCanvas, st_tree) {
    clearCanvas(FirstCanvas);

    if (!(st_tree === "::null;")) {
        let tree;
        let arr = st_tree.split(";");
        for (let st of arr) {
            if (st === "") break;//после метода split последняя строка массива arr === ""
            let x0 = 0;
            let x1 = FirstCanvas.width;
            tree = st.split(":");
            let length = tree[1].length
            let y = FirstCanvas.height / 11 * length + 10;
            for (var i = 0; i < length; i++) {
                if (tree[1].charAt(i) === "1") {
                    x1 = (x0 + x1) / 2;
                } else {
                    x0 = (x0 + x1) / 2;
                }
            }
            if (tree[0] === "") {
                tree[0] = "x1";
            } else {
                let n = tree[0].charCodeAt(1);
                tree[0] = tree[0].slice(0, 1);
                tree[0] += String.fromCharCode(n + 1)
            }
            let x = (x0 + x1) / 2;
            let xL = (x0 + (x0 + x1) / 2) / 2;
            let xR = (x1 + (x0 + x1) / 2) / 2;
            let yCh = FirstCanvas.height / 11 * (length + 1) + 3;
            if (tree[2] === "null") {
                drawNode(x, y, tree[0], FirstCanvas.getContext("2d"));
                drawLineToChild(x, y, xL, yCh, FirstCanvas.getContext("2d"));
                drawLineToChild(x, y, xR, yCh, FirstCanvas.getContext("2d"));
            } else {
                drawFuncRes(x, y, tree[2], FirstCanvas.getContext("2d"));
            }
        }
    }

}


function checkClient(clientNode, ctx, tree) {
    if (tree.length === 1) {
        alert("Диаграмма уже нарисована");
        return;
    }
    let isRight = false;
    let i = 0;
    for (i; i < tree.length; i++) {
        if (clientNode === tree[i]) {
            drawVertex(clientNode, ctx)
            isRight = true;
            break;
        }
    }

    if (!isRight) {
        alert("Такой вершины нет");
        drawAll(ctx, tree);
    } else {
        let arr1 = tree.slice(0, i);
        let arr2 = tree.slice(i + 1, tree.length);
        if (tree === short_ordered_tree) {
            short_ordered_tree = arr1.concat(arr2);
        } else {
            ordered_tree = arr1.concat(arr2);
        }
    }
}


function drawAll(ctx, tree) {
    for (let node of tree) {
        drawVertex(node, ctx)
    }

}

function drawVertex(arr, ctx) {
    let arri = arr.split(":");
    let text = arri[0];
    let myH = arri[1].split("");
    let firstH = arri[2].split("");
    let secondH = arri[3].split("");


    var x = 1000 - 34;
    var y = 600 - 34;
    let x0 = 0;
    let x1 = x;
    ctx.strokeStyle = "#000000";
    for (var i = 0; i < myH.length; i++) {
        if (myH[i] === "1") {
            x1 = (x0 + x1) / 2;
        } else {
            x0 = (x0 + x1) / 2;
        }
    }
    let myX = (x0 + x1) / 2;
    let myY = y / 7 * myH.length;

    x0 = 0;
    x1 = x;
    let firstY = y;
    let firstX;
    if (arri[2].charAt(1) === 'r') {
        if (arri[2].charAt(0) === '1') {
            x1 = (x0 + x1) / 2;
            ctx.strokeText("1", x / 4, y + 10);

        } else {
            x0 = (x0 + x1) / 2;
            ctx.strokeText("0", 3 * x / 4, y + 10);
        }
    } else {
        for (var i = 0; i < firstH.length; i++) {
            if (firstH[i] === "1") {
                x1 = (x0 + x1) / 2;
            } else {
                x0 = (x0 + x1) / 2;
            }
        }
        firstY = y / 7 * firstH.length;
    }
    firstX = (x0 + x1) / 2;


    x0 = 0;
    x1 = x;
    let secondY = y;
    let secondX;

    if (arri[3].charAt(1) === 'r') {
        if (arri[3].charAt(0) === '1') {
            x1 = (x0 + x1) / 2;
            ctx.strokeText("1", x / 4, y + 10);
        } else {
            ctx.strokeText("0", 3 * x / 4, y + 10);
            x0 = (x0 + x1) / 2;
        }
    } else {
        for (var i = 0; i < secondH.length; i++) {
            if (secondH[i] === "1") {
                x1 = (x0 + x1) / 2;
            } else {
                x0 = (x0 + x1) / 2;
            }
        }
        secondY = y / 7 * secondH.length;
    }
    secondX = (x0 + x1) / 2;

    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.strokeStyle = "#bb5a2b";
    ctx.moveTo(myX, myY + 10);
    ctx.lineTo(secondX, secondY);
    ctx.stroke();
    ctx.closePath();
    ctx.beginPath();
    ctx.strokeStyle = "#699e59";
    ctx.moveTo(myX, myY + 10);
    ctx.lineTo(firstX, firstY);
    ctx.stroke();
    ctx.closePath();
    ctx.beginPath();
    ctx.strokeStyle = "#000000";
    ctx.strokeText(text, myX - 3, myY + 8);

}

