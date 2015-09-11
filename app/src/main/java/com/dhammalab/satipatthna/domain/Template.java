package com.dhammalab.satipatthna.domain;

/**
 * Created by anthony.lipscomb on 10/4/2014.
 */
public enum Template {
    SIX_SENSES("6 senses"), EXTERNAL("3 out (External)"), INTERNAL("3 in (Internal)"), MIND_BODY("Mind / Body");

    private final String name;

    private Template(String name) {
        this.name = name;
    }

    public static Object[] getAllTemplates() {
        return Template.SIX_SENSES.getDeclaringClass().getEnumConstants();
    }

    public static Template fromInteger(int x) {
        switch (x) {
            case 0:
                return SIX_SENSES;
            case 1:
                return EXTERNAL;
            case 2:
                return INTERNAL;
            case 3:
                return MIND_BODY;
        }
        return null;
    }

    public static int toInteger(Template template) {
        if (template != null) {
            if (template.equals(Template.SIX_SENSES)) {
                return 0;
            } else if (template.equals(Template.EXTERNAL)) {
                return 1;
            } else if (template.equals(Template.INTERNAL)) {
                return 2;
            } else if (template.equals(Template.MIND_BODY)) {
                return 3;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return name;
    }
}
