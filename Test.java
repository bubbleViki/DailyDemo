import java.net.URL;

import org.codehaus.xfire.client.Client;

public class Test {
	public static void importJsonData() {
		try {
			String wsUrl = "http://localhost:8080/cidp/ws/intfsServiceWSJSON?wsdl";
			Client client = new Client(new URL(wsUrl));
			String jsonStr = "{\"test_table1\":[{\"id\":\"1\",\"name\":\"qiangjin\"},{\"id\":\"2\",\"name\":\"haohua\"},{\"id\":\"3\",\"name\":\"liziyong\"}]}";
			final Object[] results = client.invoke("importEffectDataJSON",
					new Object[] { "test_table1", jsonStr });
			String ret = (String) results[0];
			System.out.println(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getJsonData(){
		try {
			String wsUrl = "http://localhost:8080/cidp/ws/intfsServiceWSJSON?wsdl";
			Client client = new Client(new URL(wsUrl));
			final Object[] results = client.invoke("getModelDatasJSON",
					new Object[] { "test_table1", "","id,name"});
			String ret = (String) results[0];
			System.out.println(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		importJsonData();
		getJsonData();
	}
}
