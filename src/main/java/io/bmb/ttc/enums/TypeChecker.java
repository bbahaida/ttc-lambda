package io.bmb.ttc.enums;

import io.bmb.ttc.model.TriangleType;

public enum TypeChecker {
    EQUILATERAL {
        @Override
        boolean match(TriangleType triangleType) {
            return triangleType.isEquilateral();
        }

        @Override
        public String getType() {
            return "equilateral";
        }
    },
    ISOSCELES {
        @Override
        boolean match(TriangleType triangleType) {
            return triangleType.isIsosceles();
        }

        @Override
        public String getType() {
            return "isosceles";
        }
    },
    SCALENE {
        @Override
        boolean match(TriangleType triangleType) {
            return triangleType.isScalene();
        }

        @Override
        public String getType() {
            return "scalene";
        }
    };

    public static TypeChecker find(TriangleType triangleType) {
        for (TypeChecker checker : values()) {
            if (checker.match(triangleType)) {
                return checker;
            }
        }
        return null;
    }

    abstract boolean match(TriangleType triangleType);

    public abstract String getType();
}
