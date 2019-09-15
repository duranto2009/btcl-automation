package config.form;

import org.apache.struts.action.*;

import java.util.*;
public class onlineConfigurationForm extends ActionForm{
	
	private String onlinepaypalID = null;
	private String onlinepaypalUsername = null;
	private String onlinepaypalPassword = null;
	private String onlinepaypalSignature = null;
	/*******Added For Tax Payment By Joyanta*************/
	private double onlinepaypalTaxAmount = 0.0;
	/******************************************/
	
	private String onlineauthorizedapiloginid = null;
	private String onlineauthorizedtarnsactionkey = null;
	private String onlineauthorizedmd5key =  null;
	/*******Added For Tax Payment By Joyanta*************/
	private double onlineAuthorizedTaxAmount = 0.0;
	/******************************************/
	
	
	private String onlineeprocessingaccountid = null;
	/*******Added For Tax Payment By Joyanta*************/
	private double onlineeprocessingTaxAmount = 0.0;
	/******************************************/
	
	
	private boolean onlinepaymentOptionPaypal = false;
	private boolean onlinepaymentOptionAuthorized = false;
	private boolean onlinepaymentOptioneprocessing = false;
	/******For Cashu By Joyanta***********/
	private boolean onlinepaymentOptioncashu = false;
	private String onlinecashumerchantid = null;
	private String onlinecashuencryptionkey = null;
	private double onlinecashuTaxAmount = 0.0;
	/*************************************/
	
	/******For Paymentwall By Joyanta***********/
	private boolean onlinepaymentOptionpaymentwall = false;
	private String onlinepaymentwallApplicationkey = null;
	private String onlinepaymentwallSecretkey = null;
	private double onlinePaymentwallTaxamount = 0.0;
	/*************************************/
	
	/******For Ingenico By Ximran***********/
	private boolean onlinepaymentOptionIngenico = false;
	private String onlineIngenicoApplicationkey = null;
	private String onlineIngenicoUserIDkey = null;
	private String onlineIngenicoPasswordkey = null;
	private String onlineIngenicoShasecretkey = null;
	private double onlineIngenicoTaxamount = 0.0;
	/*************************************/
	
	/******For BrainTree By Ximran***********/
	private boolean onlinepaymentOptionBrainTree = false;
	private String onlineBrainTreeMerchantid = null;
	private String onlineBrainTreeMerchantAccountid = null;
	private String onlineBrainTreePublickey = null;
	private String onlineBrainTreePrivatekey = null;
	private double onlineBrainTreeTaxamount = 0.0;
	/*************************************/
	
	
	
	/****** Google In App Purchase */
	private boolean onlinepaymentOptiongoogleInAppPurchase = false;
	private String googleInAppPurchaseClientID;
	private String googleInAppPurchaseClientSecret;
	private String googleInAppPurchaseRefreshToken;
	private double googleInAppPurchasetaxamount=0.0;
	/*****************/
	private boolean appleInAppPurchase = false;
	private String appleInAppPurchasePassword = null;
	private double appleInAppPurchasetaxamount=0.0;
	
	private int autorecharge;
	private String arRechargeAtBalance = null;
	private String arAmount = null;		
	
	
	/******************* worldpay payemnt *****************/
	private boolean onlinepaymentOptionWorldpay = false;
	//private String worldpayMarchantID = null;
	private String worldpayServiceKey = null;
	private String worldpayClientKey = null;
	private double worldpayTaxamount = 0.0;
	/******************* worldpay payment *****************/
	
	

	/******************* stripe payemnt *****************/
	private boolean onlinepaymentOptionStripe = false;
	private String stripeSecretKey = null;
	private String stripePublishableKey = null;
	private double stripeTaxamount = 0.0;
	
	/******************* stripe payment *****************/
	
