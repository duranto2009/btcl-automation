var vue = new Vue({
	el:"#btcl-application",
	data:{
		ipv6address:'',
		ipv6compressedAddress:'',
		ipv6expandedAddress:'',
		prefix:'',
		range:'',
		slash64:'',
		requiredSubnets: '',
		subnetted:'',
		subnettedList:[],
		requiredSlashes:'',
		slashed:'',
		slashedList:[]
	},
	methods: {
		ipv6calc:function(){
			var a = this.ipv6address;
		    a = a.replace(/\s/g, "");
		    var c = a.split("/");
		    a = c[0];
		    c = c[1];
		    if (!this.checkipv6(a)) {
		        toastr.error("This does not look like a valid IPv6 address");
		        return;
		    }
		    a = this.formatipv6preferred(a);
		    this.ipv6compressedAddress = a + "/" + c;
		    var b = this.expand(a);
		    this.ipv6expandedAddress = b + "/" + c;
		    this.slash64= Math.pow(2, 64 - c);
		    var d = this.findprefix(c) + "::";
		    d = this.expand(d);
		    this.prefix = d;
		    var e = this.bitand(d, b);
		    this.range = e + " - " + this.last(d, e);
		},
		subnet_subnet: function () {
			this.subnettedList = [];
		    var b = this.requiredSubnets;
		    var l = this.ipv6address;
		    l = l.replace(/\s/g, "");
		    var d = l.split("/");
		    l = this.expand(d[0]);
		    d = d[1];
		    if (!this.checkipv6(l)) {
		        toastr.error('This looks like an invalid IPv6 address');
		        return;
		    }
		    if (this.splitnewslash(d, b) == false) {
		        toastr.error("Looks like the given IPv6 network cannot be split into this many new subnets");
		        return;
		    }
		    var m = this.splitnewslash(d, b);
		    var k = Math.pow(2, m - d);
		    var h = this.findprefix(d) + "::";
		    h = this.expand(h);
		    var g = this.bitand(h, l);
		    g = this.expand(g);
		    var c = g.replace(/:/g, "");
		    c = bigInt(c, 16);
		    var f = bigInt("2", 10).pow(128 - m);
		    var a = new Array;
		    a[0] = c.toString(16);
		    for (var e = 1; e < k; e++) {
		        a[e] = c.add(f).toString(16);
		        c = c.add(f);
		        if (e > 999) {
		            break;
		        }
		    }
		    var n = "To get at least " + b + " new subnets divide " + 
		    		this.formatipv6preferred(l) + "/" + d + " into " + k + 
		    		" new subnets. Each of these subnets is a /" + m + 
		    		" containing " + Math.pow(2, (64 - m)) + 
		    		" /64s. The new subnets are as follows:";
		    for (var e = 0; e < k; e++) {
		        this.subnettedList.push( this.formatipv6preferred(this.biginttoipv6(a[e])) + "/" + m);
		        if (e > 999) {
		            break;
		        }
		    }
		    
		    this.subnetted = n;
		},
		subnet_slashes: function () {
			this.slashedList = [];
		    var l = this.requiredSlashes;
		    var k = this.ipv6address;
		    k = k.replace(/\s/g, "");
		    var d = k.split("/");
		    k = this.expand(d[0]);
		    d = parseInt(d[1]);
		    if (!this.checkipv6(k)) {
		        toastr.error('This looks like an invalid IPv6 address');
		        return;
		    }
		    var b = Math.pow(2, l - d);
		    if (l < d) {
		        toastr.error("Make sure the selected slashes fit into the given network. " +
		        		"The selected slash should have a larger numeric value than the original network's slash");
		        return;
		    }
		    var h = this.findprefix(d) + "::";
		    h = this.expand(h);
		    var f = this.bitand(h, k);
		    f = this.expand(f);
		    var c = f.replace(/:/g, "");
		    c = bigInt(c, 16);
		    var g = bigInt("2", 10).pow(128 - l);
		    var a = new Array;
		    a[0] = c.toString(16);
		    for (var e = 1; e < b; e++) {
		        a[e] = c.add(g).toString(16);
		        c = c.add(g);
		        if (e > 999) {
		            break;
		        }
		    }
		    var m = "Subnetting " + this.formatipv6preferred(k) + "/" + d + " into /" + l + "s gives " 
		    		+ b + " subnets, all of which have " + Math.pow(2, 64 - l) + " /64s.";
		    for (var e = 0; e < b; e++) {
		        this.slashedList.push(this.formatipv6preferred(this.biginttoipv6(a[e])) + "/" + l);
		        if (e > 999) {
		            break;
		        }
		    }
		    this.slashed = m;
		},
		checkipv6: function(a) {
		    return (/^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))\s*$/.test(a))
		},
		biginttoipv6: function (b) {
		    var a = [];
		    var c;
		    for (var c = 0; c < 8; c++) {
		        a.push(b.slice(c * 4, (c + 1) * 4))
		    }
		    return a.join(":")
		},
		splitnewslash:function (c, d) {
		    var a = 0;
		    for (var b = 0; b < (128 - c); b++) {
		        a = Math.pow(2, b);
		        if (a >= d) {
		            return parseInt(c) + b
		        }
		    }
		    return false
		},
		formatipv6preferred:function (e) {
		    var d;
		    var b = "Not a valid IPv6 Address";
		    var c;
		    var a;
		    if (this.checkipv6(e)) {
		        d = e.toLowerCase();
		        c = d.split(":");
		        this.trimcolonsfromends(c);
		        this.fillemptysegment(c);
		        this.stripleadingzeroes(c);
		        this.removeconsecutivezeroes(c);
		        b = this.assemblebestrepresentation(c)
		    }
		    return b;
		},
		expand: function(k) {
		    var a = "";
		    var h = "";
		    var e = 8;
		    var b = 4;
		    if (k.indexOf("::") == -1) {
		        a = k
		    } else {
		        var d = k.split("::");
		        var g = 0;
		        for (var f = 0; f < d.length; f++) {
		            g += d[f].split(":").length
		        }
		        a += d[0] + ":";
		        for (var f = 0; f < e - g; f++) {
		            a += "0000:"
		        }
		        a += d[1]
		    }
		    var c = a.split(":");
		    for (var f = 0; f < e; f++) {
		        while (c[f].length < b) {
		            c[f] = "0" + c[f]
		        }
		        h += (f != e - 1) ? c[f] + ":" : c[f]
		    }
		    return h;
		},
		findprefix: function (b) {
		    var c = b;
		    var d = "";
		    for (var a = 0; a < c; a++) {
		        d += "1";
		        if ((a + 1) % 16 == 0) {
		            d += ":"
		        }
		    }
		    d = d.split(":");
		    while (d[d.length - 1].length < 16) {
		        d[d.length - 1] += "0"
		    }
		    for (var a = 0; a < d.length; a++) {
		        d[a] = parseInt(d[a], 2);
		        d[a] = d[a].toString(16)
		    }
		    return d.join(":");
		    console.log(d)
		},
		last: function (b, c) {
		    c = c.split(":");
		    b = b.split(":");
		    anded = new Array;
		    for (var a = 0; a < 8; a++) {
		        c[a] = parseInt(c[a], 16);
		        b[a] = parseInt(b[a], 16);
		        b[a] = b[a] ^ 65535;
		        anded[a] = b[a] ^ c[a];
		        anded[a] = anded[a].toString(16)
		    }
		    return anded.join(":")
		},
		bitand: function (c, a) {
		    c = c.split(":");
		    a = a.split(":");
		    anded = new Array;
		    for (var b = 0; b < 8; b++) {
		        c[b] = parseInt(c[b], 16);
		        a[b] = parseInt(a[b], 16);
		        anded[b] = c[b] & a[b];
		        anded[b] = anded[b].toString(16)
		    }
		    return anded.join(":")
		},
		assemblebestrepresentation: function(c) {
		    var a = "";
		    var b = c.length;
		    if (c[0] == "") {
		        a = ":"
		    }
		    for (i = 0; i < b; i++) {
		        a = a + c[i];
		        if (i == b - 1) {
		            break
		        }
		        a = a + ":"
		    }
		    if (c[b - 1] == "") {
		        a = a + ":"
		    }
		    return a
		},
		removeconsecutivezeroes:function (d) {
		    var a = -1;
		    var f = 0;
		    var c = false;
		    var b = 0;
		    var g = -1;
		    var e;
		    for (e = 0; e < 8; e++) {
		        if (c) {
		            if (d[e] == "0") {
		                b += 1
		            } else {
		                c = false;
		                if (b > f) {
		                    a = g;
		                    f = b
		                }
		            }
		        } else {
		            if (d[e] == "0") {
		                c = true;
		                g = e;
		                b = 1
		            }
		        }
		    }
		    if (b > f) {
		        a = g;
		        f = b
		    }
		    if (f > 1) {
		        d.splice(a, f, "")
		    }
		},
		stripleadingzeroes: function (a) {
		    var b = a.length;
		    var c;
		    for (i = 0; i < b; i++) {
		        segs = a[i].split("");
		        for (j = 0; j < 3; j++) {
		            if ((segs[0] == "0") && (segs.length > 1)) {
		                segs.splice(0, 1)
		            } else {
		                break
		            }
		        }
		        a[i] = segs.join("")
		    }
		},
		trimcolonsfromends: function (a) {
		    var b = a.length;
		    if ((a[0] == "") && (a[1] == "") && (a[2] == "")) {
		        a.shift();
		        a.shift()
		    } else {
		        if ((a[0] == "") && (a[1] == "")) {
		            a.shift()
		        } else {
		            if ((a[b - 1] == "") && (a[b - 2] == "")) {
		                a.pop()
		            }
		        }
		    }
		},
		fillemptysegment: function (b) {
		    var c;
		    var a = 8;
		    if (b[b.length - 1].indexOf(".") != -1) {
		        a = 7
		    }
		    for (c = 0; c < a; c++) {
		        if (b[c] == "") {
		            b[c] = "0";
		            break
		        }
		    }
		    while (b.length < a) {
		        b.splice(c, 0, "0")
		    }
		}
	},
	
});
