package io.github.venkat1701.yugaantar.utilities.permissions;

public enum Permission {
    VIEW_ALL("getAll"),
    VIEW("getById"),
    CREATE("save"),
    UPDATE("update"),
    DELETE("delete");

    private final String methodName;

    Permission(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }
}