	/******************* MasterCardMIG payemnt *****************/
	private boolean onlinepaymentOptionMasterCardMIG = false;
	private String masterCardMIGMerchantID = null;
	private String masterCardMIGAccessCode = null;
	private String masterCardMIGSecureHashKey = null;
	private double masterCardMIGTaxamount = 0.0;
	
	/******************* MasterCardMIG payment *****************/
	/******************* LavaPay payment added by forhad *****************/
	private boolean onlinepaymentOptionLavaPay = false;
	private String lavaPayMerchantCode = null;
	private String lavaPayPassPharse = null;
	private String lavaPayAPIKey = null;
	private double lavaPayTaxamount = 0.0;
	
	public onlineConfigurationForm()
	{
		 
	}
	public void setonlinepaypalID(String onlinepaypalID)
	{
		this.onlinepaypalID = onlinepaypalID;
	}
	public String getonlinepaypalID()
	{
		return this.onlinepaypalID;
	}
	public void setonlinepaypalUsername(String onlinepaypalUsername)
	{
		this.onlinepaypalUsername = onlinepaypalUsername;
	}
	public String getonlinepaypalUsername()
	{
		return this.onlinepaypalUsername;
	}
	public void setonlinepaypalPassword(String onlinepaypalPassword)
	{
		this.onlinepaypalPassword = onlinepaypalPassword;
	}
	public String getonlinepaypalPassword()
	{
		return this.onlinepaypalPassword;
	}
	public void setonlinepaypalSignature(String onlinepaypalSignature)
	{
		this.onlinepaypalSignature = onlinepaypalSignature;
	}
	public String getonlinepaypalSignature()
	{
		return this.onlinepaypalSignature;
	}
	
	public void setonlineauthorizedapiloginid(String onlineauthorizedapiloginid)
	{
		this.onlineauthorizedapiloginid = onlineauthorizedapiloginid;
	}
	public String getonlineauthorizedapiloginid()
	{
		return this.onlineauthorizedapiloginid;
	}
	public void setonlineauthorizedtarnsactionkey(String onlineauthorizedtarnsactionkey)
	{
		this.onlineauthorizedtarnsactionkey = onlineauthorizedtarnsactionkey;
	}
	public String getonlineauthorizedtarnsactionkey()
	{
		return this.onlineauthorizedtarnsactionkey;
	}
	public void setonlineauthorizedmd5key(String onlineauthorizedmd5key)
	{
		this.onlineauthorizedmd5key = onlineauthorizedmd5key;
	}
	public String getonlineauthorizedmd5key()
	{
		return this.onlineauthorizedmd5key;
	}
	public void setonlinepaymentOptionPaypal(boolean onlinepaymentOptionPaypal)
	{
		this.onlinepaymentOptionPaypal = onlinepaymentOptionPaypal;
	}
	public boolean getonlinepaymentOptionPaypal()
	{
		return this.onlinepaymentOptionPaypal;
	}
	public void setonlinepaymentOptionAuthorized(boolean onlinepaymentOptionAuthorized)
	{
		this.onlinepaymentOptionAuthorized = onlinepaymentOptionAuthorized;
	}
	public boolean getonlinepaymentOptionAuthorized()
	{
		return this.onlinepaymentOptionAuthorized;
	}
	public void setautorecharge(int autorecharge)
	{
		this.autorecharge = autorecharge;
	}
	public int getautorecharge()
	{
		return this.autorecharge;
	}
	public void setarAmount(String amount)
	{
		this.arAmount = amount;
	}
	public String getarAmount()
	{
		return this.arAmount;
	}
	public void setarRechargeAtBalance(String amount)
	{
		this.arRechargeAtBalance = amount;
	}
	public String getarRechargeAtBalance()
	{
		return this.arRechargeAtBalance;
	}
	public void setonlineeprocessingaccountid(String Accountid)
	{
		this.onlineeprocessingaccountid = Accountid;
	}
	public String getonlineeprocessingaccountid()
	{
		return this.onlineeprocessingaccountid;
	}
	public void setonlinepaymentOptioneprocessing(boolean onlinepaymentOptioneprocessing)
	{
		this.onlinepaymentOptioneprocessing = onlinepaymentOptioneprocessing;
	}
	public boolean getonlinepaymentOptioneprocessing()
	{
		return this.onlinepaymentOptioneprocessing;
	}
	
