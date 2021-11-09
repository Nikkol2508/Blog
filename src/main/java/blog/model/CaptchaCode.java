package blog.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private long time;

    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private String code;

    @Column(name = "secret_code", columnDefinition = "VARCHAR(22)", nullable = false, unique = true)
    private String secretCode;

}
