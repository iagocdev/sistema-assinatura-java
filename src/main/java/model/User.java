package model;

public class User {

    private int id;
    private String name;
    private String email;
    private String passwordHash; // O nome da variável corresponde à coluna password_hash

    // Getters e Setters são métodos públicos que permitem acessar e definir
    // os valores das variáveis privadas de forma controlada.
    // É uma boa prática de encapsulamento em Java.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}