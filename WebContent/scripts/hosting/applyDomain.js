function confirmBuy() {
	var f = document.forms[1];
	// alert(document.forms.length);
	// alert(f.domainName.value);
	// alert(document.getElementById("searchAgain").style.display);
	if (document.getElementById("searchAgain").style.display == "none") {
		var result = confirm('<%=domain%>'
				+ " will cost taka 1100 per year. Are you sure you want to buy this ?");
		if (!result)
			return false;
		else {
			return true;
		}

	}

}
function validate() {
	var f = document.forms[1];
	if (f.hdNAME.value.length == 0) {
		alert("Please enter a valid domain name");
		return false;
	}
	if (endsWith(f.hdNAME.value, ".বাংলা")
			|| endsWith(f.hdNAME.value, ".com.bd")) {

	} else {
		alert(f.hdNAME.value + " is not acceptable domain name.");
		return false;
	}
	return true;
}
function endsWith(str, suffix) {
	return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

function checkDomain() {
	var f = document.forms[1];
	f.actionType.value = 'check';
	var e = f.dmnType;
	var type = e.options[e.selectedIndex].text;
	f.hdName.value = '' + f.hdName_1.value + type;
}

$(function() {
	var availableTags = [
	/*
	 * "ActionScript", "AppleScript", "Asp", "BASIC", "C", "C++", "Clojure",
	 * "COBOL", "ColdFusion", "Erlang", "Fortran", "Groovy", "Haskell", "Java",
	 * "JavaScript", "Lisp", "Perl", "PHP", "Python", "Ruby", "Scala", "Scheme"
	 */
	];
	$("#hdName_1").autocomplete({
		source : availableTags
	});
});
function changeMe(val) {
	/*
	 * var suggestions =[val + '.com', val + '.org', val + '.net', val +
	 * '.edu'];
	 */
	if (!val.endsWith(".")) {
		/* $( "#hdName_1" ).autocomplete( "close" ); */
		return;
	}
	$("#hdName_1").autocomplete("option", "source",
			[ val + 'com', val + 'org', val + 'net', val + 'edu' ]);
	/*
	 * $( "#hdName_1" ).autocomplete( "option", "source", [val + '.com', val +
	 * '.org', val + '.net', val + '.edu']);
	 */
}