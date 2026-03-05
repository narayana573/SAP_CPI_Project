public CustomResponse getRequisitionByDateRange(String endpoint, Object body) {
    String finalUrl = BASE_URL + endpoint;

    // Use mandatory Rest Assured extraction chain
    Response response = RestAssured.given()
            .header("Authorization", AuthUtil.getAuthHeader())
            .get(finalUrl)
            .then()
            .extract()
            .response();

    int statusCode = response.getStatusCode();
    String status = (statusCode == 200) ? "OK" : "Error";

    // Navigate into Results -> requisitionList to extract the data
    JsonPath jsonPath = response.jsonPath();
    List<Object> requisitionNos = jsonPath.getList("Results.requisitionList.RequisitionNo");
    List<Object> requisitionStatuses = jsonPath.getList("Results.requisitionList.RequisitionStatus");
    List<Object> requisitionIds = jsonPath.getList("Results.requisitionList.RequisitionId");

    return new CustomResponse(response, statusCode, status, requisitionNos, requisitionStatuses, requisitionIds);
}
