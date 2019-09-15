const floatParse = (data) => {
	if(!isNumeric(data)) {
		return 0.0;
	}else {
		return parseFloat(data);
	}
} ;
const isNumeric = (data) => {
	return !isNaN(parseFloat(data)) && isFinite(data);
};
const isValueOutOfRange = (data, low, high, inclusive) => {
	return (
			isNumeric(data) === false ||
			(data < low) ||
			(data > high)
	)
	
};
const reloadAfterNSec = (n) => {
	setTimeout(function() {
		location.reload();	
	}, n*1000)
};