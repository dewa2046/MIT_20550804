

var GeneralPath = "http://localhost:8080/ATMDashBoardBackEnd/webresources/user/";


//Check Login
function checkLogin(arrayrecord_data) {

    var jason_data_object;

    try {

        var recordjasonobject = {"UName": arrayrecord_data[0], "Pwd": arrayrecord_data[1]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "checkLogin/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    reurnLogin(jason_data_object);
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


//Check Login
function checkAlowedFunc(arrayrecord_data) {

    var jason_data_object;

    try {

        var recordjasonobject = {"ULvl": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "checkAllowedFunc/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    reurnAllowedFunc(jason_data_object);
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

//get System Users
function getSystemUsers() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getSystemUsers/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                returnUsers(jason_data_object);
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

//get All User Functions
function getAllUserFunc() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getAllUserFunctions/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                returnAllUserFunc(jason_data_object);
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

//get User Level Function
function getUserLvlFunc(arrayrecord_data) {

    var jason_data_object;

    try {

        var recordjasonobject = {"UId": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getUserLvlFunctions/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    returnUserLvlFunction(jason_data_object);
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

//update User Function
function updateUserFunc(arrayrecord_data) {

    var jason_data_object;

    try {

        var recordjasonobject = {"UId": arrayrecord_data[0], "UFunc": arrayrecord_data[1], "UpdatedUser": arrayrecord_data[2], "UserGroupNew": arrayrecord_data[3]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "updateUserFunc/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    returnUserFunctionUpdate(jason_data_object);
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

//add New User Group
function addNewUserGroup(arrayrecord_data) {

    var jason_data_object;

    try {

        var recordjasonobject = {"UGroup": arrayrecord_data[0], "UpdatedUser": arrayrecord_data[1]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "addNewUserGroup/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    returnAddNewUserGroup(jason_data_object);
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

//update User Details
function updateUserDet(arrayrecord_data) {

    var jason_data_object;

    try {

        var recordjasonobject = {"userName": arrayrecord_data[0], "firstName": arrayrecord_data[1], "lastName": arrayrecord_data[2],
            "userLvl": arrayrecord_data[3], "userBr": arrayrecord_data[4], "userMob": arrayrecord_data[5], "userEmail": arrayrecord_data[6],
            "status": arrayrecord_data[7], "userId": arrayrecord_data[8], "updatedUser": arrayrecord_data[9]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "updateUserDetails/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    returnUserUpdateDet(jason_data_object);
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

//update User Details
//update User Details
function createNewUser(arrayrecord_data) {

    var jason_data_object;

    try {

        var recordjasonobject = {"userName": arrayrecord_data[0], "firstName": arrayrecord_data[1], "lastName": arrayrecord_data[2],
            "userLvl": arrayrecord_data[3], "userBr": arrayrecord_data[4], "userMob": arrayrecord_data[5], "userEmail": arrayrecord_data[6],
            "updateUser": arrayrecord_data[7]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "createNewUser/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    returnCreateUserDet(jason_data_object);
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

//get User Password
function getUserPassword(arrayrecord_data) {

    var jason_data_object;

    try {

        var recordjasonobject = {"UId": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getPassword/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    returnUserPassword(jason_data_object);
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

//update Password
function updatePassword(arrayrecord_data) {

    var jason_data_object;

    try {

        var recordjasonobject = {"uId": arrayrecord_data[0], "pass": arrayrecord_data[1]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "updatePassword/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    returnUpdatePassword(jason_data_object);
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

//reset Password
function resetPassword(arrayrecord_data) {

    var jason_data_object;

    try {

        var recordjasonobject = {"resetUser": arrayrecord_data[0], "resetUserBy": arrayrecord_data[1]
            ,"resetUserName":arrayrecord_data[2]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "resetPassword/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    returnResetPassword(jason_data_object);
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

//get Indicator Values
function getIndicator(arrayrecord_data) {

    var jason_data_object;

    try {

        var recordjasonobject = {"Ind": arrayrecord_data[0]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "getIndicator/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    //testsFun(jason_data_object);
                    returnIndicator(jason_data_object);
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

//Load User Level
function getUserLvl(arrayrecord_data) {

    var reqpage = arrayrecord_data[0];

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getUserLvl/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                if (reqpage == 'NewUser') {
                    loadUserLvl(jason_data_object);
                } else if (reqpage == 'EditUser') {
                    loadUserLvlEdit(jason_data_object);
                } else if (reqpage == 'ManageUserLevel') {
                    loadUserLvl_ManageUserLevel(jason_data_object);
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

////Load User Branch
//function getUserBranch(arrayrecord_data) {
//
//    var reqpage = arrayrecord_data[0];
//
//    $.ajax({
//        async: false,
//        crossDomain: true,
//        type: "GET",
//        url: GeneralPath + "getUserBranch/",
//        contentType: "application/json",
//        credentials: false,
//
//        success: function (response) {
//            jason_data_object = response;
//            var jdset = jason_data_object;
//            if (jdset !== null) {
//                if (reqpage == 'NewUser') {
//                    loadUserBranch(jason_data_object);
//                } else if (reqpage == 'EditUser') {
//                    loadUserBranchEdit(jason_data_object);
//                } else if (reqpage == 'ATMDetails') {
//                    loadBranchForATMDetails(jason_data_object);
//                }
//
//            }
//            if (jdset == null) {
//                window.alert("Something went Wrong");
//            }
//        },
//        error: function () {
//            alert('Internal Server Error!!!');
//        }
//    });
//}

//Load User Branch
function getUserBranch() {
//    var reqpage = arrayrecord_data[0];

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getUserBranch/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                loadBranch(jason_data_object);
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

//get All Branches
function getAllBranches() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getAllBranches/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                returnAllBranches(jason_data_object);
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

//get All Provinces
function getAllProvinces() {

    $.ajax({
        async: false,
        crossDomain: true,
        type: "GET",
        url: GeneralPath + "getAllProvinces/",
        contentType: "application/json",
        credentials: false,

        success: function (response) {
            jason_data_object = response;
            var jdset = jason_data_object;
            if (jdset !== null) {
                returnAllProvinces(jason_data_object);
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

//add/update Branch Details
function addUpdateBranchetails(arrayrecord_data) {

    var jason_data_object;

    try {
        var reqpage = arrayrecord_data[0];
        var recordjasonobject = {"brcode": arrayrecord_data[1], "brname": arrayrecord_data[2],
            "province": arrayrecord_data[3], "user": arrayrecord_data[4]};
        var servicerecordJSON = JSON.stringify(recordjasonobject);

        $.ajax({
            async: false,
            crossDomain: true,
            type: "POST",
            url: GeneralPath + "addUpdateBranchetails/",
            contentType: "application/json",
            credentials: false,
            data: servicerecordJSON,

            success: function (response) {
                jason_data_object = response;
                var jdset = jason_data_object;

                if (jdset !== null) {
                    if (reqpage == 'addBr') {
                        returnAddUpdateBranchetails(jason_data_object, 'add');
//                        returnAddUpdateBranchetails(jason_data_object);
                    } else if (reqpage == 'editBr') {
                        returnAddUpdateBranchetails(jason_data_object, 'edit');
//                        returnAddUpdateBranchetails(jason_data_object);
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