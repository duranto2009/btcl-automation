$(document).ready(function(){
	
	function setClientID()
	{
		var e = document.getElementById('clientList');
		document.getElementById('clientID').value = e.options[e.selectedIndex].value;
	}
});
