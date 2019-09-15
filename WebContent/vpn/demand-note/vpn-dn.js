export const floatParse = (data) => {
	if(!isNumeric(data)) {
		return 0.0;
	}else {
		return parseFloat(data);
	}
} ;
const isNumeric = (data) => {
	return !isNaN(parseFloat(data)) && isFinite(data);
};

export const fixedTwoDigit = (amount) => floatParse(amount).toFixed(2);
export const callAXIOS = (url, method, success, failure) =>{
	axios({
		method: method,
		url: url
	})
	.then(res=> res.data.responseCode == 1 ? success(res.data) : failure(res.data))
	.catch(err=>promiseError(err))
};

const promiseError = (err)=>LOG(err);