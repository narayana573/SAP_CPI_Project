public CustomResponse getPatientConsumptionInfoByPatientIdAndVisitId(String endpoint, Object body) {
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
    // Navigating to fields inside the Results.PatientConsumption object
    Object patientName = jsonPath.get("Results.PatientConsumption.PatientName");
    Object hospitalNo = jsonPath.get("Results.PatientConsumption.HospitalNo");
    Object storeId = jsonPath.get("Results.PatientConsumption.StoreId");

    return new CustomResponse(response, statusCode, status, patientName, hospitalNo, storeId);
}
