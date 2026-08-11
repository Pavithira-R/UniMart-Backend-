package lk.ac.kln.unimart.dto;

public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long id;
    private String universityEmail;
    private String fullName;
    private String role;

    public AuthResponse() {}

    public AuthResponse(String accessToken, Long id, String universityEmail, String fullName, String role) {
        this.accessToken = accessToken;
        this.id = id;
        this.universityEmail = universityEmail;
        this.fullName = fullName;
        this.role = role;
    }

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniversityEmail() {
        return universityEmail;
    }

    public void setUniversityEmail(String universityEmail) {
        this.universityEmail = universityEmail;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
