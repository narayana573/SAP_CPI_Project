public CustomResponse getRequisitionByDateRange(String endpoint, Object body) {
    String finalUrl = BASE_URL + endpoint;

    Response response = RestAssured.given()
            .header("Authorization", AuthUtil.getAuthHeader())
            .get(finalUrl)
            .then()
            .extract()
            .response();

    int statusCode = response.getStatusCode();
    String status = (statusCode == 200) ? "OK" : "Error";

    // Extracting lists. Use "Results." prefix if the data is nested.
    JsonPath jsonPath = response.jsonPath();
    List<Object> requisitionNos = jsonPath.getList("Results.RequisitionNo");
    List<Object> requisitionStatuses = jsonPath.getList("Results.RequisitionStatus");
    List<Object> requisitionIds = jsonPath.getList("Results.RequisitionId");

    return new CustomResponse(response, statusCode, status, requisitionNos, requisitionStatuses, requisitionIds);
}
