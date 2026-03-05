public CustomResponse getAllStocks(String endpoint, Object body) {
    // 1. Construct the final URL
    String finalUrl = BASE_URL + endpoint;

    // 2. Trigger request with MANDATORY methods (uncommented)
    Response response = RestAssured.given()
            .header("Authorization", AuthUtil.getAuthHeader())
            .get(finalUrl)
            .then()
            .log().all() 
            .extract()
            .response();

    // 3. Extract status details
    int statusCode = response.getStatusCode();
    String status = (statusCode == 200) ? "OK" : "Error";

    // 4. Extract data lists using the correct JSON Path
    // Added "Results." prefix because the data is inside a Results array
    List<Object> itemIds = response.jsonPath().getList("Results.ItemId");
    List<Object> itemNames = response.jsonPath().getList("Results.ItemName");
    List<Object> genericNames = response.jsonPath().getList("Results.GenericName");

    // 5. Return the initialized CustomResponse object
    return new CustomResponse(response, statusCode, status, itemIds, itemNames, genericNames);
}
