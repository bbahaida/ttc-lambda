package io.bmb.ttc.model;

import com.amazonaws.services.dynamodbv2.document.Item;
import io.bmb.ttc.enums.TypeChecker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TriangleType {
    private String id;
    private int side1;
    private int side2;
    private int side3;
    private String triangleType;

    @Transient
    public boolean isValid() {
        return greaterThanZero() && validTriangle();
    }

    private boolean greaterThanZero() {
        return side1 > 0 && side2 > 0 && side3 > 0;
    }

    private boolean validTriangle() {
        return side1 + side2 >= side3
                && side1 + side3 >= side2
                && side2 + side3 >= side1;
    }


    public void computeType() {
        if (!isValid()) {
            return;
        }
        triangleType = TypeChecker.find(this).getType();

    }

    @Transient
    public boolean isEquilateral() {
        return side1 == side2 && side2 == side3;
    }

    @Transient
    public boolean isIsosceles() {
        return !isEquilateral() && !isScalene();
    }

    @Transient
    public boolean isScalene() {
        return side1 != side2 && side2 != side3 && side1 != side3;
    }

    public Item asItem() {
        return new Item().withPrimaryKey("id", id)
                .withNumber("side1", side1)
                .withNumber("side2", side2)
                .withNumber("side3", side3)
                .withString("triangleType", triangleType);
    }
}
