package jasyncapicmp.model;

import jasyncapicmp.JAsyncApiCmpUserException;

public @interface Version {
    enum Versions {
        NA(""),
        V2_6_0("2.6.0"),
        V3_0_0("3.0.0");

        final String versionString;
        Versions(String version) {
            this.versionString = version;
        }

        public Versions from(String s) {
            for (Versions versions : Versions.values()) {
                if (versions.versionString.equals(s)) {
                    return versions;
                }
            }
            throw new JAsyncApiCmpUserException("Unknown version string: " + s);
        }
    }

    Versions until() default Versions.NA;
    Versions since() default Versions.NA;
}