	/*************For Cashu By Joyanta***********************/
	public void setonlinepaymentOptioncashu(boolean onlinepaymentOptioncashu)
	{
		this.onlinepaymentOptioncashu = onlinepaymentOptioncashu;
	}
	public boolean getonlinepaymentOptioncashu()
	{
		return this.onlinepaymentOptioncashu;
	}
	public void setonlinecashumerchantid(String onlinecashumerchantid)
	{
		this.onlinecashumerchantid = onlinecashumerchantid;
	}
	public String getonlinecashumerchantid()
	{
		return this.onlinecashumerchantid;
	}
	public void setonlinecashuencryptionkey(String onlinecashuencryptionkey)
	{
		this.onlinecashuencryptionkey = onlinecashuencryptionkey;
	}
	public String getonlinecashuencryptionkey()
	{
		return this.onlinecashuencryptionkey;
	}
	public void setonlinecashuTaxAmount(double onlinecashuTaxAmount)
	{
		this.onlinecashuTaxAmount = onlinecashuTaxAmount;
	}
	public double getonlinecashuTaxAmount()
	{
		return this.onlinecashuTaxAmount;
	}
	/********************************************************/
	
	/*************For PaymentWall By Joyanta***********************/
	public void setonlinepaymentOptionpaymentwall(boolean onlinepaymentOptionpaymentwall)
	{
		this.onlinepaymentOptionpaymentwall = onlinepaymentOptionpaymentwall;
	}
	public boolean getonlinepaymentOptionpaymentwall()
	{
		return this.onlinepaymentOptionpaymentwall;
	}
	public void setonlinepaymentwallapplicationkey(String onlinepaymentwallapplicationkey)
	{
		this.onlinepaymentwallApplicationkey = onlinepaymentwallapplicationkey;
	}
	public String getonlinepaymentwallapplicationkey()
	{
		return this.onlinepaymentwallApplicationkey;
	}
	public void setonlinepaymentwallsecretkey(String onlinepaymentwallsecretkey)
	{
		this.onlinepaymentwallSecretkey = onlinepaymentwallsecretkey;
	}
	public String getonlinepaymentwallsecretkey()
	{
		return this.onlinepaymentwallSecretkey;
	}
	public void setonlinepaymentwalltaxamount(double onlinepaymentwalltaxamount)
	{
		this.onlinePaymentwallTaxamount = onlinepaymentwalltaxamount;
	}
	public double getonlinepaymentwalltaxamount()
	{
		return this.onlinePaymentwallTaxamount;
	}
	/********************************************************/
	
	/*************For Ingenico By Ximran***********************/
	public void setonlinepaymentOptionIngenico(boolean onlinepaymentOptionIngenico)
	{
		this.onlinepaymentOptionIngenico = onlinepaymentOptionIngenico;
	}
	public boolean getonlinepaymentOptionIngenico()
	{
		return this.onlinepaymentOptionIngenico;
	}
	
	public void setonlineIngenicoapplicationkey(String onlineIngenicoapplicationkey)
	{
		this.onlineIngenicoApplicationkey = onlineIngenicoapplicationkey;
	}
	public String getonlineIngenicoapplicationkey()
	{
		return this.onlineIngenicoApplicationkey;
	}
	
	public void setonlineIngenicoUserkey(String onlineIngenicoUserkey)
	{
		this.onlineIngenicoUserIDkey = onlineIngenicoUserkey;
	}
	public String getonlineIngenicoUserkey()
	{
		return this.onlineIngenicoUserIDkey;
	}
	
