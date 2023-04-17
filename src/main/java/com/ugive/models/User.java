package com.ugive.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ugive.models.enums.Gender;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode(exclude = {
        "roles"
})
@ToString(exclude = {
        "roles"
})
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)

    private Gender gender = Gender.NOT_SELECTED;

    @Column(nullable = false)
    private String phone;
    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "e_mail", nullable = false))
    @AttributeOverride(name = "login", column = @Column(name = "user_login", nullable = false))
    @AttributeOverride(name = "password", column = @Column(name = "user_password", nullable = false))
    private AuthenticationInfo authenticationInfo;

    @Column(nullable = false)
    private Timestamp created = Timestamp.valueOf(LocalDateTime.now());

    @Column(nullable = false)
    private Timestamp changed = Timestamp.valueOf(LocalDateTime.now());

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("users")
    private Set<Role> roles = Collections.emptySet();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private UserBalance userBalance;

//    public List<Role> getRoles() {
//    List<Role> roles = new ArrayList<>();
//    // Получаем роли пользователя из базы данных
//    // и добавляем их в список roles
//    return roles;
//}
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//    List<Role> roles = getRoles();
//
//    return roles.stream()
//            .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
//            .collect(Collectors.toList());
//    }
//
//    @Override
//    public String getPassword() {
//        return userPassword;
//    }
//
//    @Override
//    public String getUsername() {
//        return userLogin;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return isActive();
//    }
}
