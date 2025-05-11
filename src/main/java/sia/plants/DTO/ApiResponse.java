package sia.plants.DTO;

public class ApiResponse {
    private String status;
    private String message;

    // Успешный ответ без сообщения
    public static ApiResponse success() {
        ApiResponse res = new ApiResponse();
        res.status = "success";
        return res;
    }

    // Ошибка с сообщением
    public static ApiResponse failure(String message) {
        ApiResponse res = new ApiResponse();
        res.status = "failure";
        res.message = message;
        return res;
    }


    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
