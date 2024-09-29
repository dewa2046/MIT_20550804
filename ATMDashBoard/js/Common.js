/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function setFunctions(func) {

    try {
//        var func = localStorage.getItem("AllowedFunc");
//        alert(func);
        var funcArr = func.split(",");
//        alert(funcArr.length);

        if (funcArr.length > 0) {
            for (var i = 0; i < funcArr.length; i++) {
//                alert(funcArr[i]);
                if (document.getElementById(funcArr[i]) != null) {
                    var divEntry = document.getElementById(funcArr[i]);
                    divEntry.style.display = "block";
                }
            }
        }

    } catch (e) {
        window.alert(e);
    }

}

function setFunctionsHome(func) {

    try {
//        var func = localStorage.getItem("AllowedFunc");
//        alert(func);
        var funcArr = func.split(",");

        if (funcArr.length > 0) {
            for (var i = 0; i < funcArr.length; i++) {
                if (document.getElementById(funcArr[i]) != null) {
                    var divEntry = document.getElementById(funcArr[i]);
                    divEntry.style.display = "block";
                    divEntry.style.display = "grid";
                    divEntry.style = "justify-items: center";
                }
            }
        }

    } catch (e) {
        window.alert(e);
    }

}

function exportTableToExcel(tbName, fName) {
    var downloadLink;
    var dataType = 'application/vnd.ms-excel';
    var tableSelect = document.getElementById(tbName);
    var tableHTML = tableSelect.outerHTML.replace(/ /g, '%20');
//                var filename = document.getElementById('hed').innerHTML;
    var filename = fName;


    // Specify file name
    filename = filename ? filename + '.xls' : 'excel_data.xls';

    // Create download link element
    downloadLink = document.createElement("a");

    document.body.appendChild(downloadLink);

    if (navigator.msSaveOrOpenBlob) {
        var blob = new Blob(['\ufeff', tableHTML], {
            type: dataType
        });
        navigator.msSaveOrOpenBlob(blob, filename);
    } else {
        // Create a link to the file
        downloadLink.href = 'data:' + dataType + ', ' + tableHTML;

        // Setting the file name
        downloadLink.download = filename;

        //triggering the function
        downloadLink.click();
    }
}

function alertBox(msg, type) {

    try {
        if (type == "e") {
//            document.getElementById("modalContent").style.background = "linear-gradient(to right, #ff4c4c, #eb75ab)";
//            document.getElementById("modaltitle").innerText = "Error";
        } else {
//            document.getElementById("modalContent").style.background = "linear-gradient(to right, #66a80f, #c0eb75)";
        }
        document.getElementById("modalContent").style.textAlign = "left";


        $("#modal_body").html(msg);
        $('#myModal').modal({show: true});
    } catch (e) {
        window.alert(e);
    }

}

function validatePhoneNumber(phoneNumber) {

    var regex = /^(?:0)[0-9]{9}$/;

    return regex.test(phoneNumber);
}

function validateEmail(email) {

    var regex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

    return regex.test(email);
}

function alertBoxConfirm(msg) {

    try {
//        document.getElementById("modalContentConfirm").style.background = "linear-gradient(to right, #4CE6FF, #7589EB)";
        document.getElementById("modalContentConfirm").style.textAlign = "left";

        $("#modal_body_confirm").html(msg);
        $('#myModalConfirm').modal({show: true});
    } catch (e) {
        window.alert(e);
    }

}

function scrollFunction() {
    //Get the button
    var mybutton = document.getElementById("myBtn");

    if (document.body.scrollTop > 100 || document.documentElement.scrollTop > 100) {
        mybutton.style.display = "block";
    } else {
        mybutton.style.display = "none";
    }
}

// When the user clicks on the button, scroll to the top of the document
function topFunction() {
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
}

