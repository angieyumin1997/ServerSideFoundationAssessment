package Assessment.VTTP.Controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Assessment.VTTP.Model.Order;
import Assessment.VTTP.Service.QuotationService;
import jakarta.json.JsonObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@RestController
@RequestMapping(path="/api", produces= MediaType.APPLICATION_JSON_VALUE)
public class PurchaseOrderRestController {

    @PostMapping(path = "/po")
    public ResponseEntity<String> submit(@RequestBody String payload){
        System.out.println("HELLO");
            QuotationService quotesvc = new QuotationService();
            Order order = new Order();
        try{
            InputStream is = new ByteArrayInputStream(payload.getBytes());
            JsonReader r = Json.createReader(is);
            JsonObject req = r.readObject();
            JsonArray lineitems = req.getJsonArray("lineItems");
            System.out.printf("PAYLOAD>>>>>>>>>>>>>>%s",payload);

            Quotation quotation = quotesvc.getQuotations(order.getitemsname(req.toString()).itemsname).orElse(null);
            double Total = 0;
            String name = order.getitemsname(req.toString()).getName();
            String invoiceid = quotation.getQuoteId();
            for (JsonValue i:lineitems){
                JsonObject a = i.asJsonObject();
                Total += a.getInt("quantity") * quotation.getQuotation(a.getString("item"));
            }
        
            JsonObject builder = Json.createObjectBuilder()
                .add("name",name)
                .add("total",Total)
                .add("invoiceId",invoiceid)
                .build();
       
            return ResponseEntity.ok(builder.toString());
        }catch (Exception ex) {

            return ResponseEntity.status(400).body("{}");

        }
    }

}
