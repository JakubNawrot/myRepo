package net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.estimate;

public enum EstimateStatus {
    DRAFT("Draft"),
    PRODUCTION("Production"),
    CANCELLED("Cancelled");

    private final String status;

    EstimateStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

