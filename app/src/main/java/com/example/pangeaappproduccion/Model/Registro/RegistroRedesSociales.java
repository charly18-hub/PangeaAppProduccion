package com.example.pangeaappproduccion.Model.Registro;

public class RegistroRedesSociales {
    private Accounts accounts;
    private Availability availability;
    private String confirmedAccount;
    private String countryOfOrigin;
    private String CountryResidence;
    private String dateOfBirth;
    private DescriptionProfile descriptionProfile;
    private String emailAddress;
    private String favoriteHobbie;
    private String firstName;
    private String gender;
    private String id;
    private Interests interests;
    private boolean isAdult;
    private Language  language;
    private String lastName;
    private String profession;
    private String profilePicture;
    private String scholarship;
    private String telephoneNumber;
    private boolean termsOfUse;
    private String userType;
    private String userName;

    public RegistroRedesSociales() {
    }

    public RegistroRedesSociales(Accounts accounts, Availability availability, String confirmedAccount,
                                 String countryOfOrigin, String countryResidence, String dateOfBirth,
                                 DescriptionProfile descriptionProfile, String emailAddress,
                                 String favoriteHobbie, String firstName, String gender, String id,
                                 Interests interests, boolean isAdult, Language language, String lastName,
                                 String profession, String profilePicture, String scholarship,
                                 String telephoneNumber, boolean termsOfUse, String userType, String userName) {
        this.accounts = accounts;
        this.availability = availability;
        this.confirmedAccount = confirmedAccount;
        this.countryOfOrigin = countryOfOrigin;
        CountryResidence = countryResidence;
        this.dateOfBirth = dateOfBirth;
        this.descriptionProfile = descriptionProfile;
        this.emailAddress = emailAddress;
        this.favoriteHobbie = favoriteHobbie;
        this.firstName = firstName;
        this.gender = gender;
        this.id = id;
        this.interests = interests;
        this.isAdult = isAdult;
        this.language = language;
        this.lastName = lastName;
        this.profession = profession;
        this.profilePicture = profilePicture;
        this.scholarship = scholarship;
        this.telephoneNumber = telephoneNumber;
        this.termsOfUse = termsOfUse;
        this.userType = userType;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Accounts getAccounts() {
        return accounts;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public String getConfirmedAccount() {
        return confirmedAccount;
    }

    public void setConfirmedAccount(String confirmedAccount) {
        this.confirmedAccount = confirmedAccount;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getCountryResidence() {
        return CountryResidence;
    }

    public void setCountryResidence(String countryResidence) {
        CountryResidence = countryResidence;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public DescriptionProfile getDescriptionProfile() {
        return descriptionProfile;
    }

    public void setDescriptionProfile(DescriptionProfile descriptionProfile) {
        this.descriptionProfile = descriptionProfile;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFavoriteHobbie() {
        return favoriteHobbie;
    }

    public void setFavoriteHobbie(String favoriteHobbie) {
        this.favoriteHobbie = favoriteHobbie;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Interests getInterests() {
        return interests;
    }

    public void setInterests(Interests interests) {
        this.interests = interests;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getScholarship() {
        return scholarship;
    }

    public void setScholarship(String scholarship) {
        this.scholarship = scholarship;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public boolean isTermsOfUse() {
        return termsOfUse;
    }

    public void setTermsOfUse(boolean termsOfUse) {
        this.termsOfUse = termsOfUse;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
