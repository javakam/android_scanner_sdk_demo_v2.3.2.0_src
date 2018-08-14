package com.richfit.zebra_ds36x8.barcode;

/**
 * Class to encapsulate scanner config info
 */
public class ScannerConfiguration {
    private String configurationName;
    private String configurationCode;

    public ScannerConfiguration(String configurationName, String configurationCode){
        this.configurationName = configurationName;
        this.configurationCode = configurationCode;
    }

    public String getConfigurationName() {
        return configurationName;
    }

    public String getConfigurationCode() {
        return configurationCode;
    }
}
