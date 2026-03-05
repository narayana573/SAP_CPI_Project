public CustomResponse getAllStocks(String endpoint, Object body) {
    // 1. Construct the final URL
    String finalUrl = BASE_URL + endpoint;

    // 2. Trigger the GET request with Bearer Token
    Response response = RestAssured.given()
            .header("Authorization", AuthUtil.getAuthHeader())
            .get(finalUrl);

    // 3. Extract status details
    int statusCode = response.getStatusCode();
    String status = (statusCode == 200) ? "OK" : "Error";

    // 4. Extract data lists using JsonPath
    JsonPath jsonPath = response.jsonPath();
    List<Object> itemIds = jsonPath.getList("itemId");
    List<Object> itemNames = jsonPath.getList("itemName");
    List<Object> genericNames = jsonPath.getList("genericName");

    // 5. Initialize and return the CustomResponse object
    // Note: Ensure your CustomResponse constructor matches these parameters
    return new CustomResponse(response, statusCode, status, itemIds, itemNames, genericNames);
}