let inactivityTime = function () {
    let time;
    window.onload = resetTimer;
    document.onmousemove = resetTimer;
    document.onkeypress = resetTimer;
    function logout() {
        alertBox("Session Expired!", "e");
        sessionStorage.clear();
        localStorage.clear();
//        window.location.href = "Login.html";
        window.location.replace("Login.html");
    }
    function resetTimer() {
        clearTimeout(time);
//        time = setTimeout(logout, 60000); //60 seconds
        time = setTimeout(logout, 1800000); //30 minutes
    }
};


function checkOne(checkbox, chkBoxName) {
    var checkboxes = document.getElementsByName(chkBoxName);
    checkboxes.forEach((item) => {
        if (item !== checkbox)
            item.checked = false;
    });
}


function showPwd(pwd) {
    var x = document.getElementById(pwd);
    if (x.type === "password") {
        x.type = "text";
    } else {
        x.type = "password";
    }
}

function sleep(ms) {
    return new Promise(
            resolve => setTimeout(resolve, ms)
    );
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function setBack(x) {
    window.location.href = x;
}


function isLen(len, expectedLen) {
    return expectedLen == len;
}

function isLenX(len, expectedLen) {
    return len >= expectedLen;
}

function isNumber(num1) {
//    if (isNaN(num1)) {
//        return false;
//    } else {
//        return true;
//    }

    let regexPattern = /^-?[0-9]+$/;

    // check if the passed number is integer or float
    let result = regexPattern.test(num1);

    if (result) {
        return true;
    } else {
        return false;
    }

}

function formatR(str, len, ch) {

    var l = 0;
    l = str.length;

    if (l > len) {
        str = str.substring(0, len);
    } else {
        for (var i = 0; i < (len - l); i++) {
            str = str + ch;
        }
    }
    return str;
}

function formatL2(str, len, ch) {

    var l = 0;
    l = str.length;

    if (l > len) {
        str = str.substring(0, len);
    } else {
        for (var i = 0; i < (len - l); i++) {
            str = ch + str;
        }
    }
    return str;
}

function isObjectEmpty(e) {
    try {
        if (e) {
            return e;
        } else {
            return "__________";
        }
    } catch (e) {
        alertBox('Error! - ' + e, 'e');
    }
}

function searchFunction(tblName, column1, column2, searchFieldId) {
    var input, filter, table, tr, td, td1, td2, i,
            txtValue, txtValue1, txtValue2;
    input = document.getElementById(searchFieldId);
    filter = input.value.toUpperCase();
    table = document.getElementById(tblName);
    tr = table.getElementsByTagName("tr");
    for (i = 0; i < tr.length; i++) {
//        td = tr[i].getElementsByTagName("td")[1];
        td1 = tr[i].getElementsByTagName("td")[column1];
        td2 = tr[i].getElementsByTagName("td")[column2];

        if (td1 || td2) {
            txtValue1 = td1.textContent || td1.innerText;
            txtValue2 = td2.textContent || td2.innerText;

            if (txtValue1.toUpperCase().indexOf(filter) > -1
                    || txtValue2.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function initializeTableFilter(tableId, searchId) {
    const searchInput = document.getElementById(searchId);
    const dataTable = document.getElementById(tableId);
    const rows = dataTable.getElementsByTagName('tr');

    searchInput.addEventListener('keyup', function (event) {
        const searchTerm = event.target.value.toLowerCase();

        for (let i = 1; i < rows.length; i++) { // start from index 1 to skip the header row
            const cells = rows[i].getElementsByTagName('td');
            let found = false;

            for (let j = 0; j < cells.length; j++) {
                const cellValue = cells[j].textContent.toLowerCase();
                if (cellValue.includes(searchTerm)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                rows[i].style.display = ''; // show the row
            } else {
                rows[i].style.display = 'none'; // hide the row
            }
        }
    });
}


function clearTable(tbl) {
    // Get the table element
    let table = document.getElementById(tbl);

    // Get all the cells in the table
    let cells = table.getElementsByTagName("td");

    // Iterate through each cell and clear its content
    for (let i = 0; i < cells.length; i++) {
        cells[i].textContent = "";
    }
}

function removeTableRows(tbl) {
    var table = document.getElementById(tbl);
    var rowCount = table.rows.length;

    // Loop through the rows in reverse order and delete each row
    for (var i = rowCount - 1; i > 0; i--) {
        table.deleteRow(i);
    }
}

function removeEmptyTableRows(tblbody) {
    var tbody = document.getElementById(tblbody);
    var rows = tbody.getElementsByTagName("tr");

    // Loop through each row and remove empty rows
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        if (row.textContent.trim() === '') {
            tbody.removeChild(row);
        }
    }
}


function addValuetoRows(row, val) {
    var td = document.createElement("td");
    if (!val) {
        val = "";
    }
    var tdText = document.createTextNode(val);
    td.appendChild(tdText);
    row.appendChild(td);
}
function addValuetoRows(row, val, style) {
    var td = document.createElement("td");
    if (!val) {
        val = "";
    }
    var tdText = document.createTextNode(val);
    td.appendChild(tdText);
    td.style = style;
    row.appendChild(td);
}
function refresh() {
    location.reload();

}
//function exportTableToXLSX(tblName, fileName) {
//    const table = document.getElementById(tblName);
//    const wb = XLSX.utils.table_to_book(table, {sheet: "Sheet1"});
//    XLSX.writeFile(wb, fileName + '.xlsx');
//}

function exportTableToXLSX(tblName, fileName) {
    const table = document.getElementById(tblName);
    const wb = XLSX.utils.table_to_book(table, {sheet: "Sheet1"});

    // Add borders to cells to create table lines
    wb.Sheets["Sheet1"]["!rows"] = [];
    for (let R = table.rows.length - 1; R >= 0; --R) {
        let row = table.rows[R];
        for (let C = row.cells.length - 1; C >= 0; --C) {
            let cell = row.cells[C];
            if (!wb.Sheets["Sheet1"]["!rows"][R])
                wb.Sheets["Sheet1"]["!rows"][R] = {};
            wb.Sheets["Sheet1"]["!rows"][R].hpx = 24; // Adjust row height if needed
            if (!wb.Sheets["Sheet1"][XLSX.utils.encode_cell({c: C, r: R})])
                wb.Sheets["Sheet1"][XLSX.utils.encode_cell({c: C, r: R})] = {};
            wb.Sheets["Sheet1"][XLSX.utils.encode_cell({c: C, r: R})].s = {border: {top: {style: 'thin'}, left: {style: 'thin'}, bottom: {style: 'thin'}, right: {style: 'thin'}}};
        }
    }

    // Write the workbook to file
    XLSX.writeFile(wb, fileName + '.xlsx');
}

function exportTableToPDF(tableName, fileName, frameName) {
    var table = document.getElementById(tableName);
    var pdfFrame = document.getElementById(frameName);

    // Set the table content to the iframe document
    var doc = pdfFrame.contentWindow.document;
    doc.open();
    doc.write('<html><head><title>' + fileName + '</title>');
    doc.write('<style>table { border-collapse: collapse; width: 100%; }');
    doc.write('th, td { border: 1px solid #000; padding: 8px; text-align: left; }</style>');
    doc.write('<style>@page { size: landscape; }</style>'); // Set page orientation to landscape
    doc.write('</head><body>');
    doc.write('<table>');
    for (var i = 0; i < table.rows.length; i++) {
        doc.write('<tr>');
        for (var j = 0; j < table.rows[i].cells.length; j++) {
            doc.write('<td>');
            doc.write(table.rows[i].cells[j].innerHTML);
            doc.write('</td>');
        }
        doc.write('</tr>');
    }
    doc.write('</table></body></html>');
    doc.close();

    // Print the iframe content to generate PDF
    pdfFrame.contentWindow.print();

//    // Rename the downloaded file
//    var link = document.createElement('a');
//    link.setAttribute('href', pdfFrame.contentWindow.document.URL);
//    link.setAttribute('download', fileName);
//    link.style.display = 'none';
//    document.body.appendChild(link);
//    link.click();
//    document.body.removeChild(link);


}

function getCurrentDate() {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0'); // Month is zero-based
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}



