package org.example;

public class AddressValidation {
    public static boolean isValidIPv4(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        String[] parts = ip.split("\\.");
        if (parts.length != 4) return false;
        for (String part : parts) {
            if (part.isEmpty()) return false;
            for (char c : part.toCharArray()) {
                if (!Character.isDigit(c)) return false;
            }
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) return false;
            }
            catch (NumberFormatException e) {
                return false;
            }
            if (part.length() > 1 && part.startsWith("0")) return false;
        }
        return true;
    }
}