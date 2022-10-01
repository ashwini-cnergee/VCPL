package com.cnergee.mypage.SOAP;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Hashtable;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.MemberDetailsObj;
import com.cnergee.mypage.utils.Utils;

public class MemberDetailSOAP {

	private String WSDL_TARGET_NAMESPACE;
	private String SOAP_URL;
	private String METHOD_NAME;
	public MemberDetailsObj memberdetailsData;
	private Map<String, MemberDetailsObj> mapMemberDetails;
	private boolean isAllData;


	public MemberDetailSOAP(String WSDL_TARGET_NAMESPACE, String SOAP_URL,
			String METHOD_NAME) {
		this.WSDL_TARGET_NAMESPACE = WSDL_TARGET_NAMESPACE;
		this.SOAP_URL = SOAP_URL;
		this.METHOD_NAME = METHOD_NAME;
	}


	public String CallSearchMemberSOAP(long memberid )throws SocketException,SocketTimeoutException,Exception {

		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, METHOD_NAME);
		//Log.i(" searchTxt ", searchTxt);
		//Log.i(" #	#####################  ", " START ");

		//Log.i(" username ", username);
		//Log.i(" password ", password);
		/*Log.i(" IMEI No ", Authobj.getIMEINo());
		Log.i(" Mobile ", Authobj.getMobileNumber());
		Log.i(" Mobile User ", Authobj.getMobLoginId());
		Log.i(" Mobile Password ", Authobj.getMobUserPass());
		Log.i(" WSDL_TARGET_NAMESPACE ", WSDL_TARGET_NAMESPACE);
		Log.i(" METHOD_NAME ", METHOD_NAME);
		Log.i(" SOAP_ACTION ", WSDL_TARGET_NAMESPACE + METHOD_NAME);
		Log.i(" searchTxt ", searchTxt);*/
		//Log.i("#####################", "");

		PropertyInfo pi = new PropertyInfo();
		pi.setName("MemberId");
		pi.setValue(memberid);
		pi.setType(String.class);
		request.addProperty(pi);

		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		//Log.i(">>>>REquest>>>>>" , request.toString());
		envelope.setOutputSoapObject(request);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.implicitTypes = true;
		envelope.addMapping(WSDL_TARGET_NAMESPACE, "",
				this.getClass());

		HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_URL);
		androidHttpTransport.debug = true;


		String str_msg = "ok";
			androidHttpTransport.call(WSDL_TARGET_NAMESPACE + METHOD_NAME,
					envelope);
			Utils.log("Member Details","request:"+request.toString());

			 Object response2 = envelope.getResponse();
			 //Log.i(" RESPONSE ",response2.toString());
			if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
				Map<String, MemberDetailsObj> mapMemberDetails = new Hashtable<String, MemberDetailsObj>();
				SoapObject response = (SoapObject) envelope.getResponse();

				Utils.log("Member Details","response:"+response.toString());
				if (response != null) {
					//Log.i(" RESPONSE ", response.toString());
					response = (SoapObject) response.getProperty("NewDataSet");
					if (response.getPropertyCount() > 0) {
						for (int i = 0; i < response.getPropertyCount(); i++) {
							SoapObject tableObj = (SoapObject) response.getProperty(i);
							setMemberDetails(tableObj, mapMemberDetails);
						}
						setMapMemberDetails(mapMemberDetails);

						str_msg = "ok";
					} else {
						str_msg = "not";
					}
				} else {
					str_msg = "not";
				}

			} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault =
																// FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				return soapFault.getMessage().toString();
			}
		return str_msg;
	}


	public void setMemberDetails(SoapObject response,
			Map<String, MemberDetailsObj> mapMemberDetails) {

		MemberDetailsObj memberdetails = new MemberDetailsObj();

		if (response.getPropertyAsString("CustomerName").toString().equals("anyType{}"))
		{
			memberdetails.setMemberName("");
		}else {

			memberdetails.setMemberName(response.getPropertyAsString("CustomerName").toString());
		}

		if (response.getPropertyAsString("MemberLoginID").toString().equals("anyType{}"))
		{
			memberdetails.setMemberLoginId("");
		}else {

			memberdetails.setMemberLoginId(response.getPropertyAsString("MemberLoginID").toString());
		}
		if (response.getPropertyAsString("MemberFromDt").toString().equals("anyType{}"))
		{
			memberdetails.setActivationDate("");
		}
		else {

			memberdetails.setActivationDate(response.getPropertyAsString("MemberFromDt").toString());
		}
		if (response.getPropertyAsString("DateofBirth").toString().equals("anyType{}"))
		{
			memberdetails.setDateofBirth("");
		}
		else {

			memberdetails.setDateofBirth(response.getPropertyAsString("DateofBirth").toString());
		}
		if (response.getPropertyAsString("MobileNoprimary").toString().equals("anyType{}"))
		{
			memberdetails.setMobileNo("");
		}
		else {

			memberdetails.setMobileNo(response.getPropertyAsString("MobileNoprimary").toString());
		}




		/*if(response.hasProperty("MobileNosecondry")) {
			if (response.getPropertyAsString("MobileNosecondry").toString().equalsIgnoreCase("anyType{}")){
				memberdetails.setAlternateNo("");
			}else{
				memberdetails.setAlternateNo(response.getPropertyAsString("MobileNosecondry").toString());
			}
		}*//*

		Utils.log("Secondary Number",":"+response.getPropertyAsString("MobileNosecondry").toString());*/
		if(response.getPropertyAsString("MobileNosecondry").equals("anyType{}"))
		{
			memberdetails.setAlternateNo("");
		}
		else {
			memberdetails.setAlternateNo(response.getPropertyAsString("MobileNosecondry").toString());
		}
		if (response.getPropertyAsString("EmailId").toString().equals("anyType{}"))
		{
			memberdetails.setEmailId("");
		}
		else {
			memberdetails.setEmailId(response.getPropertyAsString("EmailId").toString());
		}
		if (response.getPropertyAsString("LocalAddress").toString().equals("anyType{}"))
		{
			memberdetails.setInstLocAddressLine1("");
		}
		else {
			memberdetails.setInstLocAddressLine1(response.getPropertyAsString("LocalAddress").toString());
		}
		if (response.getPropertyAsString("PackageFullName").toString().equals("anyType{}"))
		{
			memberdetails.setPackageName("");
		}
		else {
			memberdetails.setPackageName(response.getPropertyAsString("PackageFullName").toString());
		}
		if (response.getPropertyAsString("Status").toString().equals("anyType{}"))
		{
			memberdetails.setStatus("");
		}else {
			memberdetails.setStatus(response.getPropertyAsString("Status").toString());
		}
		if (response.getPropertyAsString("PermanentAddress").toString().equals("anyType{}"))
		{
			memberdetails.setInstLocAddressLine2("");

		}
		else {
			memberdetails.setInstLocAddressLine2(response.getPropertyAsString("PermanentAddress").toString());
		}


		/*memberdetails.setPackageAmount(response.getPropertyAsString("PackageRate").toString());
		memberdetails.setServiceTax(response.getPropertyAsString("ServiceTax").toString());*/

        if (response.getPropertyAsString("PinCode").toString().equals("anyType{}"))
        {
            memberdetails.setPincode("");
        }else {
            memberdetails.setPincode(response.getPropertyAsString("PinCode").toString());
        }


		if(response.getPropertyAsString("MobileNoprimary1").equals("anyType{}"))
		{
			memberdetails.setMb_secondary("");
		}
		else {
			memberdetails.setMb_secondary(response.getPropertyAsString("MobileNoprimary1").toString());
		}
		if (response.getPropertyAsString("CityName").toString().equals("anyType{}"))
		{
			memberdetails.setCity("");
		}else {
			memberdetails.setCity(response.getPropertyAsString("CityName").toString());
		}
		if (response.getPropertyAsString("StateName").toString().equals("anyType{}"))
		{
			memberdetails.setState("");
		}
		else {
			memberdetails.setState(response.getPropertyAsString("StateName").toString());
		}


		mapMemberDetails.put(memberdetails.getMobileNo(), memberdetails);

	}


	/**
	 * @param mapMemberData
	 *            the mapMemberData to set
	 */
	public void setMapMemberDetails(Map<String, MemberDetailsObj> mapMemberDetails) {
		this.mapMemberDetails = mapMemberDetails;
	}

	public Map<String, MemberDetailsObj> getMapMemberDetails() {
		return this.mapMemberDetails;
	}

	public boolean isAllData() {
		return isAllData;
	}

	public void setAllData(boolean isAllData) {
		this.isAllData = isAllData;
	}


}