	public void setonlineIngenicopasswordkey(String onlineIngenicopasswordkey)
	{
		this.onlineIngenicoPasswordkey = onlineIngenicopasswordkey;
	}
	public String getonlineIngenicopasswordkey()
	{
		return this.onlineIngenicoPasswordkey;
	}
	
	public void setonlineIngenicoShasecretkey(String onlineIngenicoShasecretkey)
	{
		this.onlineIngenicoShasecretkey = onlineIngenicoShasecretkey;
	}
	public String getonlineIngenicoShasecretkey()
	{
		return this.onlineIngenicoShasecretkey;
	}
	
	public void setonlineIngenicotaxamount(double onlineIngenicotaxamount)
	{
		this.onlineIngenicoTaxamount = onlineIngenicotaxamount;
	}
	public double getonlineIngenicotaxamount()
	{
		return this.onlineIngenicoTaxamount;
	}
	/********************************************************/

	/*************For BrainTree By Ximran***********************/
	public void setonlinepaymentOptionBrainTree(boolean onlinepaymentOptionBrainTree)
	{
		this.onlinepaymentOptionBrainTree = onlinepaymentOptionBrainTree;
	}
	public boolean getonlinepaymentOptionBrainTree()
	{
		return this.onlinepaymentOptionBrainTree;
	}
	
	public void setonlineBrainTreeMerchantid(String onlineBrainTreeMerchantid)
	{
		this.onlineBrainTreeMerchantid = onlineBrainTreeMerchantid;
	}
	public String getonlineBrainTreeMerchantid()
	{
		return this.onlineBrainTreeMerchantid;
	}
	
	
	public void setonlineBrainTreeMerchantAccountid(String onlineBrainTreeMerchantAccountid)
	{
		this.onlineBrainTreeMerchantAccountid = onlineBrainTreeMerchantAccountid;
	}
	public String getonlineBrainTreeMerchantAccountid()
	{
		return this.onlineBrainTreeMerchantAccountid;
	}
	
	
	public void setonlineBrainTreePublickey(String onlineBrainTreePublickey)
	{
		this.onlineBrainTreePublickey = onlineBrainTreePublickey;
	}
	public String getonlineBrainTreePublickey()
	{
		return this.onlineBrainTreePublickey;
	}
	
	public void setonlineBrainTreePrivatekey(String onlineBrainTreePrivatekey)
	{
		this.onlineBrainTreePrivatekey = onlineBrainTreePrivatekey;
	}
	public String getonlineBrainTreePrivatekey()
	{
		return this.onlineBrainTreePrivatekey;
	}
	public double getonlineBrainTreeTaxamount() {
		return onlineBrainTreeTaxamount;
	}
	public void setonlineBrainTreeTaxamount(double onlineBrainTreeTaxamount) {
		this.onlineBrainTreeTaxamount = onlineBrainTreeTaxamount;
	}
	
//	public void setonlineBrainTreetaxamount(double onlineBrainTreetaxamount)
//	{
//		this.onlineBrainTreeTaxamount = onlineBrainTreetaxamount;
//	}
//	public double getonlineBrainTreeTaxamount()
//	{
//		return this.onlineBrainTreeTaxamount;
//	}
	
	/********************************************************/
	
	/*************Added For Tax Payment By Joyanta **********************/
	public void setonlineauthorizedTaxAmount(double amount)
	{
		this.onlineAuthorizedTaxAmount = amount;
	}
	
