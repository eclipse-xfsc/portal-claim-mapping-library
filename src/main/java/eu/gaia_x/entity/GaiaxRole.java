package eu.gaia_x.entity;

public enum GaiaxRole {
    TESTROLE("TestRole"), ADMIN("Admin");
    private final String rolename;

    GaiaxRole(String brand) {
        this.rolename = brand;
    }

    @Override
    public String toString() {
        return rolename;
    }
}
