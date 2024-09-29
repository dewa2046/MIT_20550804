
var GeneralPathReports = "http://localhost:8080/ATMDashBoardBackEnd/webresources/reports/";
var GeneralPathUser = "http://localhost:8080/ATMDashBoardBackEnd/webresources/user/";
var GeneralPathServices = "http://localhost:8080/ATMDashBoardBackEnd/webresources/services/";

//Load BranchDetails
function getBranchDetails() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPathUser + "getUserBranch/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadBranchDetails(jason_data_object);
            }
            if (jdset == null) {
                window.alert("Something went Wrong");
            }
        },
        error: function () {
            alert('Internal Server Error!!!');
        }
    });
}

//Load Province Details
function getProvinceDetails() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPathReports + "getProvinceDetails/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadProvinceDetails(jason_data_object);
            }
            if (jdset == null) {
                window.alert("Something went Wrong");
            }
        },
        error: function () {
            alert('Internal Server Error!!!');
        }
    });
}

//get ATM Venders
function getATMVenders() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPathServices + "getATMVenders/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadATMVenders(jason_data_object);
            }
            if (jdset == null) {
                window.alert("Something went Wrong");
            }
        },
        error: function () {
            alert('Internal Server Error!!!');
        }
    });
}

//get ATM Models
function getATMModels() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPathServices + "getATMModels/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadATMModels(jason_data_object);
            }
            if (jdset == null) {
                window.alert("Something went Wrong");
            }
        },
        error: function () {
            alert('Internal Server Error!!!');
        }
    });
}

//get ATM Information Report
function getATMInfoReport(arrayrecord_data) {

    try {
        var recordjasonobject = {"QRY": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPathReports + "getATMInfoReport/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    loadATMInfoReport(jason_data_object);
                } else
                    window.alert("Something went Wrong");
            },
            error: function () {
                alert('Internal Server Error!!!');
            }
        });

    } catch (e) {
        alert(e);
    }
}

//get All Down Report 
function getAllDownReport(arrayrecord_data) {

    try {
        var recordjasonobject = {"fromDate": arrayrecord_data[0], "toDate": arrayrecord_data[1],
            "uLvl": arrayrecord_data[2], "uBr": arrayrecord_data[3]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPathReports + "getAllDownReport/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    loadAllDownReport(jason_data_object);
                } else
                    window.alert("Something went Wrong");
            },
            error: function () {
                alert('Internal Server Error!!!');
            }
        });

    } catch (e) {
        alert(e);
    }

}

//get All Down Report Graph
function getAllDownGraph(arrayrecord_data) {

    try {
        var recordjasonobject = {"fromDate": arrayrecord_data[0], "toDate": arrayrecord_data[1],
            "uLvl": arrayrecord_data[2], "uBr": arrayrecord_data[3]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPathReports + "getDownTimeFaultTypeWise/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    loadAllDownGraph(jason_data_object);
                } else
                    window.alert("Something went Wrong");
            },
            error: function () {
                alert('Internal Server Error!!!');
            }
        });

    } catch (e) {
        alert(e);
    }

}

//get Down Report Graph Individual
function getDownTimeSeparate(arrayrecord_data) {

    try {
        var recordjasonobject = {"fromDate": arrayrecord_data[0], "toDate": arrayrecord_data[1],
            "option": arrayrecord_data[2], "uLvl": arrayrecord_data[3], "uBr": arrayrecord_data[4]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPathReports + "getDownTimeIndividual/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    loadDownTimeSeparate(jason_data_object);
                } else
                    window.alert("Something went Wrong");
            },
            error: function () {
                alert('Internal Server Error!!!');
            }
        });

    } catch (e) {
        alert(e);
    }

}

//get ATM Details for ATM Faults Report
function getATMforFaultsReport() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPathServices + "getATMforFaults/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadATMforFaultsReport(jason_data_object);
            }
            if (jdset == null) {
                window.alert("Something went Wrong");
            }
        },
        error: function () {
            alert('Internal Server Error!!!');
        }
    });
}

//get Faults
function getFaultsforReports() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPathServices + "getFaults/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadFaultsForReports(jason_data_object);
            }
            if (jdset == null) {
                window.alert("Something went Wrong");
            }
        },
        error: function () {
            alert('Internal Server Error!!!');
        }
    });
}

//get Fault Report Users
function getFaultReportUsers() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPathReports + "getFaultReportUsers/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadFaultReportUsers(jason_data_object);
            }
            if (jdset == null) {
                window.alert("Something went Wrong");
            }
        },
        error: function () {
            alert('Internal Server Error!!!');
        }
    });
}

//get ATM Fault Report
function getATMFaultReport(arrayrecord_data) {

    try {
        var recordjasonobject = {"QRY": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPathReports + "getATMFaultReport/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    loadATMFaultReport(jason_data_object);
                } else
                    window.alert("Something went Wrong");
            },
            error: function () {
                alert('Internal Server Error!!!');
            }
        });

    } catch (e) {
        alert(e);
    }
}

function getCashLow(arrayrecord_data) {

    try {
        var recordjasonobject = {"LIMIT": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPathReports + "getCashLow/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    loadgetCashLow(jason_data_object);
                } else
                    window.alert("Something went Wrong");
            },
            error: function () {
                alert('Internal Server Error!!!');
            }
        });

    } catch (e) {
        alert(e);
    }

}