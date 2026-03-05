public CustomResponse getBillingSummaryByPatientId(String endpoint, Object body) {
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
    // Path correction based on your Postman Results object
    Object patientId = jsonPath.get("Results.PatientId");
    Object totalDue = jsonPath.get("Results.TotalDue");

    return new CustomResponse(response, statusCode, status, patientId, totalDue);
}
