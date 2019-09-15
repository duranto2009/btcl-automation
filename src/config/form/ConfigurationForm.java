package config.form;

import org.apache.struts.action.ActionForm;

public class ConfigurationForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4180715389262249942L;
	
	private String rechargeCardAuthType;
	private String pinPasswordLength;

	public ConfigurationForm() {
		m_updateTerClientBal = 0;
		m_activeCallRemoveInt = 3600;
		balanceForCallShopPostPaidCall=50;
		rechargeCardAuthType="1";
		pinPasswordLength="4";
	}

	private int m_activeCallRemoveInt;
//	private String m_maxTalkTime;
	private String m_useLongestDestOnlyForRouting;
	private String m_useTaxForIvrPlayedDur;
	private int m_floorCallDuration;
	private String m_allowMultiRatePlan;
	private String m_allowAllGateways;
	private String m_allowPinToPinCallFree;
	private String m_makePinSequentially;
	private String m_enableSwitchIPSel;
	private String m_maxNoOfPinPerBatch;
	private String m_callDurForCeilCal;
	private String m_pinGenerationMainPrefix;
	private String m_enableSlottedRateUploadDownload;
	private String m_defaultCurrency;
	private String m_enableLossLessRouting;
	private String m_enableClientCreationForChildReseller;
	private String m_enableTaxAddAtRatePlan;
	private String m_enableProxyMediaSelection;
	private String m_enCallbackCallCallerIDSel;
	private String m_allowToCallAllAccessNo;
	private String m_minPinLenth;
	private int m_translationBeforeOrAfterAuth;
	private String m_callingCardAuthType;
	private String m_allowedResellerLevel;
	private int m_updateTerClientBal;
	private String m_enableOTPToken;

	private int maxTalkTimeForClient;
	private int minuteIVR;
	private String ivrNumber;
	private int ivrWithSecond;
	private int ivrBalanceWithCents;
	private int debugLog;
	private int logKeepingTime;
	private int writeCDRToFile;
	private int cdrFileRotation;
	private int sendChallengeRegUser;
	private int displayNameAsCallerId;
	private int fixedCallerIdForGateWay;
	private int callerIdFromAssertedId;
	private int regExpires;
	private int dtmfDigitTimeout;
	private int callbackTimeout;
	private int leaseTImeAtDue;
	private String stripFromCallerID;
	private String realmName;
	
	//private String paypalID;
	private int menuAtLeft;
	// Shantanu Begin
	
	//private String paypalUsername;
//	private String paypalPassword;
	//private String paypalSignature;
	
	
	
	// Shantanu End

	
	
	private String reportDownloadFormat;
	private String rechargeAccessNumber;
	
	private int callingCardForRegisterdUser;
	
	private String recordPerPage;
	private int balanceForCallShopPostPaidCall;
	private String callForwardingShortCode;
	private String packageDialingCode;
	
	private String m_ProxyPinToPinCall;
	
	
	public int getPinMenuAtLeft() {
		return menuAtLeft;
	}

	public void setPinMenuAtLeft(int p_menuAtLeft) {
		this.menuAtLeft = p_menuAtLeft;
	}
	
	public String getRecordPerPage() {
		return recordPerPage;
	}

	public void setRecordPerPage(String p_recordPerPage) {
		recordPerPage=p_recordPerPage;
	}
	
	
	/*public String getPaypalUsername() {
		return paypalUsername;
	}

	public void setPaypalUsername(String paypalUsername) {
		this.paypalUsername = paypalUsername;
	}

	public String getPaypalPassword() {
		return paypalPassword;
	}

	public void setPaypalPassword(String paypalPassword) {
		this.paypalPassword = paypalPassword;
	}

	public String getPaypalSignature() {
		return paypalSignature;
	}

	public void setPaypalSignature(String paypalSignature) {
		this.paypalSignature = paypalSignature;
	}*/

	public void setRechargeAccessNumber(
			String p_rechargeAccessNumber) {
		rechargeAccessNumber = p_rechargeAccessNumber;
	}

	public String getRechargeAccessNumber() {
		return rechargeAccessNumber;
	}
	
	public void setRealmName(
			String p_realmName) {
		realmName = p_realmName;
	}

	public String getRealmName() {
		return realmName;
	}
