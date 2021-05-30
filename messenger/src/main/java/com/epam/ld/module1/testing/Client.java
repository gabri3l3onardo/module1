package com.epam.ld.module1.testing;

/**
 * The type Client.
 */
public class Client {

    /**
     * Addresses for mail sending
     */
    private String addresses;

    public Client(String addresses) {
        this.addresses = addresses;
    }

    /**
     * Gets addresses.
     *
     * @return the addresses
     */
    public String getAddresses() {
        return addresses;
    }

    /**
     * Sets addresses.
     *
     * @param addresses the addresses
     */
    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }
}
