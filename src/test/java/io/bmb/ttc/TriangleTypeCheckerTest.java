package io.bmb.ttc;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TriangleTypeCheckerTest {

    private final TriangleTypeChecker checker = new TriangleTypeChecker();

    private final APIGatewayV2HTTPEvent event = new APIGatewayV2HTTPEvent();
    @Mock
    private Context context;

    @BeforeEach
    public void setUp() {
        event.setRouteKey("POST /check");

        when(context.getAwsRequestId()).thenReturn("1234");
    }

    @Test
    void handleRequest_shouldReturn400_ifBodyNull() {
        event.setBody(null);
        APIGatewayV2HTTPResponse response = checker.handleRequest(event, context);
        assertThat(response.getStatusCode()).isEqualTo(400);
    }

    @Test
    void handleRequest_shouldReturn400_ifBodyHasZeros() {
        event.setBody("{\r\n    \"side1\": 0,\r\n    \"side2\": 1,\r\n    \"side3\": 2\r\n}");
        APIGatewayV2HTTPResponse response = checker.handleRequest(event, context);
        assertThat(response.getStatusCode()).isEqualTo(400);
    }
}
