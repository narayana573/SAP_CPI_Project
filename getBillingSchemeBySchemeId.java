public CustomResponse getBillingSchemeBySchemeId(String endpoint, Object body) {
    String finalUrl = BASE_URL + endpoint;

    // Trigger GET request with mandatory Rest Assured methods
    Response response = RestAssured.given()
            .header("Authorization", AuthUtil.getAuthHeader())
            .get(finalUrl)
            .then()
            .log().all()
            .extract()
            .response();

    int statusCode = response.getStatusCode();
    String status = (statusCode == 200) ? "OK" : "Error";

    // Extracting objects from the Results node
    JsonPath jsonPath = response.jsonPath();
    Object schemeCode = jsonPath.get("Results.SchemeCode");
    Object schemeName = jsonPath.get("Results.SchemeName");
    Object schemeId = jsonPath.get("Results.SchemeId");

    return new CustomResponse(response, statusCode, status, schemeCode, schemeName, schemeId);
}
