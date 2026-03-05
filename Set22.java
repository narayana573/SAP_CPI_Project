public CustomResponse getAllStocks(String endpoint, Object body) {
    // 1. Construct the final URL
    String finalUrl = BASE_URL + endpoint;

    // 2. Trigger the GET request with mandatory Rest Assured methods
    Response response = RestAssured.given()
            .header("Authorization", AuthUtil.getAuthHeader())
            .get(finalUrl)
            .then()
            .log().all() // Fulfills your requirement to log the response
            .extract()
            .response();

    // 3. Extract status details
    int statusCode = response.getStatusCode();
    String status = (statusCode == 200) ? "OK" : "Error";

    // 4. Extract data lists from the "Results" array
    // Based on your Postman screenshot, the fields are inside the Results object
    List<Object> itemIds = response.jsonPath().getList("Results.itemId");
    List<Object> itemNames = response.jsonPath().getList("Results.itemName");
    List<Object> genericNames = response.jsonPath().getList("Results.GenericName");

    // 5. Return the initialized CustomResponse object
    return new CustomResponse(response, statusCode, status, itemIds, itemNames, genericNames);
}
