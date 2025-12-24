@Data
public class UserSignupRequestDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30)
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank
    private String email;

    @NotNull
    @Digits(integer = 10, fraction = 0, message = "Phone must be 10 digits")
    private Long phone;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
