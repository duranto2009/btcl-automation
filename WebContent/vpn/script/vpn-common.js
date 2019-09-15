function applicationBuilder(vue) {
    let clientId = 0;
    if (vue.application.clientId > 0) {
        clientId = vue.application.clientId;
    } else if (vue.application.hasOwnProperty("client")) {
        if (vue.application.client)
            clientId = vue.application.client.key;
    }

    let skipPayment = false;
    if (typeof vue.application.skipPayment === 'object') {

        if (vue.application.skipPayment.id == 1) {
            skipPayment = true;
        } else if (vue.application.skipPayment == 0) {
            skipPayment == false;
        }
    }

    //-------------start: local office ----------------

    let localOffice = null;
    //if existing local office selected
    if (vue.application.localEnd.officeSelectionType.id == 1) {
        //if existing loop selected
        if (vue.application.localEnd.useExistingLoop.id == 1) {
            localOffice = vue.application.localEnd.office;
        }
        //if existing local loop not selected
        else {

            localOffice = vue.application.localEnd.office;
            // var loopProvider;
            // if( vue.application.localEnd.hasOwnProperty("loop")){
            //     if( vue.application.localEnd.loop!=null){
            //         loopProvider = localOffice.loop.id;
            //     }
            // }
            delete (localOffice.localLoops);



            localOffice['localLoops'] = [
                {loopProvider: vue.application.localEnd.loop.id}
            ]
        }
    }
    //if existing local office not selected
    else if (vue.application.localEnd.officeSelectionType.id == 2) {
        let officeName;
        if (vue.application.localEnd.officeName != null) {
            officeName = vue.application.localEnd.officeName;
        } else {
            officeName = '';
        }
        if(vue.picked!=null && vue.picked.description == "Submit Correction"){
            localOffice = {
                id:vue.application.localEnd.id,
                officeName: officeName,
                officeAddress: vue.application.localEnd.officeAddress,
                clientId: clientId,
                moduleId: vue.moduleId,
                localLoops:
                    [
                        {loopProvider: vue.application.localEnd.loop.id}
                    ]

            };
        }
        else {
            localOffice = {
                officeName: officeName,
                officeAddress: vue.application.localEnd.officeAddress,
                clientId: clientId,
                moduleId: vue.moduleId,
                localLoops:
                    [
                        {loopProvider: vue.application.localEnd.loop.id}
                    ]

            };
        }
    }

    //--------------end: local office ------------------
    let networkId = 0;
    if (vue.application.hasOwnProperty('linkId')) {
        networkId = vue.application.linkId;
    }


    let applicationLinks = [];
    for (i = 0; i < vue.application.remoteEnds.length; i++) {
        let remoteEnd = vue.application.remoteEnds[i];
        // if existing local office used
        // if existing loop used

        //----------------------------------start: remote office -------------------------

        let remoteOffice = null;
        //if existing remote office selected
        if (remoteEnd.officeSelectionType.id == 1) {
            //if existing loop selected
            if (remoteEnd.useExistingLoop.id == 1) {
                remoteOffice = remoteEnd.office;
            }
            //if existing remote loop not selected
            else {

                remoteOffice = remoteEnd.office;
                delete (remoteOffice.localLoops);
                remoteOffice['localLoops'] = [
                    {
                        loopProvider: remoteEnd.loop.id
                    }
                ]
            }
        }
        //if existing office not selected
        else if (remoteEnd.officeSelectionType.id == 2) {
            let officeName;
            if (remoteEnd.officeName != null) {
                officeName = remoteEnd.officeName;
            } else {
                officeName = '';
            }
            if(vue.picked!=null && vue.picked.description == "Submit Correction"){
                remoteOffice = {
                    id:remoteEnd.id,
                    officeName: officeName,
                    officeAddress: remoteEnd.officeAddress,
                    clientId: clientId,
                    moduleId: vue.moduleId,
                    localLoops:
                        [
                            {
                                loopProvider: remoteEnd.loop.id
                            }
                        ]
                };
            }
            else {
                remoteOffice = {
                    officeName: officeName,
                    officeAddress: remoteEnd.officeAddress,
                    clientId: clientId,
                    moduleId: vue.moduleId,
                    localLoops:
                        [
                            {
                                loopProvider: remoteEnd.loop.id
                            }
                        ]
                };
            }
        }

        //--------------------------end: remote office ---------------------------------
        if(vue.picked!=null && vue.picked.description == "Submit Correction"){
            applicationLinks.push({
                id:vue.application.vpnApplicationLinks[i].id,
                networkLinkId: networkId,
                linkBandwidth: remoteEnd.bandwidth,
                localOfficeId:vue.application.vpnApplicationLinks[i].localOfficeId,
                localOfficeLoopId:vue.application.vpnApplicationLinks[i].localOfficeLoopId,
                remoteOfficeId:vue.application.vpnApplicationLinks[i].remoteOfficeId,
                remoteOfficeLoopId:vue.application.vpnApplicationLinks[i].remoteOfficeLoopId,
                localZoneId:vue.application.vpnApplicationLinks[i].localZoneId,
                remoteZoneId:vue.application.vpnApplicationLinks[i].remoteZoneId,
                vpnApplicationId:vue.application.vpnApplicationLinks[i].vpnApplicationId,
                linkName:vue.application.vpnApplicationLinks[i].linkName,
                linkState:vue.picked.name,
                skipPayment: skipPayment, //TODO change if needed
                localOffice: localOffice,
                remoteOffice: remoteOffice,
                localOfficeTerminalDeviceProvider: vue.application.localEnd.terminalDeviceProvider.id,
                remoteOfficeTerminalDeviceProvider: remoteEnd.terminalDeviceProvider.id,

            });
        }
        else {
            applicationLinks.push({
                networkLinkId: networkId,
                linkBandwidth: remoteEnd.bandwidth,
                skipPayment: skipPayment, //TODO change if needed
                localOffice: localOffice,
                remoteOffice: remoteOffice,
                localOfficeTerminalDeviceProvider: vue.application.localEnd.terminalDeviceProvider.id,
                remoteOfficeTerminalDeviceProvider: remoteEnd.terminalDeviceProvider.id,

            });
        }
    }

    let userId;
    if (isClientLoggedIn) {
        userId = 0;
    } else {
        userId = loggedInAccount.ID;
    }

    let layerType =0;
    if(vue.picked!=null && vue.picked.description == "Submit Correction"){
        layerType =  vue.application.layerType;
    }
    else{

        if(vue.application.hasOwnProperty("layerType")){

            layerType = vue.application.layerType.id;

        }else{
            layerType=0;
        }
    }

    let app = {

        clientId: clientId,

        userId: userId,

        moduleId: vue.moduleId,

        suggestedDate: vue.application.suggestedDate,

        comment: vue.application.comment,

        // description: this.application.description,

         layerType: layerType,

        skipPayment: skipPayment,

        vpnApplicationLinks: applicationLinks,
    };
    return app;
}

