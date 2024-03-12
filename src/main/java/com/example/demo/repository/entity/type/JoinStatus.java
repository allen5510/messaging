package com.example.demo.repository.entity.type;

public enum JoinStatus {
    WAIT(0), // 等待加入（被邀請）
    JOIN(1), // 已加入
    EXIT(2); // 已離開

    private int rawValue;

    JoinStatus(int rawValue) {
        this.rawValue = rawValue;
    }

    public int rawValue() {
        return rawValue;
    }

    public static JoinStatus valueOf(int value) {
        for (JoinStatus status : JoinStatus.values()) {
            if (status.rawValue == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("No JoinStatus with value " + value);
    }

    public boolean hasAccess() {
        return this == WAIT || this == JOIN;
    }

    public boolean isWait() {
        return this == WAIT;
    }

    public boolean isJoined() {
        return this == JOIN;
    }

    public boolean isExit() {
        return this == EXIT;
    }

}
