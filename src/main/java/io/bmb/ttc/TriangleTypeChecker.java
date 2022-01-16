package io.bmb.ttc;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import io.bmb.ttc.enums.HttpMethodHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class TriangleTypeChecker implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    public static final String TABLE_NAME = "triangle-type";

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();
        response.setStatusCode(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        try {
            String id = context.getAwsRequestId();
            String routeKey = event.getRouteKey();
            String type = "";
            if (hasType(event)) {
                type = event.getQueryStringParameters().get("type");
            }
            log.info("data received: routeKey: {}", routeKey);
            Optional<HttpMethodHandler> handler = HttpMethodHandler.getHandler(routeKey);
            String body = event.getBody();
            if (handler.isEmpty() || !handler.get().isValid(body)) {
                response.setStatusCode(400);
                response.setBody("input is not valid");
                return response;
            }
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
            DynamoDB dynamoDb = new DynamoDB(client);
            Table table = dynamoDb.getTable(TABLE_NAME);

            response.setBody(handler.get().process(id, body, table, type));
            return response;
        } catch (Exception e) {
            log.error("Error Occurred: ", e);
            response.setStatusCode(500);
            response.setBody(e.getMessage());
            return response;
        }
    }

    private boolean hasType(APIGatewayV2HTTPEvent event) {
        return event.getQueryStringParameters() != null && event.getQueryStringParameters().containsKey("type");
    }
}
