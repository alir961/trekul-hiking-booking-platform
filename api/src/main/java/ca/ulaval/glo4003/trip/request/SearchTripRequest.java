package ca.ulaval.glo4003.trip.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;
import java.time.LocalDate;

public class SearchTripRequest {
  private final String region;
  private final String startDate;
  private final String endDate;
  private final double priceLimit;
  private final String status;

  public SearchTripRequest(
      @Parameter(description = "Region where the trip is located") @QueryParam("region")
          String region,
      @Parameter(description = "Start date of the trip in format 'YYYY-MM-DD'", required = true)
          @QueryParam("startDate")
          String startDate,
      @Parameter(description = "End date of the trip in format 'YYYY-MM-DD'", required = true)
          @QueryParam("endDate")
          String endDate,
      @Parameter(description = "Maximum price limit for the trip", required = true)
          @QueryParam("priceLimit")
          double priceLimit,
      @Parameter(description = "Status to filter trips")
          @DefaultValue("AVAILABLE")
          @QueryParam("status")
          String status) {
    this.region = region;
    this.startDate = startDate;
    this.endDate = endDate;
    this.priceLimit = priceLimit;
    this.status = status;
  }

  private LocalDate parseDate(String dateStr) {
    if (dateStr == null) {
      throw new BadRequestException(
          "Invalid date, the start and end date of the trip is obligatory and must be in the format YYYY-MM-DD");
    }

    return LocalDate.parse(dateStr);
  }

  public String getRegion() {
    return this.region;
  }

  public LocalDate getStartDate() {
    return this.parseDate(this.startDate);
  }

  public LocalDate getEndDate() {
    return this.parseDate(this.endDate);
  }

  public double getPriceLimit() {
    return this.priceLimit;
  }

  public String getStatus() {
    return this.status;
  }
}
