public CustomResponse getConsumptionsListOfAPatientById(String endpoint, Object body) {
    String finalUrl = BASE_URL + endpoint;

    Response response = RestAssured.given()
            .header("Authorization", AuthUtil.getAuthHeader())
            .get(finalUrl)
            .then()
            .log().all()
            .extract()
            .response();

    int statusCode = response.getStatusCode();
    String status = (statusCode == 200) ? "OK" : "Error";

    // Extracting lists from the Results array
    JsonPath jsonPath = response.jsonPath();
    List<Object> patientConsumptionIds = jsonPath.getList("Results.PatientConsumptionId");
    List<Object> consumptionReceiptNos = jsonPath.getList("Results.ConsumptionReceiptNo");
    List<Object> totalAmounts = jsonPath.getList("Results.TotalAmount");

    return new CustomResponse(response, statusCode, status, patientConsumptionIds, consumptionReceiptNos, totalAmounts);
}
