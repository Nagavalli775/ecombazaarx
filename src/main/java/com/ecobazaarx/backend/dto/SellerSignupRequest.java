@Data
public class SellerSignupRequestDTO {

    @NotBlank
    private String businessName;

    @Email
    @NotBlank
    private String email;

    @NotNull
    @Digits(integer = 10, fraction = 0)
    private Long phone;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    private String businessAddress;
}
