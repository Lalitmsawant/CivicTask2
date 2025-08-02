package util;

public class LocationUtils {
    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Calculate distance between two points using Haversine formula
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Check if coordinates are within the specified radius
     */
    public static boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2, double radiusKm) {
        return calculateDistance(lat1, lon1, lat2, lon2) <= radiusKm;
    }

    /**
     * Validate latitude value
     */
    public static boolean isValidLatitude(Double latitude) {
        return latitude != null && latitude >= -90.0 && latitude <= 90.0;
    }

    /**
     * Validate longitude value
     */
    public static boolean isValidLongitude(Double longitude) {
        return longitude != null && longitude >= -180.0 && longitude <= 180.0;
    }

}
