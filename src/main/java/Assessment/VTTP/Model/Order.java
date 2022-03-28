package Assessment.VTTP.Model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Order {
    private String name;
    public List<String> itemsname = new ArrayList<>();;

    public List<String> getItemsname() {
        return itemsname;
    }

    public void setItemsname(List<String> itemsname) {
        this.itemsname = itemsname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
        
        public Order getitemsname(String json) {
            Order order = new Order();
        
                InputStream is = new ByteArrayInputStream(json.getBytes());
                JsonReader r = Json.createReader(is);
                JsonObject req = r.readObject();
                
                order.name= req.getString("name");
    
                JsonArray itemsArr = req.getJsonArray("lineItems");
                for (int i = 0; i < itemsArr.size(); i++)
                    order.itemsname.add(itemsArr.getJsonObject(i).getString("item"));
            System.out.printf("orderingggggg>>>>>>>%s",order.itemsname);
                return order;
            }
    
}


