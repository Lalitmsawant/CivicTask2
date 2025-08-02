package service;

import com.CivicTrackTask.CivicTrack.CivicTrackApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import util.LocationUtils;

@Service
public class LocationService {
    @Value("${app.location.max-radius-km}")
    private double maxRadiusKm;

    @Value("${app.location.default-radius-km}")
    private double defaultRadiusKm;

    /**
     * Validate location coordinates
     */
    public boolean isValidLocation(Double latitude, Double longitude) {
        return LocationUtils.isValidLatitude(latitude) && LocationUtils.isValidLongitude(longitude);
    }

    /**
     * Calculate distance between two points
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return LocationUtils.calculateDistance(lat1, lon1, lat2, lon2);
    }

    /**
     * Check if location is within allowed radius
     */
    public boolean isWithinAllowedRadius(double lat1, double lon1, double lat2, double lon2, double requestedRadius) {
        if (requestedRadius > maxRadiusKm) {
            requestedRadius = maxRadiusKm;
        }

        return LocationUtils.isWithinRadius(lat1, lon1, lat2, lon2, requestedRadius);
    }

    /**
     * Get validated radius (ensure it doesn't exceed maximum)
     */
    public double getValidatedRadius(Double requestedRadius) {
        if (requestedRadius == null || requestedRadius <= 0) {
            return defaultRadiusKm;
        }

        return 0;

    }
    }

