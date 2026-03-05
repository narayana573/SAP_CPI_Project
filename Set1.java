public CustomResponse getAllStocks(String endpoint, Object body) {
    // 1. Construct the final URL
    String finalUrl = BASE_URL + endpoint;

    // 2. Trigger the GET request and use the required extract() methods
    Response response = RestAssured.given()
            .header("Authorization", AuthUtil.getAuthHeader())
            .get(finalUrl)
            .then()
            .log().all() // Logs the response to the console as you requested
            .extract()
            .response();

    // 3. Extract status details
    int statusCode = response.getStatusCode();
    String status = (statusCode == 200) ? "OK" : "Error";

    // 4. Extract data lists using the extracted response object
    List<Object> itemIds = response.jsonPath().getList("itemId");
    List<Object> itemNames = response.jsonPath().getList("itemName");
    List<Object> genericNames = response.jsonPath().getList("genericName");

    // 5. Return the initialized CustomResponse object
    return new CustomResponse(response, statusCode, status, itemIds, itemNames, genericNames);
}
