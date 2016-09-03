package com.androidweardocs.wearablemessage.logic;

public class CalcQibla {
    
    public static final double QIB_LAT = Math.toRadians(21.423333),  QIB_LONG = Math.toRadians(39.823333);

    public static double getQibFrmNorth(double degLatitude,
            double degLongitude) {

        double latitude2 = Math.toRadians(degLatitude),longitude = Math.toRadians(degLongitude);

        double bearing = Math.sin(QIB_LONG - longitude);

        double degree = Math.cos(latitude2) * Math.tan(QIB_LAT) - Math.sin(latitude2) * Math.cos(QIB_LONG - longitude);
        double returnValue = (Math.toDegrees(Math.atan(bearing / degree)));

        if (latitude2 > QIB_LAT) {
            if ((longitude > QIB_LONG || longitude < (Math
                    .toRadians(-180d) + QIB_LONG))
                    && (returnValue > 0 && returnValue <= 90)) {

                returnValue += 180;

            } else if (!(longitude > QIB_LONG || longitude < (Math
                    .toRadians(-180d) + QIB_LONG))
                    && (returnValue > -90 && returnValue < 0)) {

                returnValue += 180;

            }

        }
        if (latitude2 < QIB_LAT) {

            if ((longitude > QIB_LONG || longitude < (Math
                    .toRadians(-180d) + QIB_LONG))
                    && (returnValue > 0 && returnValue < 90)) {

                returnValue += 180;

            }
            if (!(longitude > QIB_LONG || longitude < (Math
                    .toRadians(-180d) + QIB_LONG))
                    && (returnValue > -90 && returnValue <= 0)) {

                returnValue += 180;
            }

        }
        return (returnValue) - 10;
    }

}