/*	public void setPaypalID(
			String p_paypalID) {
		paypalID = p_paypalID;
	}

	public String getPaypalID() {
		return paypalID;
	}*/
	
	public void setStripFromCallerID(
			String p_stripFromCallerID) {
		stripFromCallerID = p_stripFromCallerID;
	}

	public String getStripFromCallerID() {
		return stripFromCallerID;
	}
	
	
	public void setCallingCardForRegisterdUser(int p_callingCardForRegisterdUser) {
		callingCardForRegisterdUser = p_callingCardForRegisterdUser;
	}

	public int getCallingCardForRegisterdUser() {
		return callingCardForRegisterdUser;
	}

	public void setActiveCallRemoveInt(int p_activeCallRemoveInt) {
		m_activeCallRemoveInt = p_activeCallRemoveInt;
	}

	public int getActiveCallRemoveInt() {
		return m_activeCallRemoveInt;
	}

//	public void setMaxTalkTime(String p_maxTalkTime) {
//		m_maxTalkTime = p_maxTalkTime;
//	}
//
//	public String getMaxTalkTime() {
//		return m_maxTalkTime;
//	}

	public void setUseLongestDestOnlyForRouting(
			String p_useLongestDestOnlyForRouting) {
		m_useLongestDestOnlyForRouting = p_useLongestDestOnlyForRouting;
	}

	public String getUseLongestDestOnlyForRouting() {
		return m_useLongestDestOnlyForRouting;
	}

	public void setUseTaxForIvrPlayedDur(String p_useTaxForIvrPlayedDur) {
		m_useTaxForIvrPlayedDur = p_useTaxForIvrPlayedDur;
	}

	public String getUseTaxForIvrPlayedDur() {
		return m_useTaxForIvrPlayedDur;
	}

	public void setFloorCallDuration(int p_floorCallDuration) {
		m_floorCallDuration = p_floorCallDuration;
	}

	public int getFloorCallDuration() {
		return m_floorCallDuration;
	}

	public void setAllowMultiRatePlan(String p_allowMultiRatePlan) {
		m_allowMultiRatePlan = p_allowMultiRatePlan;
	}

	public String getAllowMultiRatePlan() {
		return m_allowMultiRatePlan;
	}

	public void setAllowAllGateways(String p_allowAllGateways) {
		m_allowAllGateways = p_allowAllGateways;
	}

	public String getAllowAllGateways() {
		return m_allowAllGateways;
	}

	public void setAllowPinToPinCallFree(String p_allowPinToPinCallFree) {
		m_allowPinToPinCallFree = p_allowPinToPinCallFree;
	}

	public String getAllowPinToPinCallFree() {
		return m_allowPinToPinCallFree;
	}

	public void setMakePinSequentially(String p_makePinSequentially) {
		m_makePinSequentially = p_makePinSequentially;
	}

	public String getMakePinSequentially() {
		return m_makePinSequentially;
	}

	public void setEnableSwitchIPSel(String p_enableSwitchIPSel) {
		m_enableSwitchIPSel = p_enableSwitchIPSel;
	}

	public String getEnableSwitchIPSel() {
		return m_enableSwitchIPSel;
	}

	public void setMaxNoOfPinPerBatch(String p_maxNoOfPinPerBatch) {
		m_maxNoOfPinPerBatch = p_maxNoOfPinPerBatch;
	}

	public String getMaxNoOfPinPerBatch() {
		return m_maxNoOfPinPerBatch;
	}

	public void setCallDurForCeilCal(String p_callDurForCeilCal) {
		m_callDurForCeilCal = p_callDurForCeilCal;
	}

	public String getCallDurForCeilCal() {
		return m_callDurForCeilCal;
	}

	public void setPinGenerationMainPrefix(String p_pinGenerationMainPrefix) {
		m_pinGenerationMainPrefix = p_pinGenerationMainPrefix;
	}

	public String getPinGenerationMainPrefix() {
		return m_pinGenerationMainPrefix;
	}

	public void setEnableSlottedRateUploadDownload(
			String p_enableSlottedRateUploadDownload) {
		m_enableSlottedRateUploadDownload = p_enableSlottedRateUploadDownload;
	}

	public String getEnableSlottedRateUploadDownload() {
		return m_enableSlottedRateUploadDownload;
	}

	public void setDefaultCurrency(String p_defaultCurrency) {
		m_defaultCurrency = p_defaultCurrency;
	}

	public String getDefaultCurrency() {
		return m_defaultCurrency;
	}

	public void setEnableLossLessRouting(String p_enableLossLessRouting) {
		m_enableLossLessRouting = p_enableLossLessRouting;
	}

	public String getEnableLossLessRouting() {
		return m_enableLossLessRouting;
	}

	public void setEnableClientCreationForChildReseller(
			String p_enableClientCreationForChildReseller) {
		m_enableClientCreationForChildReseller = p_enableClientCreationForChildReseller;
	}

	public String getEnableClientCreationForChildReseller() {
		return m_enableClientCreationForChildReseller;
	}

	public void setEnableTaxAddAtRatePlan(String p_enableTaxAddAtRatePlan) {
		m_enableTaxAddAtRatePlan = p_enableTaxAddAtRatePlan;
	}

	public String getEnableTaxAddAtRatePlan() {
		return m_enableTaxAddAtRatePlan;
	}

	public void setEnableProxyMediaSelection(String p_enableProxyMediaSelection) {
		m_enableProxyMediaSelection = p_enableProxyMediaSelection;
	}

	public String getEnableProxyMediaSelection() {
		return m_enableProxyMediaSelection;
	}

	public void setEnCallbackCallCallerIDSel(String p_enCallbackCallCallerIDSel) {
		m_enCallbackCallCallerIDSel = p_enCallbackCallCallerIDSel;
	}

	public String getEnCallbackCallCallerIDSel() {
		return m_enCallbackCallCallerIDSel;
	}

	public void setAllowToCallAllAccessNo(String p_allowToCallAllAccessNo) {
		m_allowToCallAllAccessNo = p_allowToCallAllAccessNo;
	}

	public String getAllowToCallAllAccessNo() {
		return m_allowToCallAllAccessNo;
	}

	public void setMinPinLenth(String p_minPinLenth) {
		m_minPinLenth = p_minPinLenth;
	}

	public String getMinPinLenth() {
		return m_minPinLenth;
	}

	public void setTranslationBeforeOrAfterAuth(
			int p_enRateAndParentCheckForBalTransfer) {
		m_translationBeforeOrAfterAuth = p_enRateAndParentCheckForBalTransfer;
	}

	public int getTranslationBeforeOrAfterAuth() {
		return m_translationBeforeOrAfterAuth;
	}

	public void setCallingCardAuthType(String p_callingCardAuthType) {
		m_callingCardAuthType = p_callingCardAuthType;
	}

	public String getCallingCardAuthType() {
		return m_callingCardAuthType;
	}

	public void setAllowedResellerLevel(String p_allowedResellerLevel) {
		m_allowedResellerLevel = p_allowedResellerLevel;
	}

	public String getAllowedResellerLevel() {
		return m_allowedResellerLevel;
	}

	public void setUpdateTerClientBal(int p_updateTerClientBal) {
		m_updateTerClientBal = p_updateTerClientBal;
	}

	public int getUpdateTerClientBal() {
		return m_updateTerClientBal;
	}

	public void setEnableOTPToken(String p_enableOTPToken) {
		m_enableOTPToken = p_enableOTPToken;
	}

	public String getEnableOTPToken() {
		return m_enableOTPToken;
	}

	public int getMaxTalkTimeForClient() {
		return maxTalkTimeForClient;
	}

	public void setMaxTalkTimeForClient(int maxTalkTimeForClient) {
		this.maxTalkTimeForClient = maxTalkTimeForClient;
	}

	public int getMinuteIVR() {
		return minuteIVR;
	}

	public void setMinuteIVR(int minuteIVR) {
		this.minuteIVR = minuteIVR;
	}

	public String getIvrNumber() {
		return ivrNumber;
	}

	public void setIvrNumber(String ivrNumber) {
		this.ivrNumber = ivrNumber;
	}

	public int getIvrWithSecond() {
		return ivrWithSecond;
	}

	public void setIvrWithSecond(int ivrWithSecond) {
		this.ivrWithSecond = ivrWithSecond;
	}

	public int getIvrBalanceWithCents() {
		return ivrBalanceWithCents;
	}

	public void setIvrBalanceWithCents(int ivrBalanceWithCents) {
		this.ivrBalanceWithCents = ivrBalanceWithCents;
	}

	public int getDebugLog() {
		return debugLog;
	}

	public void setDebugLog(int debugLog) {
		this.debugLog = debugLog;
	}

	public int getLogKeepingTime() {
		return logKeepingTime;
	}

	public void setLogKeepingTime(int logKeepingTime) {
		this.logKeepingTime = logKeepingTime;
	}

	public int getWriteCDRToFile() {
		return writeCDRToFile;
	}

	public void setWriteCDRToFile(int writeCDRToFile) {
		this.writeCDRToFile = writeCDRToFile;
	}

	

	public int getCdrFileRotation() {
		return cdrFileRotation;
	}

	public void setCdrFileRotation(int cdrFileRotation) {
		this.cdrFileRotation = cdrFileRotation;
	}

	public int getSendChallengeRegUser() {
		return sendChallengeRegUser;
	}

	public void setSendChallengeRegUser(int sendChallengeRegUser) {
		this.sendChallengeRegUser = sendChallengeRegUser;
	}

	public int getDisplayNameAsCallerId() {
		return displayNameAsCallerId;
	}

	public void setDisplayNameAsCallerId(int displayNameAsCallerId) {
		this.displayNameAsCallerId = displayNameAsCallerId;
	}

	public int getFixedCallerIdForGateWay() {
		return fixedCallerIdForGateWay;
	}

	public void setFixedCallerIdForGateWay(int fixedCallerIdForGateWay) {
		this.fixedCallerIdForGateWay = fixedCallerIdForGateWay;
	}

	public int getCallerIdFromAssertedId() {
		return callerIdFromAssertedId;
	}

	public void setCallerIdFromAssertedId(int callerIdFromAssertedId) {
		this.callerIdFromAssertedId = callerIdFromAssertedId;
	}

	public int getRegExpires() {
		return regExpires;
	}

	public void setRegExpires(int regExpires) {
		this.regExpires = regExpires;
	}

	public int getDtmfDigitTimeout() {
		return dtmfDigitTimeout;
	}

	public void setDtmfDigitTimeout(int dtmfDigitTimeout) {
		this.dtmfDigitTimeout = dtmfDigitTimeout;
	}

	public int getCallbackTimeout() {
		return callbackTimeout;
	}

	public void setCallbackTimeout(int callbackTimeout) {
		this.callbackTimeout = callbackTimeout;
	}

	public int getLeaseTImeAtDue() {
		return leaseTImeAtDue;
	}

	public void setLeaseTImeAtDue(int leaseTImeAtDue) {
		this.leaseTImeAtDue = leaseTImeAtDue;
	}

	public String getReportDownloadFormat() {
		return reportDownloadFormat;
	}

	public void setReportDownloadFormat(String reportDownloadFormat) {
		this.reportDownloadFormat = reportDownloadFormat;
	}

	public void setBalanceForCallShopPostPaidCall(int p_balanceForCallShopPostPaidCall) {
		balanceForCallShopPostPaidCall=p_balanceForCallShopPostPaidCall;
		
	}
	
	public int getBalanceForCallShopPostPaidCall() {
		return balanceForCallShopPostPaidCall;
		
	}



	public String getCallForwardingShortCode() {
		return callForwardingShortCode;
	}

	public void setCallForwardingShortCode(String callForwardingShortCode) {
		this.callForwardingShortCode = callForwardingShortCode;
	}

	public String getPackageDialingCode() {
		return packageDialingCode;
	}

	public void setPackageDialingCode(String packageDialingCode) {
		this.packageDialingCode = packageDialingCode;
	}

	public String getRechargeCardAuthType() {
		return rechargeCardAuthType;
	}

	public void setRechargeCardAuthType(String rechargeCardAuthType) {
		this.rechargeCardAuthType = rechargeCardAuthType;
	}

	public String getPinPasswordLength() {
		return pinPasswordLength;
	}

	public void setPinPasswordLength(String pinPasswordLength) {
		this.pinPasswordLength = pinPasswordLength;
	}

	public String getProxyPinToPinCall() {
		return m_ProxyPinToPinCall;
	}

	public void setProxyPinToPinCall(String m_ProxyPinToPinCall) {
		this.m_ProxyPinToPinCall = m_ProxyPinToPinCall;
	}
}
