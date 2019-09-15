var vue = new Vue({
	el:"#btcl-application",
	data:{
		address: '',
		netmask:'',
		subnet:'',
		privateRange : [
			{
				'from' : '10.0.0.0',
				'to' : '10.255.255.255'
			},
			{
				'from' : '172.16.0.0',
				'to' : '172.31.255.255'
			},
			{
				'from' : '192.168.0.0',
				'to' : '192.168.255.255'
			}
			
		],
		subnetList:[],
		errorMsg: "Please Enter valid IP Address in (X.X.X.X) format and Netmask value (0-32)",
	},
	methods:{
		getIpInt:function(){
			return this.ip2int(this.address);
		},
		getSubnetInt:function() {
			return parseInt(this.netmask,10);
		},
		getMask:function(){
			return ((1<<32-this.getSubnetInt())-1 )>>>0;
		},
		getXORElement:function(){
			return (this.netmask ===  '0') ? -1 : this.getMask();
		},
		getSubnetAddress:function(){
			const xorElement = this.getXORElement();
			return this.int2ip(-1 ^ xorElement);
		},
		getNetworkAddress:function(){
			const ipInt = this.getIpInt();
			const subnetIP = this.getSubnetAddress();
			const networkInt = this.ip2int(subnetIP);
			return this.int2ip( ipInt & networkInt );
		},
		getBroadcastAddress:function(){
			const mask = this.getMask();
			const networkAddressInt = this.ip2int(this.getNetworkAddress());
			return this.int2ip( networkAddressInt | mask );
		},
		getTotalHosts:function() {
			return this.getMask() + 1;
		},
		privateChecker:function(pvtIP){
			const ipInt = this.getIpInt(); 
			return this.ip2int(pvtIP.from)<=ipInt && ipInt<=this.ip2int(pvtIP.to)
		},
		getType:function(){
			if( this.privateRange.some( this.privateChecker)){
				return "Private";
			} 
			return "Public";
		},
		getIPClass:function(){
			const firstOctet = parseInt(this.address.split(".")[0],10);
			return ipClass = 
			((1 <= firstOctet && firstOctet < 128) ? "class A" :
				((128 <= firstOctet && firstOctet < 192) ? "class B" :
					((192 <= firstOctet && firstOctet < 224) ? "class C" :
						((224 <= firstOctet && firstOctet < 240) ? "class D" :
							((240 <= firstOctet && firstOctet < 255) ? "class E" : "undefined class")
						)
					)
				)
			);
		},
		
		lowerThanNetmask:function(val){
			return val<this.getSubnetInt();
		},
		produceSubnet:function(value){
			
			const actualNetworkAddress = this.ip2int(this.getNetworkAddress());
			const start = this.getSubnetInt();
			const end = parseInt(value, 10);
			this.subnetList.push("# of /" + value + " subnet: " + Math.pow(2, end-start) + " Showing first (1000)");
			for(var i=0;i<Math.pow(2, (end-start));i++){
				if(i>999){break;}
				const val = i<<(32-end);
				this.subnetList.push((i+1)+": "+this.int2ip(actualNetworkAddress|val) +"/" + value);
			}
			this.subnetList.push("--------------------------------------------------");
			
		},
		showSubnetList:function(){
			this.subnetList=[];
			this.subnet = this.subnet.trim().replace(/\s/g, "");
			subnetList = this.subnet.split(",");
			if(subnetList.some(this.lowerThanNetmask)){
				toastr.error("Please Provide a value which is greater than netmask");
				return;
			}
			subnetList.forEach(this.produceSubnet);
		},
		ip2int:function(ip) {
		    return ip.split('.').reduce(function(ipInt, octet) { return (ipInt<<8) + parseInt(octet, 10)}, 0) >>> 0;
		},
		int2ip:function(ipInt) {
		    return ( (ipInt>>>24) +'.' + (ipInt>>16 & 255) +'.' + (ipInt>>8 & 255) +'.' + (ipInt & 255) );
		},
		validate: function () {
			return this.validateAddress() && this.validateNetmask();
		},
		validateAddress: function() {
			this.address = this.address.trim().replace(/\s/g, "");
			const regex =  /^(?=\d+\.\d+\.\d+\.\d+$)(?:(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.?){4}$/;
			if(!regex.test(this.address)){
//				toastr.error("Enter a valid IP Address in X.X.X.X format");
				return false;
			}
			return true;
		},
		validateNetmask : function() {
			this.netmask = this.netmask.trim().replace(/\s/g, "");
			const regex = /^(3[012]|[12][0-9]|[0-9])$/
			if(!regex.test(this.netmask)){
//				toastr.error("Enter a valid Netmask i.e. (0-32)");
				return false;
			}
			return true;
		},
	},
	
	computed:{
		subnetMask:function(){
			if(!this.validate()){
				return this.errorMsg;
			}
			return this.getSubnetAddress();
		},
		networkAddress:function(){
			if(!this.validate()){
				return this.errorMsg;
			}
			return this.getNetworkAddress()  + "/" + this.getSubnetInt();
		},
		broadcastAddress:function(){
			if(!this.validate()){
				return this.errorMsg;
			}
			return this.getBroadcastAddress();
		},
		numberOfHosts:function(){
			if(!this.validate()){
				return this.errorMsg;
			}
			return this.getTotalHosts();
		},
		type: function(){
			if(!this.validate()){
				return this.errorMsg;
			}
			return this.getType();
		},
		ipClass:function() {
			if(!this.validate()){
				return this.errorMsg;
			}
			return this.getIPClass(); 
		},
	},
});
