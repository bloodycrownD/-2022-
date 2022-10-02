package untils.constant;

public enum StatusCode {

    OK("200 OK"),
    BadRequest("400 BadRequest"),
    NotFound("404 NotFound"),
    HTTP_Version_Not_Supported("505 HTTP Version Not Supported"),
    MovedPermanently("301 MovedPermanently");

    StatusCode(String status){
    }

}
