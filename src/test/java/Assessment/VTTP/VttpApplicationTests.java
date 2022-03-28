package Assessment.VTTP;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import Assessment.VTTP.Controller.PurchaseOrderRestController;
import Assessment.VTTP.Controller.Quotation;
import Assessment.VTTP.Service.QuotationService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockHttpServletResponse;

@SpringBootTest
@AutoConfigureMockMvc
class VttpApplicationTests {

	@Autowired
	private PurchaseOrderRestController ctrl;

	@Autowired
	MockMvc mvc;

	@Test
	void contextLoads() {
		assertNotNull(ctrl);
	}


	@Test
	void getquotationfail() {
	List<String> items = new ArrayList<>();
	items.add("durian");
	items.add("plum");
	items.add("pear");

	
	QuotationService qSvc = new QuotationService();
	Optional<Quotation> optQuot = qSvc.getQuotations(items);

	// optQuot should be empty because "plum" is not a valid item
	Assertions.assertFalse(optQuot.isPresent());
	}


	@Test
	void shouldReturnCorrectResult() throws Exception {
		
		JsonArray lineitems =Json.createArrayBuilder()
			.add(Json.createObjectBuilder().add("item","apple").add("quantity",1))
			.add(Json.createObjectBuilder().add("item","durian").add("quantity",1))
			.build();

		JsonObject reqPayload = Json.createObjectBuilder()
			.add("address", "teletubby ave 1")
			.add("email", "twinkywinky@teletubby.com")
			.add("name", "twinky winky")
			.add("lineItems",lineitems)
			.build();

		RequestBuilder req = MockMvcRequestBuilders.post("/api/po")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(reqPayload.toString());

		MvcResult resp = mvc.perform(req).andReturn();
		MockHttpServletResponse httpResp = resp.getResponse();

		Assertions.assertEquals(200, httpResp.getStatus());

		Optional<JsonObject> opt = string2Json(httpResp.getContentAsString());
		assertTrue(opt.isPresent());

		JsonObject obj = opt.get();
		for (String s: List.of("invoiceId", "name", "total"))
			assertFalse(obj.isNull(s));

	}

	public static Optional<JsonObject> string2Json(String s) {
		try (InputStream is = new ByteArrayInputStream(s.getBytes())) {
			JsonReader reader = Json.createReader(is);
			return Optional.of(reader.readObject());
		} catch (Exception ex) {
			System.err.printf("Error: %s\n", ex.getMessage());
			return Optional.empty();
		}
	}
}