	public double getonlineauthorizedTaxAmount()
	{
		return this.onlineAuthorizedTaxAmount;
	}
	public void setonlineeprocessingTaxAmount(double amount)
	{
		this.onlineeprocessingTaxAmount = amount;
	}
	public double getonlineeprocessingTaxAmount()
	{
		return this.onlineeprocessingTaxAmount;
	}
	public void setonlinepaypalTaxAmount(double amount)
	{
		this.onlinepaypalTaxAmount = amount;
	}
	public double getonlinepaypalTaxAmount()
	{
		return this.onlinepaypalTaxAmount;
	}
	/********************************************************/
	public String getGoogleInAppPurchaseClientID() {
		return googleInAppPurchaseClientID;
	}
	public void setGoogleInAppPurchaseClientID(
			String googleInAppPurchaseClientID) {
		this.googleInAppPurchaseClientID = googleInAppPurchaseClientID;
	}
	public String getGoogleInAppPurchaseClientSecret() {
		return googleInAppPurchaseClientSecret;
	}
	public void setGoogleInAppPurchaseClientSecret(
			String googleInAppPurchaseClientSecret) {
		this.googleInAppPurchaseClientSecret = googleInAppPurchaseClientSecret;
	}
	public String getGoogleInAppPurchaseRefreshToken() {
		return googleInAppPurchaseRefreshToken;
	}
	public void setGoogleInAppPurchaseRefreshToken(
			String googleInAppPurchaseRefreshToken) {
		this.googleInAppPurchaseRefreshToken = googleInAppPurchaseRefreshToken;
	}
	public double getGoogleInAppPurchasetaxamount() {
		return googleInAppPurchasetaxamount;
	}
	public void setGoogleInAppPurchasetaxamount(double googleInAppPurchasetaxamount) {
		this.googleInAppPurchasetaxamount = googleInAppPurchasetaxamount;
	}
	public boolean getOnlinepaymentOptiongoogleInAppPurchase() {
		return onlinepaymentOptiongoogleInAppPurchase;
	}
	public void setOnlinepaymentOptiongoogleInAppPurchase(
			boolean onlinepaymentOptionGoogleInAppPurchase) {
		this.onlinepaymentOptiongoogleInAppPurchase = onlinepaymentOptionGoogleInAppPurchase;
	}
	public boolean getAppleInAppPurchase() {
		return appleInAppPurchase;
	}
	public void setAppleInAppPurchase(boolean appleInAppPurchase) {
		this.appleInAppPurchase = appleInAppPurchase;
	}
	public String getAppleInAppPurchasePassword() {
		return appleInAppPurchasePassword;
	}
	public void setAppleInAppPurchasePassword(String appleInAppPurchasePassword) {
		this.appleInAppPurchasePassword = appleInAppPurchasePassword;
	}
	public double getAppleInAppPurchasetaxamount() {
		return appleInAppPurchasetaxamount;
	}
	public void setAppleInAppPurchasetaxamount(double appleInAppPurchasetaxamount) {
		this.appleInAppPurchasetaxamount = appleInAppPurchasetaxamount;
	}
	
	
/************************** wordpay fields ***************************/
	
	public void setOnlinepaymentOptionWorldpay(boolean onlinepaymentOptionWorldpay){
		this.onlinepaymentOptionWorldpay = onlinepaymentOptionWorldpay;
	}
	
	public boolean getOnlinepaymentOptionWorldpay(){
		return this.onlinepaymentOptionWorldpay;
	}
	
	/*public void setWorldpayMarchantID(String worldpayMarchantID){
		this.worldpayMarchantID = worldpayMarchantID;
	}
	
	public String getWorldpayMarchantID(){
		return this.worldpayMarchantID;
	}*/
	
	public void setWorldpayServiceKey(String worldpayServiceKey){
		this.worldpayServiceKey = worldpayServiceKey;
	}

	public String getWorldpayServiceKey(){
		return this.worldpayServiceKey;
	}
	
	public void setWorldpayClientKey(String worldpayClientKey){
		this.worldpayClientKey = worldpayClientKey;
	}
	
	public String getWorldpayClientKey(){
		return this.worldpayClientKey;
	}
	
	public void setWorldpayTaxamount(double worldpayTaxamount){
		this.worldpayTaxamount = worldpayTaxamount;
	}
	
	public double getWorldpayTaxamount(){
		return this.worldpayTaxamount;
	}

/************************** stripe fields ***************************/
	
