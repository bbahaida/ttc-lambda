package io.bmb.ttc.enums;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.bmb.ttc.enums.HttpMethodHandler.CHECK;
import static io.bmb.ttc.enums.HttpMethodHandler.LIST;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpMethodHandlerTest {

    @Mock
    private Table table;

    @Test
    void getHandler_shouldReturn_CHECK() {
        Assertions.assertThat(HttpMethodHandler.getHandler("POST /check"))
                .isNotEmpty().contains(CHECK);
    }

    @Test
    void getHandler_shouldReturn_Empty() {
        Assertions.assertThat(HttpMethodHandler.getHandler("POST"))
                .isEmpty();
    }

    @Test
    void getHandler_shouldReturn_LIST() {
        Assertions.assertThat(HttpMethodHandler.getHandler("GET /list"))
                .isNotEmpty().contains(LIST);
    }

    @Test
    void getHandler_shouldReturn_Empty2() {
        Assertions.assertThat(HttpMethodHandler.getHandler("GET"))
                .isEmpty();
    }


    /*@Test
    void process_shouldReturn_List(){
        ItemCollection<QueryOutcome> collection = new ItemCollection<>() {
            @Override
            public Integer getMaxResultSize() {
                return null;
            }

            @Override
            public Page<Item, QueryOutcome> firstPage() {
                return null;
            }
        };
        when(table.query(any(QuerySpec.class))).thenReturn(collection);
        LIST.process("1234", "", table, "isosceles");
        verify(table, times(1)).query(any(QuerySpec.class));
    }*/
    @Test
    void process_shouldReturn_Item() {

        PutItemOutcome putItemOutcome = new PutItemOutcome(new PutItemResult());
        when(table.putItem(any(Item.class))).thenReturn(putItemOutcome);
        String body = "{\r\n    \"side1\": 1,\r\n    \"side2\": 1,\r\n    \"side3\": 2\r\n}";
        CHECK.process("1234", body, table, "");
        verify(table, times(1)).putItem(any(Item.class));
    }

}