function getDataListByURL(url) {
    return axios.get(url)
        .then(result => {
            if (result.data.responseCode == 1) {
                // this.officeList =
                return result.data.payload;
            }
            return [];
            // debugger;
        })
        .catch(function (error) {
        });

}

function getVPNOfficeByClientId(clientId) {
    let url = context + 'vpn/link/get-office-by-client.do?clientId=' + clientId;
    return getDataListByURL(url);
}

function deleteFromRepeater(obj, property, Index) {
    obj[property].splice(Index, 1);
}

function getVPNNetworkLinksByClientId(clientId) {
    let url = context + 'vpn/network/get-all-by-client.do?id=' + clientId;
    return getDataListByURL(url);
}

function submitBandwidthChangeForm(vue) {
    if (vue.application.hasOwnProperty("vpnApplicationLinks")) {
        vue.application.vpnApplicationLinks[0].linkBandwidth = vue.changedBandwidth;
        let app = vue.application;
        submitData(app, vue.link.linkBandwidth);
        return;

    }
    let userId;
    if (isClientLoggedIn) {
        userId = 0;
    } else {
        userId = loggedInAccount.ID;
    }


    let app = {

        clientId: vue.application.client.key,

        userId: userId,

        moduleId: vue.moduleId,

        suggestedDate: vue.application.suggestedDate,

        comment: vue.application.comment,


        // description: this.application.description,

        // layerType: vue.application.layerType.id,

        // skipPayment: skipPayment,

        vpnApplicationLinks: [{
            networkLinkId: vue.link.id,
            linkBandwidth: vue.changedBandwidth,
            linkName: vue.link.linkName,
            localOfficeId: vue.link.localOfficeId,
            remoteOfficeId: vue.link.remoteOfficeId,
            localOfficeTerminalDeviceProvider: vue.link.localOfficeTerminalDeviceProvider,
            remoteOfficeTerminalDeviceProvider: vue.link.remoteOfficeTerminalDeviceProvider,
            localZoneId: vue.link.localZoneId,
            remoteZoneId: vue.link.remoteZoneId,
            localOfficeLoopId: vue.link.localEndOffice.localLoops[0].id,
            localOffice: vue.link.localEndOffice,
            remoteOfficeLoopId: vue.link.remoteEndOffice.localLoops[0].id,
            remoteOffice: vue.link.remoteEndOffice,


        }],
    };
    submitData(app, vue.link.linkBandwidth);
}

