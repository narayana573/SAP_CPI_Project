public CustomResponse getPatientConsumptions(String endpoint, Object body) {
    String finalUrl = BASE_URL + endpoint;

    Response response = RestAssured.given()
            .header("Authorization", AuthUtil.getAuthHeader())
            .get(finalUrl)
            .then()
            .extract()
            .response();

    int statusCode = response.getStatusCode();
    String status = (statusCode == 200) ? "OK" : "Error";

    JsonPath jsonPath = response.jsonPath();
    // Use the nested path pattern for consistency
    List<Object> patientIds = jsonPath.getList("Results.PatientId");
    List<Object> hospitalNos = jsonPath.getList("Results.HospitalNo");
    List<Object> patientVisitIds = jsonPath.getList("Results.PatientVisitId");

    return new CustomResponse(response, statusCode, status, patientIds, hospitalNos, patientVisitIds);
}
