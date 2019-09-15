const IPv4Regex = /^(?=\d+\.\d+\.\d+\.\d+$)(?:(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.?){4}$/;
const IPv6Regex = /^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))\s*$/;
export const ip2int=function(ip) {
    return ip.split('.').reduce(function(ipInt, octet) { return (ipInt<<8) + parseInt(octet, 10)}, 0) >>> 0;
};
const int2ip=function(ipInt) {
    return ( (ipInt>>>24) +'.' + (ipInt>>16 & 255) +'.' + (ipInt>>8 & 255) +'.' + (ipInt & 255) );
};

const isValidIPAddress = function (address, version) {
    address.trim().replace(/\s/g, "");
    if("ipv4" === version.toLowerCase()){
        return IPv4Regex.test(address);
    }else {
        return IPv6Regex.test(address);
    }
};

const checkIPAddress= function(from, to, regex) {

    if(!regex.test(from)){
        toastr.error("from IP is not a valid IP Address");
        return false;
    }
    if(!regex.test(to)){
        toastr.error("to IP is not a valid IP Address");
        return false;
    }
    return true;

};


const checkRange= function (from , to){
    //only IPv4 are handled currently.
    let fromBigInt = ip2int(from);
    let toBigInt = ip2int(to);
    if(fromBigInt > toBigInt) {
        toastr.error("From IP Address is greater than To IP Address");
        return false;
    }
    return true;
};

const checkVersion= function (version){
    if(!( version === "IPv4" || version === "IPv6")){
        toastr.error("Please Select Version");
        return false;
    }
    return true;
};
export const validate = function (from, to, version) {
    from.trim().replace(/\s/g,"");
    to.trim().replace(/\s/g,"");
    return  checkVersion(version) &&
                (version==="IPv4"?checkIPAddress(from, to, IPv4Regex):checkIPAddress(from, to, IPv6Regex)) &&
                    checkRange(from, to);

};

const privateRange = [
    {
        'from' : '10.0.0.0',
        'to' : '10.255.255.255'
    },
    {
        'from' : '172.16.0.0',
        'to' : '172.31.255.255'
    },
    {
        'from' : "192.168.0.0",
        'to' : '192.168.255.255'
    }

];
const privateChecker = (block, fromInt, toInt)=>{

};
const checkIsPrivate = (fromIP, toIP) =>{
    let fromInt = ip2int(fromIP);
    let toInt = ip2int(toIP);
    if( privateRange.some( block=>{
        return ip2int(block.from)<=fromInt && fromInt<=ip2int(block.to)
            &&
            ip2int(block.from)<=toInt && toInt<=ip2int(block.to)
        } ) 

    ){
        return true;
    }else {
        toastr.error("The Provide ip range is not a valid private ip range", "Failure");
        return false;
    }
}