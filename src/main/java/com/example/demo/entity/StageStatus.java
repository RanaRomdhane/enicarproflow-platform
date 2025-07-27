package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * StageStatus enum with human-readable labels and JSON/JPA mapping support.
 */
public enum StageStatus {
    EN_ATTENTE("En attente"),
    EN_COURS("En cours"),
    VALIDE("Validé"),
    ANNULE("Annulé");

    private final String label;

    StageStatus(String label) {
        this.label = label;
    }

    /**
     * Used by Jackson to serialize enum values to JSON.
     */
    @JsonValue
    public String getLabel() {
        return label;
    }

    /**
     * Used by Jackson to map JSON string values back to enum constants.
     */
    @JsonCreator
    public static StageStatus fromLabel(String label) {
        if (label == null) return null;
        for (StageStatus status : values()) {
            if (status.label.equalsIgnoreCase(label.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown StageStatus label: " + label);
    }

    /**
     * This converter class will handle the conversion between the database
     * string values and the enum constants.
     */
    @Converter(autoApply = true)
    public static class StageStatusConverter implements AttributeConverter<StageStatus, String> {
        @Override
        public String convertToDatabaseColumn(StageStatus status) {
            return status != null ? status.getLabel() : null;
        }

        @Override
        public StageStatus convertToEntityAttribute(String label) {
            return label != null ? StageStatus.fromLabel(label) : null;
        }
    }
}