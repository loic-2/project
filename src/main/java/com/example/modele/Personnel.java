package com.example.modele;

public class Personnel 
{

    private String nom;
    private String login;
    private String role;
    private String telephone;
    private String dateAjout;
    private String mdp;


    public Personnel(String nom, String login, String role, String telephone, String dateAjout,
    String mdp)
    {
        this.nom=nom;
        this.telephone=telephone;
        this.role=role;
        this.login=login;
        this.dateAjout=dateAjout;
        this.mdp=mdp;
    }
    

    /**
     * @return String return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return String return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return String return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return String return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return String return the dateAjout
     */
    public String getDateAjout() {
        return dateAjout;
    }

    /**
     * @param dateAjout the dateAjout to set
     */
    public void setDateAjout(String dateAjout) {
        this.dateAjout = dateAjout;
    }


    /**
     * @return String return the mdp
     */
    public String getMdp() {
        return mdp;
    }

    /**
     * @param mdp the mdp to set
     */
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

}
