package io.github.samuel_pinheiro_c_lopes.spring_common.email.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonMailDTO {

    @JsonProperty("mailFrom")
    private String mailFrom;

    @JsonProperty("mailTo")
    private String mailTo;

    @JsonProperty("mailSubject")
    private String mailSubject;

    @JsonProperty("mailBody")
    private String mailBody;

    // 1. No-Arg Constructor (Required by Jackson for deserialization)
    public CommonMailDTO() {
    }

    // 2. All-Args Constructor (For convenience when sending emails)
    public CommonMailDTO(String mailFrom, String mailTo, String mailSubject, String mailBody) {
        this.mailFrom = mailFrom;
        this.mailTo = mailTo;
        this.mailSubject = mailSubject;
        this.mailBody = mailBody;
    }

    // 3. Methods matching your old Interface (Keeps your existing code working)
    public String mailFrom() { return mailFrom; }
    public String mailTo() { return mailTo; }
    public String mailSubject() { return mailSubject; }
    public String mailBody() { return mailBody; }

    // 4. Setters (Required for Jackson to populate the object)
    public void setMailFrom(String mailFrom) { this.mailFrom = mailFrom; }
    public void setMailTo(String mailTo) { this.mailTo = mailTo; }
    public void setMailSubject(String mailSubject) { this.mailSubject = mailSubject; }
    public void setMailBody(String mailBody) { this.mailBody = mailBody; }
}