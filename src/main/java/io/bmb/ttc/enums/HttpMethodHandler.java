package io.bmb.ttc.enums;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import io.bmb.ttc.model.TriangleType;
import io.bmb.ttc.utils.Mapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public enum HttpMethodHandler {

    CHECK {
        @Override
        public boolean isValid(String body) {
            if (body == null) {
                return false;
            }
            TriangleType triangleType = Mapper.convert(body);
            return triangleType != null && triangleType.isValid();
        }

        @Override
        boolean handle(String routeKey) {
            return "POST /check".matches(routeKey);
        }

        @Override
        public String process(String id, String body, Table table, String type) {
            TriangleType triangleType = Mapper.convert(body);
            triangleType.setId(id);
            triangleType.computeType();
            Item item = triangleType.asItem();
            table.putItem(item);
            return Mapper.asString(triangleType);
        }
    },
    LIST {
        @Override
        public boolean isValid(String body) {
            return true;
        }

        @Override
        boolean handle(String routeKey) {
            return "GET /list".matches(routeKey);
        }

        @Override
        public String process(String id, String body, Table table, String type) {
            List<TriangleType> items = new ArrayList<>();
            log.info("process List function");
            if (type.isBlank()) {
                throw new IllegalArgumentException("You should specify the query param type");
            }
            QuerySpec spec = new QuerySpec()
                    .withKeyConditionExpression("triangleType = :type")
                    .withValueMap(new ValueMap()
                            .withString(":type", type));
            ItemCollection<QueryOutcome> result = table.query(spec);
            for (Item item : result) {
                items.add(fromItem(item));
            }
            return Mapper.asString(items);
        }
    };

    public static Optional<HttpMethodHandler> getHandler(String routeKey) {
        for (HttpMethodHandler handler : values()) {
            if (handler.handle(routeKey)) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }

    TriangleType fromItem(Item item) {
        return TriangleType.builder()
                .id(item.getString("id"))
                .side1(item.getNumber("side1").intValue())
                .side2(item.getNumber("side2").intValue())
                .side3(item.getNumber("side3").intValue())
                .triangleType(item.getString("triangleType"))
                .build();
    }

    public abstract boolean isValid(String body);

    abstract boolean handle(String routeKey);

    public abstract String process(String id, String body, Table dynamoDb, String type);
}