	public void setOnlinepaymentOptionStripe(boolean onlinepaymentOptionStripe){
		this.onlinepaymentOptionStripe = onlinepaymentOptionStripe;
	}
	
	public boolean getOnlinepaymentOptionStripe(){
		return this.onlinepaymentOptionStripe;
	}
	
	public void setStripeSecretKey(String stripeSecretKey){
		this.stripeSecretKey = stripeSecretKey;
	}

	public String getStripeSecretKey(){
		return this.stripeSecretKey;
	}
	
	public void setStripePublishableKey(String stripePublishableKey){
		this.stripePublishableKey = stripePublishableKey;
	}
	
	public String getStripePublishableKey(){
		return this.stripePublishableKey;
	}
	
	public void setStripeTaxamount(double stripeTaxamount){
		this.stripeTaxamount = stripeTaxamount;
	}
	
	public double getStripeTaxamount(){
		return this.stripeTaxamount;
	}
	
/************************** masterCardMIG fields ***************************/
	
	public void setOnlinepaymentOptionMasterCardMIG(boolean onlinepaymentOptionMasterCardMIG){
		this.onlinepaymentOptionMasterCardMIG = onlinepaymentOptionMasterCardMIG;
	}
	
	public boolean getOnlinepaymentOptionMasterCardMIG(){
		return this.onlinepaymentOptionMasterCardMIG;
	}
	
	public void setMasterCardMIGMerchantID(String masterCardMIGMerchantID){
		this.masterCardMIGMerchantID = masterCardMIGMerchantID;
	}
	
	public String getMasterCardMIGMerchantID(){
		return this.masterCardMIGMerchantID;
	}
	
	public void setMasterCardMIGAccessCode(String masterCardMIGAccessCode){
		this.masterCardMIGAccessCode = masterCardMIGAccessCode;
	}

	public String getMasterCardMIGAccessCode(){
		return this.masterCardMIGAccessCode;
	}
	
	public void setMasterCardMIGSecureHashKey(String masterCardMIGSecureHashKey){
		this.masterCardMIGSecureHashKey = masterCardMIGSecureHashKey;
	}
	
	public String getMasterCardMIGSecureHashKey(){
		return this.masterCardMIGSecureHashKey;
	}
	
	public void setMasterCardMIGTaxamount(double masterCardMIGTaxamount){
		this.masterCardMIGTaxamount = masterCardMIGTaxamount;
	}
	
	public double getMasterCardMIGTaxamount(){
		return this.masterCardMIGTaxamount;
	}
	
/************************** LavaPay fields added by forhad ***************************/
	
	public void setOnlinepaymentOptionLavaPay(boolean onlinepaymentOptionLavaPay){
		this.onlinepaymentOptionLavaPay = onlinepaymentOptionLavaPay;
	}
	
	public boolean getOnlinepaymentOptionLavaPay(){
		return this.onlinepaymentOptionLavaPay;
	}
	
	public void setLavaPayMerchantCode(String lavaPayMerchantCode){
		this.lavaPayMerchantCode = lavaPayMerchantCode;
	}
	
	public String getLavaPayMerchantCode(){
		return this.lavaPayMerchantCode;
	}
	
	public void setLavaPayPassPharse(String lavaPayPassPharse){
		this.lavaPayPassPharse = lavaPayPassPharse;
	}

	public String getLavaPayPassPharse(){
		return this.lavaPayPassPharse;
	}
	
	public void setLavaPayAPIKey(String lavaPayAPIKey){
		this.lavaPayAPIKey = lavaPayAPIKey;
	}
	
	public String getLavaPayAPIKey(){
		return this.lavaPayAPIKey;
	}
	
	public void setLavaPayTaxamount(double lavaPayTaxamount){
		this.lavaPayTaxamount = lavaPayTaxamount;
	}
	
	public double getLavaPayTaxamount(){
		return this.lavaPayTaxamount;
	}
	
}
