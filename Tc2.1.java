public CustomResponse getMainStore(String endpoint, Object body) {
    // 1. Construct the final URL
    String finalUrl = BASE_URL + endpoint;

    // 2. Trigger the GET call with mandatory Rest Assured methods
    Response response = RestAssured.given()
            .header("Authorization", AuthUtil.getAuthHeader())
            .get(finalUrl)
            .then()
            .extract()
            .response();

    // 3. Extract status details
    int statusCode = response.getStatusCode();
    String status = (statusCode == 200) ? "OK" : "Error";

    // 4. Extract single objects (assuming they are inside 'Results' based on the pattern)
    JsonPath jsonPath = response.jsonPath();
    Object storeId = jsonPath.get("Results.StoreId");
    Object category = jsonPath.get("Results.Category");
    Object isActive = jsonPath.get("Results.IsActive");

    // 5. Return the initialized CustomResponse object
    return new CustomResponse(response, statusCode, status, storeId, category, isActive);
}
