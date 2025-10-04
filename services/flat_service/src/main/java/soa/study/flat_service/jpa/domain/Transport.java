package soa.study.flat_service.jpa.domain;

import lombok.Getter;

@Getter
public enum Transport {
    NONE(0),
    FEW(1),
    LITTLE(2),
    NORMAL(3);

    private final int level;

    Transport(int level) {
        this.level = level;
    }

    public boolean isGreaterThan(Transport other) {
        if (other == null) {
            return true;
        }
        return this.level > other.level;
    }
}