const submitData = (app, linkBW) => {
    let url = "bandwidth-change";
    axios.post(context + 'vpn/link/' + url + '.do', {
        // 'application': JSON.stringify(this.application)
        application: app,
        previousBandwidth: linkBW
    }).then(result => {
        // debugger;
        if (result.data.responseCode == 2) {
            toastr.error(result.data.msg);
        } else if (result.data.responseCode == 1) {
            toastr.success("Your request has been processed", "Success");
            toastr.options.timeOut = 2500;
            //redirect to specific page
            window.location.href = context + 'vpn/link/search.do';
        } else {
            toastr.error("Your request was not accepted", "Failure");
            // window.location.href = context + 'vpn/link/add.do';
            toastr.options.timeOut = 2500;

        }
    }).catch((err) => LOG(err));
};

function officeValidate(office, officeName) {
    //existing office
    if (office.officeSelectionType.id == 1) {

    }

    //new office create
    else if (office.officeSelectionType.id == 2) {
        //office name validate
        if (office.officeName == null) {
            errorMessage("Office Name must be provided for " + officeName);
            return false;
        }

        if (office.officeAddress == null) {
            errorMessage("Office Address must be provided for " + officeName);
            return false;
        }
        if (office.loop == null) {
            errorMessage("Loop Provider must be selected for " + officeName);
            return false;
        }
        if (office.terminalDeviceProvider == null) {
            errorMessage("Terminal Device Provider must be selected for " + officeName);
            return false;
        }

    }

    return true;
}

function validateVPNNewLinkApplication(vue) {
    // start: form validation
    if (vue.application.client == null) {
        errorMessage("Client must be selected.");
        return false;
    }
    if (vue.application.layerType == null||vue.application.layerType == 0) {
        errorMessage("Layer Type must be selected.");
        return false;
    }
    if (vue.application.suggestedDate == null) {
        errorMessage("Please provide a suggested date.");
        return false;
    }

    //local office end validate
    if (officeValidate(vue.application.localEnd, "Local End Office ") == false) {
        return false;
    }

    //remote office end validate
    for (i = 0; i < vue.application.remoteEnds.length; i++) {
        if (officeValidate(vue.application.remoteEnds[i], "Remote End Office "+ ( i+1)) == false)
            return false;
    }

    //bandwidth validation
    if (vue.application.remoteEnds.some(r => !r.hasOwnProperty("bandwidth"))) {
        errorMessage("Bandwidth must be provided.");
        return false;

    }

    // end: form validation'
    return true;
}

function postRequestedDataToServer(url, postObject, redirect) {
    this.loading = true;
    axios.post(context + 'vpn/link/' + url + '.do', postObject)
        .then(result => {
            // vue.loading = false;
            if (result.data.responseCode == 2) {
                toastr.error(result.data.msg);
                // window.location.reload(true);
            } else if (result.data.responseCode == 1) {
                // let redirect = this.redirectURLBuilder();
                toastr.success("Your request has been processed", "Success");
                window.location.href = redirect;
                // window.location.href = context + redirect;
            } else {
                toastr.error("Your request was not accepted", "Failure");
            }
            // this.loading = false;
        }).catch(function (error) {
    });
}