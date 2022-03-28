package Assessment.VTTP.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import Assessment.VTTP.Controller.Quotation;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

public class QuotationService {

    public Optional<Quotation> getQuotations(List<String> items){
        
        try{
            JsonArrayBuilder builder = Json.createArrayBuilder();
            for(String i:items){
                builder.add(i);
            }

       Quotation quotation = new Quotation();

        RequestEntity<String> req = RequestEntity
            .post("https://quotation.chuklee.com/quotation")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Accept","MediaType.APPLICATION_JSON")
            .body(builder.build().toString());
            
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req,String.class);
        System.out.printf("ABCDE>>>>>>>>>>>>>>>>>>>>>>>>%s",resp.getBody());

        String quoteobject = resp.getBody();
        InputStream is = new ByteArrayInputStream(quoteobject.getBytes());
            JsonReader reader = Json.createReader(is);
            JsonObject o = reader.readObject();
        System.out.printf("QUOTEOBJECT>>>>>>>>>>>>>>>>>>>>>>>>%s", o);

        String quoteid = o.getString("quoteId");
        quotation.setQuoteId(quoteid);
        System.out.printf("QUOTEID123>>>>>>>>>>>>>>>>>>>>>>>>%s", quoteid);

        JsonArray quotationobject = o.getJsonArray("quotations");
        
        Map<String, Float> map = new HashMap<>();

        for(JsonValue i: quotationobject){ 
            JsonObject a = i.asJsonObject();
            String item = a.getString("item");
            Float unitprice = a.getJsonNumber("unitPrice").bigDecimalValue().floatValue();
            map.put(item, unitprice);
    }
       
        quotation.setQuotations(map);
        System.out.printf("MAP123>>>>>>>>>>>>>>>>>>>>>>>>%s", map);
        
        return Optional.of(quotation);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return Optional.empty();
    }


}
