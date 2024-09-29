
var GeneralPath = "http://localhost:8080/ATMDashBoardBackEnd/webresources/services/";
var GeneralPath1 = "http://localhost:8080/ATMDashBoardBackEnd/webresources/user/";


//get ATM Models
function getATMModels() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getATMModels/",
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

//Load BranchDetails
function getBranchDetails() {
//    var reqpage = arrayrecord_data[0];

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath1 + "getUserBranch/",
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

//get ATM Venders
function getATMVenders() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getATMVenders/",
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

//add/update ATM information
function addUpdateATMDetails(arrayrecord_data) {

    try {
        var recordjasonobject = {"atmname": arrayrecord_data[0], "brcode": arrayrecord_data[1], "location": arrayrecord_data[2],
            "ip": arrayrecord_data[3], "os": arrayrecord_data[4], "model": arrayrecord_data[5], "vender": arrayrecord_data[6], "installedDate": arrayrecord_data[7],
            "divtype": arrayrecord_data[8], "serial": arrayrecord_data[9], "emv": arrayrecord_data[10], "recycler": arrayrecord_data[11],
            "status": arrayrecord_data[12], "remote": arrayrecord_data[13], "overseas": arrayrecord_data[14], "uid": arrayrecord_data[15]};

        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "addUpdateATMDetails/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    loadResults(jason_data_object);
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

//get ATM Information
function getATMDetails(arrayrecord_data) {

    try {
        var recordjasonobject = {"ATM": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getATMDetails/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    loadATMDetails(jason_data_object);
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

//get All ATM Details
function getAllATMDetails(arrayrecord_data) {

    try {
        var recordjasonobject = {"UBr": arrayrecord_data[0], "ULvl": arrayrecord_data[1]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getAllATMDetails/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    loadAllATMDetails(jason_data_object);
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

//get ATM Contact Details
function getATMContactDetails(arrayrecord_data) {

    try {
        var recordjasonobject = {"ATM": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getATMContactDetails/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    loadATMContactDetails(jason_data_object);
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

//add/update ATM information
function addUpdateATMContacts(arrayrecord_data) {

    try {
        var recordjasonobject = {"atmname": arrayrecord_data[0],
            "pf1": arrayrecord_data[1], "off1": arrayrecord_data[2], "grade1": arrayrecord_data[3], "mob1": arrayrecord_data[4], "ext1": arrayrecord_data[5],
            "pf2": arrayrecord_data[6], "off2": arrayrecord_data[7], "grade2": arrayrecord_data[8], "mob2": arrayrecord_data[9], "ext2": arrayrecord_data[10],
            "pf3": arrayrecord_data[11], "off3": arrayrecord_data[12], "grade3": arrayrecord_data[13], "mob3": arrayrecord_data[14], "ext3": arrayrecord_data[15],
            "uid": arrayrecord_data[16]};

        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "addUpdateATMContacts/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    loadAddUpdateATMContacts(jason_data_object);
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
//get ATM Status
function getATMStatus(arrayrecord_data) {

    try {
        var recordjasonobject = {"UBr": arrayrecord_data[0], "ULvl": arrayrecord_data[1]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getATMStatus/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    loadATMStatus(jason_data_object);
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

//get DashBoard 1 and 2 details
function getDashboard_1_2() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getDashBoardDetail_1_2/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadDashboardDetails_1_2(jason_data_object);
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

//get Last ATM status updated date
function getATMUpdatedTime() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getATMUpdatedTime/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadATMUpdatedTime(jason_data_object);
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

//Load ATM List
function getATMDeviceList() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getATMDeviceList/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadATMDeviceList(jason_data_object);
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

//get Dashboard4 Details
function getDashboard_4() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getDashboard_4/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadDashboardDetails_4(jason_data_object);
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

//get Dash board list separatly
function getDashboardSeparate(qry) {

    try {
        var recordjasonobject = {"Qry": qry};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
//        window.alert(arrayrecord_data[1]);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getDashboardSeparate/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    loadDashboardSep(jason_data_object);
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

//get Dash board list separatly 3
function getDashboardSeparate_3(qry) {

    try {
        var recordjasonobject = {"Qry": qry};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
//        window.alert(arrayrecord_data[1]);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getDashboardSeparate_3/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    loadDashboardSep_3(jason_data_object);
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

//get All Fault Types
function getFaultAllTypes(type) {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getFaultAllTypes/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                if (type == "INI") {
                    loadFaultAllTypes(jason_data_object);
                } else if (type == "ADD") {
                    loadFaultAllTypesADD(jason_data_object);
                }

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

//get Fault Type Individually
function getFaultTypeDetails(arrayrecord_data) {

    var jason_data_object;

    try {

        var recordjasonobject = {"FId": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getFaultTypeDetails/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    loadFaultTypeDetails(jason_data_object);
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

//Update Fault Details
function updateFaultDetails(arrayrecord_data) {

    var jason_data_object;

    try {
        var recordjasonobject = {"farID": arrayrecord_data[0], "farDesc": arrayrecord_data[1],
            "farStatus": arrayrecord_data[2], "user": arrayrecord_data[3]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "updateFaultDetails/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    returnUpdateFaultDetails(jason_data_object);
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

//Update Fault Details
function addFaultDetails(arrayrecord_data) {

    var jason_data_object;

    try {
        var recordjasonobject = {"farDesc": arrayrecord_data[0], "faultCat": arrayrecord_data[1], "user": arrayrecord_data[2]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "addFaultDetails/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    returnAddFaultDetails(jason_data_object);
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

//get ATM Details for ATM Faults
function getATMforFaults() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getATMforFaults/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadATMforFaults(jason_data_object);
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
function getFaults() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getFaults/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadFaults(jason_data_object);
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

//Add New Fault Entry
function addFaultEntry(arrayrecord_data) {

    var jason_data_object;

    try {
        var recordjasonobject = {"atm": arrayrecord_data[0], "fault": arrayrecord_data[1],
            "action": arrayrecord_data[2], "result": arrayrecord_data[3],
            "remark": arrayrecord_data[4], "user": arrayrecord_data[5]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "addFaultEntry/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    returnAddFaultEntry(jason_data_object);
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

//get create entry
function getEntryCreated(arrayrecord_data) {

    try {
        var recordjasonobject = {"Type": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
//        window.alert(arrayrecord_data[1]);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getEntry/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    if (arrayrecord_data[0] == "ALL") {
                        loadAllEntry(jason_data_object);
                    } else {
                        loadEntryATM(jason_data_object);
                    }

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

//get ATM Cash Details
function getCashDetails(arrayrecord_data) {

    try {
        var recordjasonobject = {"ATM": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getCashDetails/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    loadCashDetails(jason_data_object);
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

//get ATM SMS Details
function getSMSDetails(arrayrecord_data) {

    try {
        var recordjasonobject = {"ATM": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);
        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getSMSDetails/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    loadSMSDetails(jason_data_object);
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

