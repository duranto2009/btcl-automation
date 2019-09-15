package jUnitTesting;

import java.io.*;

import com.google.gson.Gson;
import common.pdf.JasperAPI;
import nix.advicenote.NIXTDReconnectAdviceNote;
import util.KeyValuePair;
import nix.document.NIXWorkOrder;

public class JasperTestingLLI {

	public static void main(String[] args) throws Exception {
		FileOutputStream fs = new FileOutputStream("C:/Users/HP/Desktop/a.pdf");
		NIXTDReconnectAdviceNote an = new NIXTDReconnectAdviceNote();
		JasperAPI.getInstance().renderPDFToBrowser(an, fs);

//		System.out.println(new Gson().fromJson("{"
//
//				+"value: raihan"
//				+"}", KeyValuePair.class));
//		FileOutputStream fs = new FileOutputStream("C:/Users/REVE/Desktop/a.pdf");
//		NIXWorkOrder nwo = new NIXWorkOrder();
//		JasperAPI.getInstance().renderPDFToBrowser(nwo, fs);
	}
}
